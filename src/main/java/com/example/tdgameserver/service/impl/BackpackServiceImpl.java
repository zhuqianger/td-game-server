package com.example.tdgameserver.service.impl;

import com.example.tdgameserver.config.JsonConfigLoader;
import com.example.tdgameserver.entity.backpack.BackpackType;
import com.example.tdgameserver.entity.backpack.Item;
import com.example.tdgameserver.entity.backpack.PlayerItem;
import com.example.tdgameserver.mapper.PlayerItemMapper;
import com.example.tdgameserver.service.BackpackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 背包服务实现类
 */
@Service
public class BackpackServiceImpl implements BackpackService {
    
    @Autowired
    private PlayerItemMapper playerItemMapper;
    
    @Autowired
    private JsonConfigLoader jsonConfigLoader;
    
    // 玩家背包数据缓存，key为玩家ID，value为道具列表
    private final Map<Integer, List<PlayerItem>> playerItemsCache = new ConcurrentHashMap<>();
    
    // 玩家道具数量缓存，key为"playerId:itemId"，value为数量
    private final Map<String, Integer> itemQuantityCache = new ConcurrentHashMap<>();
    
    // 配置缓存
    private volatile List<Item> itemConfigsCache = null;
    private volatile List<BackpackType> backpackTypeConfigsCache = null;
    private volatile Map<Integer, Item> itemConfigMapCache = null;
    private volatile Map<Integer, BackpackType> backpackTypeConfigMapCache = null;
    
    @Override
    public List<PlayerItem> getPlayerItems(Integer playerId) {
        // 先从缓存获取
        List<PlayerItem> cachedItems = playerItemsCache.get(playerId);
        if (cachedItems != null) {
            return new ArrayList<>(cachedItems); // 返回副本，避免外部修改
        }
        
        // 缓存未命中，从数据库查询
        List<PlayerItem> items = playerItemMapper.selectByPlayerId(playerId);
        
        // 保存到缓存
        playerItemsCache.put(playerId, new ArrayList<>(items));
        
        // 同时更新数量缓存
        updateQuantityCache(playerId, items);
        
        return items;
    }
    
