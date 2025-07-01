package com.example.tdgameserver.service.impl;

import com.example.tdgameserver.entity.Player;
import com.example.tdgameserver.mapper.PlayerMapper;
import com.example.tdgameserver.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 玩家服务实现类
 */
@Service
public class PlayerServiceImpl implements PlayerService {
    
    @Autowired
    private PlayerMapper playerMapper;
    
    @Override
    public Player getPlayerById(Long playerId) {
        return playerMapper.selectByPlayerId(playerId);
    }
    
    @Override
    public Player getPlayerByName(String playerName) {
        return playerMapper.selectByPlayerName(playerName);
    }
    
    @Override
    public Player createPlayer(String playerName, String password) {
        // 检查玩家名称是否已存在
        if (isPlayerNameExists(playerName)) {
            return null;
        }
        
        // 创建新玩家
        Player player = new Player();
        player.setPlayerName(playerName);
        player.setPassword(password);
        player.setCreateTime(LocalDateTime.now());
        player.setUpdateTime(LocalDateTime.now());
        
        // TODO: 生成玩家ID的逻辑，这里暂时使用时间戳
        player.setPlayerId(System.currentTimeMillis());
        
        int result = playerMapper.insert(player);
        return result > 0 ? player : null;
    }
    
    @Override
    public boolean updatePlayer(Player player) {
        if (player == null || player.getPlayerId() == null) {
            return false;
        }
        
        player.setUpdateTime(LocalDateTime.now());
        return playerMapper.update(player) > 0;
    }
    
    @Override
    public boolean deletePlayer(Long playerId) {
        return playerMapper.deleteByPlayerId(playerId) > 0;
    }
    
    @Override
    public List<Player> getAllPlayers() {
        return playerMapper.selectAll();
    }
    
    @Override
    public Player login(String playerName, String password) {
        if (playerName == null || password == null) {
            return null;
        }
        
        return playerMapper.selectByPlayerNameAndPassword(playerName, password);
    }
    
    @Override
    public boolean isPlayerNameExists(String playerName) {
        if (playerName == null) {
            return false;
        }
        
        Player existingPlayer = playerMapper.selectByPlayerName(playerName);
        return existingPlayer != null;
    }
} 