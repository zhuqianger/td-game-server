package com.example.tdgameserver.entity.operator;

import lombok.Data;

import java.util.List;
/**
 * 干员基础信息实体类
 */
@Data
public class Operator {
    private Integer id;                  // 配置ID
    private String operatorName;         // 干员名称
    private Integer professionId;        // 职业ID
    private Integer rarity;              // 稀有度 (1-6星)
    
    // 基础信息
    // 部署费用和阻挡数通过精英化等级在 operator_base_attributes.json 中配置
    // 攻击间隔和再部署时间通过职业在 professions.json 中配置
    
    // 技能和天赋ID列表
    private List<Integer> skillIds;      // 技能ID列表
    private List<Integer> talentIds;     // 天赋ID列表
    
    // 构造函数
    public Operator() {}
    
    public Operator(String operatorName, Integer professionId, Integer rarity) {
        this.operatorName = operatorName;
        this.professionId = professionId;
        this.rarity = rarity;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "operatorName='" + operatorName + '\'' +
                ", professionId=" + professionId +
                ", rarity=" + rarity +
                ", skillIds=" + skillIds +
                ", talentIds=" + talentIds +
                '}';
    }
} 