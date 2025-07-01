package com.example.tdgameserver.mapper;

import com.example.tdgameserver.entity.Player;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 玩家Mapper接口
 */
@Mapper
public interface PlayerMapper {
    
    /**
     * 根据玩家ID查询玩家
     * @param playerId 玩家ID
     * @return 玩家信息
     */
    Player selectByPlayerId(@Param("playerId") Long playerId);
    
    /**
     * 根据玩家名称查询玩家
     * @param playerName 玩家名称
     * @return 玩家信息
     */
    Player selectByPlayerName(@Param("playerName") String playerName);
    
    /**
     * 插入新玩家
     * @param player 玩家信息
     * @return 影响行数
     */
    int insert(Player player);
    
    /**
     * 更新玩家信息
     * @param player 玩家信息
     * @return 影响行数
     */
    int update(Player player);
    
    /**
     * 删除玩家
     * @param playerId 玩家ID
     * @return 影响行数
     */
    int deleteByPlayerId(@Param("playerId") Long playerId);
    
    /**
     * 查询所有玩家
     * @return 玩家列表
     */
    List<Player> selectAll();
    
    /**
     * 根据玩家名称和密码查询玩家（用于登录验证）
     * @param playerName 玩家名称
     * @param password 密码
     * @return 玩家信息
     */
    Player selectByPlayerNameAndPassword(@Param("playerName") String playerName, @Param("password") String password);
} 