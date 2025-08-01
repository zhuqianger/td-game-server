-- 玩家道具表建表语句
-- 用于存储玩家拥有的道具信息，一个道具一条记录

CREATE TABLE `player_item` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `player_id` INT NOT NULL COMMENT '玩家ID',
    `item_id` INT NOT NULL COMMENT '道具ID（对应配置文件中的道具ID）',
    `quantity` INT NOT NULL DEFAULT 1 COMMENT '道具数量',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_item` (`player_id`, `item_id`) COMMENT '玩家道具唯一索引',
    INDEX `idx_player_id` (`player_id`) COMMENT '玩家ID索引',
    INDEX `idx_item_id` (`item_id`) COMMENT '道具ID索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='玩家道具表'; 