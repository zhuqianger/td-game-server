package com.example.tdgameserver.requestEntity;

import lombok.Data;

import java.util.List;

/**
 * 干员请求实体类
 * 用于处理干员相关请求
 */
@Data
public class OperatorRequest {
    /**
     * 干员ID
     */
    private Integer operatorId;
    
    /**
     * 技能ID（可选，用于技能相关操作）
     */
    private Integer skillId;

    /**
     * 经验道具列表(可选，升级时用到)
     */
    private List<ItemRequest> itemList;

} 