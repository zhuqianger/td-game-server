package com.example.tdgameserver.network;

import lombok.Data;

@Data
public class GameMessage {
    public int messageId; //消息Id
    public byte[] payload; //消息内容

    public GameMessage() {
    }

    public GameMessage(int messageId) {
        this.messageId = messageId;
        this.payload = new byte[1];
    }

    public GameMessage(int messageId, byte[] payload){
        this.messageId = messageId;
        this.payload = payload;
    }
}
