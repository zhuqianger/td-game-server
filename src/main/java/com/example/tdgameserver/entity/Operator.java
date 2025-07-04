package com.example.tdgameserver.entity;

import lombok.Data;

import java.util.List;
/**
 * 干员基础信息实体类
 */
@Data
public class Operator {
    private Integer id;                  // 配置ID
    private String operatorName;         // 干员名称
    private OperatorProfession profession;           // 职业（字符串格式，便于JSON转换）
    private Integer rarity;              // 稀有度 (1-6星)
    
    // 基础属性
    private Integer maxHP;               // 最大生命值
    private Integer attack;              // 攻击力
    private Integer defense;             // 防御力
    private Integer magicResistance;     // 法术抗性
    private Integer deployCost;          // 部署费用
    private Integer blockCount;          // 阻挡数
    private Float attackInterval;        // 攻击间隔
    private Float redeployTime;          // 再部署时间
    
    // 攻击范围
    private Integer attackRangeX;        // 攻击范围X
    private Integer attackRangeY;        // 攻击范围Y
    
    // 技能和天赋ID列表
    private List<String> skillIds;       // 技能ID列表
    private List<String> talentIds;      // 天赋ID列表
    
    // 构造函数
    public Operator() {}
    
    public Operator(String operatorName, OperatorProfession profession, Integer rarity) {
        this.operatorName = operatorName;
        this.profession = profession;
        this.rarity = rarity;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "operatorName='" + operatorName + '\'' +
                ", profession=" + profession +
                ", rarity=" + rarity +
                ", maxHP=" + maxHP +
                ", attack=" + attack +
                ", defense=" + defense +
                ", magicResistance=" + magicResistance +
                ", deployCost=" + deployCost +
                ", blockCount=" + blockCount +
                ", attackInterval=" + attackInterval +
                ", redeployTime=" + redeployTime +
                ", attackRangeX=" + attackRangeX +
                ", attackRangeY=" + attackRangeY +
                ", skillIds=" + skillIds +
                ", talentIds=" + talentIds +
                '}';
    }
} 