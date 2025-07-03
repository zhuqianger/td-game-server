package com.example.tdgameserver.handler;

import com.example.tdgameserver.entity.PlayerOperator;
import com.example.tdgameserver.network.GameMessage;
import com.example.tdgameserver.network.GameMessageHandlerRegistry;
import com.example.tdgameserver.network.MessageId;
import com.example.tdgameserver.service.OperatorService;
import com.example.tdgameserver.session.PlayerSession;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 干员处理器
 * 负责处理玩家干员相关的请求，干员基础信息从配置文件中读取
 */
@Slf4j
@Component
public class OperatorHandler {
    
    @Autowired
    private OperatorService operatorService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private final GameMessageHandlerRegistry handlerRegistry = GameMessageHandlerRegistry.getInstance();
    
    public OperatorHandler() {
        registerHandlers();
    }
    
    public void registerHandlers() {
        handlerRegistry.registerHandler(MessageId.REQ_GET_PLAYER_OPERATORS.getId(), this::handleGetPlayerOperators);
        handlerRegistry.registerHandler(MessageId.REQ_ADD_PLAYER_OPERATOR.getId(), this::handleAddPlayerOperator);
        handlerRegistry.registerHandler(MessageId.REQ_LEVEL_UP_OPERATOR.getId(), this::handleLevelUpOperator);
        handlerRegistry.registerHandler(MessageId.REQ_ELITE_OPERATOR.getId(), this::handleEliteOperator);
        handlerRegistry.registerHandler(MessageId.REQ_UPGRADE_SKILL.getId(), this::handleUpgradeSkill);
        handlerRegistry.registerHandler(MessageId.REQ_MASTER_SKILL.getId(), this::handleMasterSkill);
        handlerRegistry.registerHandler(MessageId.REQ_UPDATE_OPERATOR_HP.getId(), this::handleUpdateOperatorHP);
    }
    
