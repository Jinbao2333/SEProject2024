drop database if exists paperLibrary;
create database paperLibrary;

use paperLibrary;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- ----------------------------
-- Table structure for attach_file
-- ----------------------------
DROP TABLE IF EXISTS `attach_file`;
CREATE TABLE `attach_file`  (
  `upload_id` int NULL DEFAULT NULL,
  `file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  INDEX `fk_attach_file_upload_1`(`upload_id` ASC) USING BTREE,
  CONSTRAINT `fk_attach_file_upload_1` FOREIGN KEY (`upload_id`) REFERENCES `upload` (`upload_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attach_file
-- ----------------------------

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `text` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `super_id` int NULL DEFAULT NULL,
  `paper_id` int NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `fk_comment_paper_1`(`paper_id` ASC) USING BTREE,
  INDEX `fk_comment_comment_1`(`super_id` ASC) USING BTREE,
  INDEX `fk_comment_user_1`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_comment_comment_1` FOREIGN KEY (`super_id`) REFERENCES `comment` (`comment_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_paper_1` FOREIGN KEY (`paper_id`) REFERENCES `paper` (`paper_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------

INSERT INTO `comment` VALUES (0, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for cover
-- ----------------------------
DROP TABLE IF EXISTS `cover`;
CREATE TABLE `cover`  (
  `paper_id` int NOT NULL,
  `field_id` int NOT NULL,
  PRIMARY KEY (`paper_id`, `field_id`) USING BTREE,
  INDEX `fk_cover_field_1`(`field_id` ASC) USING BTREE,
  CONSTRAINT `fk_cover_field_1` FOREIGN KEY (`field_id`) REFERENCES `field` (`field_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_cover_paper_1` FOREIGN KEY (`paper_id`) REFERENCES `paper` (`paper_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cover
-- ----------------------------


-- ----------------------------
-- Table structure for field
-- ----------------------------
DROP TABLE IF EXISTS `field`;
CREATE TABLE `field`  (
  `field_id` int NOT NULL AUTO_INCREMENT,
  `field_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pid` int NULL DEFAULT NULL,
  PRIMARY KEY (`field_id`) USING BTREE,
  INDEX `fk_field_field_1`(`pid` ASC) USING BTREE,
  CONSTRAINT `fk_field_field_1` FOREIGN KEY (`pid`) REFERENCES `field` (`field_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of field
-- ----------------------------
INSERT INTO `field` VALUES (0, '全部', NULL);
INSERT INTO `field` VALUES (1, 'ECNU', 0);
INSERT INTO `field` VALUES (2, 'SJTU', 0);
INSERT INTO `field` VALUES (3, 'FDU', 0);
INSERT INTO `field` VALUES (4, 'A-001', 1);
INSERT INTO `field` VALUES (5, 'A-002', 1);
INSERT INTO `field` VALUES (6, 'A-003', 1);
INSERT INTO `field` VALUES (7, 'A-001', 2);
INSERT INTO `field` VALUES (8, 'A-002', 2);
INSERT INTO `field` VALUES (9, 'A-003', 2);
INSERT INTO `field` VALUES (10, 'A-001', 3);
INSERT INTO `field` VALUES (11, 'A-002', 3);
INSERT INTO `field` VALUES (12, 'A-003', 3);


-- ----------------------------
-- Table structure for note
-- ----------------------------
DROP TABLE IF EXISTS `note`;
CREATE TABLE `note`  (
  `note_id` int NOT NULL AUTO_INCREMENT,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`note_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of note
-- ----------------------------


-- ----------------------------
-- Table structure for paper
-- ----------------------------
DROP TABLE IF EXISTS `paper`;
CREATE TABLE `paper`  (
  `paper_id` int NOT NULL AUTO_INCREMENT,
  `unique_id` varchar(255) DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `conference` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `summary` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`paper_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of paper
-- ----------------------------


-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `permission_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `comment` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`permission_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, 'Home', '/home', ' 主页', 'el-icon-house');
INSERT INTO `permission` VALUES (2, 'ChangeProfile', '/changeProfile', '信息修改', '');
-- INSERT INTO `permission` VALUES (3, 'Category_query', '/category-query', ' 浏览', 'el-icon-reading');
INSERT INTO `permission` VALUES (3, 'Category', '/category', ' 浏览', 'el-icon-reading');
INSERT INTO `permission` VALUES (5, 'Upload', '/upload', ' 上传', 'el-icon-upload');
INSERT INTO `permission` VALUES (6, 'Search', '/search', ' 检索', 'el-icon-search');
INSERT INTO `permission` VALUES (7, 'User', '/user', '用户管理', 'el-icon-user');
INSERT INTO `permission` VALUES (8, 'Field', '/field', '货架管理', 'el-icon-menu');
INSERT INTO `permission` VALUES (9, 'Role', '/role', '权限管理', 'el-icon-s-custom');
INSERT INTO `permission` VALUES (10, 'PaperList', '/paperList', '列表', 'el-icon-s-reading');
INSERT INTO `permission` VALUES (11, 'Detail', '/detail', '详情', NULL);

-- ----------------------------
-- Table structure for publish
-- ----------------------------
DROP TABLE IF EXISTS `publish`;
CREATE TABLE `publish`  (
  `author_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `paper_id` int NULL DEFAULT NULL,
  INDEX `fk_publish_paper_1`(`paper_id` ASC) USING BTREE,
  CONSTRAINT `fk_publish_paper_1` FOREIGN KEY (`paper_id`) REFERENCES `paper` (`paper_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of publish
-- ----------------------------


-- ----------------------------
-- Table structure for reference
-- ----------------------------
DROP TABLE IF EXISTS `reference`;
CREATE TABLE `reference`  (
  `paper_id` int NOT NULL,
  `reference_id` int NOT NULL,
  PRIMARY KEY (`paper_id`, `reference_id`) USING BTREE,
  INDEX `fk_refer`(`reference_id` ASC) USING BTREE,
  CONSTRAINT `fk_refer` FOREIGN KEY (`reference_id`) REFERENCES `paper` (`paper_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reference
-- ----------------------------


-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `comment` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'admin', '管理员');
INSERT INTO `role` VALUES (2, 'user', '普通用户');
INSERT INTO `role` VALUES (3, 'block', '封禁用户');


-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `role_id` int NOT NULL,
  `permission_id` int NOT NULL,
  PRIMARY KEY (`role_id`, `permission_id`) USING BTREE,
  INDEX `fk_user_permission_permission_1`(`permission_id` ASC) USING BTREE,
  INDEX `fk_role_permission_role_1`(`role_id` ASC) USING BTREE,
  CONSTRAINT `fk_role_permission_role_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_permission_permission_1` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`permission_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (1, 1);
INSERT INTO `role_permission` VALUES (2, 1);
INSERT INTO `role_permission` VALUES (3, 1);
INSERT INTO `role_permission` VALUES (4, 1);
INSERT INTO `role_permission` VALUES (1, 2);
INSERT INTO `role_permission` VALUES (2, 2);
INSERT INTO `role_permission` VALUES (4, 2);
INSERT INTO `role_permission` VALUES (1, 3);
INSERT INTO `role_permission` VALUES (2, 3);
INSERT INTO `role_permission` VALUES (4, 3);
INSERT INTO `role_permission` VALUES (1, 5);
INSERT INTO `role_permission` VALUES (2, 5);
INSERT INTO `role_permission` VALUES (1, 6);
INSERT INTO `role_permission` VALUES (2, 6);
INSERT INTO `role_permission` VALUES (1, 7);
INSERT INTO `role_permission` VALUES (1, 8);
INSERT INTO `role_permission` VALUES (1, 9);
INSERT INTO `role_permission` VALUES (1, 10);
INSERT INTO `role_permission` VALUES (2, 10);
INSERT INTO `role_permission` VALUES (4, 10);
INSERT INTO `role_permission` VALUES (1, 11);
INSERT INTO `role_permission` VALUES (2, 11);

-- ----------------------------
-- Table structure for upload
-- ----------------------------
DROP TABLE IF EXISTS `upload`;
CREATE TABLE `upload`  (
  `upload_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `upload_date` datetime NULL DEFAULT NULL,
  `paper_id` int NULL DEFAULT NULL,
  `note_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`upload_id`) USING BTREE,
  INDEX `fk_upload_note_1`(`note_id` ASC) USING BTREE,
  INDEX `fk_upload_user_1`(`user_id` ASC) USING BTREE,
  INDEX `fk_upload_paper_1`(`paper_id` ASC) USING BTREE,
  CONSTRAINT `fk_upload_paper_1` FOREIGN KEY (`paper_id`) REFERENCES `paper` (`paper_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_upload_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of upload
-- ----------------------------


-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role_id` int NULL DEFAULT NULL,
  `valid` int NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (-1, '已删除内容', NULL, NULL, NULL, 0);
INSERT INTO `user` VALUES (1, 'root', '$2a$10$lWKXyNqfyCOLxLr6rD34MeEXy0s9amHTgnxHAqJOG5Z60xVonHcUO', NULL, 1, 1);
INSERT INTO `user` VALUES (2, 'test1', '$2a$10$lWKXyNqfyCOLxLr6rD34MeEXy0s9amHTgnxHAqJOG5Z60xVonHcUO', NULL, 2, 1);

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO `paper` VALUES (1, 'YIDA20232024', '【取件通知】您的快递已到，请至A-001-5货架2号区域领取', '', '2024-03-10', '', '', '');
INSERT INTO `cover` VALUES (1, 0);
INSERT INTO `cover` VALUES (1, 1);
INSERT INTO `cover` VALUES (1, 3);
INSERT INTO `publish` VALUES ('贺云航', 18810242048, 1);