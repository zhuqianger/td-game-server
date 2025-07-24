package com.example.tdgameserver.handler;

import com.example.tdgameserver.entity.PlayerOperator;
import com.example.tdgameserver.network.GameMessage;
import com.example.tdgameserver.network.GameMessageHandlerRegistry;
import com.example.tdgameserver.network.MessageId;
import com.example.tdgameserver.requestEntity.OperatorRequest;
import com.example.tdgameserver.service.OperatorService;
import com.example.tdgameserver.session.PlayerSession;
import com.example.tdgameserver.util.MessageUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 干员处理器
 * 负责处理玩家干员相关的请求，干员基础信息从配置文件中读取
 */
@Slf4j
@Component
public class OperatorHandler {
    
    @Autowired
    private OperatorService operatorService;
    
    private final GameMessageHandlerRegistry handlerRegistry = GameMessageHandlerRegistry.getInstance();
    private final Gson gson = new Gson();
    
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
    }
    
    /**
     * 获取玩家干员列表
     */
    public void handleGetPlayerOperators(PlayerSession session, GameMessage message) {
        try {
            Integer playerId = session.getPlayerId();
            log.info("玩家 {} 请求获取干员列表", playerId);
            
            List<PlayerOperator> playerOperators = operatorService.getPlayerOperators(playerId);
            
            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "获取干员列表成功");
            response.add("data", gson.toJsonTree(playerOperators));
            
            session.sendMessage(MessageId.RESP_GET_PLAYER_OPERATORS.getId(), response.toString().getBytes());
            log.info("玩家 {} 获取干员列表成功，共 {} 个干员", playerId, playerOperators.size());
            
        } catch (Exception e) {
            log.error("获取玩家干员列表失败，玩家ID: {}", session.getPlayerId(), e);
            session.sendMessage(MessageId.ERROR_MSG.getId(), "获取干员列表失败：服务器内部错误".getBytes());
        }
    }

    /**
     * 添加玩家干员
     */
    public void handleAddPlayerOperator(PlayerSession session, GameMessage message) {
        try {
            String requestData = new String(message.getPayload());
            
            // 使用MessageUtil通用转换
            OperatorRequest request = MessageUtil.convertMessage(requestData, OperatorRequest.class);
            if (request == null) {
                session.sendMessage(MessageId.ERROR_MSG.getId(), "无效的请求数据格式".getBytes());
                return;
            }
            
            // 验证请求参数
            String validationError = validateAddOperatorRequest(request);
            if (validationError != null) {
                session.sendMessage(MessageId.ERROR_MSG.getId(), validationError.getBytes());
                return;
            }

            Integer playerId = session.getPlayerId();
            Integer operatorId = request.getOperatorId();
            log.info("玩家 {} 请求添加干员，干员ID: {}", playerId, operatorId);

            boolean success = operatorService.addPlayerOperator(playerId, operatorId);

            JsonObject response = new JsonObject();
            response.addProperty("success", success);
            response.addProperty("message", success ? "获得干员成功" : "获得干员失败");
            if (success) {
                response.addProperty("operatorId", operatorId);
            }

            session.sendMessage(MessageId.RESP_ADD_PLAYER_OPERATOR_SUCCESS.getId(), response.toString().getBytes());
            log.info("玩家 {} {} 干员 {}", playerId, success ? "添加" : "添加失败", operatorId);

        } catch (Exception e) {
            log.error("添加玩家干员失败，玩家ID: {}", session.getPlayerId(), e);
            session.sendMessage(MessageId.ERROR_MSG.getId(), "添加干员失败：服务器内部错误".getBytes());
        }
    }
    
    /**
     * 升级干员
     */
    public void handleLevelUpOperator(PlayerSession session, GameMessage message) {
        try {
            String requestData = new String(message.getPayload());
            
            // 使用MessageUtil通用转换
            OperatorRequest request = MessageUtil.convertMessage(requestData, OperatorRequest.class);
            if (request == null) {
                session.sendMessage(MessageId.ERROR_MSG.getId(), "无效的请求数据格式".getBytes());
                return;
            }
            
            // 验证请求参数
            String validationError = validateOperatorRequest(request);
            if (validationError != null) {
                session.sendMessage(MessageId.ERROR_MSG.getId(), validationError.getBytes());
                return;
            }
            
            Integer playerId = session.getPlayerId();
            Integer operatorId = request.getOperatorId();
            log.info("玩家 {} 请求升级干员，干员ID: {}", playerId, operatorId);
            
            boolean success = operatorService.levelUpOperator(playerId, operatorId);
            
            JsonObject response = new JsonObject();
            response.addProperty("success", success);
            response.addProperty("message", success ? "升级成功" : "升级失败");
            if (success) {
                response.addProperty("operatorId", operatorId);
            }
            
            session.sendMessage(MessageId.RESP_LEVEL_UP_OPERATOR_SUCCESS.getId(), response.toString().getBytes());
            log.info("玩家 {} {} 干员 {}", playerId, success ? "升级" : "升级失败", operatorId);
            
        } catch (Exception e) {
            log.error("升级干员失败，玩家ID: {}", session.getPlayerId(), e);
            session.sendMessage(MessageId.ERROR_MSG.getId(), "升级干员失败：服务器内部错误".getBytes());
        }
    }
    
    /**
     * 精英化干员
     */
    public void handleEliteOperator(PlayerSession session, GameMessage message) {
        try {
            String requestData = new String(message.getPayload());
            
            // 使用MessageUtil通用转换
            OperatorRequest request = MessageUtil.convertMessage(requestData, OperatorRequest.class);
            if (request == null) {
                session.sendMessage(MessageId.ERROR_MSG.getId(), "无效的请求数据格式".getBytes());
                return;
            }
            
            // 验证请求参数
            String validationError = validateOperatorRequest(request);
            if (validationError != null) {
                session.sendMessage(MessageId.ERROR_MSG.getId(), validationError.getBytes());
                return;
            }
            
            Integer playerId = session.getPlayerId();
            Integer operatorId = request.getOperatorId();
            log.info("玩家 {} 请求精英化干员，干员ID: {}", playerId, operatorId);
            
            boolean success = operatorService.eliteOperator(playerId, operatorId);
            
            JsonObject response = new JsonObject();
            response.addProperty("success", success);
            response.addProperty("message", success ? "精英化成功" : "精英化失败");
            if (success) {
                response.addProperty("operatorId", operatorId);
            }
            
            session.sendMessage(MessageId.RESP_ELITE_OPERATOR_SUCCESS.getId(), response.toString().getBytes());
            log.info("玩家 {} {} 干员 {}", playerId, success ? "精英化" : "精英化失败", operatorId);
            
        } catch (Exception e) {
            log.error("精英化干员失败，玩家ID: {}", session.getPlayerId(), e);
            session.sendMessage(MessageId.ERROR_MSG.getId(), "精英化干员失败：服务器内部错误".getBytes());
        }
    }
    
    /**
     * 升级技能
     */
    public void handleUpgradeSkill(PlayerSession session, GameMessage message) {
        try {
            String requestData = new String(message.getPayload());
            
            // 使用MessageUtil通用转换
            OperatorRequest request = MessageUtil.convertMessage(requestData, OperatorRequest.class);
            if (request == null) {
                session.sendMessage(MessageId.ERROR_MSG.getId(), "无效的请求数据格式".getBytes());
                return;
            }
            
            // 验证请求参数
            String validationError = validateOperatorRequest(request);
            if (validationError != null) {
                session.sendMessage(MessageId.ERROR_MSG.getId(), validationError.getBytes());
                return;
            }
            
            Integer playerId = session.getPlayerId();
            Integer operatorId = request.getOperatorId();
            log.info("玩家 {} 请求升级技能，干员ID: {}", playerId, operatorId);

            boolean success = operatorService.upgradeSkill(playerId, operatorId);
            
            JsonObject response = new JsonObject();
            response.addProperty("success", success);
            response.addProperty("message", success ? "技能升级成功" : "技能升级失败");
            if (success) {
                response.addProperty("operatorId", operatorId);
            }
            
            session.sendMessage(MessageId.RESP_UPGRADE_SKILL_SUCCESS.getId(), response.toString().getBytes());
            log.info("玩家 {} {} 技能，干员ID: {}", playerId, success ? "升级" : "升级失败", operatorId);
            
        } catch (Exception e) {
            log.error("升级技能失败，玩家ID: {}", session.getPlayerId(), e);
            session.sendMessage(MessageId.ERROR_MSG.getId(), "升级技能失败：服务器内部错误".getBytes());
        }
    }
    
    /**
     * 技能专精
     */
    public void handleMasterSkill(PlayerSession session, GameMessage message) {
        try {
            String requestData = new String(message.getPayload());
            
            // 使用MessageUtil通用转换
            OperatorRequest request = MessageUtil.convertMessage(requestData, OperatorRequest.class);
            if (request == null) {
                session.sendMessage(MessageId.ERROR_MSG.getId(), "无效的请求数据格式".getBytes());
                return;
            }
            
            // 验证请求参数
            String validationError = validateOperatorRequest(request);
            if (validationError != null) {
                session.sendMessage(MessageId.ERROR_MSG.getId(), validationError.getBytes());
                return;
            }
            
            Integer playerId = session.getPlayerId();
            Integer operatorId = request.getOperatorId();
            log.info("玩家 {} 请求技能专精，干员ID: {}", playerId, operatorId);
            
            boolean success = operatorService.masterSkill(playerId, operatorId);
            
            JsonObject response = new JsonObject();
            response.addProperty("success", success);
            response.addProperty("message", success ? "技能专精成功" : "技能专精失败");
            if (success) {
                response.addProperty("operatorId", operatorId);
            }
            
            session.sendMessage(MessageId.RESP_MASTER_SKILL_SUCCESS.getId(), response.toString().getBytes());
            log.info("玩家 {} {} 技能专精，干员ID: {}", playerId, success ? "完成" : "失败", operatorId);
            
        } catch (Exception e) {
            log.error("技能专精失败，玩家ID: {}", session.getPlayerId(), e);
            session.sendMessage(MessageId.ERROR_MSG.getId(), "技能专精失败：服务器内部错误".getBytes());
        }
    }
    
    /**
     * 验证添加干员请求参数
     */
    private String validateAddOperatorRequest(OperatorRequest request) {
        if (request == null) {
            return "请求参数不能为空";
        }
        
        if (request.getOperatorId() == null) {
            return "干员ID不能为空";
        }
        
        if (request.getOperatorId() <= 0) {
            return "干员ID必须大于0";
        }
        
        return null;
    }
    
    /**
     * 验证干员操作请求参数
     */
    private String validateOperatorRequest(OperatorRequest request) {
        if (request == null) {
            return "请求参数不能为空";
        }
        
        if (request.getOperatorId() == null) {
            return "干员ID不能为空";
        }
        
        if (request.getOperatorId() <= 0) {
            return "干员ID必须大于0";
        }
        
        return null;
    }
} 