    /**
     * 获取玩家干员列表
     */
    public void handleGetPlayerOperators(PlayerSession session, GameMessage message) {
        try {
            Integer playerId = session.getPlayerId();
            log.info("玩家 {} 请求获取干员列表", playerId);
            
            List<PlayerOperator> playerOperators = operatorService.getPlayerOperators(playerId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("playerOperators", playerOperators);
            
            GameMessage responseMessage = new GameMessage();
            responseMessage.setMessageId(MessageId.RESP_GET_PLAYER_OPERATORS.getId());
            responseMessage.setPayload(objectMapper.writeValueAsString(response).getBytes());
            
            session.sendMessage(responseMessage);
            log.info("玩家 {} 获取干员列表成功，共 {} 个干员", playerId, playerOperators.size());
            
        } catch (Exception e) {
            log.error("获取玩家干员列表失败，玩家ID: {}", session.getPlayerId(), e);
            session.sendMessage(MessageId.ERROR_MSG.getId(), "获取干员列表失败".getBytes());
        }
    }
    
    /**
     * 添加玩家干员
     */
    public void handleAddPlayerOperator(PlayerSession session, GameMessage message) {
        try {
            Map<String, Object> request = objectMapper.readValue(new String(message.getPayload()), new TypeReference<Map<String, Object>>() {});
            Integer operatorId = (Integer) request.get("operatorId");
            
            Integer playerId = session.getPlayerId();
            log.info("玩家 {} 请求添加干员，干员ID: {}", playerId, operatorId);
            
            boolean success = operatorService.addPlayerOperator(playerId, operatorId);
            
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "获得干员成功");
                log.info("玩家 {} 添加干员成功，干员ID: {}", playerId, operatorId);
            } else {
                response.put("success", false);
                response.put("message", "获得干员失败");
                log.warn("玩家 {} 添加干员失败，干员ID: {}", playerId, operatorId);
            }
            
            GameMessage responseMessage = new GameMessage();
            responseMessage.setMessageId(success ? MessageId.RESP_ADD_PLAYER_OPERATOR_SUCCESS.getId() : MessageId.RESP_ADD_PLAYER_OPERATOR_FAIL.getId());
            responseMessage.setPayload(objectMapper.writeValueAsString(response).getBytes());
            
            session.sendMessage(responseMessage);
            
        } catch (Exception e) {
            log.error("添加玩家干员失败，玩家ID: {}, 干员ID: {}", session.getPlayerId(), 
                     message.getPayload() != null ? new String(message.getPayload()) : "null", e);
            session.sendMessage(MessageId.ERROR_MSG.getId(), "添加干员失败".getBytes());
        }
    }
    
    /**
     * 升级干员
     */
    public void handleLevelUpOperator(PlayerSession session, GameMessage message) {
        try {
            Map<String, Object> request = objectMapper.readValue(new String(message.getPayload()), new TypeReference<Map<String, Object>>() {});
            Integer operatorId = (Integer) request.get("operatorId");
            
            Integer playerId = session.getPlayerId();
            log.info("玩家 {} 请求升级干员，干员ID: {}", playerId, operatorId);
            
            boolean success = operatorService.levelUpOperator(playerId, operatorId);
            
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "升级成功");
                log.info("玩家 {} 升级干员成功，干员ID: {}", playerId, operatorId);
            } else {
                response.put("success", false);
                response.put("message", "升级失败");
                log.warn("玩家 {} 升级干员失败，干员ID: {}", playerId, operatorId);
            }
            
            GameMessage responseMessage = new GameMessage();
            responseMessage.setMessageId(success ? MessageId.RESP_LEVEL_UP_OPERATOR_SUCCESS.getId() : MessageId.RESP_LEVEL_UP_OPERATOR_FAIL.getId());
            responseMessage.setPayload(objectMapper.writeValueAsString(response).getBytes());
            session.sendMessage(responseMessage);
            
        } catch (Exception e) {
            log.error("升级干员失败，玩家ID: {}, 干员ID: {}", session.getPlayerId(), 
                     message.getPayload() != null ? new String(message.getPayload()) : "null", e);
            session.sendMessage(MessageId.ERROR_MSG.getId(), "升级干员失败".getBytes());
        }
    }
    
    /**
     * 精英化干员
     */
    public void handleEliteOperator(PlayerSession session, GameMessage message) {
        try {
            Map<String, Object> request = objectMapper.readValue(new String(message.getPayload()), new TypeReference<Map<String, Object>>() {});
            Integer operatorId = (Integer) request.get("operatorId");
            
            Integer playerId = session.getPlayerId();
            log.info("玩家 {} 请求精英化干员，干员ID: {}", playerId, operatorId);
            
            boolean success = operatorService.eliteOperator(playerId, operatorId);
            
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "精英化成功");
                log.info("玩家 {} 精英化干员成功，干员ID: {}", playerId, operatorId);
            } else {
                response.put("success", false);
                response.put("message", "精英化失败");
                log.warn("玩家 {} 精英化干员失败，干员ID: {}", playerId, operatorId);
            }
            
            GameMessage responseMessage = new GameMessage();
            responseMessage.setMessageId(success ? MessageId.RESP_ELITE_OPERATOR_SUCCESS.getId() : MessageId.RESP_ELITE_OPERATOR_FAIL.getId());
            responseMessage.setPayload(objectMapper.writeValueAsString(response).getBytes());
            
            session.sendMessage(responseMessage);
            
        } catch (Exception e) {
            log.error("精英化干员失败，玩家ID: {}, 干员ID: {}", session.getPlayerId(), 
                     message.getPayload() != null ? new String(message.getPayload()) : "null", e);
            session.sendMessage(MessageId.ERROR_MSG.getId(), "精英化干员失败".getBytes());
        }
    }
    
    /**
     * 升级技能
     */
    public void handleUpgradeSkill(PlayerSession session, GameMessage message) {
        try {
            Map<String, Object> request = objectMapper.readValue(new String(message.getPayload()), new TypeReference<Map<String, Object>>() {});
            Integer operatorId = (Integer) request.get("operatorId");
            
            Integer playerId = session.getPlayerId();
            log.info("玩家 {} 请求升级技能，干员ID: {}", playerId, operatorId);
            
            boolean success = operatorService.upgradeSkill(playerId, operatorId);
            
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "技能升级成功");
                log.info("玩家 {} 升级技能成功，干员ID: {}", playerId, operatorId);
            } else {
                response.put("success", false);
                response.put("message", "技能升级失败");
                log.warn("玩家 {} 升级技能失败，干员ID: {}", playerId, operatorId);
            }
            
            GameMessage responseMessage = new GameMessage();
            responseMessage.setMessageId(success ? MessageId.RESP_UPGRADE_SKILL_SUCCESS.getId() : MessageId.RESP_UPGRADE_SKILL_FAIL.getId());
            responseMessage.setPayload(objectMapper.writeValueAsString(response).getBytes());
            
            session.sendMessage(responseMessage);
            
        } catch (Exception e) {
            log.error("升级技能失败，玩家ID: {}, 干员ID: {}", session.getPlayerId(), 
                     message.getPayload() != null ? new String(message.getPayload()) : "null", e);
            session.sendMessage(MessageId.ERROR_MSG.getId(), "升级技能失败".getBytes());
        }
    }
    
    /**
     * 技能专精
     */
    public void handleMasterSkill(PlayerSession session, GameMessage message) {
        try {
            Map<String, Object> request = objectMapper.readValue(new String(message.getPayload()), new TypeReference<Map<String, Object>>() {});
            Integer operatorId = (Integer) request.get("operatorId");
            
            Integer playerId = session.getPlayerId();
            log.info("玩家 {} 请求技能专精，干员ID: {}", playerId, operatorId);
            
            boolean success = operatorService.masterSkill(playerId, operatorId);
            
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "技能专精成功");
                log.info("玩家 {} 技能专精成功，干员ID: {}", playerId, operatorId);
            } else {
                response.put("success", false);
                response.put("message", "技能专精失败");
                log.warn("玩家 {} 技能专精失败，干员ID: {}", playerId, operatorId);
            }
            
            GameMessage responseMessage = new GameMessage();
            responseMessage.setMessageId(success ? MessageId.RESP_MASTER_SKILL_SUCCESS.getId() : MessageId.RESP_MASTER_SKILL_FAIL.getId());
            responseMessage.setPayload(objectMapper.writeValueAsString(response).getBytes());
            
            session.sendMessage(responseMessage);
            
        } catch (Exception e) {
            log.error("技能专精失败，玩家ID: {}, 干员ID: {}", session.getPlayerId(), 
                     message.getPayload() != null ? new String(message.getPayload()) : "null", e);
            session.sendMessage(MessageId.ERROR_MSG.getId(), "技能专精失败".getBytes());
        }
    }
    
    /**
     * 更新干员生命值
     */
    public void handleUpdateOperatorHP(PlayerSession session, GameMessage message) {
        try {
            Map<String, Object> request = objectMapper.readValue(new String(message.getPayload()), new TypeReference<Map<String, Object>>() {});
            Integer operatorId = (Integer) request.get("operatorId");
            Integer currentHP = (Integer) request.get("currentHP");
            
            Integer playerId = session.getPlayerId();
            log.info("玩家 {} 请求更新干员生命值，干员ID: {}, 当前生命值: {}", playerId, operatorId, currentHP);
            
            boolean success = operatorService.updateOperatorHP(playerId, operatorId, currentHP);
            
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "生命值更新成功");
                log.info("玩家 {} 更新干员生命值成功，干员ID: {}, 生命值: {}", playerId, operatorId, currentHP);
            } else {
                response.put("success", false);
                response.put("message", "生命值更新失败");
                log.warn("玩家 {} 更新干员生命值失败，干员ID: {}, 生命值: {}", playerId, operatorId, currentHP);
            }
            
            GameMessage responseMessage = new GameMessage();
            responseMessage.setMessageId(success ? MessageId.RESP_UPDATE_OPERATOR_HP_SUCCESS.getId() : MessageId.RESP_UPDATE_OPERATOR_HP_FAIL.getId());
            responseMessage.setPayload(objectMapper.writeValueAsString(response).getBytes());
            
            session.sendMessage(responseMessage);
            
        } catch (Exception e) {
            log.error("更新干员生命值失败，玩家ID: {}, 干员ID: {}", session.getPlayerId(), 
                     message.getPayload() != null ? new String(message.getPayload()) : "null", e);
            session.sendMessage(MessageId.ERROR_MSG.getId(), "更新干员生命值失败".getBytes());
        }
    }
} 