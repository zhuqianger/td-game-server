package com.example.tdgameserver.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 通用JSON配置加载器
 * 支持任意JSON格式的配置文件，自动加载到ConfigManager
 */
@Component
@Slf4j
public class JsonConfigLoader {
    private static final String CONFIG_PATH = "classpath:config-data/*.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    // 存储表名和对应实体类的映射关系
    private final Map<String, Class<?>> tableEntityMap = new HashMap<>();
    /**
     * 手动触发加载所有配置文件
     * 应该在ConfigInitializer初始化完成后调用
     */
    public void loadAllConfigsManually() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(CONFIG_PATH);

            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                if (fileName == null) {
                    continue;
                }

                String tableName = fileName.substring(0, fileName.lastIndexOf("."));
                log.info("开始加载配置文件: {}", fileName);
                loadJsonConfig(tableName, resource.getInputStream());
            }

        } catch (IOException e) {
            log.error("加载配置文件失败", e);
        }
    }

    /**
     * 注册表名和实体类的映射关系
     * @param tableName 表名
     * @param entityClass 实体类
     */
    public void registerTableEntity(String tableName, Class<?> entityClass) {
        tableEntityMap.put(tableName, entityClass);
        log.info("注册表名和实体类映射: {} -> {}", tableName, entityClass.getSimpleName());
    }

    /**
     * 加载JSON配置文件并转换为实体对象
     */
    private void loadJsonConfig(String tableName, InputStream inputStream) {
        try {
            JsonNode rootNode = objectMapper.readTree(inputStream);
            Map<Integer, Object> configMap = parseConfigData(rootNode, tableName);

            // 将配置数据加载到ConfigManager中
            ConfigManager.loadConfig(tableName, configMap);
            log.info("成功加载配置文件: {}, 共 {} 条数据", tableName, configMap.size());

        } catch (IOException e) {
            log.error("加载配置文件失败: {}", tableName, e);
        }
    }

    /**
     * 解析JSON数据并转换为实体对象
     */
    private Map<Integer, Object> parseConfigData(JsonNode rootNode, String tableName) {
        Map<Integer, Object> configMap = new HashMap<>();
        Class<?> entityClass = tableEntityMap.get(tableName);
        
        if (entityClass == null) {
            log.warn("表 {} 未注册实体类，跳过加载", tableName);
            return configMap;
        }

        List<JsonNode> dataNodes = new ArrayList<>();
        
        // 尝试解析为数组格式
        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                dataNodes.add(node);
            }
        } else {
            // 尝试解析为对象格式，查找包含数组的字段
            for (Iterator<String> it = rootNode.fieldNames(); it.hasNext(); ) {
                String fieldName = it.next();
                JsonNode fieldNode = rootNode.get(fieldName);
                if (fieldNode.isArray()) {
                    for (JsonNode node : fieldNode) {
                        dataNodes.add(node);
                    }
                    break; // 只处理第一个数组字段
                }
            }
        }

        // 转换为实体对象
        for (JsonNode node : dataNodes) {
            try {
                Object entity = objectMapper.convertValue(node, entityClass);
                int id = node.has("id") ? node.get("id").asInt() : 0;
                configMap.put(id, entity);
            } catch (Exception e) {
                log.error("转换实体对象失败: {}, id: {}", tableName, 
                         node.has("id") ? node.get("id").asInt() : "unknown", e);
            }
        }
        
        return configMap;
    }

    /**
     * 获取指定表的所有配置数据
     * @param tableName 表名
     * @param clazz 实体类
     * @param <T> 实体类型
     * @return 实体对象列表
     */
    public <T> List<T> getConfigList(String tableName, Class<T> clazz) {
        try {
            Collection<Object> configList = ConfigManager.getConfigList(tableName);
            List<T> result = new ArrayList<>();
            
            for (Object config : configList) {
                if (clazz.isInstance(config)) {
                    result.add(clazz.cast(config));
                }
            }
            
            return result;
        } catch (Exception e) {
            log.error("获取配置列表失败: {}", tableName, e);
            return new ArrayList<>();
        }
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
        try {
            Object config = ConfigManager.getConfig(tableName, id);
            if (config != null && clazz.isInstance(config)) {
                return clazz.cast(config);
            }
            return null;
        } catch (Exception e) {
            log.error("获取配置失败: {}, id: {}", tableName, id, e);
            return null;
        }
    }
}
