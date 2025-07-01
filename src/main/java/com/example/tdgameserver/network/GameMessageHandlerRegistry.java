package com.example.tdgameserver.network;


import java.util.concurrent.ConcurrentHashMap;

public class GameMessageHandlerRegistry {
    private static final GameMessageHandlerRegistry INSTANCE = new GameMessageHandlerRegistry();
    private final ConcurrentHashMap<Integer, GameMessageHandler> handlers = new ConcurrentHashMap<>();

    private GameMessageHandlerRegistry(){}

    public static GameMessageHandlerRegistry getInstance(){
        return INSTANCE;
    }

    public void registerHandler(int messageId,GameMessageHandler handler){
        handlers.put(messageId,handler);
    }

    public GameMessageHandler getHandler(int messageId){
        return handlers.get(messageId);
    }

    public void unregisterHandler(int messageId,GameMessageHandler handler){
        handlers.remove(messageId,handler);
    }

}
