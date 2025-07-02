package com.example.tdgameserver.service;

import com.example.tdgameserver.entity.Operator;
import com.example.tdgameserver.entity.PlayerOperator;

import java.util.List;

/**
 * 干员服务接口
 */
public interface OperatorService {
    
    /**
     * 根据干员ID查询干员信息
     */
    Operator getOperatorById(String operatorId);
    
    /**
     * 查询所有干员信息
     */
    List<Operator> getAllOperators();
    
    /**
     * 根据职业查询干员列表
     */
    List<Operator> getOperatorsByProfession(String profession);
    
    /**
     * 根据稀有度查询干员列表
     */
    List<Operator> getOperatorsByRarity(Integer rarity);
    
    /**
     * 获取玩家拥有的干员列表
     */
    List<PlayerOperator> getPlayerOperators(Long playerId);
    
    /**
     * 获取玩家拥有的指定职业干员
     */
    List<PlayerOperator> getPlayerOperatorsByProfession(Long playerId, String profession);
    
    /**
     * 获取玩家拥有的指定稀有度干员
     */
    List<PlayerOperator> getPlayerOperatorsByRarity(Long playerId, Integer rarity);
    
    /**
     * 玩家获得干员
     */
    boolean addPlayerOperator(Long playerId, String operatorId);
    
    /**
     * 升级干员
     */
    boolean levelUpOperator(Long playerId, String operatorId);
    
    /**
     * 精英化干员
     */
    boolean eliteOperator(Long playerId, String operatorId);
    
    /**
     * 升级技能
     */
    boolean upgradeSkill(Long playerId, String operatorId);
    
    /**
     * 技能专精
     */
    boolean masterSkill(Long playerId, String operatorId);
    
    /**
     * 更新干员当前生命值
     */
    boolean updateOperatorHP(Long playerId, String operatorId, Integer currentHP);
    
    /**
     * 检查玩家是否拥有指定干员
     */
    boolean hasOperator(Long playerId, String operatorId);
} 