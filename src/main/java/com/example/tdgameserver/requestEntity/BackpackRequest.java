package com.example.tdgameserver.requestEntity;

import lombok.Data;

/**
 * 背包请求实体类
 * 用于处理背包相关请求
 */
@Data
public class BackpackRequest {
    /**
     * 背包类型ID
     */
    private Integer backpackTypeId;
    
    /**
     * 道具ID
     */
    private Integer itemId;
    
    /**
     * 道具数量
     */
    private Integer count;
} 