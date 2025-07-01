package com.example.tdgameserver.session;

import com.example.tdgameserver.network.GameMessage;
import com.example.tdgameserver.network.MessageId;
import com.example.tdgameserver.session.PlayerSession;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Slf4j
public class SessionManager {
    private static final SessionManager INSTANCE = new SessionManager();

    //通道->会话
    private final ConcurrentHashMap<Channel,PlayerSession> channelSessions = new ConcurrentHashMap<>();

    //玩家id -> 会话
    private final ConcurrentHashMap<Long,PlayerSession> playerSessions = new ConcurrentHashMap<>();

    // 使用CopyOnWriteArrayList来存储监听器，保证线程安全
    private final CopyOnWriteArrayList<Consumer<PlayerSession>> sessionCloseListeners = new CopyOnWriteArrayList<>();

    private SessionManager(){

    }

    public static SessionManager getInstance(){
        return INSTANCE;
    }

    /**
     * 注册会话关闭监听器
     * @param listener 监听器回调函数
     */
    public void registerSessionCloseListener(Consumer<PlayerSession> listener) {
        if (listener != null) {
            sessionCloseListeners.add(listener);
            log.debug("注册新的会话关闭监听器，当前监听器数量: {}", sessionCloseListeners.size());
        }
    }

    /**
     * 移除会话关闭监听器
     * @param listener 要移除的监听器
     */
    public void removeSessionCloseListener(Consumer<PlayerSession> listener) {
        if (listener != null) {
            sessionCloseListeners.remove(listener);
            log.debug("移除会话关闭监听器，当前监听器数量: {}", sessionCloseListeners.size());
        }
    }

    public void addSession(PlayerSession session){
        if (session.getPlayerId() != null) {
            playerSessions.put(session.getPlayerId(), session);
        }
        channelSessions.put(session.getChannel(),session);
    }

    public void bindPlayerId(Long playerId, PlayerSession session){
        session.setPlayerId(playerId);
        playerSessions.put(playerId,session);
    }

    public void removeSession(PlayerSession session) {
        if (session.getPlayerId() != null) {
            playerSessions.remove(session.getPlayerId());
        }
        channelSessions.remove(session.getChannel());
        
        // 触发所有会话关闭监听器
        for (Consumer<PlayerSession> listener : sessionCloseListeners) {
            try {
                listener.accept(session);
            } catch (Exception e) {
                log.error("执行会话关闭监听器失败", e);
            }
        }
    }

    public PlayerSession getSessionByChannel(Channel channel){
        return channelSessions.get(channel);
    }

    public PlayerSession getSessionsByPlayerId(Long playerId){
        return playerSessions.get(playerId);
    }

    //向指定玩家发送消息
    public void sendMessage(Long playerId, GameMessage message){
        PlayerSession session = getSessionsByPlayerId(playerId);
        if(session != null && session.isActive()){
            session.sendMessage(message);
        }
    }

    //向所有在线玩家广播消息
    public void broadcastMessage(GameMessage message){
        Collection<PlayerSession> sessions = channelSessions.values();
        for(PlayerSession session : sessions){
            if(session.isActive()){
                session.sendMessage(message);
            }
        }
    }

    //向指定玩家列表广播消息
    public void broadcastMessage(Collection<Long> playerIds,GameMessage message){
        for(Long playerId : playerIds){
            sendMessage(playerId, message);
        }
    }

    /**
     * 广播消息给所有在线玩家
     * @param messageId 消息ID
     * @param data 消息数据
     */
    public void broadcast(MessageId messageId, byte[] data) {
        GameMessage message = new GameMessage(messageId.getId(), data);
        for (PlayerSession session : playerSessions.values()) {
            if (session.isActive()) {
                session.sendMessage(message);
            }
        }
    }
}
