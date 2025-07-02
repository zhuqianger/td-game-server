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
    public Player getPlayerById(Integer playerId) {
        return playerMapper.selectByPlayerId(playerId);
    }
    
    @Override
    public Player getPlayerByName(String playerName) {
        return playerMapper.selectByPlayerName(playerName);
    }
    
    @Override
    public Player createPlayer(String playerName, String password) {
        // 检查玩家名称是否已存在
        Player existingPlayer = playerMapper.selectByPlayerName(playerName);
        if (existingPlayer != null) {
            return null; // 玩家名称已存在
        }
        
        // 创建新玩家（这里简化处理，实际应该生成唯一的playerId）
        Player newPlayer = new Player(null, playerName, password);
        int result = playerMapper.insert(newPlayer);
        
        if (result > 0) {
            // 重新查询获取完整的玩家信息（包括生成的ID）
            return playerMapper.selectByPlayerName(playerName);
        }
        
        return null;
    }
    
    @Override
    public boolean updatePlayer(Player player) {
        return playerMapper.update(player) > 0;
    }
    
    @Override
    public boolean deletePlayer(Integer playerId) {
        return playerMapper.deleteByPlayerId(playerId) > 0;
    }
    
    @Override
    public List<Player> getAllPlayers() {
        return playerMapper.selectAll();
    }
    
    @Override
    public Player authenticatePlayer(String playerName, String password) {
        Player player = playerMapper.selectByPlayerName(playerName);
        if (player != null && password.equals(player.getPassword())) {
            return player;
        }
        return null;
    }

    @Override
    public boolean isPlayerNameExists(String playerName) {
        return false;
    }
} 