package com.example.tdgameserver.entity;

import lombok.Data;

/**
 * 干员基础属性配置
 */
@Data
public class OperatorBase {
    /**
     * 配置ID
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
     * 基础生命值
     */
    private Integer baseHP;
    
    /**
     * 基础攻击力
     */
    private Integer baseAttack;
    
    /**
     * 基础防御力
     */
    private Integer baseDefense;
    
    /**
     * 基础法术抗性
     */
    private Integer baseMagicResistance;
} 