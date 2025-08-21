package com.example.tdgameserver.entity.operator;

import lombok.Data;

/**
 * 干员属性成长配置实体类
 */
@Data
public class OperatorGrowth {

    /**
     * 配置唯一ID
     */
    private Integer id;

    /**
     * 干员ID
     */
    private Integer operatorId;
    
    /**
     * 精英化等级
     */
    private Integer eliteLevel;
    
    /**
     * 每级生命值成长
     */
    private Integer hpGrowthPerLevel;
    
    /**
     * 每级攻击力成长
     */
    private Integer attackGrowthPerLevel;
    
    /**
     * 每级防御力成长
     */
    private Integer defenseGrowthPerLevel;
    
    /**
     * 每级法术抗性成长
     */
    private Integer magicResistanceGrowthPerLevel;
} 