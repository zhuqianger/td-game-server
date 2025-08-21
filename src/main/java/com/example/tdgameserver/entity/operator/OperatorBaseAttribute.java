package com.example.tdgameserver.entity.operator;

import lombok.Data;
import java.util.List;

/**
 * 干员基础属性配置实体类
 */
@Data
public class OperatorBaseAttribute {

    /**
     * 配置唯一ID
     */
    private Integer id;

    /**
     * 干员ID
     */
    private Integer operatorId;

    /**
     * 精英化等级(0-2)
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

    /**
     * 攻击范围 [X轴, Y轴]，以人物为原心
     */
    private List<Integer> attackRange;
    
    /**
     * 部署费用
     */
    private Integer deployCost;
    
    /**
     * 阻挡数量
     */
    private Integer blockCount;
}
