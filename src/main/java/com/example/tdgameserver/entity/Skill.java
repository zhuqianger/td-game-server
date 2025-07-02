package com.example.tdgameserver.entity;

import lombok.Data;

import java.util.List;

/**
 * 技能实体类
 */
@Data
public class Skill {
    private Integer id;                  // 技能ID（主键）
    private String skillName;            // 技能名称
    private SkillType skillType;         // 技能类型
    private String description;          // 技能描述
    
    // 技能属性
    private Float cooldown;              // 冷却时间
    private Float duration;              // 持续时间
    private Integer spCost;              // 技力消耗
    private Integer spRecovery;          // 技力回复
    
    // 触发条件
    private Boolean autoTrigger;         // 自动触发
    private Float triggerChance;         // 触发概率
    private String triggerCondition;     // 触发条件
    
    // 技能效果列表
    private List<SkillEffect> effects;   // 技能效果列表
    
    // 构造函数
    public Skill() {}
    
    public Skill(Integer id, String skillName, SkillType skillType) {
        this.id = id;
        this.skillName = skillName;
        this.skillType = skillType;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", skillName='" + skillName + '\'' +
                ", skillType=" + skillType +
                ", description='" + description + '\'' +
                ", cooldown=" + cooldown +
                ", duration=" + duration +
                ", spCost=" + spCost +
                ", spRecovery=" + spRecovery +
                ", autoTrigger=" + autoTrigger +
                ", triggerChance=" + triggerChance +
                ", triggerCondition='" + triggerCondition + '\'' +
                ", effects=" + effects +
                '}';
    }
} 