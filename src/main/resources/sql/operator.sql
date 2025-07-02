-- 干员基础信息表
CREATE TABLE `operator` (
    `operator_id` VARCHAR(50) NOT NULL COMMENT '干员ID',
    `operator_name` VARCHAR(100) NOT NULL COMMENT '干员名称',
    `profession` VARCHAR(20) NOT NULL COMMENT '职业',
    `rarity` INT NOT NULL COMMENT '稀有度 (1-6星)',
    `max_hp` INT NOT NULL COMMENT '最大生命值',
    `attack` INT NOT NULL COMMENT '攻击力',
    `defense` INT NOT NULL COMMENT '防御力',
    `magic_resistance` INT NOT NULL COMMENT '法术抗性',
    `deploy_cost` INT NOT NULL COMMENT '部署费用',
    `block_count` INT NOT NULL COMMENT '阻挡数',
    `attack_interval` FLOAT NOT NULL COMMENT '攻击间隔',
    `redeploy_time` FLOAT NOT NULL COMMENT '再部署时间',
    `attack_range_x` INT NOT NULL COMMENT '攻击范围X',
    `attack_range_y` INT NOT NULL COMMENT '攻击范围Y',
    `skill_ids` TEXT COMMENT '技能ID列表(JSON格式)',
    `talent_ids` TEXT COMMENT '天赋ID列表(JSON格式)',
    PRIMARY KEY (`operator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='干员基础信息表';

-- 玩家干员关联表
CREATE TABLE `player_operator` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `operator_id` VARCHAR(50) NOT NULL COMMENT '干员ID',
    `elite_level` INT NOT NULL DEFAULT 0 COMMENT '精英化等级 (0-2)',
    `level` INT NOT NULL DEFAULT 1 COMMENT '等级 (1-90)',
    `skill_level` INT NOT NULL DEFAULT 1 COMMENT '技能等级 (1-7)',
    `skill_mastery` INT NOT NULL DEFAULT 0 COMMENT '技能专精 (0-3)',
    `current_hp` INT COMMENT '当前生命值',
    `is_owned` BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否拥有',
    `create_time` BIGINT NOT NULL COMMENT '创建时间',
    `update_time` BIGINT NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_operator` (`player_id`, `operator_id`),
    INDEX `idx_player_id` (`player_id`),
    INDEX `idx_operator_id` (`operator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='玩家干员关联表';