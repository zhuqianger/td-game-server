package com.example.tdgameserver.mapper;

import com.example.tdgameserver.entity.PlayerItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 玩家道具数据访问层接口
 */
@Mapper
public interface PlayerItemMapper {
    
    /**
     * 根据玩家ID查询所有道具
     * @param playerId 玩家ID
     * @return 道具列表
     */
    List<PlayerItem> selectByPlayerId(@Param("playerId") Integer playerId);
    
    /**
     * 根据玩家ID和道具ID查询道具
     * @param playerId 玩家ID
     * @param itemId 道具ID
     * @return 道具信息
     */
    PlayerItem selectByPlayerIdAndItemId(@Param("playerId") Integer playerId, @Param("itemId") Integer itemId);
    
    /**
     * 根据玩家ID和背包类型ID查询道具
     * @param playerId 玩家ID
     * @param backpackTypeId 背包类型ID
     * @return 道具列表
     */
    List<PlayerItem> selectByPlayerIdAndBackpackTypeId(@Param("playerId") Integer playerId, @Param("backpackTypeId") Integer backpackTypeId);
    
    /**
     * 插入新道具
     * @param playerItem 道具信息
     * @return 影响行数
     */
    int insert(PlayerItem playerItem);
    
    /**
     * 更新道具数量
     * @param playerItem 道具信息
     * @return 影响行数
     */
    int update(PlayerItem playerItem);
    
    /**
     * 根据玩家ID和道具ID删除道具
     * @param playerId 玩家ID
     * @param itemId 道具ID
     * @return 影响行数
     */
    int deleteByPlayerIdAndItemId(@Param("playerId") Integer playerId, @Param("itemId") Integer itemId);
    
    /**
     * 根据玩家ID删除所有道具
     * @param playerId 玩家ID
     * @return 影响行数
     */
    int deleteByPlayerId(@Param("playerId") Integer playerId);
} 