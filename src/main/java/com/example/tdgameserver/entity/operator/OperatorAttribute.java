package com.example.tdgameserver.entity.operator;

import lombok.Data;

/**
 * 干员个性化属性配置
 */
@Data
public class OperatorAttribute {
    /**
     * 干员ID
     */
    private Integer id;
    
    /**
     * 干员名称
     */
    private String name;
    
    /**
     * 稀有度
     */
    private Integer rarity;
    
    /**
     * 职业ID
     */
    private Integer professionId;
    
    /**
     * 生命值修正系数
     */
    private Double hpModifier;
    
    /**
     * 攻击力修正系数
     */
    private Double attackModifier;
    
    /**
     * 防御力修正系数
     */
    private Double defenseModifier;
    
    /**
     * 法术抗性修正系数
     */
    private Double magicResistanceModifier;
    
    /**
     * 描述
     */
    private String description;
} 