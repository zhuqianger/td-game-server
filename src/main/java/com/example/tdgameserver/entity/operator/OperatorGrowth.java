package com.example.tdgameserver.entity.operator;

import lombok.Data;

/**
 * 干员属性成长配置
 */
@Data
public class OperatorGrowth {
    /**
     * 配置ID (rarity*1000 + eliteLevel*100 + level)
     */
    private Integer id;
    
    /**
     * 稀有度
     */
    private Integer rarity;
    
    /**
     * 精英化等级
     */
    private Integer eliteLevel;
    
    /**
     * 等级
     */
    private Integer level;
    
    /**
     * 生命值成长
     */
    private Integer hpGrowth;
    
    /**
     * 攻击力成长
     */
    private Integer attackGrowth;
    
    /**
     * 防御力成长
     */
    private Integer defenseGrowth;
    
    /**
     * 法术抗性成长
     */
    private Integer magicResistanceGrowth;
} 