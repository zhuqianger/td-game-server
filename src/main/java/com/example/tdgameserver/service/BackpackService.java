package com.example.tdgameserver.service;

import com.example.tdgameserver.entity.backpack.Item;
import com.example.tdgameserver.entity.backpack.BackpackType;
import com.example.tdgameserver.entity.backpack.PlayerItem;

import java.util.List;
import java.util.Map;

/**
 * 背包服务接口
 */
public interface BackpackService {
    
    /**
     * 获取玩家所有道具
     * @param playerId 玩家ID
     * @return 道具列表
     */
    List<PlayerItem> getPlayerItems(Integer playerId);
    
    /**
     * 根据背包类型获取玩家道具（通过配置过滤）
     * @param playerId 玩家ID
     * @param backpackTypeId 背包类型ID
     * @return 道具列表
     */
    List<PlayerItem> getPlayerItemsByBackpackType(Integer playerId, Integer backpackTypeId);

    /**
     * 添加道具
     * @param playerId 玩家ID
     * @param itemId 道具ID
     * @param quantity 数量
     * @return 是否成功
     */
    boolean addItem(Integer playerId, Integer itemId, Integer quantity);
    
    /**
     * 使用道具
     * @param playerId 玩家ID
     * @param itemId 道具ID
     * @param quantity 使用数量
     * @return 是否成功
     */
    boolean useItem(Integer playerId, Integer itemId, Integer quantity);
    
    /**
     * 检查玩家是否拥有足够数量的道具
     * @param playerId 玩家ID
     * @param itemId 道具ID
     * @param requiredQuantity 需要的数量
     * @return 是否足够
     */
    boolean hasEnoughItems(Integer playerId, Integer itemId, Integer requiredQuantity);
    
    /**
     * 玩家离线时清理缓存
     * @param playerId 玩家ID
     */
    void clearPlayerCache(Integer playerId);
    
    /**
     * 获取道具配置
     * @param itemId 道具ID
     * @return 道具配置
     */
    Item getItemConfig(Integer itemId);
    
    /**
     * 获取背包类型配置
     * @param backpackTypeId 背包类型ID
     * @return 背包类型配置
     */
    BackpackType getBackpackTypeConfig(Integer backpackTypeId);
    
    /**
     * 获取所有道具配置
     * @return 道具配置列表
     */
    List<Item> getAllItemConfigs();
    
    /**
     * 获取所有背包类型配置
     * @return 背包类型配置列表
     */
    List<BackpackType> getAllBackpackTypeConfigs();
} 