package com.example.tdgameserver.mapper;

import com.example.tdgameserver.entity.PlayerStage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 玩家关卡通关记录Mapper接口
 */
@Mapper
public interface PlayerStageMapper {
    
    /**
     * 插入玩家关卡通关记录
     */
    int insert(PlayerStage playerStage);
    
    /**
     * 根据玩家ID和关卡ID查询通关记录
     */
    PlayerStage selectByPlayerIdAndStageId(@Param("playerId") int playerId, @Param("stageId") Integer stageId);
    
    /**
     * 根据玩家ID查询所有通关记录
     */
    List<PlayerStage> selectByPlayerId(@Param("playerId") int playerId);
    
    /**
     * 更新玩家关卡通关记录
     */
    int update(PlayerStage playerStage);
    
    /**
     * 根据玩家ID和关卡ID删除通关记录
     */
    int deleteByPlayerIdAndStageId(@Param("playerId") int playerId, @Param("stageId") Integer stageId);
} 