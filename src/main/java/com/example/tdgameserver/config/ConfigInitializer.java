package com.example.tdgameserver.config;

import com.example.tdgameserver.entity.operator.Operator;
import com.example.tdgameserver.entity.skill.Skill;
import com.example.tdgameserver.entity.talent.Talent;
import com.example.tdgameserver.entity.operator.OperatorLevel;
import com.example.tdgameserver.entity.operator.OperatorGrowth;
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
        configService.registerTableEntity("operators_config", Operator.class);
        configService.registerTableEntity("skills_config", Skill.class);
        configService.registerTableEntity("talents_config", Talent.class);
        configService.registerTableEntity("operator_levels_config", OperatorLevel.class);
        configService.registerTableEntity("operator_growths_config", OperatorGrowth.class);
        configService.registerTableEntity("items_config", Item.class);
        configService.registerTableEntity("backpack_types_config", BackpackType.class);
        configService.registerTableEntity("stages_config", Stage.class);
        
        log.info("配置表映射关系初始化完成");
        
        // 加载所有配置文件
        jsonConfigLoader.loadAllConfigsManually();
    }
} 