package com.example.tdgameserver.handler;

import com.example.tdgameserver.entity.PlayerOperator;
import com.example.tdgameserver.network.GameMessage;
import com.example.tdgameserver.network.GameMessageHandlerRegistry;
import com.example.tdgameserver.network.MessageId;
import com.example.tdgameserver.network.Response;
import com.example.tdgameserver.requestEntity.OperatorRequest;
import com.example.tdgameserver.service.OperatorService;
import com.example.tdgameserver.session.PlayerSession;
import com.example.tdgameserver.util.MessageUtil;
import com.google.gson.Gson;
import lombok.Data;
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
            
            Response response = Response.success("获取干员列表成功", playerOperators);
            
            session.sendMessage(MessageId.RESP_GET_PLAYER_OPERATORS.getId(), gson.toJson(response).getBytes());
            log.info("玩家 {} 获取干员列表成功，共 {} 个干员", playerId, playerOperators.size());
            
        } catch (Exception e) {
            log.error("获取玩家干员列表失败，玩家ID: {}", session.getPlayerId(), e);
            Response response = Response.error("获取干员列表失败：服务器内部错误");
            session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
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
                Response response = Response.error("无效的请求数据格式");
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }
            
            // 验证请求参数
            String validationError = validateAddOperatorRequest(request);
            if (validationError != null) {
                Response response = Response.error(validationError);
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }

            Integer playerId = session.getPlayerId();
            Integer operatorId = request.getOperatorId();
            log.info("玩家 {} 请求添加干员，干员ID: {}", playerId, operatorId);

            boolean success = operatorService.addPlayerOperator(playerId, operatorId);

            Response response = success ? 
                Response.success("获得干员成功", new OperatorData(operatorId)) :
                Response.error("获得干员失败");

            session.sendMessage(MessageId.RESP_ADD_PLAYER_OPERATOR.getId(), gson.toJson(response).getBytes());
            log.info("玩家 {} {} 干员 {}", playerId, success ? "添加" : "添加失败", operatorId);

        } catch (Exception e) {
            log.error("添加玩家干员失败，玩家ID: {}", session.getPlayerId(), e);
            Response response = Response.error("添加干员失败：服务器内部错误");
            session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
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
                Response response = Response.error("无效的请求数据格式");
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }
            
            // 验证请求参数
            String validationError = validateOperatorRequest(request);
            if (validationError != null) {
                Response response = Response.error(validationError);
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }
            
            Integer playerId = session.getPlayerId();
            Integer operatorId = request.getOperatorId();
            log.info("玩家 {} 请求升级干员，干员ID: {}", playerId, operatorId);
            
            boolean success = operatorService.levelUpOperator(playerId, operatorId);
            
            Response response = success ? 
                Response.success("升级成功", new OperatorData(operatorId)) :
                Response.error("升级失败");
            
            session.sendMessage(MessageId.RESP_LEVEL_UP_OPERATOR.getId(), gson.toJson(response).getBytes());
            log.info("玩家 {} {} 干员 {}", playerId, success ? "升级" : "升级失败", operatorId);
            
        } catch (Exception e) {
            log.error("升级干员失败，玩家ID: {}", session.getPlayerId(), e);
            Response response = Response.error("升级干员失败：服务器内部错误");
            session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
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
                Response response = Response.error("无效的请求数据格式");
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }
            
            // 验证请求参数
            String validationError = validateOperatorRequest(request);
            if (validationError != null) {
                Response response = Response.error(validationError);
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }
            
            Integer playerId = session.getPlayerId();
            Integer operatorId = request.getOperatorId();
            log.info("玩家 {} 请求精英化干员，干员ID: {}", playerId, operatorId);
            
            boolean success = operatorService.eliteOperator(playerId, operatorId);
            
            Response response = success ? 
                Response.success("精英化成功", new OperatorData(operatorId)) :
                Response.error("精英化失败");
            
            session.sendMessage(MessageId.RESP_ELITE_OPERATOR.getId(), gson.toJson(response).getBytes());
            log.info("玩家 {} {} 干员 {}", playerId, success ? "精英化" : "精英化失败", operatorId);
            
        } catch (Exception e) {
            log.error("精英化干员失败，玩家ID: {}", session.getPlayerId(), e);
            Response response = Response.error("精英化干员失败：服务器内部错误");
            session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
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
                Response response = Response.error("无效的请求数据格式");
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }
            
            // 验证请求参数
            String validationError = validateOperatorRequest(request);
            if (validationError != null) {
                Response response = Response.error(validationError);
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }
            
            Integer playerId = session.getPlayerId();
            Integer operatorId = request.getOperatorId();
            log.info("玩家 {} 请求升级技能，干员ID: {}", playerId, operatorId);

            boolean success = operatorService.upgradeSkill(playerId, operatorId);
            
            Response response = success ? 
                Response.success("技能升级成功", new OperatorData(operatorId)) :
                Response.error("技能升级失败");
            
            session.sendMessage(MessageId.RESP_UPGRADE_SKILL.getId(), gson.toJson(response).getBytes());
            log.info("玩家 {} {} 技能，干员ID: {}", playerId, success ? "升级" : "升级失败", operatorId);
            
        } catch (Exception e) {
            log.error("升级技能失败，玩家ID: {}", session.getPlayerId(), e);
            Response response = Response.error("升级技能失败：服务器内部错误");
            session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
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
                Response response = Response.error("无效的请求数据格式");
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }
            
            // 验证请求参数
            String validationError = validateOperatorRequest(request);
            if (validationError != null) {
                Response response = Response.error(validationError);
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }
            
            Integer playerId = session.getPlayerId();
            Integer operatorId = request.getOperatorId();
            log.info("玩家 {} 请求技能专精，干员ID: {}", playerId, operatorId);
            
            boolean success = operatorService.masterSkill(playerId, operatorId);
            
            Response response = success ? 
                Response.success("技能专精成功", new OperatorData(operatorId)) :
                Response.error("技能专精失败");
            
            session.sendMessage(MessageId.RESP_MASTER_SKILL.getId(), gson.toJson(response).getBytes());
            log.info("玩家 {} {} 技能专精，干员ID: {}", playerId, success ? "完成" : "失败", operatorId);
            
        } catch (Exception e) {
            log.error("技能专精失败，玩家ID: {}", session.getPlayerId(), e);
            Response response = Response.error("技能专精失败：服务器内部错误");
            session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
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
    
    /**
     * 干员数据类
     */
    @Data
    public static class OperatorData {
        private Integer operatorId;
        
        public OperatorData(Integer operatorId) {
            this.operatorId = operatorId;
        }
    }
} 