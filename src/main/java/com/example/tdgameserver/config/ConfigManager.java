package com.example.tdgameserver.config;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ConfigManager {
    // 存储格式: Map<表名, Map<ID, 实体对象>>
    private static final Map<String, Map<Integer, Object>> configDataMap = new ConcurrentHashMap<>();

    private ConfigManager(){
        // 私有构造函数，防止实例化
    }

    /**
     * 加载配置数据
     * @param tableName 表名
     * @param configMap 配置数据Map (ID -> 实体对象)
     */
    public static void loadConfig(String tableName, Map<Integer, Object> configMap){
        configDataMap.put(tableName, configMap);
        log.info("加载配置表{}完成,共{}条数据", tableName, configMap.size());
    }

    /**
     * 获取指定表的所有配置数据
     * @param tableName 表名
     * @return 实体对象集合
     */
    public static Collection<Object> getConfigList(String tableName){
        Map<Integer, Object> configMap = configDataMap.get(tableName);
        return configMap != null ? configMap.values() : Collections.emptyList();
    }

    /**
     * 获取指定表的指定ID配置数据
     * @param tableName 表名
     * @param id 配置ID
     * @return 实体对象
     */
    public static Object getConfig(String tableName, int id){
        Map<Integer, Object> configMap = configDataMap.get(tableName);
        return configMap != null ? configMap.get(id) : null;
    }

    /**
     * 获取指定表的配置数据Map
     * @param tableName 表名
     * @return 配置数据Map
     */
    public static Map<Integer, Object> getConfigMap(String tableName){
        return configDataMap.get(tableName);
    }

    /**
     * 检查表是否存在
     * @param tableName 表名
     * @return 是否存在
     */
    public static boolean hasTable(String tableName){
        return configDataMap.containsKey(tableName);
    }

    /**
     * 清除所有配置数据
     */
    public static void clear(){
        configDataMap.clear();
        log.info("清除所有配置数据");
    }
}
