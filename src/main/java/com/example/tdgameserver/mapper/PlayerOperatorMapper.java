package com.example.tdgameserver.mapper;

import com.example.tdgameserver.entity.PlayerOperator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 玩家干员关联数据访问层接口
 */
@Mapper
public interface PlayerOperatorMapper {
    
    /**
     * 根据ID查询玩家干员关联信息
     * @param id 主键ID
     * @return 玩家干员关联信息
     */
    PlayerOperator selectById(@Param("id") Integer id);
    
    /**
     * 根据玩家ID和干员ID查询玩家干员关联信息
     * @param playerId 玩家ID
     * @param operatorId 干员ID
     * @return 玩家干员关联信息
     */
    PlayerOperator selectByPlayerIdAndOperatorId(@Param("playerId") Integer playerId, @Param("operatorId") Integer operatorId);
    
    /**
     * 根据玩家ID查询所有干员关联信息
     * @param playerId 玩家ID
     * @return 干员关联信息列表
     */
    List<PlayerOperator> selectByPlayerId(@Param("playerId") Integer playerId);
    
    /**
     * 根据玩家ID和职业查询干员关联信息
     * @param playerId 玩家ID
     * @param profession 职业
     * @return 干员关联信息列表
     */
    List<PlayerOperator> selectByPlayerIdAndProfession(@Param("playerId") Integer playerId, @Param("profession") String profession);
    
    /**
     * 根据玩家ID和稀有度查询干员关联信息
     * @param playerId 玩家ID
     * @param rarity 稀有度
     * @return 干员关联信息列表
     */
    List<PlayerOperator> selectByPlayerIdAndRarity(@Param("playerId") Integer playerId, @Param("rarity") Integer rarity);
    
    /**
     * 插入新的玩家干员关联信息
     * @param playerOperator 玩家干员关联信息
     * @return 影响行数
     */
    int insert(PlayerOperator playerOperator);
    
    /**
     * 更新玩家干员关联信息
     * @param playerOperator 玩家干员关联信息
     * @return 影响行数
     */
    int update(PlayerOperator playerOperator);
    
    /**
     * 根据ID删除玩家干员关联信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据玩家ID和干员ID删除玩家干员关联信息
     * @param playerId 玩家ID
     * @param operatorId 干员ID
     * @return 影响行数
     */
    int deleteByPlayerIdAndOperatorId(@Param("playerId") Integer playerId, @Param("operatorId") Integer operatorId);
    
    /**
     * 更新干员等级
     * @param id 主键ID
     * @param level 等级
     * @return 影响行数
     */
    int updateLevel(@Param("id") Integer id, @Param("level") Integer level);
    
    /**
     * 更新干员精英化等级
     * @param id 主键ID
     * @param eliteLevel 精英化等级
     * @return 影响行数
     */
    int updateEliteLevel(@Param("id") Integer id, @Param("eliteLevel") Integer eliteLevel);
    
    /**
     * 更新干员技能等级
     * @param id 主键ID
     * @param skillLevel 技能等级
     * @return 影响行数
     */
    int updateSkillLevel(@Param("id") Integer id, @Param("skillLevel") Integer skillLevel);
    
    /**
     * 更新干员技能专精
     * @param id 主键ID
     * @param skillMastery 技能专精
     * @return 影响行数
     */
    int updateSkillMastery(@Param("id") Integer id, @Param("skillMastery") Integer skillMastery);
    
    /**
     * 更新干员当前经验值
     * @param id 主键ID
     * @param currentExp 当前经验值
     * @return 影响行数
     */
    int updateCurrentExp(@Param("id") Integer id, @Param("currentExp") Integer currentExp);
    
    /**
     * 增加干员经验值
     * @param id 主键ID
     * @param exp 要增加的经验值
     * @return 影响行数
     */
    int addExp(@Param("id") Integer id, @Param("exp") Integer exp);
    
    /**
     * 更新干员等级和经验值（升级时使用）
     * @param id 主键ID
     * @param level 新等级
     * @param currentExp 新经验值（通常为0）
     * @return 影响行数
     */
    int updateLevelAndExp(@Param("id") Integer id, @Param("level") Integer level, @Param("currentExp") Integer currentExp);
    
    /**
     * 更新干员当前生命值
     * @param id 主键ID
     * @param currentHP 当前生命值
     * @return 影响行数
     */
    int updateCurrentHP(@Param("id") Integer id, @Param("currentHP") Integer currentHP);
} 