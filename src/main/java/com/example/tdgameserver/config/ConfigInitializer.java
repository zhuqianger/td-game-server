package com.example.tdgameserver.config;

import com.example.tdgameserver.entity.Operator;
import com.example.tdgameserver.entity.Skill;
import com.example.tdgameserver.entity.Talent;
import com.example.tdgameserver.entity.OperatorLevel;
import com.example.tdgameserver.entity.OperatorBase;
import com.example.tdgameserver.entity.OperatorGrowth;
import com.example.tdgameserver.entity.OperatorAttribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * 配置初始化器
 * 负责注册表名和实体类的映射关系
 */
@Component
@Slf4j
public class ConfigInitializer {
    
    @Autowired
    private ConfigService configService;
    
    @Autowired
    private JsonConfigLoader jsonConfigLoader;
    
    @PostConstruct
    public void init() {
        log.info("开始初始化配置表映射关系...");
        
        // 注册表名和实体类的映射关系
        configService.registerTableEntity("operators", Operator.class);
        configService.registerTableEntity("skills", Skill.class);
        configService.registerTableEntity("talents", Talent.class);
        configService.registerTableEntity("operator_levels", OperatorLevel.class);
        configService.registerTableEntity("operator_bases", OperatorBase.class);
        configService.registerTableEntity("operator_growths", OperatorGrowth.class);
        configService.registerTableEntity("operator_attributes", OperatorAttribute.class);
        
        log.info("配置表映射关系初始化完成");
        
        // 加载所有配置文件
        jsonConfigLoader.loadAllConfigsManually();
    }
} 