package com.example.tdgameserver.service.impl;

import com.example.tdgameserver.entity.OperatorLevelUpResult;
import com.example.tdgameserver.entity.PlayerOperator;
import com.example.tdgameserver.mapper.PlayerOperatorMapper;
import com.example.tdgameserver.requestEntity.OperatorLevelUpRequest;
import com.example.tdgameserver.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 干员服务实现类
 * 负责管理玩家拥有的干员信息，干员基础信息从配置文件中读取
 */
@Service
public class OperatorServiceImpl implements OperatorService {

    private static final int MAX_ELITE_LEVEL = 2;

    private static final int MAX_LEVEL = 90;

    @Autowired
    private PlayerOperatorMapper playerOperatorMapper;
    
    @Override
    public List<PlayerOperator> getPlayerOperators(Integer playerId) {
        return playerOperatorMapper.selectByPlayerId(playerId);
    }

    @Override
    @Transactional
    public boolean addPlayerOperator(Integer playerId, Integer operatorId) {
        // 检查玩家是否已经拥有该干员
        PlayerOperator existingOperator = playerOperatorMapper.selectByPlayerIdAndOperatorId(playerId, operatorId);
        if (existingOperator != null) {
            return false; // 玩家已经拥有该干员
        }
        
        // 创建新的玩家干员关联
        PlayerOperator playerOperator = new PlayerOperator(playerId, operatorId);
        int result = playerOperatorMapper.insert(playerOperator);
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean levelUpOperator(Integer playerId, Integer operatorId) {
        PlayerOperator playerOperator = playerOperatorMapper.selectByPlayerIdAndOperatorId(playerId, operatorId);
        if (playerOperator == null) {
            return false; // 玩家不拥有该干员
        }
        
        // 检查等级上限（假设最大等级为90）
        if (playerOperator.getLevel() >= 90) {
            return false; // 已达到最大等级
        }
        
        // 升级干员
        int newLevel = playerOperator.getLevel() + 1;
        int result = playerOperatorMapper.updateLevel(playerOperator.getId(), newLevel);
        return result > 0;
    }

    @Override
    public OperatorLevelUpResult levelUpOperatorWithExpItems(Integer playerId, OperatorLevelUpRequest request) {
        return null;
    }

    @Override
    @Transactional
    public boolean eliteOperator(Integer playerId, Integer operatorId) {
        PlayerOperator playerOperator = playerOperatorMapper.selectByPlayerIdAndOperatorId(playerId, operatorId);
        if (playerOperator == null) {
            return false; // 玩家不拥有该干员
        }
        
        // 检查精英化等级上限（假设最大精英化等级为2）
        if (playerOperator.getEliteLevel() >= 2) {
            return false; // 已达到最大精英化等级
        }
        
        // 精英化干员
        int newEliteLevel = playerOperator.getEliteLevel() + 1;
        int result = playerOperatorMapper.updateEliteLevel(playerOperator.getId(), newEliteLevel);
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean upgradeSkill(Integer playerId, Integer operatorId) {
        PlayerOperator playerOperator = playerOperatorMapper.selectByPlayerIdAndOperatorId(playerId, operatorId);
        if (playerOperator == null) {
            return false; // 玩家不拥有该干员
        }
        
        // 检查技能等级上限（假设最大技能等级为7）
        if (playerOperator.getSkillLevel() >= 7) {
            return false; // 已达到最大技能等级
        }
        
        // 升级技能
        int newSkillLevel = playerOperator.getSkillLevel() + 1;
        int result = playerOperatorMapper.updateSkillLevel(playerOperator.getId(), newSkillLevel);
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean masterSkill(Integer playerId, Integer operatorId) {
        PlayerOperator playerOperator = playerOperatorMapper.selectByPlayerIdAndOperatorId(playerId, operatorId);
        if (playerOperator == null) {
            return false; // 玩家不拥有该干员
        }
        
        // 检查技能专精等级上限（假设最大专精等级为3）
        if (playerOperator.getSkillMastery() >= 3) {
            return false; // 已达到最大专精等级
        }
        
        // 专精技能
        int newSkillMastery = playerOperator.getSkillMastery() + 1;
        int result = playerOperatorMapper.updateSkillMastery(playerOperator.getId(), newSkillMastery);
        return result > 0;
    }

    
    @Override
    public boolean hasOperator(Integer playerId, Integer operatorId) {
        PlayerOperator playerOperator = playerOperatorMapper.selectByPlayerIdAndOperatorId(playerId, operatorId);
        return playerOperator != null;
    }
    
    @Override
    @Transactional
    public boolean addOperatorExp(Integer playerId, Integer operatorId, Integer exp) {
        PlayerOperator playerOperator = playerOperatorMapper.selectByPlayerIdAndOperatorId(playerId, operatorId);
        if (playerOperator == null) {
            return false; // 玩家不拥有该干员
        }
        
        if (exp <= 0) {
            return false; // 经验值必须大于0
        }
        
        // 增加经验值
        int result = playerOperatorMapper.addExp(playerOperator.getId(), exp);
        return result > 0;
    }

    
    @Override
    public boolean canLevelUp(Integer playerId, Integer operatorId) {
        PlayerOperator playerOperator = playerOperatorMapper.selectByPlayerIdAndOperatorId(playerId, operatorId);
        if (playerOperator == null) {
            return false; // 玩家不拥有该干员
        }
        
        // 检查等级上限
        if (playerOperator.getLevel() >= 90) {
            return false; // 已达到最大等级
        }
        
        // 这里可以添加经验值检查逻辑
        // 例如：检查当前经验值是否达到升级所需经验值
        // 暂时简化处理，只要有经验值就可以升级
        return playerOperator.getCurrentExp() > 0;
    }
} 