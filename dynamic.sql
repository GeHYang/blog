/*
 Navicat Premium Data Transfer

 Source Server         : yang
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : yang_blog

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 23/04/2023 17:46:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dynamic
-- ----------------------------
DROP TABLE IF EXISTS `dynamic`;
CREATE TABLE `dynamic`  (
  `id` bigint(0) NOT NULL COMMENT '动态ID',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '动态文本内容',
  `imgs` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '动态图片',
  `remind_id` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '@的用户对应的id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `create_by` bigint(0) NOT NULL COMMENT '创建者id',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `update_by` bigint(0) NOT NULL COMMENT '更新者id',
  `del_flag` int(0) NOT NULL DEFAULT 0 COMMENT '删除状态：0正常，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dynamic
-- ----------------------------
INSERT INTO `dynamic` VALUES (1, '去玩了', NULL, NULL, '2023-04-23 11:33:32', 1, '2023-04-23 11:33:38', 1, 0);
INSERT INTO `dynamic` VALUES (2, '布达拉宫好好玩', NULL, NULL, '2023-04-23 11:33:58', 2, '2023-04-23 11:34:02', 2, 0);

SET FOREIGN_KEY_CHECKS = 1;
