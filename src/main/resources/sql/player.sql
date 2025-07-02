-- Player表建表语句
-- 玩家信息表

CREATE TABLE `player` (
    `player_id` INT NOT NULL AUTO_INCREMENT COMMENT '玩家ID，主键，自增',
    `player_name` VARCHAR(50) NOT NULL COMMENT '玩家名称',
    `password` VARCHAR(255) NOT NULL COMMENT '玩家密码',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`player_id`),
    UNIQUE KEY `uk_player_name` (`player_name`) COMMENT '玩家名称唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='玩家信息表';

-- 创建索引
CREATE INDEX `idx_create_time` ON `player` (`create_time`) COMMENT '创建时间索引';
CREATE INDEX `idx_update_time` ON `player` (`update_time`) COMMENT '更新时间索引'; 