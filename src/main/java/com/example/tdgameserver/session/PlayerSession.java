package com.example.tdgameserver.session;

import com.example.tdgameserver.network.GameMessage;
import io.netty.channel.Channel;
import lombok.Data;

@Data
public class PlayerSession {
    private Integer playerId;
    private Channel channel;
    private Integer lastActiveTimes;
    private boolean authenticated = false;  // 身份验证状态
    private String username;                // 用户名（验证通过后设置）
    private Integer currentMatchId;            // 当前对局ID

    public PlayerSession(Channel channel){
        this.channel = channel;
        this.lastActiveTimes = (int) System.currentTimeMillis();
        this.authenticated = false;  // 默认未认证
    }

    public void updateLastActiveTime(){
        this.lastActiveTimes = (int) System.currentTimeMillis();
    }

    /**
     * 设置身份验证成功
     */
    public void setAuthenticated(String username, Integer playerId) {
        this.authenticated = true;
        this.username = username;
        this.playerId = playerId;
    }

    /**
     * 清除身份验证状态（用于断开连接或重新验证）
     */
    public void clearAuthentication() {
        this.authenticated = false;
        this.username = null;
        this.playerId = null;
    }

    /**
     * 检查会话是否有效（连接活跃且已认证）
     */
    public boolean isValidSession() {
        return isActive() && isAuthenticated();
    }

    //发送字节数组消息
    public void sendMessage(int messageId,byte[] payload){
        if(channel != null && channel.isActive()){
            GameMessage message = new GameMessage(messageId,payload);
            channel.writeAndFlush(message);
        }
    }

    //发送字符串消息
    public void sendMessage(int messageId,String content){
        sendMessage(messageId,content.getBytes());
    }


    //发送GameMessage对象
    public void sendMessage(GameMessage message){
        if(channel != null && channel.isActive()){
            channel.writeAndFlush(message);
        }
    }

    public boolean isActive(){
        return channel != null && channel.isActive();
    }
}
