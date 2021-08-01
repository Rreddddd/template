-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`          int(11)     NOT NULL AUTO_INCREMENT,
    `account`     varchar(50) NOT NULL COMMENT '登录用户名',
    `password`    varchar(50) NOT NULL COMMENT '登录密码',
    `name`        varchar(20) NOT NULL COMMENT '姓名',
    `phone`       int(11)     NULL COMMENT '移动电话号码',
    `email`       varchar(50) NULL COMMENT '邮箱',
    `create_time` datetime    NOT NULL COMMENT '创建时间',
    `freeze`      bit         NOT NULL DEFAULT b'0' COMMENT '是否冻结',
    `position_id` int         NULL COMMENT '职位/身份',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `index1` (`account`) USING HASH
);
-- 用户令牌
DROP TABLE IF EXISTS `user_token`;
CREATE TABLE `user_token`
(
    `series`         varchar(64) NOT NULL,
    `username`       varchar(50) NOT NULL,
    `token`          varchar(64) NOT NULL,
    `create_time`    datetime    NOT NULL,
    `last_used_time` datetime    NOT NULL,
    `valid`          bit         NOT NULL,
    PRIMARY KEY (`series`)
);
-- 模块
DROP TABLE IF EXISTS `module`;
CREATE TABLE `module`
(
    `id`    int(11)      NOT NULL AUTO_INCREMENT,
    `title` varchar(20)  NOT NULL COMMENT '模块名称',
    `url`   varchar(200) NOT NULL COMMENT '模块路径',
    PRIMARY KEY (`id`)
);
-- 菜单
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`
(
    `id`         int(11)     NOT NULL AUTO_INCREMENT,
    `parent_id`  int(11)     NOT NULL DEFAULT '-1' COMMENT '父级id',
    `module_id`  int(11)     NULL COMMENT '模块id',
    `title`      varchar(20) NOT NULL COMMENT '菜单名称',
    `icon_class` varchar(50) NULL COMMENT '图标类名',
    `icon_color` varchar(7)  NULL COMMENT '图标16进制带#号颜色',
    `_order`     int(4)      NOT NULL COMMENT '同级顺序',
    PRIMARY KEY (`id`),
    INDEX `index1` (`parent_id`) USING BTREE,
    INDEX `index2` (`module_id`) USING BTREE
);
-- 权限关联
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`
(
    `id`      int(11) NOT NULL AUTO_INCREMENT,
    `auth_id` int(11) NOT NULL COMMENT '授权方',
    `menu_id` int(11) NOT NULL COMMENT '菜单',
    `type`    int(2)  NOT NULL COMMENT '0:用户,1:职位/身份',
    PRIMARY KEY (`id`),
    INDEX `index1` (`auth_id`, `type`) USING BTREE
);
-- 人员 职位/身份
DROP TABLE IF EXISTS `position`;
CREATE TABLE `position`
(
    `id`      int(11)     NOT NULL AUTO_INCREMENT,
    `name`    varchar(20) NOT NULL,
    `visible` bit         NOT NULL,
    PRIMARY KEY (`id`)
);

insert into user
values (null, 'admin', '{red}{MD5}202cb962ac59075b964b07152d234b70', 'Administrator', null, null, now(), 0, 1);

insert into position
values (null, '超级管理员', 0);

insert into module
values (1, '模块管理', '/sys/module');
insert into module
values (2, '导航管理', '/sys/menu');

insert into menu
values (1, -1, null, '系统管理', 'iconfont icon-xitongguanli', '#6666fb', 0);
insert into menu
values (2, 1, 1, '模块管理', 'iconfont icon-huaban61', '#7982fc', 1);
insert into menu
values (3, 1, 2, '导航管理', 'iconfont icon-daohang', '#0079ff', 2);