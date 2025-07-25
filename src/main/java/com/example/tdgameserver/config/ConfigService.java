package com.example.tdgameserver.config;

import com.example.tdgameserver.entity.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 配置服务
 * 提供统一的配置数据访问接口
 */
@Service
@Slf4j
public class ConfigService {
    
    @Autowired
    private JsonConfigLoader jsonConfigLoader;

    /**
     * 注册表名和实体类的映射关系
     * @param tableName 表名
     * @param entityClass 实体类
     */
    public void registerTableEntity(String tableName, Class<?> entityClass) {
        jsonConfigLoader.registerTableEntity(tableName, entityClass);
    }

    /**
     * 获取指定表的所有配置数据
     * @param tableName 表名
     * @param clazz 实体类
     * @param <T> 实体类型
     * @return 实体对象列表
     */
    public <T> List<T> getConfigList(String tableName, Class<T> clazz) {
        return jsonConfigLoader.getConfigList(tableName, clazz);
    }

    /**
     * 获取指定表的指定ID配置数据
     * @param tableName 表名
     * @param id 配置ID
     * @param clazz 实体类
     * @param <T> 实体类型
     * @return 实体对象
     */
    public <T> T getConfig(String tableName, int id, Class<T> clazz) {
        return jsonConfigLoader.getConfig(tableName, id, clazz);
    }
    
    // 移除特殊方法，保留通用方法
} 