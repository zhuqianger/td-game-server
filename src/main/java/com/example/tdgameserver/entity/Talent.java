package com.example.tdgameserver.entity;

import lombok.Data;

import java.util.List;

/**
 * 天赋实体类
 */
@Data
public class Talent {
    private Integer id;                  // 天赋ID（主键）
    private String talentName;            // 天赋名称
    private TalentType talentType;        // 天赋类型
    private String description;           // 天赋描述
    
    // 天赋效果列表
    private List<TalentEffect> effects;   // 天赋效果列表
    
    // 触发条件
    private Boolean isAlwaysActive;       // 是否始终激活
    private String activationCondition;   // 激活条件
    private Float activationChance;       // 激活概率
    
    // 解锁条件
    private Integer requiredEliteLevel;   // 需要的精英化等级
    private Integer requiredLevel;        // 需要的等级
    
    // 构造函数
    public Talent() {}
    
    public Talent(Integer id, String talentName, TalentType talentType) {
        this.id = id;
        this.talentName = talentName;
        this.talentType = talentType;
    }
    
    @Override
    public String toString() {
        return "Talent{" +
                "id=" + id +
                ", talentName='" + talentName + '\'' +
                ", talentType=" + talentType +
                ", description='" + description + '\'' +
                ", effects=" + effects +
                ", isAlwaysActive=" + isAlwaysActive +
                ", activationCondition='" + activationCondition + '\'' +
                ", activationChance=" + activationChance +
                ", requiredEliteLevel=" + requiredEliteLevel +
                ", requiredLevel=" + requiredLevel +
                '}';
    }
} 