package com.example.tdgameserver.handler;

import com.example.tdgameserver.constant.StageConstant;
import com.example.tdgameserver.entity.stage.PlayerStage;
import com.example.tdgameserver.requestEntity.StageRequest;
import com.example.tdgameserver.service.StageService;
import com.example.tdgameserver.network.GameMessage;
import com.example.tdgameserver.network.GameMessageHandlerRegistry;
import com.example.tdgameserver.network.MessageId;
import com.example.tdgameserver.network.Response;
import com.example.tdgameserver.session.PlayerSession;
import com.example.tdgameserver.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.example.tdgameserver.config.ConfigService;

@Slf4j
@Component
public class StageHandler {

    @Autowired
    private StageService stageService;

    @Autowired
    private ConfigService configService;

    private final GameMessageHandlerRegistry handlerRegistry = GameMessageHandlerRegistry.getInstance();
    private final Gson gson = new Gson();

    public StageHandler() {
        registerHandlers();
    }

    public void registerHandlers() {
        handlerRegistry.registerHandler(MessageId.REQ_GET_PLAYER_STAGES.getId(), this::handleGetPlayerStages);
        handlerRegistry.registerHandler(MessageId.REQ_SAVE_STAGE_RECORD.getId(), this::handleSaveStageRecord);
    }


    /**
     * 获取玩家关卡通关记录
     */
    public void handleGetPlayerStages(PlayerSession session, GameMessage message) {
        try {
            List<PlayerStage> playerStages = stageService.getPlayerStages(session.getPlayerId());
            Response response = Response.success("获取玩家关卡记录成功", playerStages);
            session.sendMessage(MessageId.RESP_GET_PLAYER_STAGES.getId(), gson.toJson(response).getBytes());
            log.info("玩家 {} 获取关卡通关记录成功", session.getPlayerId());
        } catch (Exception e) {
            log.error("获取玩家关卡记录失败", e);
            Response response = Response.error("获取玩家关卡记录失败: " + e.getMessage());
            session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
        }
    }
    /**
     * 保存关卡通关记录
     */
    public void handleSaveStageRecord(PlayerSession session, GameMessage message) {
        try {
            String requestData = new String(message.getPayload());
            StageRequest request = MessageUtil.convertMessage(requestData, StageRequest.class);
            if (request == null) {
                Response response = Response.error("参数不完整");
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }
            Integer stageId = request.getStageId();
            Integer star = request.getStar();
            List<Integer> operatorIds = request.getOperatorIds();
            if (stageId == null || star == null || operatorIds == null) {
                Response response = Response.error("参数不完整");
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }
            if (star < StageConstant.MIN_STAGE_STAR || star > StageConstant.MAX_STAGE_STAR) {
                Response response = Response.error("星级必须在1-3之间");
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }
            if (operatorIds.isEmpty()) {
                Response response = Response.error("干员列表不能为空");
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }
            boolean success = stageService.saveStageRecord(session.getPlayerId(), request);
            if (success) {
                Response response = Response.success("通关记录保存成功", null);
                session.sendMessage(MessageId.RESP_SAVE_STAGE_RECORD.getId(), gson.toJson(response).getBytes());
                log.info("玩家 {} 保存关卡 {} 通关记录成功", session.getPlayerId(), stageId);
            } else {
                Response response = Response.error("通关记录保存失败");
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
            }
        } catch (Exception e) {
            log.error("保存通关记录失败", e);
            Response response = Response.error("保存通关记录失败: " + e.getMessage());
            session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
        }
    }
} 