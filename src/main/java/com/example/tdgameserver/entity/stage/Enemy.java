package com.example.tdgameserver.entity.stage;

import lombok.Data;
import java.util.List;

/**
 * 敌人配置实体类
 */
@Data
public class Enemy {

    /**
     * 敌人ID
     */
    private Integer enemyId;

    /**
     * 敌人名称
     */
    private String enemyName;

    /**
     * 敌人类型
     */
    private Integer enemyType;

    /**
     * 生命值
     */
    private Integer hp;

    /**
     * 攻击力
     */
    private Integer attack;

    /**
     * 防御力
     */
    private Integer defense;

    /**
     * 魔法抗性
     */
    private Integer magicResistance;

    /**
     * 移动速度
     */
    private Float moveSpeed;

    /**
     * 阻挡数量
     */
    private Integer blockCount;

    /**
     * 攻击范围，相对于敌人位置的坐标列表 [[x_offset, y_offset], ...]
     */
    private List<List<Integer>> attackRange;
}
