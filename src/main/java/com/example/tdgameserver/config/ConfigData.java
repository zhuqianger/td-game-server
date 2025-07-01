package com.example.tdgameserver.config;


import lombok.Data;

import java.util.Map;

@Data
public class ConfigData {
    private int id;
    private String name;
    private Map<String,Object> data;

    public ConfigData() {

    }

    public ConfigData(int id, String name, Map<String, Object> data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key){
        return (T)data.get(key);
    }
    
}
