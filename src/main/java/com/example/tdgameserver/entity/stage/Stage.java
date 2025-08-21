package com.example.tdgameserver.entity.stage;

import lombok.Data;

/**
 * 关卡配置实体类
 */
@Data
public class Stage {

    /**
     * 关卡ID
     */
    private Integer id;

    /**
     * 关卡名称
     */
    private String stageName;

    /**
     * 关卡类型
     */
    private Integer stageType;

    /**
     * 难度等级
     */
    private Integer difficulty;

    /**
     * 章节编号
     */
    private Integer chapter;
} 