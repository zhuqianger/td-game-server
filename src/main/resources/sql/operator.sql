-- 玩家干员关联表
-- 用于存储玩家拥有的干员信息，干员的基础属性从配置文件中读取
CREATE TABLE `player_operator` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `player_id` INT NOT NULL COMMENT '玩家ID',
    `operator_id` INT NOT NULL COMMENT '干员ID（对应配置文件中的干员ID）',
    `elite_level` INT NOT NULL DEFAULT 0 COMMENT '精英化等级 (0-2)',
    `level` INT NOT NULL DEFAULT 1 COMMENT '等级 (1-90)',
    `current_exp` INT NOT NULL DEFAULT 0 COMMENT '当前经验值',
    `skill_level` INT NOT NULL DEFAULT 1 COMMENT '技能等级 (1-7)',
    `skill_mastery` INT NOT NULL DEFAULT 0 COMMENT '技能专精 (0-3)',
    `create_time` INT NOT NULL COMMENT '创建时间（毫秒时间戳）',
    `update_time` INT NOT NULL COMMENT '更新时间（毫秒时间戳）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_operator` (`player_id`, `operator_id`) COMMENT '玩家干员唯一索引',
    INDEX `idx_player_id` (`player_id`) COMMENT '玩家ID索引',
    INDEX `idx_operator_id` (`operator_id`) COMMENT '干员ID索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='玩家干员关联表';

-- 创建索引
CREATE INDEX `idx_create_time` ON `player_operator` (`create_time`) COMMENT '创建时间索引';
CREATE INDEX `idx_update_time` ON `player_operator` (`update_time`) COMMENT '更新时间索引';