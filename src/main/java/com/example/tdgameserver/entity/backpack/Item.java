package com.example.tdgameserver.entity.backpack;

import lombok.Data;

/**
 * 道具配置实体类
 */
@Data
public class Item {
    
    /**
     * 道具ID
     */
    private Integer id;
    
    /**
     * 道具名称
     */
    private String name;
    
    /**
     * 道具品质
     */
    private Integer quality;
    
    /**
     * 所属背包类型ID
     */
    private Integer backpackTypeId;
    
    /**
     * 道具描述
     */
    private String description;
} 