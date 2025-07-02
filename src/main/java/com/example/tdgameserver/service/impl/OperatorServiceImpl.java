package com.example.tdgameserver.service.impl;

import com.example.tdgameserver.entity.Operator;
import com.example.tdgameserver.entity.PlayerOperator;
import com.example.tdgameserver.mapper.OperatorMapper;
import com.example.tdgameserver.mapper.PlayerOperatorMapper;
import com.example.tdgameserver.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 干员服务实现类
 */
@Service
public class OperatorServiceImpl implements OperatorService {
    
    @Autowired
    private OperatorMapper operatorMapper;
    
    @Autowired
    private PlayerOperatorMapper playerOperatorMapper;
    
    @Override
    public Operator getOperatorById(String operatorId) {
        return operatorMapper.selectByOperatorId(operatorId);
    }
    
    @Override
    public List<Operator> getAllOperators() {
        return operatorMapper.selectAll();
    }
    
    @Override
    public List<Operator> getOperatorsByProfession(String profession) {
        return operatorMapper.selectByProfession(profession);
    }
    
    @Override
    public List<Operator> getOperatorsByRarity(Integer rarity) {
        return operatorMapper.selectByRarity(rarity);
    }
    
    @Override
    public List<PlayerOperator> getPlayerOperators(Long playerId) {
        return playerOperatorMapper.selectByPlayerId(playerId);
    }
    
    @Override
    public List<PlayerOperator> getPlayerOperatorsByProfession(Long playerId, String profession) {
        return playerOperatorMapper.selectByPlayerIdAndProfession(playerId, profession);
    }
    
    @Override
    public List<PlayerOperator> getPlayerOperatorsByRarity(Long playerId, Integer rarity) {
        return playerOperatorMapper.selectByPlayerIdAndRarity(playerId, rarity);
    }
    
    @Override
    @Transactional
    public boolean addPlayerOperator(Long playerId, String operatorId) {
        // 检查是否已经拥有该干员
        PlayerOperator existing = playerOperatorMapper.selectByPlayerIdAndOperatorId(playerId, operatorId);
        if (existing != null) {
            return false; // 已经拥有该干员
        }
        
        // 检查干员是否存在
        Operator operator = operatorMapper.selectByOperatorId(operatorId);
        if (operator == null) {
            return false; // 干员不存在
        }
        
        // 创建玩家干员记录
        PlayerOperator playerOperator = new PlayerOperator(playerId, operatorId);
        playerOperator.setCurrentHP(operator.getMaxHP()); // 设置初始生命值为最大生命值
        
        return playerOperatorMapper.insert(playerOperator) > 0;
    }
    
    @Override
    @Transactional
    public boolean levelUpOperator(Long playerId, String operatorId) {
        PlayerOperator playerOperator = playerOperatorMapper.selectByPlayerIdAndOperatorId(playerId, operatorId);
        if (playerOperator == null) {
            return false;
        }
        
        // 检查等级上限
        if (playerOperator.getLevel() >= 90) {
            return false;
        }
        
        int newLevel = playerOperator.getLevel() + 1;
        return playerOperatorMapper.updateLevel(playerOperator.getId(), newLevel) > 0;
    }
    
    @Override
    @Transactional
    public boolean eliteOperator(Long playerId, String operatorId) {
        PlayerOperator playerOperator = playerOperatorMapper.selectByPlayerIdAndOperatorId(playerId, operatorId);
        if (playerOperator == null) {
            return false;
        }
        
        // 检查精英化等级上限
        if (playerOperator.getEliteLevel() >= 2) {
            return false;
        }
        
        int newEliteLevel = playerOperator.getEliteLevel() + 1;
        return playerOperatorMapper.updateEliteLevel(playerOperator.getId(), newEliteLevel) > 0;
    }
    
    @Override
    @Transactional
    public boolean upgradeSkill(Long playerId, String operatorId) {
        PlayerOperator playerOperator = playerOperatorMapper.selectByPlayerIdAndOperatorId(playerId, operatorId);
        if (playerOperator == null) {
            return false;
        }
        
        // 检查技能等级上限
        if (playerOperator.getSkillLevel() >= 7) {
            return false;
        }
        
        int newSkillLevel = playerOperator.getSkillLevel() + 1;
        return playerOperatorMapper.updateSkillLevel(playerOperator.getId(), newSkillLevel) > 0;
    }
    
    @Override
    @Transactional
    public boolean masterSkill(Long playerId, String operatorId) {
        PlayerOperator playerOperator = playerOperatorMapper.selectByPlayerIdAndOperatorId(playerId, operatorId);
        if (playerOperator == null) {
            return false;
        }
        
        // 检查技能专精上限
        if (playerOperator.getSkillMastery() >= 3) {
            return false;
        }
        
        int newSkillMastery = playerOperator.getSkillMastery() + 1;
        return playerOperatorMapper.updateSkillMastery(playerOperator.getId(), newSkillMastery) > 0;
    }
    
    @Override
    @Transactional
    public boolean updateOperatorHP(Long playerId, String operatorId, Integer currentHP) {
        PlayerOperator playerOperator = playerOperatorMapper.selectByPlayerIdAndOperatorId(playerId, operatorId);
        if (playerOperator == null) {
            return false;
        }
        
        // 获取干员基础信息以检查最大生命值
        Operator operator = operatorMapper.selectByOperatorId(operatorId);
        if (operator == null) {
            return false;
        }
        
        // 确保当前生命值不超过最大生命值
        int maxHP = operator.getMaxHP();
        if (currentHP > maxHP) {
            currentHP = maxHP;
        }
        
        return playerOperatorMapper.updateCurrentHP(playerOperator.getId(), currentHP) > 0;
    }
    
    @Override
    public boolean hasOperator(Long playerId, String operatorId) {
        PlayerOperator playerOperator = playerOperatorMapper.selectByPlayerIdAndOperatorId(playerId, operatorId);
        return playerOperator != null && playerOperator.getIsOwned();
    }
} 