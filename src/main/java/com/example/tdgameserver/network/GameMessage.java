package com.example.tdgameserver.network;

import lombok.Data;

@Data
public class GameMessage {
    private int messageId; //消息Id
    private byte[] payload; //消息内容

    public GameMessage(int messageId) {
        this.messageId = messageId;
        this.payload = new byte[1];
    }

    public GameMessage(int messageId, byte[] payload){
        this.messageId = messageId;
        this.payload = payload;
    }
}
