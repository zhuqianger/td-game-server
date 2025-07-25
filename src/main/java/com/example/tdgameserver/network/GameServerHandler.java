package com.example.tdgameserver.network;

import com.example.tdgameserver.session.PlayerSession;
import com.example.tdgameserver.session.SessionManager;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameServerHandler extends ChannelInboundHandlerAdapter {
    private final GameMessageHandlerRegistry handlerRegistry = GameMessageHandlerRegistry.getInstance();
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final Gson gson = new Gson();
    

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        PlayerSession session = new PlayerSession(ctx.channel());
        sessionManager.addSession(session);
        log.info("新客户端连接:{}，等待身份验证", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        PlayerSession session = sessionManager.getSessionByChannel(channel);
        if (session != null) {
            if (session.isAuthenticated()) {
                log.info("已认证玩家断开连接：{}，用户名：{}", channel.remoteAddress(), session.getUsername());
            } else {
                log.info("未认证客户端断开连接：{}", channel.remoteAddress());
            }
            sessionManager.removeSession(session);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof GameMessage) {
            GameMessage gameMsg = (GameMessage) msg;
            PlayerSession session = sessionManager.getSessionByChannel(ctx.channel());
            if (session != null) {
                session.updateLastActiveTime();
                
                if (!session.isAuthenticated()) {
                    if (gameMsg.getMessageId() == MessageId.REQ_LOGIN.getId()) {
                        handleGameMessage(session, gameMsg);
                    } else {
                        log.warn("未认证连接尝试发送非登录消息，messageId: {}", gameMsg.getMessageId());
                        String errorMsg = "请先进行身份验证，发送登录消息";
                        String responseJson = gson.toJson(new ErrorResponse(false, errorMsg));
                        session.sendMessage(MessageId.RESP_LOGIN.getId(), responseJson.getBytes());
                    }
                } else {
                    handleGameMessage(session, gameMsg);
                }
            }
        }
    }

    public void handleGameMessage(PlayerSession session, GameMessage msg) {
        int messageId = msg.getMessageId();
        GameMessageHandler handler = handlerRegistry.getHandler(messageId);
        if (session.getPlayerId() != null) {
            log.info("收到消息:messageId = {},playerId = {}", msg.getMessageId(), session.getPlayerId());
        }
        if (handler != null) {
            try {
                handler.handle(session, msg);
            } catch (Exception e) {
                log.error("处理消息异常", e);
            }
        } else {
            log.warn("未找到消息处理器：messageId = {}", messageId);
            String errorMsg = "未找到消息处理器";
            String responseJson = gson.toJson(new ErrorResponse(false, errorMsg));
            session.sendMessage(MessageId.ERROR_MSG.getId(), responseJson.getBytes());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("连接异常", cause);
        ctx.close();
        channelInactive(ctx);
    }

    /**
     * 错误响应类
     */
    private static class ErrorResponse {
        private boolean success;
        private String message;
        
        public ErrorResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
