-- 为player_operator表添加current_exp字段
-- 执行前请备份数据库

-- 添加current_exp字段
ALTER TABLE `player_operator` 
ADD COLUMN `current_exp` INT NOT NULL DEFAULT 0 COMMENT '当前经验值' 
AFTER `level`;

-- 更新现有数据的经验值为0
UPDATE `player_operator` SET `current_exp` = 0 WHERE `current_exp` IS NULL;

-- 验证字段添加成功
DESCRIBE `player_operator`; 