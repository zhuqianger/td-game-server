package com.example.tdgameserver.requestEntity;

import lombok.Data;

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
} 