    @Override
    public List<PlayerItem> getPlayerItemsByBackpackType(Integer playerId, Integer backpackTypeId) {
        // 获取所有道具配置
        List<Item> allItems = getAllItemConfigs();
        
        // 过滤出指定背包类型的道具ID
        List<Integer> targetItemIds = new ArrayList<>();
        for (Item item : allItems) {
            if (item.getBackpackTypeId().equals(backpackTypeId)) {
                targetItemIds.add(item.getId());
            }
        }
        
        // 如果该背包类型没有道具，返回空列表
        if (targetItemIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 从缓存获取玩家所有道具
        List<PlayerItem> allPlayerItems = getPlayerItems(playerId);
        
        // 过滤出指定背包类型的道具
        List<PlayerItem> result = new ArrayList<>();
        for (PlayerItem playerItem : allPlayerItems) {
            if (targetItemIds.contains(playerItem.getItemId())) {
                result.add(playerItem);
            }
        }
        
        return result;
    }
    
    @Override
    public Map<Integer, Map<String, Object>> getPlayerItemsWithConfig(Integer playerId) {
        List<PlayerItem> playerItems = getPlayerItems(playerId);
        Map<Integer, Map<String, Object>> result = new HashMap<>();
        
        for (PlayerItem playerItem : playerItems) {
            Item itemConfig = getItemConfig(playerItem.getItemId());
            if (itemConfig != null) {
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("playerItem", playerItem);
                itemData.put("itemConfig", itemConfig);
                itemData.put("backpackTypeConfig", getBackpackTypeConfig(itemConfig.getBackpackTypeId()));
                result.put(playerItem.getItemId(), itemData);
            }
        }
        
        return result;
    }
    
    @Override
    public boolean addItem(Integer playerId, Integer itemId, Integer quantity) {
        if (quantity <= 0) {
            return false;
        }
        
        // 检查道具配置是否存在
        Item itemConfig = getItemConfig(itemId);
        if (itemConfig == null) {
            return false;
        }
        
        // 从缓存检查玩家是否已有该道具
        String cacheKey = playerId + ":" + itemId;
        Integer cachedQuantity = itemQuantityCache.get(cacheKey);
        
        boolean success;
        if (cachedQuantity != null) {
            // 玩家已有该道具，增加数量
            success = playerItemMapper.addQuantity(playerId, itemId, quantity) > 0;
            if (success) {
                // 更新缓存
                updateCacheAfterAdd(playerId, itemId, quantity);
            }
        } else {
            // 玩家没有该道具，检查数据库确认
            PlayerItem existingItem = playerItemMapper.selectByPlayerIdAndItemId(playerId, itemId);
            
            if (existingItem != null) {
                // 数据库中有但缓存中没有，先更新缓存
                itemQuantityCache.put(cacheKey, existingItem.getQuantity());
                
                // 增加数量
                success = playerItemMapper.addQuantity(playerId, itemId, quantity) > 0;
                if (success) {
                    // 更新缓存
                    updateCacheAfterAdd(playerId, itemId, quantity);
                }
            } else {
                // 新增道具
                PlayerItem newItem = new PlayerItem();
                newItem.setPlayerId(playerId);
                newItem.setItemId(itemId);
                newItem.setQuantity(quantity);
                success = playerItemMapper.insert(newItem) > 0;
                if (success) {
                    // 更新缓存
                    updateCacheAfterAdd(playerId, itemId, quantity);
                }
            }
        }
        
        return success;
    }
    
    @Override
    public boolean useItem(Integer playerId, Integer itemId, Integer quantity) {
        if (quantity <= 0) {
            return false;
        }
        
        // 检查是否拥有足够数量
        if (!hasEnoughItems(playerId, itemId, quantity)) {
            return false;
        }
        
        // 减少数量
        int result = playerItemMapper.reduceQuantity(playerId, itemId, quantity);
        
        if (result > 0) {
            // 更新缓存
            updateCacheAfterUse(playerId, itemId, quantity);
            
            // 清理数量为0的道具
            playerItemMapper.deleteZeroQuantityItems(playerId);
        }
        
        return result > 0;
    }
    
    @Override
    public boolean hasEnoughItems(Integer playerId, Integer itemId, Integer requiredQuantity) {
        // 先从缓存获取
        String cacheKey = playerId + ":" + itemId;
        Integer cachedQuantity = itemQuantityCache.get(cacheKey);
        
        if (cachedQuantity != null) {
            return cachedQuantity >= requiredQuantity;
        }
        
        // 缓存未命中，从数据库查询
        Integer currentQuantity = playerItemMapper.getItemQuantity(playerId, itemId);
        if (currentQuantity != null) {
            // 更新缓存
            itemQuantityCache.put(cacheKey, currentQuantity);
        }
        
        return currentQuantity != null && currentQuantity >= requiredQuantity;
    }
    
    @Override
    public Item getItemConfig(Integer itemId) {
        // 确保配置缓存已加载
        ensureItemConfigsLoaded();
        return itemConfigMapCache.get(itemId);
    }
    
    @Override
    public BackpackType getBackpackTypeConfig(Integer backpackTypeId) {
        // 确保配置缓存已加载
        ensureBackpackTypeConfigsLoaded();
        return backpackTypeConfigMapCache.get(backpackTypeId);
    }
    
    @Override
    public List<Item> getAllItemConfigs() {
        // 确保配置缓存已加载
        ensureItemConfigsLoaded();
        return new ArrayList<>(itemConfigsCache);
    }
    
    @Override
    public List<BackpackType> getAllBackpackTypeConfigs() {
        // 确保配置缓存已加载
        ensureBackpackTypeConfigsLoaded();
        return new ArrayList<>(backpackTypeConfigsCache);
    }
    
    /**
     * 确保道具配置已加载到缓存
     */
    private void ensureItemConfigsLoaded() {
        if (itemConfigsCache == null) {
            synchronized (this) {
                if (itemConfigsCache == null) {
                    itemConfigsCache = jsonConfigLoader.getConfigList("items", Item.class);
                    itemConfigMapCache = new HashMap<>();
                    for (Item item : itemConfigsCache) {
                        itemConfigMapCache.put(item.getId(), item);
                    }
                }
            }
        }
    }
    
    /**
     * 确保背包类型配置已加载到缓存
     */
    private void ensureBackpackTypeConfigsLoaded() {
        if (backpackTypeConfigsCache == null) {
            synchronized (this) {
                if (backpackTypeConfigsCache == null) {
                    backpackTypeConfigsCache = jsonConfigLoader.getConfigList("backpack_types", BackpackType.class);
                    backpackTypeConfigMapCache = new HashMap<>();
                    for (BackpackType backpackType : backpackTypeConfigsCache) {
                        backpackTypeConfigMapCache.put(backpackType.getId(), backpackType);
                    }
                }
            }
        }
    }
    
    /**
     * 玩家离线时清理缓存
     * @param playerId 玩家ID
     */
    public void clearPlayerCache(Integer playerId) {
        // 清理背包数据缓存
        playerItemsCache.remove(playerId);
        
        // 清理该玩家的所有道具数量缓存
        List<String> keysToRemove = new ArrayList<>();
        for (String key : itemQuantityCache.keySet()) {
            if (key.startsWith(playerId + ":")) {
                keysToRemove.add(key);
            }
        }
        for (String key : keysToRemove) {
            itemQuantityCache.remove(key);
        }
    }
    
    /**
     * 更新数量缓存
     */
    private void updateQuantityCache(Integer playerId, List<PlayerItem> items) {
        for (PlayerItem item : items) {
            String cacheKey = playerId + ":" + item.getItemId();
            itemQuantityCache.put(cacheKey, item.getQuantity());
        }
    }
    
    /**
     * 添加道具后更新缓存
     */
    private void updateCacheAfterAdd(Integer playerId, Integer itemId, Integer quantity) {
        // 更新背包数据缓存
        List<PlayerItem> cachedItems = playerItemsCache.get(playerId);
        if (cachedItems != null) {
            boolean found = false;
            for (PlayerItem item : cachedItems) {
                if (item.getItemId().equals(itemId)) {
                    item.setQuantity(item.getQuantity() + quantity);
                    found = true;
                    break;
                }
            }
            if (!found) {
                PlayerItem newItem = new PlayerItem();
                newItem.setPlayerId(playerId);
                newItem.setItemId(itemId);
                newItem.setQuantity(quantity);
                cachedItems.add(newItem);
            }
        }
        
        // 更新数量缓存
        String cacheKey = playerId + ":" + itemId;
        Integer currentQuantity = itemQuantityCache.get(cacheKey);
        if (currentQuantity != null) {
            itemQuantityCache.put(cacheKey, currentQuantity + quantity);
        } else {
            itemQuantityCache.put(cacheKey, quantity);
        }
    }
    
    /**
     * 使用道具后更新缓存
     */
    private void updateCacheAfterUse(Integer playerId, Integer itemId, Integer quantity) {
        // 更新背包数据缓存
        List<PlayerItem> cachedItems = playerItemsCache.get(playerId);
        if (cachedItems != null) {
            for (PlayerItem item : cachedItems) {
                if (item.getItemId().equals(itemId)) {
                    int newQuantity = Math.max(0, item.getQuantity() - quantity);
                    item.setQuantity(newQuantity);
                    
                    // 如果数量为0，从缓存中移除
                    if (newQuantity == 0) {
                        cachedItems.remove(item);
                    }
                    break;
                }
            }
        }
        
        // 更新数量缓存
        String cacheKey = playerId + ":" + itemId;
        Integer currentQuantity = itemQuantityCache.get(cacheKey);
        if (currentQuantity != null) {
            int newQuantity = Math.max(0, currentQuantity - quantity);
            if (newQuantity == 0) {
                itemQuantityCache.remove(cacheKey);
            } else {
                itemQuantityCache.put(cacheKey, newQuantity);
            }
        }
    }
} 