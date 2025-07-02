package com.example.tdgameserver.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 玩家实体类
 */
@Data
public class Player {
    
    /**
     * 玩家ID
     */
    private Integer playerId;
    
    /**
     * 玩家名称
     */
    private String playerName;
    
    /**
     * 玩家密码
     */
    private String password;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    // 构造函数
    public Player() {}
    
    public Player(Integer playerId, String playerName, String password) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.password = password;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public Player(Integer playerId, String playerName, String password, LocalDateTime createTime, LocalDateTime updateTime) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.password = password;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
    
    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", playerName='" + playerName + '\'' +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 