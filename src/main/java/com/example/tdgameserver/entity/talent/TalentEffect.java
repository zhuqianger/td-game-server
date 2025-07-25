package com.example.tdgameserver.entity.talent;

import lombok.Data;

/**
 * 天赋效果实体类
 */
@Data
public class TalentEffect {
    private String effectId;             // 效果ID
    private String effectName;           // 效果名称
    private Float value;                 // 效果数值
    private String targetType;           // 目标类型
    private String condition;            // 触发条件
    private String description;          // 效果描述
    private Float duration;              // 持续时间
    
    // 构造函数
    public TalentEffect() {}
    
    public TalentEffect(String effectName, Float value) {
        this.effectName = effectName;
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "TalentEffect{" +
                "effectId='" + effectId + '\'' +
                ", effectName='" + effectName + '\'' +
                ", value=" + value +
                ", targetType='" + targetType + '\'' +
                ", condition='" + condition + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                '}';
    }
} 