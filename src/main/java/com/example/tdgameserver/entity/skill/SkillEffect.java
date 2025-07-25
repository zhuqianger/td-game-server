package com.example.tdgameserver.entity.skill;

import lombok.Data;

/**
 * 技能效果实体类
 */
@Data
public class SkillEffect {
    private String effectId;             // 效果ID
    private SkillEffectType effectType;  // 效果类型
    private Float value;                 // 效果数值
    private Float duration;              // 持续时间
    private String description;          // 效果描述
    private String targetType;           // 目标类型
    private String condition;            // 触发条件
    
    // 构造函数
    public SkillEffect() {}
    
    public SkillEffect(SkillEffectType effectType, Float value) {
        this.effectType = effectType;
        this.value = value;
    }

    @Override
    public String toString() {
        return "SkillEffect{" +
                "effectId='" + effectId + '\'' +
                ", effectType=" + effectType +
                ", value=" + value +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                ", targetType='" + targetType + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }
} 