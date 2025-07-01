package com.example.tdgameserver.config;


import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ConfigManager {
    private static final ConfigManager INSTANCE = new ConfigManager();

    private final Map<String, Map<Integer,ConfigData>> configDataMap = new ConcurrentHashMap<>();

    private ConfigManager(){

    }

    public static ConfigManager getInstance(){
        return INSTANCE;
    }

    public void loadConfig(String tableName, Collection<ConfigData> configList){
        Map<Integer,ConfigData> configMap = new HashMap<>();
        for(ConfigData config : configList){
            configMap.put(config.getId(),config);
        }
        configDataMap.put(tableName,configMap);
        log.info("加载配置表{}完成,共{}条数据",tableName,configMap.size());
    }

    public Collection<ConfigData> getConfigList(String tableName){
        Map<Integer,ConfigData> configMap = configDataMap.get(tableName);
        return configMap != null ? configMap.values() : Collections.emptyList();
    }

    public ConfigData getConfig(String tableName,int id){
        Map<Integer,ConfigData> configMap = configDataMap.get(tableName);
        return configMap != null ? configMap.get(id) : null;
    }

    public void clear(){
        configDataMap.clear();
        log.info("清除所有配置数据");
    }


}
