package com.example.tdgameserver.util;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息工具类
 * 提供通用的消息转换功能
 */
@Slf4j
public class MessageUtil {
    
    private static final Gson gson = new Gson();
    
    /**
     * 通用消息转换方法
     * 将收到的消息转换为指定的Java类实例
     * 
     * @param message 收到的消息内容
     * @param targetClass 要转换的目标类
     * @param <T> 目标类型
     * @return 转换后的对象实例，转换失败返回null
     */
    public static <T> T convertMessage(String message, Class<T> targetClass) {
        if (message == null || message.trim().isEmpty()) {
            log.warn("消息内容为空");
            return null;
        }
        
        try {
            // 检查是否为JSON格式
            if (message.trim().startsWith("{") && message.trim().endsWith("}")) {
                T result = gson.fromJson(message, targetClass);
                if (result != null) {
                    log.info("成功转换消息为 {} 类型", targetClass.getSimpleName());
                    return result;
                } else {
                    log.warn("消息转换结果为空，消息内容: {}", message);
                }
            } else {
                log.warn("消息内容不是有效的JSON格式: {}", message);
            }
        } catch (Exception e) {
            log.error("消息转换失败，消息内容: {}, 目标类型: {}, 错误: {}", 
                     message, targetClass.getSimpleName(), e.getMessage());
        }
        return null;
    }
}
