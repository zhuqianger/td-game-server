package com.example.tdgameserver.network;

import lombok.Data;

/**
 * 统一响应类
 * 所有handler都使用此类作为响应格式
 */
@Data
public class Response {
    private boolean success;
    private String message;
    private Object data;
    
    public Response(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    /**
     * 创建成功响应
     */
    public static Response success(String message, Object data) {
        return new Response(true, message, data);
    }
    
    /**
     * 创建成功响应（无数据）
     */
    public static Response success(String message) {
        return new Response(true, message, null);
    }
    
    /**
     * 创建失败响应
     */
    public static Response error(String message) {
        return new Response(false, message, null);
    }
    
    /**
     * 创建失败响应（带数据）
     */
    public static Response error(String message, Object data) {
        return new Response(false, message, data);
    }
} 