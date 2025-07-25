package com.example.tdgameserver.entity.operator;

import lombok.Data;

/**
 * 干员升级结果实体类
 * 用于返回干员升级后的信息
 */
@Data
public class OperatorLevelUpResult {
    /**
     * 是否升级成功
     */
    private boolean success;
    
    /**
     * 升级前的等级
     */
    private Integer oldLevel;
    
    /**
     * 升级后的等级
     */
    private Integer newLevel;
    
    /**
     * 升级后的经验值
     */
    private Integer currentExp;
    
    /**
     * 消耗的经验道具总经验值
     */
    private Integer totalExpUsed;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 构造函数
     */
    public OperatorLevelUpResult() {}
    
    /**
     * 成功构造函数
     */
    public OperatorLevelUpResult(Integer oldLevel, Integer newLevel, Integer currentExp, Integer totalExpUsed) {
        this.success = true;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
        this.currentExp = currentExp;
        this.totalExpUsed = totalExpUsed;
    }
    
    /**
     * 失败构造函数
     */
    public OperatorLevelUpResult(String errorMessage) {
        this.success = false;
        this.errorMessage = errorMessage;
    }
} 