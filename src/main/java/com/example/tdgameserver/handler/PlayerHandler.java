package com.example.tdgameserver.handler;

import com.example.tdgameserver.entity.Player;
import com.example.tdgameserver.network.GameMessage;
import com.example.tdgameserver.network.GameMessageHandlerRegistry;
import com.example.tdgameserver.network.MessageId;
import com.example.tdgameserver.service.PlayerService;
import com.example.tdgameserver.session.PlayerSession;
import com.example.tdgameserver.session.SessionManager;
import com.example.tdgameserver.util.MessageUtil;
import lombok.Data;
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

    public PlayerHandler() {
        this.sessionManager = SessionManager.getInstance();
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
            
            // 使用MessageUtil通用转换接口
            LoginRequest loginRequest = MessageUtil.convertMessage(loginData, LoginRequest.class);
            if (loginRequest == null) {
                session.sendMessage(MessageId.RESP_LOGIN_FAIL.getId(), "登录失败：无效的登录数据格式");
                return;
            }
            
            // 验证用户名和密码
            String validationError = validateLoginRequest(loginRequest);
            if (validationError != null) {
                session.sendMessage(MessageId.RESP_LOGIN_FAIL.getId(), "登录失败：" + validationError);
                return;
            }
            
            // 通过PlayerService查询数据库验证用户
            Player player = playerService.authenticatePlayer(loginRequest.getUsername(), loginRequest.getPassword());
            if (player != null) {
                handleSuccessfulLogin(session, player);
            } else {
                handleFailedLogin(session, loginRequest.getUsername());
            }
            
        } catch (Exception e) {
            log.error("处理登录消息失败", e);
            session.sendMessage(MessageId.RESP_LOGIN_FAIL.getId(), "登录失败：服务器内部错误");
        }
    }
    
    /**
     * 验证登录请求数据
     */
    private String validateLoginRequest(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return "用户名不能为空";
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return "密码不能为空";
        }
        return null; // 验证通过
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
     * 登录请求实体类
     */
    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
} 