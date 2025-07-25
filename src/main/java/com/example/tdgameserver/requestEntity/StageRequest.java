package com.example.tdgameserver.requestEntity;

import lombok.Data;
import java.util.List;

/**
 * 关卡相关请求实体类
 */
@Data
public class StageRequest {
    /**
     * 关卡ID
     */
    private Integer stageId;
    
    /**
     * 通关星级
     */
    private Integer star;
    
    /**
     * 使用的干员ID列表
     */
    private List<Integer> operatorIds;
} 