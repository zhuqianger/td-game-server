package com.example.tdgameserver.requestEntity;

import lombok.Data;
import java.util.List;

/**
 * 干员升级请求实体类
 * 用于处理干员升级相关请求
 */
@Data
public class OperatorLevelUpRequest {
    /**
     * 干员ID
     */
    private Integer operatorId;
    
    /**
     * 经验道具列表
     */
    private List<ExpItem> expItems;
    
    /**
     * 经验道具内部类
     */
    @Data
    public static class ExpItem {
        /**
         * 道具ID
         */
        private Integer itemId;
        
        /**
         * 道具数量
         */
        private Integer count;
    }
} 