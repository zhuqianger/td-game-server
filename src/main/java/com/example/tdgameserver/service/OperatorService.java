package com.example.tdgameserver.service;

import com.example.tdgameserver.entity.PlayerOperator;

import java.util.List;

/**
 * 干员服务接口
 * 负责管理玩家拥有的干员信息，干员基础信息从配置文件中读取
 */
public interface OperatorService {
    
    /**
     * 获取玩家拥有的所有干员
     * @param playerId 玩家ID
     * @return 干员列表
     */
    List<PlayerOperator> getPlayerOperators(Integer playerId);
    
    /**
     * 根据职业获取玩家拥有的干员
     * @param playerId 玩家ID
     * @param profession 职业
     * @return 干员列表
     */
    List<PlayerOperator> getPlayerOperatorsByProfession(Integer playerId, String profession);
    
    /**
     * 根据稀有度获取玩家拥有的干员
     * @param playerId 玩家ID
     * @param rarity 稀有度
     * @return 干员列表
     */
    List<PlayerOperator> getPlayerOperatorsByRarity(Integer playerId, Integer rarity);
    
    /**
     * 为玩家添加干员
     * @param playerId 玩家ID
     * @param operatorId 干员ID
     * @return 是否添加成功
     */
    boolean addPlayerOperator(Integer playerId, Integer operatorId);
    
    /**
     * 升级干员等级
     * @param playerId 玩家ID
     * @param operatorId 干员ID
     * @return 是否升级成功
     */
    boolean levelUpOperator(Integer playerId, Integer operatorId);
    
    /**
     * 精英化干员
     * @param playerId 玩家ID
     * @param operatorId 干员ID
     * @return 是否精英化成功
     */
    boolean eliteOperator(Integer playerId, Integer operatorId);
    
    /**
     * 升级干员技能
     * @param playerId 玩家ID
     * @param operatorId 干员ID
     * @return 是否升级成功
     */
    boolean upgradeSkill(Integer playerId, Integer operatorId);
    
    /**
     * 专精干员技能
     * @param playerId 玩家ID
     * @param operatorId 干员ID
     * @return 是否专精成功
     */
    boolean masterSkill(Integer playerId, Integer operatorId);
    
    /**
     * 更新干员当前生命值
     * @param playerId 玩家ID
     * @param operatorId 干员ID
     * @param currentHP 当前生命值
     * @return 是否更新成功
     */
    boolean updateOperatorHP(Integer playerId, Integer operatorId, Integer currentHP);
    
    /**
     * 检查玩家是否拥有指定干员
     * @param playerId 玩家ID
     * @param operatorId 干员ID
     * @return 是否拥有
     */
    boolean hasOperator(Integer playerId, Integer operatorId);
} 