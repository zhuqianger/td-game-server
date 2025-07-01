package com.example.tdgameserver.network;


import com.example.tdgameserver.session.PlayerSession;

@FunctionalInterface
public interface GameMessageHandler {
    void handle(PlayerSession session, GameMessage message);
}
