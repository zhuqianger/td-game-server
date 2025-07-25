package com.example.tdgameserver.service;

import com.example.tdgameserver.entity.Stage;
import com.example.tdgameserver.entity.PlayerStage;
import com.example.tdgameserver.requestEntity.StageRequest;
import java.util.List;

/**
 * 关卡服务接口
 */
public interface StageService {

    /**
     * 根据关卡ID获取关卡配置
     */
    Stage getStageById(Integer stageId);
    
    /**
     * 获取玩家所有通关记录
     */
    List<PlayerStage> getPlayerStages(int playerId);
    
    /**
     * 获取玩家指定关卡的通关记录
     */
    PlayerStage getPlayerStage(int playerId, Integer stageId);
    
    /**
     * 保存关卡通关记录
     */
    boolean saveStageRecord(int playerId, StageRequest request);
    
    /**
     * 检查玩家是否已通关指定关卡
     */
    boolean hasPassedStage(int playerId, Integer stageId);
    
    /**
     * 获取玩家通关星级
     */
    Integer getPlayerStageStar(int playerId, Integer stageId);
} 