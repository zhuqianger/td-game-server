package com.example.tdgameserver.mapper;

import com.example.tdgameserver.entity.backpack.PlayerItem;
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
     * 根据玩家ID和道具ID列表查询道具
     * @param playerId 玩家ID
     * @param itemIds 道具ID列表
     * @return 道具列表
     */
    List<PlayerItem> selectByPlayerIdAndItemIds(@Param("playerId") Integer playerId, @Param("itemIds") List<Integer> itemIds);
    
    /**
     * 查询玩家拥有的道具数量
     * @param playerId 玩家ID
     * @return 道具数量
     */
    int countByPlayerId(@Param("playerId") Integer playerId);
    
    /**
     * 查询玩家指定道具的数量
     * @param playerId 玩家ID
     * @param itemId 道具ID
     * @return 道具数量
     */
    int getItemQuantity(@Param("playerId") Integer playerId, @Param("itemId") Integer itemId);
    
    /**
     * 插入新道具
     * @param playerItem 道具信息
     * @return 影响行数
     */
    int insert(PlayerItem playerItem);
    
    /**
     * 更新道具数量
     * @param playerId 玩家ID
     * @param itemId 道具ID
     * @param quantity 新数量
     * @return 影响行数
     */
    int updateQuantity(@Param("playerId") Integer playerId, @Param("itemId") Integer itemId, @Param("quantity") Integer quantity);
    
    /**
     * 增加道具数量
     * @param playerId 玩家ID
     * @param itemId 道具ID
     * @param addQuantity 增加的数量
     * @return 影响行数
     */
    int addQuantity(@Param("playerId") Integer playerId, @Param("itemId") Integer itemId, @Param("addQuantity") Integer addQuantity);
    
    /**
     * 减少道具数量
     * @param playerId 玩家ID
     * @param itemId 道具ID
     * @param reduceQuantity 减少的数量
     * @return 影响行数
     */
    int reduceQuantity(@Param("playerId") Integer playerId, @Param("itemId") Integer itemId, @Param("reduceQuantity") Integer reduceQuantity);
    
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
    
    /**
     * 删除数量为0的道具
     * @param playerId 玩家ID
     * @return 影响行数
     */
    int deleteZeroQuantityItems(@Param("playerId") Integer playerId);
} 