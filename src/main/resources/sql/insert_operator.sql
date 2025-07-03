-- 插入玩家干员测试数据
-- 用于向player_operator表中插入测试数据

INSERT INTO `player_operator` (`player_id`, `operator_id`, `elite_level`, `level`, `skill_level`, `skill_mastery`, `create_time`, `update_time`) VALUES
-- 玩家1的干员数据
(1, 1001, 1, 30, 4, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),
(1, 1002, 0, 20, 3, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),
(1, 1003, 2, 50, 6, 2, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),

-- 玩家2的干员数据
(2, 1001, 0, 15, 2, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),
(2, 1004, 1, 35, 5, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),

-- 玩家3的干员数据
(3, 1002, 1, 40, 5, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),
(3, 1005, 0, 25, 3, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),

-- 玩家4的干员数据
(4, 1003, 0, 10, 1, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),
(4, 1006, 1, 45, 6, 2, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),

-- 玩家5的干员数据
(5, 1001, 2, 60, 7, 3, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),
(5, 1004, 1, 38, 5, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),
(5, 1007, 0, 18, 2, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()); 