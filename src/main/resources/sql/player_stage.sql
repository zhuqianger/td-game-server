CREATE TABLE player_stage (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    player_id BIGINT NOT NULL COMMENT '玩家ID',
    stage_id VARCHAR(32) NOT NULL COMMENT '关卡ID',
    star INT NOT NULL DEFAULT 1 COMMENT '通关星级（1~3）',
    operator_ids VARCHAR(255) NOT NULL COMMENT '通关时所用干员ID列表，逗号分隔',
    pass_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '通关时间',
    UNIQUE KEY uk_player_stage (player_id, stage_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='玩家关卡通关记录表'; 