package com.example.tdgameserver.entity.operator;

import lombok.Data;

/**
 * 职业配置实体类
 */
@Data
public class Profession {
    
    /**
     * 职业ID
     */
    private Integer id;
    
    /**
     * 职业名称
     */
    private String name;
    
    /**
     * 职业描述
     */
    private String description;
    
    /**
     * 排序顺序
     */
    private Integer sortOrder;
}
