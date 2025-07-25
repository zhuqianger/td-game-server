package com.example.tdgameserver.entity.operator;

import lombok.Data;

/**
 * 干员升级配置实体类
 */
@Data
public class OperatorLevel {
    private Integer id;                  // 配置ID
    private Integer rarity;              // 干员稀有度 (1-6星)
    private Integer eliteLevel;          // 精英化等级 (0-2)
    private Integer level;               // 干员等级 (1-10级)
    private Integer needExp;             // 升级所需经验
    
    // 构造函数
    public OperatorLevel() {}
    
    public OperatorLevel(Integer rarity, Integer eliteLevel, Integer level, Integer needExp) {
        this.rarity = rarity;
        this.eliteLevel = eliteLevel;
        this.level = level;
        this.needExp = needExp;
    }
    
    @Override
    public String toString() {
        return "OperatorLevel{" +
                "id=" + id +
                ", rarity=" + rarity +
                ", eliteLevel=" + eliteLevel +
                ", level=" + level +
                ", needExp=" + needExp +
                '}';
    }
} 