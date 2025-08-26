package com.example.tdgameserver.service;

import com.example.tdgameserver.entity.operator.PlayerOperator;
import com.example.tdgameserver.entity.operator.OperatorLevelUpResult;
import com.example.tdgameserver.requestEntity.OperatorRequest;

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
     * 使用经验道具升级干员
     * @param playerId 玩家ID
     * @param request 升级请求
     * @return 升级结果
     */
    OperatorLevelUpResult levelUpOperatorWithExpItems(Integer playerId, OperatorRequest request);
    
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
     * 检查玩家是否拥有指定干员
     * @param playerId 玩家ID
     * @param operatorId 干员ID
     * @return 是否拥有
     */
    boolean hasOperator(Integer playerId, Integer operatorId);
    
    /**
     * 为干员增加经验值
     * @param playerId 玩家ID
     * @param operatorId 干员ID
     * @param exp 要增加的经验值
     * @return 是否增加成功
     */
    boolean addOperatorExp(Integer playerId, Integer operatorId, Integer exp);
    /**
     * 检查干员是否可以升级
     * @param playerId 玩家ID
     * @param operatorId 干员ID
     * @return 是否可以升级
     */
    boolean canLevelUp(Integer playerId, Integer operatorId);
} 