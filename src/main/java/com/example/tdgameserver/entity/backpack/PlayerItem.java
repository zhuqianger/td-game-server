package com.example.tdgameserver.entity.backpack;

import lombok.Data;

/**
 * 玩家道具实体类
 */
@Data
public class PlayerItem {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 玩家ID
     */
    private Integer playerId;
    
    /**
     * 道具ID
     */
    private Integer itemId;
    
    /**
     * 道具数量
     */
    private Integer quantity;
    
    /**
     * 创建时间
     */
    private Integer createTime;
    
    /**
     * 更新时间
     */
    private Integer updateTime;
} 