package com.example.tdgameserver.service.impl;

import com.example.tdgameserver.config.JsonConfigLoader;
import com.example.tdgameserver.entity.BackpackType;
import com.example.tdgameserver.entity.Item;
import com.example.tdgameserver.entity.PlayerItem;
import com.example.tdgameserver.mapper.PlayerItemMapper;
import com.example.tdgameserver.service.BackpackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 背包服务实现类
 */
@Service
public class BackpackServiceImpl implements BackpackService {
    
    @Autowired
    private PlayerItemMapper playerItemMapper;
    
    @Autowired
    private JsonConfigLoader jsonConfigLoader;
    
    @Override
    public List<PlayerItem> getPlayerItems(Integer playerId) {
        return playerItemMapper.selectByPlayerId(playerId);
    }
    
    @Override
    public List<PlayerItem> getPlayerItemsByBackpackType(Integer playerId, Integer backpackTypeId) {
        return playerItemMapper.selectByPlayerIdAndBackpackTypeId(playerId, backpackTypeId);
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
        
        // 查询玩家是否已有该道具
        PlayerItem existingItem = playerItemMapper.selectByPlayerIdAndItemId(playerId, itemId);
        
        if (existingItem != null) {
            // 更新数量
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setUpdateTime(LocalDateTime.now());
            return playerItemMapper.update(existingItem) > 0;
        } else {
            // 新增道具
            PlayerItem newItem = new PlayerItem();
            newItem.setPlayerId(playerId);
            newItem.setItemId(itemId);
            newItem.setQuantity(quantity);
            newItem.setCreateTime(LocalDateTime.now());
            newItem.setUpdateTime(LocalDateTime.now());
            return playerItemMapper.insert(newItem) > 0;
        }
    }
    
    @Override
    public boolean useItem(Integer playerId, Integer itemId, Integer quantity) {
        if (quantity <= 0) {
            return false;
        }
        
        // 查询玩家道具
        PlayerItem playerItem = playerItemMapper.selectByPlayerIdAndItemId(playerId, itemId);
        if (playerItem == null || playerItem.getQuantity() < quantity) {
            return false;
        }
        
        // 更新数量
        int newQuantity = playerItem.getQuantity() - quantity;
        if (newQuantity == 0) {
            // 数量为0时删除记录
            return playerItemMapper.deleteByPlayerIdAndItemId(playerId, itemId) > 0;
        } else {
            // 更新数量
            playerItem.setQuantity(newQuantity);
            playerItem.setUpdateTime(LocalDateTime.now());
            return playerItemMapper.update(playerItem) > 0;
        }
    }
    
    @Override
    public Item getItemConfig(Integer itemId) {
        return jsonConfigLoader.getConfig("items", itemId, Item.class);
    }
    
    @Override
    public BackpackType getBackpackTypeConfig(Integer backpackTypeId) {
        return jsonConfigLoader.getConfig("backpack_types", backpackTypeId, BackpackType.class);
    }
    
    @Override
    public List<Item> getAllItemConfigs() {
        return jsonConfigLoader.getConfigList("items", Item.class);
    }
    
    @Override
    public List<BackpackType> getAllBackpackTypeConfigs() {
        return jsonConfigLoader.getConfigList("backpack_types", BackpackType.class);
    }
} 