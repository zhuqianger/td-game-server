package com.example.tdgameserver.entity;

import lombok.Data;

/**
 * 玩家干员关联实体类
 * 用于存储玩家拥有的干员信息
 */
@Data
public class PlayerOperator {
    private Integer id;                     // 主键ID
    private Integer playerId;               // 玩家ID
    private Integer operatorId;          // 干员ID
    private Integer eliteLevel;          // 精英化等级 (0-2)
    private Integer level;               // 等级 (1-90)
    private Integer skillLevel;          // 技能等级 (1-7)
    private Integer skillMastery;        // 技能专精 (0-3)
    private Integer currentHP;           // 当前生命值
    private Boolean isOwned;             // 是否拥有
    private Integer createTime;             // 创建时间
    private Integer updateTime;             // 更新时间
    
    // 构造函数
    public PlayerOperator() {}
    
    public PlayerOperator(Integer playerId, Integer operatorId) {
        this.playerId = playerId;
        this.operatorId = operatorId;
        this.eliteLevel = 0;
        this.level = 1;
        this.skillLevel = 1;
        this.skillMastery = 0;
        this.isOwned = true;
        this.createTime = (int) System.currentTimeMillis();
        this.updateTime = (int) System.currentTimeMillis();
    }
    
    @Override
    public String toString() {
        return "PlayerOperator{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", operatorId='" + operatorId + '\'' +
                ", eliteLevel=" + eliteLevel +
                ", level=" + level +
                ", skillLevel=" + skillLevel +
                ", skillMastery=" + skillMastery +
                ", currentHP=" + currentHP +
                ", isOwned=" + isOwned +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 