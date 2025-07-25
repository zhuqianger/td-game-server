package com.example.tdgameserver.service.impl;

import com.example.tdgameserver.config.ConfigService;
import com.example.tdgameserver.entity.stage.Stage;
import com.example.tdgameserver.entity.stage.PlayerStage;
import com.example.tdgameserver.mapper.PlayerStageMapper;
import com.example.tdgameserver.service.StageService;
import com.example.tdgameserver.requestEntity.StageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 关卡服务实现类
 */
@Service
public class StageServiceImpl implements StageService {

    @Autowired
    private PlayerStageMapper playerStageMapper;

    @Autowired
    private ConfigService configService;

    @Override
    public Stage getStageById(Integer stageId) {
        List<Stage> stages = configService.getConfigList("stages", Stage.class);
        return stages.stream()
                .filter(stage -> stage.getStageId().equals(stageId))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<PlayerStage> getPlayerStages(int playerId) {
        return playerStageMapper.selectByPlayerId(playerId);
    }
    
    @Override
    public PlayerStage getPlayerStage(int playerId, Integer stageId) {
        return playerStageMapper.selectByPlayerIdAndStageId(playerId, stageId);
    }
    
    @Override
    public boolean saveStageRecord(int playerId, StageRequest request) {
        // 检查关卡是否存在
        Stage stage = getStageById(request.getStageId());
        if (stage == null) {
            return false;
        }
        
        // 检查星级是否有效
        if (request.getStar() < 1 || request.getStar() > 3) {
            return false;
        }
        
        // 将干员ID列表转换为逗号分隔的字符串
        String operatorIdsStr = request.getOperatorIds().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        
        // 查询是否已有通关记录
        PlayerStage existingRecord = getPlayerStage(playerId, request.getStageId());
        
        if (existingRecord != null) {
            // 更新记录（如果新星级更高）
            if (request.getStar() > existingRecord.getStar()) {
                existingRecord.setStar(request.getStar());
                existingRecord.setOperatorIds(operatorIdsStr);
                existingRecord.setPassTime(LocalDateTime.now());
                return playerStageMapper.update(existingRecord) > 0;
            }
            return true; // 星级没有提升，但也不算失败
        } else {
            // 插入新记录
            PlayerStage newRecord = new PlayerStage();
            newRecord.setPlayerId(playerId);
            newRecord.setStageId(request.getStageId());
            newRecord.setStar(request.getStar());
            newRecord.setOperatorIds(operatorIdsStr);
            newRecord.setPassTime(LocalDateTime.now());
            return playerStageMapper.insert(newRecord) > 0;
        }
    }
    
    @Override
    public boolean hasPassedStage(int playerId, Integer stageId) {
        PlayerStage record = getPlayerStage(playerId, stageId);
        return record != null;
    }
    
    @Override
    public Integer getPlayerStageStar(int playerId, Integer stageId) {
        PlayerStage record = getPlayerStage(playerId, stageId);
        return record != null ? record.getStar() : 0;
    }
} 