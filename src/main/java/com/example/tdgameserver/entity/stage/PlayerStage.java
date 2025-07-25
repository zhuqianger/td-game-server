package com.example.tdgameserver.entity.stage;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 玩家关卡通关记录实体类
 */
@Data
public class PlayerStage {
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 玩家ID
     */
    private int playerId;
    
    /**
     * 关卡ID
     */
    private Integer stageId;
    
    /**
     * 通关星级（1~3）
     */
    private Integer star;
    
    /**
     * 通关时所用干员ID列表，逗号分隔
     */
    private String operatorIds;
    
    /**
     * 通关时间
     */
    private LocalDateTime passTime;
} 