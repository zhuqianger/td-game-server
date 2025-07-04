package com.example.tdgameserver.handler;

import com.example.tdgameserver.entity.PlayerItem;
import com.example.tdgameserver.network.GameMessage;
import com.example.tdgameserver.network.GameMessageHandlerRegistry;
import com.example.tdgameserver.network.MessageId;
import com.example.tdgameserver.service.BackpackService;
import com.example.tdgameserver.session.PlayerSession;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class BackpackHandler {

    @Autowired
    private BackpackService backpackService;

    private final GameMessageHandlerRegistry handlerRegistry = GameMessageHandlerRegistry.getInstance();
    private final Gson gson = new Gson();

    public BackpackHandler() {
        registerHandlers();
    }

    public void registerHandlers() {
        handlerRegistry.registerHandler(MessageId.REQ_GET_BACKPACK.getId(), this::handleGetBackpack);
        handlerRegistry.registerHandler(MessageId.REQ_GET_BACKPACK_BY_TYPE.getId(), this::handleGetBackpackByType);
        handlerRegistry.registerHandler(MessageId.REQ_ADD_ITEM.getId(), this::handleAddItem);
        handlerRegistry.registerHandler(MessageId.REQ_USE_ITEM.getId(), this::handleUseItem);
    }

    /**
     * 处理获取背包请求
     */
    public void handleGetBackpack(PlayerSession session, GameMessage message) {
        try {
            Integer playerId = session.getPlayerId();
            Map<Integer, Map<String, Object>> itemsWithConfig = backpackService.getPlayerItemsWithConfig(playerId);

            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "获取背包成功");
            response.add("data", gson.toJsonTree(itemsWithConfig));

            session.sendMessage(MessageId.RESP_GET_BACKPACK.getId(), response.toString().getBytes());
            log.info("玩家 {} 获取背包成功，共 {} 种道具", playerId, itemsWithConfig.size());

        } catch (Exception e) {
            log.error("处理获取背包请求失败", e);
            session.sendMessage(MessageId.ERROR_MSG.getId(), "获取背包失败：服务器内部错误".getBytes());
        }
    }

    /**
     * 处理根据背包类型获取道具请求
     */
    public void handleGetBackpackByType(PlayerSession session, GameMessage message) {
        try {
            String requestData = new String(message.getPayload());
            JsonObject request = gson.fromJson(requestData, JsonObject.class);
            
            if (!request.has("backpackTypeId")) {
                session.sendMessage(MessageId.ERROR_MSG.getId(), "缺少背包类型ID".getBytes());
                return;
            }

            Integer playerId = session.getPlayerId();
            Integer backpackTypeId = request.get("backpackTypeId").getAsInt();
            
            List<PlayerItem> items = backpackService.getPlayerItemsByBackpackType(playerId, backpackTypeId);

            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "获取背包成功");
            response.add("data", gson.toJsonTree(items));

            session.sendMessage(MessageId.RESP_GET_BACKPACK_BY_TYPE.getId(), response.toString().getBytes());
            log.info("玩家 {} 获取背包类型 {} 成功，共 {} 种道具", playerId, backpackTypeId, items.size());

        } catch (Exception e) {
            log.error("处理根据背包类型获取道具请求失败", e);
            session.sendMessage(MessageId.ERROR_MSG.getId(), "获取背包失败：服务器内部错误".getBytes());
        }
    }

    /**
     * 处理添加道具请求
     */
    public void handleAddItem(PlayerSession session, GameMessage message) {
        try {
            String requestData = new String(message.getPayload());
            JsonObject request = gson.fromJson(requestData, JsonObject.class);
            
            if (!request.has("itemId") || !request.has("quantity")) {
                session.sendMessage(MessageId.ERROR_MSG.getId(), "缺少道具ID或数量".getBytes());
                return;
            }

            Integer playerId = session.getPlayerId();
            Integer itemId = request.get("itemId").getAsInt();
            Integer quantity = request.get("quantity").getAsInt();
            
            boolean success = backpackService.addItem(playerId, itemId, quantity);

            JsonObject response = new JsonObject();
            response.addProperty("success", success);
            response.addProperty("message", success ? "添加道具成功" : "添加道具失败");
            if (success) {
                response.addProperty("itemId", itemId);
                response.addProperty("quantity", quantity);
            }

            session.sendMessage(MessageId.RESP_ADD_ITEM.getId(), response.toString().getBytes());
            log.info("玩家 {} {} 道具 {} x{}", playerId, success ? "添加" : "添加失败", itemId, quantity);

        } catch (Exception e) {
            log.error("处理添加道具请求失败", e);
            session.sendMessage(MessageId.ERROR_MSG.getId(), "添加道具失败：服务器内部错误".getBytes());
        }
    }

    /**
     * 处理使用道具请求
     */
    public void handleUseItem(PlayerSession session, GameMessage message) {
        try {
            if (!session.isAuthenticated()) {
                session.sendMessage(MessageId.ERROR_MSG.getId(), "请先登录".getBytes());
                return;
            }

            String requestData = new String(message.getPayload());
            JsonObject request = gson.fromJson(requestData, JsonObject.class);
            
            if (!request.has("itemId") || !request.has("quantity")) {
                session.sendMessage(MessageId.ERROR_MSG.getId(), "缺少道具ID或数量".getBytes());
                return;
            }

            Integer playerId = session.getPlayerId();
            Integer itemId = request.get("itemId").getAsInt();
            Integer quantity = request.get("quantity").getAsInt();
            
            boolean success = backpackService.useItem(playerId, itemId, quantity);

            JsonObject response = new JsonObject();
            response.addProperty("success", success);
            response.addProperty("message", success ? "使用道具成功" : "使用道具失败");
            if (success) {
                response.addProperty("itemId", itemId);
                response.addProperty("quantity", quantity);
            }

            session.sendMessage(MessageId.RESP_USE_ITEM.getId(), response.toString().getBytes());
            log.info("玩家 {} {} 道具 {} x{}", playerId, success ? "使用" : "使用失败", itemId, quantity);

        } catch (Exception e) {
            log.error("处理使用道具请求失败", e);
            session.sendMessage(MessageId.ERROR_MSG.getId(), "使用道具失败：服务器内部错误".getBytes());
        }
    }
} 