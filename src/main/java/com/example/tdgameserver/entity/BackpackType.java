package com.example.tdgameserver.entity;

import lombok.Data;

/**
 * 背包类型配置实体类
 */
@Data
public class BackpackType {
    
    /**
     * 背包类型ID
     */
    private Integer id;
    
    /**
     * 背包类型名称
     */
    private String name;
    
    /**
     * 背包类型代码
     */
    private String code;
    
    /**
     * 背包类型描述
     */
    private String description;
    
    /**
     * 排序顺序
     */
    private Integer sortOrder;
} 