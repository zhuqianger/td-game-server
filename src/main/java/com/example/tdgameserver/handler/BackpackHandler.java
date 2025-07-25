package com.example.tdgameserver.handler;

import com.example.tdgameserver.entity.backpack.PlayerItem;
import com.example.tdgameserver.network.GameMessage;
import com.example.tdgameserver.network.GameMessageHandlerRegistry;
import com.example.tdgameserver.network.MessageId;
import com.example.tdgameserver.network.Response;
import com.example.tdgameserver.requestEntity.BackpackRequest;
import com.example.tdgameserver.service.BackpackService;
import com.example.tdgameserver.session.PlayerSession;
import com.example.tdgameserver.session.SessionManager;
import com.example.tdgameserver.util.MessageUtil;
import com.google.gson.Gson;
import lombok.Data;
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
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final Gson gson = new Gson();

    public BackpackHandler() {
        registerHandlers();
        registerSessionCloseListener();
    }

    public void registerHandlers() {
        handlerRegistry.registerHandler(MessageId.REQ_GET_BACKPACK.getId(), this::handleGetBackpack);
        handlerRegistry.registerHandler(MessageId.REQ_GET_BACKPACK_BY_TYPE.getId(), this::handleGetBackpackByType);
        handlerRegistry.registerHandler(MessageId.REQ_ADD_ITEM.getId(), this::handleAddItem);
        handlerRegistry.registerHandler(MessageId.REQ_USE_ITEM.getId(), this::handleUseItem);
    }
    
    /**
     * 注册会话关闭监听器
     */
    private void registerSessionCloseListener() {
        sessionManager.registerSessionCloseListener(this::onPlayerOffline);
        log.info("背包处理器已注册会话关闭监听器");
    }
    
    /**
     * 玩家离线时的处理
     */
    private void onPlayerOffline(PlayerSession session) {
        Integer playerId = session.getPlayerId();
        if (playerId != null) {
            backpackService.clearPlayerCache(playerId);
            log.info("玩家 {} 离线，已清理背包缓存", playerId);
        }
    }

    /**
     * 处理获取背包请求
     */
    public void handleGetBackpack(PlayerSession session, GameMessage message) {
        try {
            Integer playerId = session.getPlayerId();
            Map<Integer, Map<String, Object>> itemsWithConfig = backpackService.getPlayerItemsWithConfig(playerId);

            Response response = Response.success("获取背包成功", itemsWithConfig);
            session.sendMessage(MessageId.RESP_GET_BACKPACK.getId(), gson.toJson(response).getBytes());
            log.info("玩家 {} 获取背包成功，共 {} 种道具", playerId, itemsWithConfig.size());

        } catch (Exception e) {
            log.error("处理获取背包请求失败", e);
            Response response = Response.error("获取背包失败：服务器内部错误");
            session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
        }
    }

    /**
     * 处理根据背包类型获取道具请求
     */
    public void handleGetBackpackByType(PlayerSession session, GameMessage message) {
        try {
            String requestData = new String(message.getPayload());
            
            // 使用MessageUtil通用转换
            BackpackRequest request = MessageUtil.convertMessage(requestData, BackpackRequest.class);
            if (request == null) {
                Response response = Response.error("无效的请求数据格式");
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }
            
            // 验证请求参数
            String validationError = validateBackpackRequest(request);
            if (validationError != null) {
                Response response = Response.error(validationError);
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }

            Integer playerId = session.getPlayerId();
            Integer backpackTypeId = request.getBackpackTypeId();
            
            List<PlayerItem> items = backpackService.getPlayerItemsByBackpackType(playerId, backpackTypeId);

            Response response = Response.success("获取背包成功", items);
            session.sendMessage(MessageId.RESP_GET_BACKPACK_BY_TYPE.getId(), gson.toJson(response).getBytes());
            log.info("玩家 {} 获取背包类型 {} 成功，共 {} 种道具", playerId, backpackTypeId, items.size());

        } catch (Exception e) {
            log.error("处理根据背包类型获取道具请求失败", e);
            Response response = Response.error("获取背包失败：服务器内部错误");
            session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
        }
    }

    /**
     * 处理添加道具请求
     */
    public void handleAddItem(PlayerSession session, GameMessage message) {
        try {
            String requestData = new String(message.getPayload());
            
            // 使用MessageUtil通用转换
            BackpackRequest request = MessageUtil.convertMessage(requestData, BackpackRequest.class);
            if (request == null) {
                Response response = Response.error("无效的请求数据格式");
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }
            
            // 验证请求参数
            String validationError = validateAddItemRequest(request);
            if (validationError != null) {
                Response response = Response.error(validationError);
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }

            Integer playerId = session.getPlayerId();
            Integer itemId = request.getItemId();
            Integer quantity = request.getCount();
            
            boolean success = backpackService.addItem(playerId, itemId, quantity);

            Response response = success ? 
                Response.success("添加道具成功", new ItemData(itemId, quantity)) :
                Response.error("添加道具失败");

            session.sendMessage(MessageId.RESP_ADD_ITEM.getId(), gson.toJson(response).getBytes());
            log.info("玩家 {} {} 道具 {} x{}", playerId, success ? "添加" : "添加失败", itemId, quantity);

        } catch (Exception e) {
            log.error("处理添加道具请求失败", e);
            Response response = Response.error("添加道具失败：服务器内部错误");
            session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
        }
    }

    /**
     * 处理使用道具请求
     */
    public void handleUseItem(PlayerSession session, GameMessage message) {
        try {
            String requestData = new String(message.getPayload());
            
            // 使用MessageUtil通用转换
            BackpackRequest request = MessageUtil.convertMessage(requestData, BackpackRequest.class);
            if (request == null) {
                Response response = Response.error("无效的请求数据格式");
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }
            
            // 验证请求参数
            String validationError = validateUseItemRequest(request);
            if (validationError != null) {
                Response response = Response.error(validationError);
                session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
                return;
            }

            Integer playerId = session.getPlayerId();
            Integer itemId = request.getItemId();
            Integer quantity = request.getCount();
            
            boolean success = backpackService.useItem(playerId, itemId, quantity);

            Response response = success ? 
                Response.success("使用道具成功", new ItemData(itemId, quantity)) :
                Response.error("使用道具失败");

            session.sendMessage(MessageId.RESP_USE_ITEM.getId(), gson.toJson(response).getBytes());
            log.info("玩家 {} {} 道具 {} x{}", playerId, success ? "使用" : "使用失败", itemId, quantity);

        } catch (Exception e) {
            log.error("处理使用道具请求失败", e);
            Response response = Response.error("使用道具失败：服务器内部错误");
            session.sendMessage(MessageId.ERROR_MSG.getId(), gson.toJson(response).getBytes());
        }
    }
    
    /**
     * 验证背包请求参数
     */
    private String validateBackpackRequest(BackpackRequest request) {
        if (request.getBackpackTypeId() == null) {
            return "缺少背包类型ID";
        }
        if (request.getBackpackTypeId() <= 0) {
            return "背包类型ID必须大于0";
        }
        return null;
    }
    
    /**
     * 验证添加道具请求参数
     */
    private String validateAddItemRequest(BackpackRequest request) {
        if (request.getItemId() == null) {
            return "缺少道具ID";
        }
        if (request.getCount() == null) {
            return "缺少道具数量";
        }
        if (request.getItemId() <= 0) {
            return "道具ID必须大于0";
        }
        if (request.getCount() <= 0) {
            return "道具数量必须大于0";
        }
        return null;
    }
    
    /**
     * 验证使用道具请求参数
     */
    private String validateUseItemRequest(BackpackRequest request) {
        if (request.getItemId() == null) {
            return "缺少道具ID";
        }
        if (request.getCount() == null) {
            return "缺少道具数量";
        }
        if (request.getItemId() <= 0) {
            return "道具ID必须大于0";
        }
        if (request.getCount() <= 0) {
            return "道具数量必须大于0";
        }
        return null;
    }
    
    /**
     * 道具数据类
     */
    @Data
    public static class ItemData {
        private Integer itemId;
        private Integer quantity;
        
        public ItemData(Integer itemId, Integer quantity) {
            this.itemId = itemId;
            this.quantity = quantity;
        }
    }
} 