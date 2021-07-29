-- 用户表
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`
(
    `id`          int(11)      NOT NULL AUTO_INCREMENT,
    `parent_id`   int(11)      NOT NULL DEFAULT '-1' COMMENT '父级id，-1表示顶级',
    `account`     varchar(50)  NOT NULL COMMENT '登录用户名,非用户类型默认空字符串',
    `password`    varchar(50)  NOT NULL COMMENT '登录密码,非用户类型默认空字符串',
    `name`        varchar(20)  NOT NULL COMMENT '姓名',
    `spell`       varchar(100) NOT NULL COMMENT '姓名拼音，（简拼，全拼）',
    `phone`       int(11)      NULL COMMENT '移动电话号码',
    `email`       varchar(50)  NULL COMMENT '邮箱',
    `create_time` datetime     NOT NULL COMMENT '创建时间',
    `type`        int(2)       NOT NULL COMMENT '用户类型，0用户，1部门',
    `freeze`      bit          NOT NULL DEFAULT b'0' COMMENT '是否冻结',
    `_order`      int(11)      NOT NULL COMMENT '同级序号',
    PRIMARY KEY (`id`),
    INDEX `index1` (`parent_id`) USING BTREE,
    INDEX `index2` (`account`) USING HASH
);
insert into member
values (null, -1, 'admin', '1', '超级管理员', 'cjgly,chaojiguanliyuan', null, null, now(), 0, 0, 0);
-- 用户令牌
DROP TABLE IF EXISTS `member_token`;
CREATE TABLE `member_token` (
                            `id`  int(11) NOT NULL ,
                            `username`  varchar(50) NOT NULL ,
                            `token`  varchar(64) NOT NULL ,
                            `create_time`  datetime NOT NULL ,
                            `last_used_time`  datetime NULL ,
                            `valid`  bit NOT NULL ,
                            PRIMARY KEY (`id`),
                            INDEX `index1` (`username`) USING HASH,
                            UNIQUE INDEX `index2` (`token`) USING HASH
);