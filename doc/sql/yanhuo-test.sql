/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 80034
Source Host           : localhost:3306
Source Database       : yanhuo-test

Target Server Type    : MYSQL
Target Server Version : 80034
File Encoding         : 65001

Date: 2023-12-23 22:10:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_album
-- ----------------------------
DROP TABLE IF EXISTS `t_album`;
CREATE TABLE `t_album` (
  `id` varchar(50) NOT NULL,
  `title` varchar(50) DEFAULT NULL,
  `uid` varchar(50) DEFAULT NULL,
  `album_cover` varchar(255) DEFAULT NULL,
  `sort` int DEFAULT NULL,
  `img_count` bigint DEFAULT '0',
  `collection_count` bigint DEFAULT '0',
  `creator` varchar(50) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `updater` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_album_img_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_album_img_relation`;
CREATE TABLE `t_album_img_relation` (
  `id` varchar(50) NOT NULL,
  `aid` varchar(50) DEFAULT NULL,
  `nid` varchar(50) DEFAULT NULL,
  `creator` varchar(50) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `updater` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category` (
  `id` varchar(50) NOT NULL,
  `title` varchar(50) NOT NULL,
  `pid` varchar(50) DEFAULT NULL,
  `sort` int DEFAULT NULL,
  `like_count` bigint DEFAULT NULL,
  `desc` longtext,
  `normal_cover` varchar(255) DEFAULT NULL COMMENT '分类的封面，如果是一级分类就是随便看看的封面，二级分类则是主封面',
  `hot_cover` varchar(255) DEFAULT NULL COMMENT '热门封面',
  `creator` varchar(50) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `updater` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_chat
-- ----------------------------
DROP TABLE IF EXISTS `t_chat`;
CREATE TABLE `t_chat` (
  `id` varchar(50) NOT NULL,
  `send_uid` varchar(50) DEFAULT NULL,
  `accept_uid` varchar(50) DEFAULT NULL,
  `content` longtext,
  `timestamp` bigint DEFAULT NULL,
  `creator` varchar(50) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `updater` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment` (
  `id` varchar(50) NOT NULL,
  `nid` varchar(50) DEFAULT NULL,
  `uid` varchar(50) DEFAULT NULL,
  `pid` varchar(50) DEFAULT NULL,
  `reply_id` varchar(50) DEFAULT '0',
  `reply_uid` varchar(50) DEFAULT '0',
  `level` int DEFAULT NULL,
  `sort` int DEFAULT NULL,
  `content` longtext,
  `count` bigint DEFAULT '0',
  `two_comment_count` bigint DEFAULT '0',
  `creator` varchar(50) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `updater` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_follower
-- ----------------------------
DROP TABLE IF EXISTS `t_follower`;
CREATE TABLE `t_follower` (
  `id` varchar(50) NOT NULL,
  `uid` varchar(50) DEFAULT NULL,
  `fid` varchar(50) DEFAULT NULL,
  `creator` varchar(50) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `updater` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_like_or_collection
-- ----------------------------
DROP TABLE IF EXISTS `t_like_or_collection`;
CREATE TABLE `t_like_or_collection` (
  `id` varchar(50) NOT NULL,
  `uid` varchar(50) NOT NULL COMMENT '点赞的用户',
  `like_or_collection_id` bigint NOT NULL COMMENT '点赞和收藏的id(可能是图片或者评论)',
  `publish_uid` bigint NOT NULL COMMENT '点赞和收藏通知的用户',
  `type` int NOT NULL,
  `timestamp` bigint NOT NULL,
  `creator` varchar(50) NOT NULL,
  `create_date` datetime NOT NULL,
  `updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_note
-- ----------------------------
DROP TABLE IF EXISTS `t_note`;
CREATE TABLE `t_note` (
  `id` varchar(50) NOT NULL,
  `content` longtext,
  `note_cover` varchar(255) DEFAULT NULL,
  `uid` varchar(50) DEFAULT NULL,
  `c_id` varchar(50) DEFAULT NULL,
  `c_pid` varchar(50) DEFAULT NULL,
  `urls` longtext,
  `count` int DEFAULT NULL,
  `sort` int DEFAULT NULL,
  `pinned` int DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `type` int DEFAULT NULL,
  `view_count` bigint DEFAULT '0',
  `like_count` bigint DEFAULT '0',
  `collection_count` bigint DEFAULT '0',
  `comment_count` bigint DEFAULT '0',
  `creator` varchar(50) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `updater` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `update_date_index` (`update_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE `t_tag` (
  `id` varchar(50) NOT NULL,
  `title` varchar(50) DEFAULT NULL,
  `like_count` bigint DEFAULT NULL,
  `sort` int DEFAULT NULL,
  `creator` varchar(50) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `updater` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_tag_note_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_tag_note_relation`;
CREATE TABLE `t_tag_note_relation` (
  `id` varchar(50) NOT NULL,
  `nid` varchar(50) NOT NULL,
  `tid` varchar(50) NOT NULL,
  `creator` varchar(50) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `updater` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` varchar(50) NOT NULL,
  `yx_id` bigint DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `avatar` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `gender` tinyint DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `desc` longtext,
  `status` tinyint DEFAULT NULL,
  `cover` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `birthday` varchar(50) DEFAULT NULL,
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `trend_count` bigint DEFAULT '0',
  `follower_count` bigint DEFAULT '0',
  `fan_count` bigint DEFAULT '0',
  `creator` varchar(50) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `updater` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
