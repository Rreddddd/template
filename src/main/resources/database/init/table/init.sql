-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`          int(11)      NOT NULL AUTO_INCREMENT,
    `account`     varchar(50)  NOT NULL COMMENT '登录用户名',
    `password`    varchar(50)  NOT NULL COMMENT '登录密码',
    `name`        varchar(20)  NOT NULL COMMENT '姓名',
    `phone`       char(11)     NULL COMMENT '移动电话号码',
    `email`       varchar(50)  NULL COMMENT '邮箱',
    `head_img`    varchar(100) NULL COMMENT '头像相对路径',
    `create_time` datetime     NOT NULL COMMENT '创建时间',
    `freeze`      bit          NOT NULL DEFAULT b'0' COMMENT '是否冻结',
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
    `id`         int(11)     NOT NULL,
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
    `type`    int(2)  NOT NULL COMMENT '0:用户,1:职位/身份',
    `menu_id` int(11) NOT NULL COMMENT '菜单',
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
-- 人员_职位 关联
DROP TABLE IF EXISTS `user_position`;
CREATE TABLE `user_position`
(
    `user_id`     int(11) NOT NULL,
    `position_id` int(11) NOT NULL,
    PRIMARY KEY (`user_id`, `position_id`)
);
-- 属性设置
DROP TABLE IF EXISTS `property`;
CREATE TABLE `property`
(
    `id`        int(11)      NOT NULL AUTO_INCREMENT,
    `target_id` int(11)      NOT NULL,
    `key`       varchar(50)  NOT NULL,
    `value`     varchar(500) NULL,
    PRIMARY KEY (`id`),
    INDEX `index1` (`target_id`, `key`, `value`) USING BTREE
);
-- 访问记录
DROP TABLE IF EXISTS `access_record`;
CREATE TABLE `access_record`
(
    `id`      int(11)  NOT NULL AUTO_INCREMENT,
    `user_id` int(11)  NOT NULL,
    `type`    bit      NOT NULL,
    `time`    datetime NOT NULL,
    PRIMARY KEY (`id`)
);
-- 聊天记录
DROP TABLE IF EXISTS `im_record`;
CREATE TABLE `im_record`
(
    `id`         int(11)  NOT NULL AUTO_INCREMENT,
    `series`     char(36) NOT NULL,
    `from_id`    int(11)  NOT NULL,
    `to_id`      int(11)  NOT NULL,
    `content_id` int(11)  NOT NULL,
    `read`       bit      NOT NULL,
    `time`       datetime NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `index1` (`series`, `to_id`, `from_id`, `content_id`, `read`, `time`) USING BTREE
);
-- 聊天内容
DROP TABLE IF EXISTS `im_content`;
CREATE TABLE `im_content`
(
    `id`           int(11)      NOT NULL AUTO_INCREMENT,
    `content`      varchar(512) NOT NULL,
    `content_type` int(1)       NOT NULL,
    PRIMARY KEY (`id`)
)
    DEFAULT CHARACTER SET = utf8mb4;
-- 聊天列表
DROP TABLE IF EXISTS `im_list`;
CREATE TABLE `im_list`
(
    `id`              int(11)  NOT NULL AUTO_INCREMENT,
    `user_id`         int(11)  NOT NULL,
    `target_id`       int(11)  NOT NULL,
    `origin`          bit      NOT NULL,
    `last_content_id` int(11)  NOT NULL,
    `content_time`    datetime NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `index1` (`user_id`, `target_id`, `last_content_id`, `time`) USING BTREE
);

insert into user
values (null, 'admin', '{red}{red1}ac360b7295f9c09b7a284df3d2be2659', 'Administrator', null, null, null, now(), 0);
insert into user
values (null, 'test', '{red}{red1}ac360b7295f9c09b7a284df3d2be2659', 'test', null, null, null, now(), 0);

insert into position
values (null, '超级管理员', 0);

insert into user_position
values (1, 1);

insert into module
values (null, '模块管理', '/sys/module');
insert into module
values (null, '导航管理', '/sys/menu');
insert into module
values (null, '人员职位', '/sys/position');
insert into module
values (null, '人员管理', '/sys/user');

insert into menu
values (1, -1, null, '系统管理', 'iconfont icon-xitongguanli', '#6666fb', 0);
insert into menu
values (2, 1, 1, '模块管理', 'iconfont icon-huaban61', '#7982fc', 1);
insert into menu
values (3, 1, 2, '导航管理', 'iconfont icon-daohang', '#0079ff', 2);
insert into menu
values (4, 1, 3, '人员职位', 'iconfont icon-pipeizhiwei', '#0079ff', 3);
insert into menu
values (5, 1, 4, '人员管理', 'iconfont icon-renyuan', '#0079ff', 4);

insert into property
values (null, -1, 'default_leave_msg', '');