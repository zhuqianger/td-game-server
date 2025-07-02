package com.example.tdgameserver.handler;

import com.example.tdgameserver.entity.Operator;
import com.example.tdgameserver.entity.PlayerOperator;
import com.example.tdgameserver.network.GameMessage;
import com.example.tdgameserver.network.MessageId;
import com.example.tdgameserver.service.OperatorService;
import com.example.tdgameserver.session.PlayerSession;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 干员处理器
 */
@Component
public class OperatorHandler {
    
    @Autowired
    private OperatorService operatorService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * 获取干员信息
     */
    public void handleGetOperatorInfo(PlayerSession session, GameMessage message) {
        try {
            Map<String, Object> request = objectMapper.readValue(new String(message.getPayload()), new TypeReference<Map<String, Object>>() {});
            String operatorId = (String) request.get("operatorId");
            
            Operator operator = operatorService.getOperatorById(operatorId);
            
            Map<String, Object> response = new HashMap<>();
            if (operator != null) {
                response.put("success", true);
                response.put("operator", operator);
            } else {
                response.put("success", false);
                response.put("message", "干员不存在");
            }
            
            GameMessage responseMessage = new GameMessage();
            responseMessage.setMessageId(MessageId.RESP_GET_OPERATOR_INFO.getId());
            responseMessage.setPayload(objectMapper.writeValueAsString(response).getBytes());
            
            session.sendMessage(responseMessage);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取所有干员信息
     */
    public void handleGetAllOperators(PlayerSession session, GameMessage message) {
        try {
            List<Operator> operators = operatorService.getAllOperators();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("operators", operators);
            
            GameMessage responseMessage = new GameMessage();
            responseMessage.setMessageId(MessageId.RESP_GET_ALL_OPERATORS.getId());
            responseMessage.setPayload(objectMapper.writeValueAsString(response).getBytes());
            
            session.sendMessage(responseMessage);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取玩家干员列表
     */
    public void handleGetPlayerOperators(PlayerSession session, GameMessage message) {
        try {
            Long playerId = session.getPlayerId();
            List<PlayerOperator> playerOperators = operatorService.getPlayerOperators(playerId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("playerOperators", playerOperators);
            
            GameMessage responseMessage = new GameMessage();
            responseMessage.setMessageId(MessageId.RESP_GET_PLAYER_OPERATORS.getId());
            responseMessage.setPayload(objectMapper.writeValueAsString(response).getBytes());
            
            session.sendMessage(responseMessage);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 添加玩家干员
     */
    public void handleAddPlayerOperator(PlayerSession session, GameMessage message) {
        try {
            Map<String, Object> request = objectMapper.readValue(new String(message.getPayload()), new TypeReference<Map<String, Object>>() {});
            String operatorId = (String) request.get("operatorId");
            
            Long playerId = session.getPlayerId();
            boolean success = operatorService.addPlayerOperator(playerId, operatorId);
            
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "获得干员成功");
            } else {
                response.put("success", false);
                response.put("message", "获得干员失败");
            }
            
            GameMessage responseMessage = new GameMessage();
            responseMessage.setMessageId(success ? MessageId.RESP_ADD_PLAYER_OPERATOR_SUCCESS.getId() : MessageId.RESP_ADD_PLAYER_OPERATOR_FAIL.getId());
            responseMessage.setPayload(objectMapper.writeValueAsString(response).getBytes());
            
            session.sendMessage(responseMessage);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 升级干员
     */
    public void handleLevelUpOperator(PlayerSession session, GameMessage message) {
        try {
            Map<String, Object> request = objectMapper.readValue(new String(message.getPayload()), new TypeReference<Map<String, Object>>() {});
            String operatorId = (String) request.get("operatorId");
            
            Long playerId = session.getPlayerId();
            boolean success = operatorService.levelUpOperator(playerId, operatorId);
            
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "升级成功");
            } else {
                response.put("success", false);
                response.put("message", "升级失败");
            }
            
            GameMessage responseMessage = new GameMessage();
            responseMessage.setMessageId(success ? MessageId.RESP_LEVEL_UP_OPERATOR_SUCCESS.getId() : MessageId.RESP_LEVEL_UP_OPERATOR_FAIL.getId());
            responseMessage.setPayload(objectMapper.writeValueAsString(response).getBytes());
            
            session.sendMessage(responseMessage);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 精英化干员
     */
    public void handleEliteOperator(PlayerSession session, GameMessage message) {
        try {
            Map<String, Object> request = objectMapper.readValue(new String(message.getPayload()), new TypeReference<Map<String, Object>>() {});
            String operatorId = (String) request.get("operatorId");
            
            Long playerId = session.getPlayerId();
            boolean success = operatorService.eliteOperator(playerId, operatorId);
            
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "精英化成功");
            } else {
                response.put("success", false);
                response.put("message", "精英化失败");
            }
            
            GameMessage responseMessage = new GameMessage();
            responseMessage.setMessageId(success ? MessageId.RESP_ELITE_OPERATOR_SUCCESS.getId() : MessageId.RESP_ELITE_OPERATOR_FAIL.getId());
            responseMessage.setPayload(objectMapper.writeValueAsString(response).getBytes());
            
            session.sendMessage(responseMessage);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 升级技能
     */
    public void handleUpgradeSkill(PlayerSession session, GameMessage message) {
        try {
            Map<String, Object> request = objectMapper.readValue(new String(message.getPayload()), new TypeReference<Map<String, Object>>() {});
            String operatorId = (String) request.get("operatorId");
            
            Long playerId = session.getPlayerId();
            boolean success = operatorService.upgradeSkill(playerId, operatorId);
            
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "技能升级成功");
            } else {
                response.put("success", false);
                response.put("message", "技能升级失败");
            }
            
            GameMessage responseMessage = new GameMessage();
            responseMessage.setMessageId(success ? MessageId.RESP_UPGRADE_SKILL_SUCCESS.getId() : MessageId.RESP_UPGRADE_SKILL_FAIL.getId());
            responseMessage.setPayload(objectMapper.writeValueAsString(response).getBytes());
            
            session.sendMessage(responseMessage);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 技能专精
     */
    public void handleMasterSkill(PlayerSession session, GameMessage message) {
        try {
            Map<String, Object> request = objectMapper.readValue(new String(message.getPayload()), new TypeReference<Map<String, Object>>() {});
            String operatorId = (String) request.get("operatorId");
            
            Long playerId = session.getPlayerId();
            boolean success = operatorService.masterSkill(playerId, operatorId);
            
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "技能专精成功");
            } else {
                response.put("success", false);
                response.put("message", "技能专精失败");
            }
            
            GameMessage responseMessage = new GameMessage();
            responseMessage.setMessageId(success ? MessageId.RESP_MASTER_SKILL_SUCCESS.getId() : MessageId.RESP_MASTER_SKILL_FAIL.getId());
            responseMessage.setPayload(objectMapper.writeValueAsString(response).getBytes());
            
            session.sendMessage(responseMessage);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 更新干员生命值
     */
    public void handleUpdateOperatorHP(PlayerSession session, GameMessage message) {
        try {
            Map<String, Object> request = objectMapper.readValue(new String(message.getPayload()), new TypeReference<Map<String, Object>>() {});
            String operatorId = (String) request.get("operatorId");
            Integer currentHP = (Integer) request.get("currentHP");
            
            Long playerId = session.getPlayerId();
            boolean success = operatorService.updateOperatorHP(playerId, operatorId, currentHP);
            
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "生命值更新成功");
            } else {
                response.put("success", false);
                response.put("message", "生命值更新失败");
            }
            
            GameMessage responseMessage = new GameMessage();
            responseMessage.setMessageId(success ? MessageId.RESP_UPDATE_OPERATOR_HP_SUCCESS.getId() : MessageId.RESP_UPDATE_OPERATOR_HP_FAIL.getId());
            responseMessage.setPayload(objectMapper.writeValueAsString(response).getBytes());
            
            session.sendMessage(responseMessage);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 