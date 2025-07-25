package com.example.tdgameserver.service.impl;

import com.example.tdgameserver.config.ConfigService;
import com.example.tdgameserver.entity.operator.Operator;
import com.example.tdgameserver.entity.operator.OperatorAttribute;
import com.example.tdgameserver.entity.operator.OperatorBase;
import com.example.tdgameserver.entity.operator.OperatorGrowth;
import com.example.tdgameserver.entity.operator.PlayerOperator;
import com.example.tdgameserver.service.OperatorAttributeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 干员属性计算服务实现
 */
@Service
@Slf4j
public class OperatorAttributeServiceImpl implements OperatorAttributeService {
    
    @Autowired
    private ConfigService configService;
    
    @Override
    public AttributeResult calculateAttributes(PlayerOperator playerOperator) {
        try {
            // 获取干员基础信息
            Operator operator = configService.getConfig("operators", playerOperator.getOperatorId(), Operator.class);
            if (operator == null) {
                log.error("未找到干员配置: {}", playerOperator.getOperatorId());
                return new AttributeResult(0, 0, 0, 0);
            }
            
            // 获取干员个性化属性配置
            OperatorAttribute attribute = configService.getConfig("operator_attributes", playerOperator.getOperatorId(), OperatorAttribute.class);
            if (attribute == null) {
                log.error("未找到干员属性配置: {}", playerOperator.getOperatorId());
                return new AttributeResult(0, 0, 0, 0);
            }
            
            // 获取基础属性配置
            OperatorBase base = getOperatorBase(operator.getRarity(), playerOperator.getEliteLevel());
            if (base == null) {
                log.error("未找到基础属性配置: rarity={}, eliteLevel={}", operator.getRarity(), playerOperator.getEliteLevel());
                return new AttributeResult(0, 0, 0, 0);
            }
            
            // 获取属性成长配置
            OperatorGrowth growth = getOperatorGrowth(operator.getRarity(), playerOperator.getEliteLevel(), playerOperator.getLevel());
            if (growth == null) {
                log.error("未找到属性成长配置: rarity={}, eliteLevel={}, level={}", 
                         operator.getRarity(), playerOperator.getEliteLevel(), playerOperator.getLevel());
                return new AttributeResult(0, 0, 0, 0);
            }
            
            // 计算最终属性
            int finalHP = calculateFinalAttribute(base.getBaseHP(), growth.getHpGrowth(), attribute.getHpModifier());
            int finalAttack = calculateFinalAttribute(base.getBaseAttack(), growth.getAttackGrowth(), attribute.getAttackModifier());
            int finalDefense = calculateFinalAttribute(base.getBaseDefense(), growth.getDefenseGrowth(), attribute.getDefenseModifier());
            int finalMagicResistance = calculateFinalAttribute(base.getBaseMagicResistance(), growth.getMagicResistanceGrowth(), attribute.getMagicResistanceModifier());
            
            return new AttributeResult(finalHP, finalAttack, finalDefense, finalMagicResistance);
            
        } catch (Exception e) {
            log.error("计算干员属性失败: {}", playerOperator.getOperatorId(), e);
            return new AttributeResult(0, 0, 0, 0);
        }
    }
    
    /**
     * 获取基础属性配置
     */
    private OperatorBase getOperatorBase(int rarity, int eliteLevel) {
        // 遍历所有基础属性配置，找到匹配的
        for (Object obj : configService.getConfigList("operator_bases",OperatorBase.class)) {
            if (obj instanceof OperatorBase) {
                OperatorBase base = (OperatorBase) obj;
                if (base.getRarity() == rarity && base.getEliteLevel() == eliteLevel) {
                    return base;
                }
            }
        }
        return null;
    }
    
    /**
     * 获取属性成长配置
     */
    private OperatorGrowth getOperatorGrowth(int rarity, int eliteLevel, int level) {
        // 计算配置ID
        int configId = rarity * 1000 + eliteLevel * 100 + level;
        return configService.getConfig("operator_growths", configId, OperatorGrowth.class);
    }
    
    /**
     * 计算最终属性值
     * 公式: (基础属性 + 成长属性) * 修正系数
     */
    private int calculateFinalAttribute(int baseValue, int growthValue, Double modifier) {
        if (modifier == null) {
            modifier = 1.0;
        }
        return (int) Math.round((baseValue + growthValue) * modifier);
    }
} 