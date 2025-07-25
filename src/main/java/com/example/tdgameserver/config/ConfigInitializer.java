package com.example.tdgameserver.config;

import com.example.tdgameserver.entity.operator.Operator;
import com.example.tdgameserver.entity.skill.Skill;
import com.example.tdgameserver.entity.talent.Talent;
import com.example.tdgameserver.entity.operator.OperatorLevel;
import com.example.tdgameserver.entity.operator.OperatorBase;
import com.example.tdgameserver.entity.operator.OperatorGrowth;
import com.example.tdgameserver.entity.operator.OperatorAttribute;
import com.example.tdgameserver.entity.backpack.Item;
import com.example.tdgameserver.entity.backpack.BackpackType;
import com.example.tdgameserver.entity.stage.Stage;
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
        configService.registerTableEntity("items", Item.class);
        configService.registerTableEntity("backpack_types", BackpackType.class);
        configService.registerTableEntity("stages", Stage.class);
        
        log.info("配置表映射关系初始化完成");
        
        // 加载所有配置文件
        jsonConfigLoader.loadAllConfigsManually();
    }
} 