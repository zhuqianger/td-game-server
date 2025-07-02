package com.example.tdgameserver.mapper;

import com.example.tdgameserver.entity.PlayerOperator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 玩家干员关联Mapper接口
 */
@Mapper
public interface PlayerOperatorMapper {
    
    /**
     * 根据ID查询玩家干员信息
     */
    PlayerOperator selectById(@Param("id") Long id);
    
    /**
     * 根据玩家ID和干员ID查询
     */
    PlayerOperator selectByPlayerIdAndOperatorId(@Param("playerId") Long playerId, @Param("operatorId") String operatorId);
    
    /**
     * 查询玩家拥有的所有干员
     */
    List<PlayerOperator> selectByPlayerId(@Param("playerId") Long playerId);
    
    /**
     * 查询玩家拥有的指定职业干员
     */
    List<PlayerOperator> selectByPlayerIdAndProfession(@Param("playerId") Long playerId, @Param("profession") String profession);
    
    /**
     * 查询玩家拥有的指定稀有度干员
     */
    List<PlayerOperator> selectByPlayerIdAndRarity(@Param("playerId") Long playerId, @Param("rarity") Integer rarity);
    
    /**
     * 插入玩家干员信息
     */
    int insert(PlayerOperator playerOperator);
    
    /**
     * 更新玩家干员信息
     */
    int update(PlayerOperator playerOperator);
    
    /**
     * 删除玩家干员信息
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据玩家ID和干员ID删除
     */
    int deleteByPlayerIdAndOperatorId(@Param("playerId") Long playerId, @Param("operatorId") String operatorId);
    
    /**
     * 更新干员等级
     */
    int updateLevel(@Param("id") Long id, @Param("level") Integer level);
    
    /**
     * 更新精英化等级
     */
    int updateEliteLevel(@Param("id") Long id, @Param("eliteLevel") Integer eliteLevel);
    
    /**
     * 更新技能等级
     */
    int updateSkillLevel(@Param("id") Long id, @Param("skillLevel") Integer skillLevel);
    
    /**
     * 更新技能专精
     */
    int updateSkillMastery(@Param("id") Long id, @Param("skillMastery") Integer skillMastery);
    
    /**
     * 更新当前生命值
     */
    int updateCurrentHP(@Param("id") Long id, @Param("currentHP") Integer currentHP);
} 