package com.example.tdgameserver.service;

import com.example.tdgameserver.entity.PlayerOperator;

/**
 * 干员属性计算服务
 */
public interface OperatorAttributeService {
    
    /**
     * 计算干员的最终属性
     * @param playerOperator 玩家干员
     * @return 属性计算结果
     */
    AttributeResult calculateAttributes(PlayerOperator playerOperator);
    
    /**
     * 属性计算结果
     */
    class AttributeResult {
        private int finalHP;
        private int finalAttack;
        private int finalDefense;
        private int finalMagicResistance;
        
        public AttributeResult(int finalHP, int finalAttack, int finalDefense, int finalMagicResistance) {
            this.finalHP = finalHP;
            this.finalAttack = finalAttack;
            this.finalDefense = finalDefense;
            this.finalMagicResistance = finalMagicResistance;
        }
        
        // Getters
        public int getFinalHP() { return finalHP; }
        public int getFinalAttack() { return finalAttack; }
        public int getFinalDefense() { return finalDefense; }
        public int getFinalMagicResistance() { return finalMagicResistance; }
        
        @Override
        public String toString() {
            return String.format("HP: %d, Attack: %d, Defense: %d, MagicResistance: %d", 
                               finalHP, finalAttack, finalDefense, finalMagicResistance);
        }
    }
} 