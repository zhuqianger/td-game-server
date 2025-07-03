package com.example.tdgameserver.entity;

import lombok.Data;

import java.util.List;

/**
 * 技能实体类
 */
@Data
public class Skill {
    private Integer id;                  // 配置ID
    private String skillId;              // 技能ID
    private String skillName;            // 技能名称
    private String skillType;            // 技能类型（PASSIVE/ACTIVE）
    private String description;          // 技能描述
    private Integer spCost;              // 技能费用
    private Integer duration;            // 持续时间
    
    // 技能属性
    private Float cooldown;              // 冷却时间
    private Float triggerChance;         // 触发概率
    private Integer spRecovery;          // 技力回复
    
    // 触发条件
    private Boolean autoTrigger;         // 自动触发
    private String triggerCondition;     // 触发条件
    
    // 技能效果列表
    private List<SkillEffect> effects;   // 技能效果列表
    
    // 构造函数
    public Skill() {}
    
    public Skill(String skillId, String skillName, String skillType, String description) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.skillType = skillType;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", skillId='" + skillId + '\'' +
                ", skillName='" + skillName + '\'' +
                ", skillType='" + skillType + '\'' +
                ", description='" + description + '\'' +
                ", spCost=" + spCost +
                ", duration=" + duration +
                ", cooldown=" + cooldown +
                ", triggerChance=" + triggerChance +
                ", spRecovery=" + spRecovery +
                ", autoTrigger=" + autoTrigger +
                ", triggerCondition='" + triggerCondition + '\'' +
                ", effects=" + effects +
                '}';
    }
} 