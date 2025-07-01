package com.example.tdgameserver.service;

import com.example.tdgameserver.entity.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PlayerService测试类
 */
@SpringBootTest
@ActiveProfiles("test")
public class PlayerServiceTest {
    
    @Autowired
    private PlayerService playerService;
    
    @Test
    public void testCreatePlayer() {
        // 测试创建玩家
        String playerName = "testPlayer" + System.currentTimeMillis();
        String password = "testPassword";
        
        Player player = playerService.createPlayer(playerName, password);
        
        assertNotNull(player);
        assertEquals(playerName, player.getPlayerName());
        assertEquals(password, player.getPassword());
        assertNotNull(player.getPlayerId());
        assertNotNull(player.getCreateTime());
        assertNotNull(player.getUpdateTime());
    }
    
    @Test
    public void testGetPlayerById() {
        // 先创建一个玩家
        String playerName = "testPlayer" + System.currentTimeMillis();
        String password = "testPassword";
        Player createdPlayer = playerService.createPlayer(playerName, password);
        
        // 根据ID查询玩家
        Player foundPlayer = playerService.getPlayerById(createdPlayer.getPlayerId());
        
        assertNotNull(foundPlayer);
        assertEquals(createdPlayer.getPlayerId(), foundPlayer.getPlayerId());
        assertEquals(createdPlayer.getPlayerName(), foundPlayer.getPlayerName());
    }
    
    @Test
    public void testGetPlayerByName() {
        // 先创建一个玩家
        String playerName = "testPlayer" + System.currentTimeMillis();
        String password = "testPassword";
        Player createdPlayer = playerService.createPlayer(playerName, password);
        
        // 根据名称查询玩家
        Player foundPlayer = playerService.getPlayerByName(playerName);
        
        assertNotNull(foundPlayer);
        assertEquals(createdPlayer.getPlayerId(), foundPlayer.getPlayerId());
        assertEquals(createdPlayer.getPlayerName(), foundPlayer.getPlayerName());
    }
    
    @Test
    public void testLogin() {
        // 先创建一个玩家
        String playerName = "testPlayer" + System.currentTimeMillis();
        String password = "testPassword";
        Player createdPlayer = playerService.createPlayer(playerName, password);
        
        // 测试登录成功
        Player loginPlayer = playerService.login(playerName, password);
        assertNotNull(loginPlayer);
        assertEquals(createdPlayer.getPlayerId(), loginPlayer.getPlayerId());
        
        // 测试登录失败
        Player failedLogin = playerService.login(playerName, "wrongPassword");
        assertNull(failedLogin);
    }
    
    @Test
    public void testIsPlayerNameExists() {
        // 先创建一个玩家
        String playerName = "testPlayer" + System.currentTimeMillis();
        String password = "testPassword";
        playerService.createPlayer(playerName, password);
        
        // 测试玩家名称存在
        assertTrue(playerService.isPlayerNameExists(playerName));
        
        // 测试玩家名称不存在
        assertFalse(playerService.isPlayerNameExists("nonExistentPlayer"));
    }
    
    @Test
    public void testUpdatePlayer() {
        // 先创建一个玩家
        String playerName = "testPlayer" + System.currentTimeMillis();
        String password = "testPassword";
        Player createdPlayer = playerService.createPlayer(playerName, password);
        
        // 更新玩家信息
        String newPassword = "newPassword";
        createdPlayer.setPassword(newPassword);
        
        boolean updateResult = playerService.updatePlayer(createdPlayer);
        assertTrue(updateResult);
        
        // 验证更新结果
        Player updatedPlayer = playerService.getPlayerById(createdPlayer.getPlayerId());
        assertEquals(newPassword, updatedPlayer.getPassword());
    }
    
    @Test
    public void testDeletePlayer() {
        // 先创建一个玩家
        String playerName = "testPlayer" + System.currentTimeMillis();
        String password = "testPassword";
        Player createdPlayer = playerService.createPlayer(playerName, password);
        
        // 删除玩家
        boolean deleteResult = playerService.deletePlayer(createdPlayer.getPlayerId());
        assertTrue(deleteResult);
        
        // 验证删除结果
        Player deletedPlayer = playerService.getPlayerById(createdPlayer.getPlayerId());
        assertNull(deletedPlayer);
    }
} 