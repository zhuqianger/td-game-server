package com.example.tdgameserver.service;

import com.example.tdgameserver.entity.Player;

import java.util.List;

/**
 * 玩家服务接口
 */
public interface PlayerService {
    
    /**
     * 根据玩家ID获取玩家信息
     * @param playerId 玩家ID
     * @return 玩家信息
     */
    Player getPlayerById(Integer playerId);
    
    /**
     * 根据玩家名称获取玩家信息
     * @param playerName 玩家名称
     * @return 玩家信息
     */
    Player getPlayerByName(String playerName);
    
    /**
     * 创建新玩家
     * @param playerName 玩家名称
     * @param password 密码
     * @return 创建的玩家信息
     */
    Player createPlayer(String playerName, String password);
    
    /**
     * 更新玩家信息
     * @param player 玩家信息
     * @return 是否更新成功
     */
    boolean updatePlayer(Player player);
    
    /**
     * 删除玩家
     * @param playerId 玩家ID
     * @return 是否删除成功
     */
    boolean deletePlayer(Integer playerId);
    
    /**
     * 获取所有玩家
     * @return 玩家列表
     */
    List<Player> getAllPlayers();
    
    /**
     * 验证玩家登录
     * @param playerName 玩家名称
     * @param password 密码
     * @return 验证成功返回玩家信息，失败返回null
     */
    Player authenticatePlayer(String playerName, String password);
    
    /**
     * 检查玩家名称是否已存在
     * @param playerName 玩家名称
     * @return 是否存在
     */
    boolean isPlayerNameExists(String playerName);
} 