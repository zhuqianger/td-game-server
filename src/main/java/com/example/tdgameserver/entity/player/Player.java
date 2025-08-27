package com.example.tdgameserver.entity.player;

import lombok.Data;
import org.springframework.data.relational.core.sql.In;

import java.time.LocalDateTime;

/**
 * 玩家实体类
 */
@Data
public class Player {
    
    /**
     * 玩家ID
     */
    private Integer id;
    
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
    private Integer createTime;
    
    /**
     * 更新时间
     */
    private Integer updateTime;
    
    // 构造函数
    public Player() {}
    
    public Player(Integer id, String playerName, String password) {
        this.id = id;
        this.playerName = playerName;
        this.password = password;
        this.createTime = 0;
        this.updateTime = 0;
    }

    public Player(Integer id, String playerName, String password, Integer createTime, Integer updateTime) {
        this.id = id;
        this.playerName = playerName;
        this.password = password;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
    
    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", playerName='" + playerName + '\'' +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 