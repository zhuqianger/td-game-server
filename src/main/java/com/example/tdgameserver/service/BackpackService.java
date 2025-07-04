package com.example.tdgameserver.service;

import com.example.tdgameserver.entity.Item;
import com.example.tdgameserver.entity.BackpackType;
import com.example.tdgameserver.entity.PlayerItem;

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
     * 根据背包类型获取玩家道具
     * @param playerId 玩家ID
     * @param backpackTypeId 背包类型ID
     * @return 道具列表
     */
    List<PlayerItem> getPlayerItemsByBackpackType(Integer playerId, Integer backpackTypeId);
    
    /**
     * 获取玩家道具（包含配置信息）
     * @param playerId 玩家ID
     * @return 道具信息Map，key为道具ID，value为包含配置信息的道具数据
     */
    Map<Integer, Map<String, Object>> getPlayerItemsWithConfig(Integer playerId);
    
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