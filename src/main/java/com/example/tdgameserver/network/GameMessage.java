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
    
    /**
     * 统一响应类
     * 所有handler都可以使用这个类来构建响应
     */
    @Data
    public static class Response {
        private boolean success;
        private String message;
        private Object data;
        
        public Response(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
    }
}
