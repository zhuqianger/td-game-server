package com.example.tdgameserver.handler;

import com.example.tdgameserver.entity.Player;
import com.example.tdgameserver.network.GameMessage;
import com.example.tdgameserver.network.GameMessageHandlerRegistry;
import com.example.tdgameserver.network.MessageId;
import com.example.tdgameserver.service.PlayerService;
import com.example.tdgameserver.session.PlayerSession;
import com.example.tdgameserver.session.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PlayerHandler {

    @Autowired
    private PlayerService playerService;

    private final GameMessageHandlerRegistry handlerRegistry = GameMessageHandlerRegistry.getInstance();
    private final SessionManager sessionManager;
    private final Gson gson;

    public PlayerHandler() {
        this.sessionManager = SessionManager.getInstance();
        this.gson = new Gson();
        registerHandlers();
    }

    public void registerHandlers() {
        handlerRegistry.registerHandler(MessageId.REQ_LOGIN.getId(), this::handleLogin);
    }


    /**
     * 处理登录请求
     */
    public void handleLogin(PlayerSession session, GameMessage message) {
        try {
            String loginData = new String(message.getPayload());
            log.info("收到登录请求：{}", loginData);
            
            LoginCredentials credentials = parseLoginData(loginData);
            if (credentials == null) {
                session.sendMessage(MessageId.RESP_LOGIN_FAIL.getId(), "登录失败：无效的登录数据格式");
                return;
            }
            
            // 验证用户名和密码
            if (!validateCredentials(credentials)) {
                session.sendMessage(MessageId.RESP_LOGIN_FAIL.getId(), "登录失败：用户名或密码不能为空");
                return;
            }
            
            // 通过PlayerService查询数据库验证用户
            Player player = playerService.login(credentials.username(), credentials.password());
            if (player != null) {
                handleSuccessfulLogin(session, player);
            } else {
                handleFailedLogin(session, credentials.username());
            }
            
        } catch (Exception e) {
            log.error("处理登录消息失败", e);
            session.sendMessage(MessageId.RESP_LOGIN_FAIL.getId(), "登录失败：服务器内部错误");
        }
    }
    
    /**
     * 解析登录数据
     */
    private LoginCredentials parseLoginData(String loginData) {
        if (loginData.trim().startsWith("{") && loginData.trim().endsWith("}")) {
            try {
                JsonObject json = gson.fromJson(loginData, JsonObject.class);
                if (json.has("username") && json.has("password")) {
                    String username = json.get("username").getAsString();
                    String password = json.get("password").getAsString();
                    log.info("成功解析JSON格式登录数据 - 用户名: {}", username);
                    return new LoginCredentials(username, password);
                }
                log.warn("JSON格式不正确，缺少username或password字段：{}", loginData);
            } catch (Exception e) {
                log.warn("JSON格式解析失败：{}, 错误: {}", loginData, e.getMessage());
            }
        }
        return null;
    }
    
    /**
     * 验证登录凭据
     */
    private boolean validateCredentials(LoginCredentials credentials) {
        return credentials.username() != null &&
               credentials.password() != null &&
               !credentials.username().isEmpty() &&
               !credentials.password().isEmpty();
    }
    
    /**
     * 处理登录成功
     */
    private void handleSuccessfulLogin(PlayerSession session, Player player) {
        session.setAuthenticated(player.getPlayerName(), player.getPlayerId());
        sessionManager.bindPlayerId(player.getPlayerId(), session);

        log.info("玩家 {} 身份验证成功，ID：{}", player.getPlayerName(), player.getPlayerId());
        
        String successMsg = "登录成功，欢迎 " + player.getPlayerName() + "！";
        session.sendMessage(MessageId.RESP_LOGIN_SUCCESS.getId(), successMsg.getBytes());
    }
    
    /**
     * 处理登录失败
     */
    private void handleFailedLogin(PlayerSession session, String username) {
        log.warn("玩家 {} 身份验证失败，用户不存在或密码不正确", username);
        String errorMsg = "登录失败：用户名或密码错误，请检查您的凭据";
        session.sendMessage(MessageId.RESP_LOGIN_FAIL.getId(), errorMsg.getBytes());
    }

    /**
         * 登录凭据内部类
         */
    private record LoginCredentials(String username, String password) {

    }
} 