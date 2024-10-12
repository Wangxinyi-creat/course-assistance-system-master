/*
 Navicat Premium Data Transfer

 Source Server         : mysql@localhost
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : localhost:3306
 Source Schema         : smart-classroom

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 25/04/2024 19:05:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for scas_course
-- ----------------------------
DROP TABLE IF EXISTS `scas_course`;
CREATE TABLE `scas_course`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `course_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程名称',
  `teacher_id` int NULL DEFAULT NULL COMMENT '教师ID',
  `class_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '上课时间',
  `course_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程类型',
  `course_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '课程介绍',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '课程状态',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of scas_course
-- ----------------------------
INSERT INTO `scas_course` VALUES (1, '测试', 3, '[{\"dictLabel\":\"周一\",\"dictValue\":\"15:00-17:00\"}]', '必修课', '测试', 2, 'teacher', '2024-03-19 15:10:29', 'teacher', '2024-04-25 18:44:16');

-- ----------------------------
-- Table structure for scas_course_resource
-- ----------------------------
DROP TABLE IF EXISTS `scas_course_resource`;
CREATE TABLE `scas_course_resource`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `course_id` int NOT NULL COMMENT '课程ID',
  `resource_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源名称',
  `resource_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '资源描述',
  `resource_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资料链接',
  `upload_date` datetime NULL DEFAULT NULL COMMENT '上传日期',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程资源表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of scas_course_resource
-- ----------------------------

-- ----------------------------
-- Table structure for scas_course_reviews
-- ----------------------------
DROP TABLE IF EXISTS `scas_course_reviews`;
CREATE TABLE `scas_course_reviews`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `course_id` int NOT NULL COMMENT '课程ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `review_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '评价内容',
  `review_date` datetime NULL DEFAULT NULL COMMENT '评价日期',
  `rating` int NOT NULL COMMENT '评分，范围1到5',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程评价表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of scas_course_reviews
-- ----------------------------
INSERT INTO `scas_course_reviews` VALUES (1, 1, 'student', '老师讲课水平非常高。', '2024-04-25 12:39:21', 5);

-- ----------------------------
-- Table structure for scas_experiment
-- ----------------------------
DROP TABLE IF EXISTS `scas_experiment`;
CREATE TABLE `scas_experiment`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `course_id` int NOT NULL COMMENT '课程ID',
  `job_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '作业号',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学生用户名',
  `experiment_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '实验报告标题',
  `experiment_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '实验报告描述',
  `deadline` datetime NOT NULL COMMENT '截止日期',
  `answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '回答',
  `upload_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '实验报告附件',
  `upload_date` datetime NULL DEFAULT NULL COMMENT '上传日期',
  `grade` double NULL DEFAULT NULL COMMENT '成绩',
  `review` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评语',
  `my_code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '实验代码',
  `result` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '结果',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '作业表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of scas_experiment
-- ----------------------------
INSERT INTO `scas_experiment` VALUES (1, 1, '第一次', 'student', '黑盒测试', '学习运用黑盒测试技术', '2024-04-25 00:00:00', '完成!', 'https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/smart-classroom/file/20240422/da49c6c7945345b7bd098c14703aef69.docx', '2024-04-25 18:55:06', NULL, NULL, 'csacascacaccascacasc', '正确');
INSERT INTO `scas_experiment` VALUES (2, 1, '第二次', 'student', '白盒测试', '运用白盒技术', '2024-04-30 00:00:00', '完成', 'https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/smart-classroom/file/20240422/e0e7c330681b448b94fc662419558d94.docx', '2024-04-22 15:21:51', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for scas_my_courses
-- ----------------------------
DROP TABLE IF EXISTS `scas_my_courses`;
CREATE TABLE `scas_my_courses`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID，关联用户表的user_name字段',
  `course_id` int NOT NULL COMMENT '课程ID，关联课程表的id字段',
  `enrollment_date` datetime NULL DEFAULT NULL COMMENT '选课日期',
  `ordinary_grade` double NULL DEFAULT NULL COMMENT '平时成绩',
  `exam_grade` double NULL DEFAULT NULL COMMENT '考试成绩',
  `grade` double NULL DEFAULT NULL COMMENT '总分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '我的课程表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of scas_my_courses
-- ----------------------------
INSERT INTO `scas_my_courses` VALUES (1, 'student', 1, '2024-04-22 15:08:47', NULL, NULL, NULL);
INSERT INTO `scas_my_courses` VALUES (2, '202001020136', 1, '2024-04-22 15:24:02', NULL, NULL, NULL);
INSERT INTO `scas_my_courses` VALUES (4, '202001020133', 1, '2024-04-23 20:54:12', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for scas_question_replies
-- ----------------------------
DROP TABLE IF EXISTS `scas_question_replies`;
CREATE TABLE `scas_question_replies`  (
  `reply_id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `course_id` int NOT NULL COMMENT '课程ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提问者的用户名',
  `question_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '提问内容',
  `reply_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '回复内容',
  `question_date` datetime NULL DEFAULT NULL COMMENT '提问日期',
  `reply_date` datetime NULL DEFAULT NULL COMMENT '回复日期',
  PRIMARY KEY (`reply_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '提问回复表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of scas_question_replies
-- ----------------------------
INSERT INTO `scas_question_replies` VALUES (1, 1, 'student', '黑盒测试是怎么实现的呢？', NULL, '2024-04-25 12:38:46', NULL);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` int NULL DEFAULT 0 COMMENT '父部门id',
  `sort_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '部门状态（1正常 0停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, 0, 0, '信息科学与工程学院', NULL, NULL, NULL, 1, 'system', '2024-04-22 15:11:08', 'system', '2024-04-22 15:11:08');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` int NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典属性值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '所属字典类型',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, '男', '1', 'sex');
INSERT INTO `sys_dict_data` VALUES (2, '女', '2', 'sex');
INSERT INTO `sys_dict_data` VALUES (3, '通知', '1', 'noticeType');
INSERT INTO `sys_dict_data` VALUES (4, '公告', '2', 'noticeType');
INSERT INTO `sys_dict_data` VALUES (5, '管理员', '管理员', 'Identity');
INSERT INTO `sys_dict_data` VALUES (6, '老师', '老师', 'Identity');
INSERT INTO `sys_dict_data` VALUES (7, '学生', '学生', 'Identity');
INSERT INTO `sys_dict_data` VALUES (8, '必修课', '必修课', 'courseType');
INSERT INTO `sys_dict_data` VALUES (9, '选修课', '选修课', 'courseType');
INSERT INTO `sys_dict_data` VALUES (10, '实践教学', '实践教学', 'courseType');
INSERT INTO `sys_dict_data` VALUES (11, '选课中', '1', 'courseStatus');
INSERT INTO `sys_dict_data` VALUES (12, '教学中', '2', 'courseStatus');
INSERT INTO `sys_dict_data` VALUES (13, '已结束', '3', 'courseStatus');
INSERT INTO `sys_dict_data` VALUES (14, '周一', '周一', 'week');
INSERT INTO `sys_dict_data` VALUES (15, '周二', '周二', 'week');
INSERT INTO `sys_dict_data` VALUES (16, '周三', '周三', 'week');
INSERT INTO `sys_dict_data` VALUES (17, '周四', '周四', 'week');
INSERT INTO `sys_dict_data` VALUES (18, '周五', '周五', 'week');
INSERT INTO `sys_dict_data` VALUES (19, '周六', '周六', 'week');
INSERT INTO `sys_dict_data` VALUES (20, '周日', '周日', 'week');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` int NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态（1正常 0停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '性别', 'sex', 1, 'admin', '2023-10-10 09:31:13', 'admin', '2023-12-27 14:52:47', '用户性别');
INSERT INTO `sys_dict_type` VALUES (2, '公告类型', 'noticeType', 1, 'admin', '2024-03-03 10:06:06', 'admin', '2024-03-03 10:06:06', '公告类型');
INSERT INTO `sys_dict_type` VALUES (3, '用户身份', 'Identity', 1, 'admin', '2024-03-18 11:30:31', 'admin', '2024-03-18 11:30:31', '用户身份');
INSERT INTO `sys_dict_type` VALUES (4, '课程类型', 'courseType', 1, 'admin', '2024-03-18 13:49:41', 'admin', '2024-03-18 13:49:41', '课程类型');
INSERT INTO `sys_dict_type` VALUES (5, '课程状态', 'courseStatus', 1, 'admin', '2024-03-18 13:50:28', 'admin', '2024-03-18 13:50:28', '课程状态');
INSERT INTO `sys_dict_type` VALUES (6, '星期', 'week', 1, 'admin', '2024-03-18 14:12:54', 'admin', '2024-03-18 14:12:54', '星期');

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `info_id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户账号',
  `ipaddr` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录IP地址',
  `login_location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录地点',
  `browser` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '登录日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
INSERT INTO `sys_login_log` VALUES (1, '202001020133', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:09:57');
INSERT INTO `sys_login_log` VALUES (2, '202001020133', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:10:12');
INSERT INTO `sys_login_log` VALUES (3, 'system', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:10:44');
INSERT INTO `sys_login_log` VALUES (4, '202001020133', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:12:46');
INSERT INTO `sys_login_log` VALUES (5, 'system', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:13:52');
INSERT INTO `sys_login_log` VALUES (6, 'teacher', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:16:25');
INSERT INTO `sys_login_log` VALUES (7, 'student', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:16:58');
INSERT INTO `sys_login_log` VALUES (8, '202001020136', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:23:49');
INSERT INTO `sys_login_log` VALUES (9, 'teacher', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:24:39');
INSERT INTO `sys_login_log` VALUES (10, 'system', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:40:36');
INSERT INTO `sys_login_log` VALUES (11, 'student', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 16:25:49');
INSERT INTO `sys_login_log` VALUES (12, 'system', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 16:43:57');
INSERT INTO `sys_login_log` VALUES (13, 'teacher', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 16:57:09');
INSERT INTO `sys_login_log` VALUES (14, '202001020136', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-23 20:50:43');
INSERT INTO `sys_login_log` VALUES (15, 'teacher', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-23 20:51:26');
INSERT INTO `sys_login_log` VALUES (16, 'system', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-23 20:52:26');
INSERT INTO `sys_login_log` VALUES (17, '202001020133', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-23 20:53:10');
INSERT INTO `sys_login_log` VALUES (18, 'student', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-23 20:55:06');
INSERT INTO `sys_login_log` VALUES (19, 'student', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-23 20:56:13');
INSERT INTO `sys_login_log` VALUES (20, 'teacher', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 10:21:32');
INSERT INTO `sys_login_log` VALUES (21, 'student', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 10:21:59');
INSERT INTO `sys_login_log` VALUES (22, 'student', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 10:22:13');
INSERT INTO `sys_login_log` VALUES (23, 'teacher', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 12:25:17');
INSERT INTO `sys_login_log` VALUES (24, 'teacher', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 12:27:25');
INSERT INTO `sys_login_log` VALUES (25, 'student', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 12:37:25');
INSERT INTO `sys_login_log` VALUES (26, 'teacher', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 15:03:41');
INSERT INTO `sys_login_log` VALUES (27, 'student', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 15:03:56');
INSERT INTO `sys_login_log` VALUES (28, 'student', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 16:20:16');
INSERT INTO `sys_login_log` VALUES (29, 'student', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 16:21:09');
INSERT INTO `sys_login_log` VALUES (30, 'teacher', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 16:23:36');
INSERT INTO `sys_login_log` VALUES (31, 'student', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 17:54:29');
INSERT INTO `sys_login_log` VALUES (32, 'teacher', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 17:54:53');
INSERT INTO `sys_login_log` VALUES (33, 'system', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 18:30:07');
INSERT INTO `sys_login_log` VALUES (34, '202001020133', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 18:33:03');
INSERT INTO `sys_login_log` VALUES (35, '202001020136', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 18:33:48');
INSERT INTO `sys_login_log` VALUES (36, 'teacher', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 18:43:58');
INSERT INTO `sys_login_log` VALUES (37, 'admin', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 18:54:46');
INSERT INTO `sys_login_log` VALUES (38, '202001020136', '127.0.0.1', '内网IP', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 19:00:46');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '菜单编号',
  `menu_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由名称',
  `path` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由路径',
  `component` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径(位置)',
  `el_icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单图标(element-ui 图标样式)',
  `is_link` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外部链接',
  `is_iframe` tinyint(1) NULL DEFAULT 0 COMMENT '是否内嵌(0-否   1-是)',
  `is_hide` tinyint(1) NULL DEFAULT 0 COMMENT '是否隐藏-侧边栏(0-否   1-是)',
  `is_affix` tinyint(1) NULL DEFAULT 0 COMMENT '是否固定tagsView(0-否   1-是)',
  `menu_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单类型(0-目录，1-菜单  2-按钮)',
  `parent_id` int NULL DEFAULT 0 COMMENT '指向父类编号(0表示为父节点，若为子类填写父类id)',
  `permission` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '按钮权限标识',
  `sort_id` int NULL DEFAULT NULL COMMENT '排序',
  `show_flag` tinyint(1) NULL DEFAULT 1 COMMENT '是否可见(0-否 1-是)',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 79 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (5, '系统管理', '', '', '', 'el-icon-setting', NULL, 0, 0, 0, '0', 0, NULL, 21, 0, '', NULL, 'admin', '2024-03-19 09:06:13');
INSERT INTO `sys_menu` VALUES (6, '用户管理', 'powerUser', '/powerUser', 'system/user/index', 'el-icon-user', NULL, 0, 0, 0, '1', 30, NULL, 2, 1, '', NULL, 'admin', '2023-11-20 09:27:24');
INSERT INTO `sys_menu` VALUES (7, '角色管理', 'powerRole', '/powerRole', 'system/role/index', 'el-icon-lock', NULL, 0, 0, 0, '1', 30, NULL, 3, 1, '', NULL, 'admin', '2023-11-20 09:27:31');
INSERT INTO `sys_menu` VALUES (8, '菜单管理', 'powerMenu', '/powerMenu', 'system/menu/index', 'el-icon-notebook-2', NULL, 0, 0, 0, '1', 5, NULL, 3, 0, '', NULL, '', NULL);
INSERT INTO `sys_menu` VALUES (9, '添加用户', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 6, 'system:user:add', 1, 1, '', NULL, '', NULL);
INSERT INTO `sys_menu` VALUES (10, '修改用户', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 6, 'system:user:update', 2, 1, '', NULL, '', NULL);
INSERT INTO `sys_menu` VALUES (11, '删除用户', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 6, 'system:user:delete', 3, 1, '', NULL, '', NULL);
INSERT INTO `sys_menu` VALUES (12, '添加角色', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 7, 'system:role:add', 1, 1, '', NULL, '', NULL);
INSERT INTO `sys_menu` VALUES (13, '修改角色', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 7, 'system:role:update', 2, 1, '', NULL, '', NULL);
INSERT INTO `sys_menu` VALUES (14, '删除角色', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 7, 'system:role:delete', 3, 1, '', NULL, '', NULL);
INSERT INTO `sys_menu` VALUES (15, '查询用户', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 6, 'system:user:get', 0, 1, '', NULL, '', NULL);
INSERT INTO `sys_menu` VALUES (16, '查询角色', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 7, 'system:role:get', 0, 1, '', NULL, '', NULL);
INSERT INTO `sys_menu` VALUES (17, '日志管理', NULL, NULL, NULL, 'el-icon-folder-opened', NULL, 0, 0, 0, '0', 0, NULL, 22, 1, '', NULL, 'admin', '2023-12-27 14:14:19');
INSERT INTO `sys_menu` VALUES (18, '登录日志', 'loginLog', '/loginLog', 'log/login-log', 'el-icon-folder', NULL, 0, 0, 0, '1', 17, NULL, 0, 1, '', NULL, 'admin', '2023-10-09 17:13:33');
INSERT INTO `sys_menu` VALUES (19, '操作日志', 'operateLog', '/operateLog', 'log/operate-log', 'el-icon-document', NULL, 0, 0, 0, '1', 17, NULL, 1, 1, '', NULL, 'admin', '2023-10-09 17:13:47');
INSERT INTO `sys_menu` VALUES (21, '字典管理', '/dict', '/dict', 'system/dict/index', 'el-icon-notebook-1', NULL, 0, 0, 0, '1', 5, NULL, 5, 0, '', NULL, '', NULL);
INSERT INTO `sys_menu` VALUES (22, '公告管理', 'notice', '/notice', 'system/notice/index', 'el-icon-message-solid', NULL, 0, 0, 0, '1', 0, NULL, 7, 1, '', NULL, 'admin', '2024-04-18 10:53:20');
INSERT INTO `sys_menu` VALUES (23, '查看公告', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 22, 'system:notice:get', 0, 1, '', NULL, '', NULL);
INSERT INTO `sys_menu` VALUES (24, '添加公告', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 22, 'system:notice:add', 1, 1, '', NULL, '', NULL);
INSERT INTO `sys_menu` VALUES (25, '修改公告', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 22, 'system:notice:update', 2, 1, '', NULL, '', NULL);
INSERT INTO `sys_menu` VALUES (26, '删除公告', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 22, 'system:notice:delete', 3, 1, '', NULL, '', NULL);
INSERT INTO `sys_menu` VALUES (29, '学院管理', 'dept', '/dept', 'system/dept/index', 'el-icon-school', NULL, 0, 0, 0, '1', 30, NULL, 1, 1, 'admin', '2023-11-20 09:11:30', 'admin', '2024-03-18 11:32:06');
INSERT INTO `sys_menu` VALUES (30, '权限管理', NULL, NULL, NULL, 'el-icon-lock', NULL, 0, 0, 0, '0', 0, NULL, 20, 1, 'admin', '2023-11-20 09:25:02', 'admin', '2023-12-27 14:14:09');
INSERT INTO `sys_menu` VALUES (31, '删除部门', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 29, 'system:dept:delete', 4, 1, 'admin', '2023-11-21 08:54:33', 'admin', '2023-11-21 08:54:33');
INSERT INTO `sys_menu` VALUES (32, '新增部门', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 29, 'system:dept:add', 2, 1, 'admin', '2023-11-21 08:54:48', 'admin', '2023-11-21 08:54:48');
INSERT INTO `sys_menu` VALUES (33, '修改部门', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 29, 'system:dept:update', 3, 1, 'admin', '2023-11-21 08:55:05', 'admin', '2023-11-21 08:55:05');
INSERT INTO `sys_menu` VALUES (34, '查看部门', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 29, 'system:dept:get', 1, 1, 'admin', '2023-11-21 08:55:32', 'admin', '2023-11-21 08:55:32');
INSERT INTO `sys_menu` VALUES (35, '课程管理', 'course', '/course', 'scas/course/index', 'el-icon-notebook-2', NULL, 0, 0, 0, '1', 0, NULL, 1, 1, 'admin', '2024-03-18 13:17:57', 'admin', '2024-04-16 12:28:59');
INSERT INTO `sys_menu` VALUES (36, '获取课程', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 35, 'scas:course:get', 1, 1, 'admin', '2024-03-18 13:20:50', 'admin', '2024-03-18 13:20:50');
INSERT INTO `sys_menu` VALUES (37, '新增课程', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 35, 'scas:course:add', 2, 1, 'admin', '2024-03-18 13:21:04', 'admin', '2024-03-18 13:21:04');
INSERT INTO `sys_menu` VALUES (38, '修改课程', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 35, 'scas:course:update', 3, 1, 'admin', '2024-03-18 13:21:16', 'admin', '2024-03-18 13:21:16');
INSERT INTO `sys_menu` VALUES (39, '删除课程', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 35, 'scas:course:delete', 4, 1, 'admin', '2024-03-18 13:21:30', 'admin', '2024-03-18 13:21:30');
INSERT INTO `sys_menu` VALUES (46, '报名课程', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 35, 'scas:mycourses:add', 5, 1, 'admin', '2024-03-18 16:24:30', 'admin', '2024-03-18 16:24:30');
INSERT INTO `sys_menu` VALUES (50, '课程评价', 'reviews', '/reviews', 'scas/reviews/index', 'el-icon-c-scale-to-original', NULL, 0, 0, 0, '1', 0, NULL, 5, 1, 'admin', '2024-03-18 19:01:21', 'admin', '2024-04-18 10:53:09');
INSERT INTO `sys_menu` VALUES (52, '查看评价', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 50, 'scas:reviews:get', 1, 1, 'admin', '2024-03-18 19:19:00', 'admin', '2024-03-18 19:19:00');
INSERT INTO `sys_menu` VALUES (53, '删除评价', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 50, 'scas:reviews:delete', 2, 1, 'admin', '2024-03-18 19:19:21', 'admin', '2024-03-18 19:19:21');
INSERT INTO `sys_menu` VALUES (54, '提问回复', 'replies', '/replies', 'scas/replies/index', 'el-icon-microphone', NULL, 0, 0, 0, '1', 0, NULL, 6, 1, 'admin', '2024-03-18 19:25:08', 'admin', '2024-04-18 10:53:13');
INSERT INTO `sys_menu` VALUES (55, '获取提问回复', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 54, 'scas:replies:get', 1, 1, 'admin', '2024-03-18 19:34:58', 'admin', '2024-03-18 19:34:58');
INSERT INTO `sys_menu` VALUES (56, '新增提问', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 54, 'scas:replies:add', 2, 1, 'admin', '2024-03-18 19:35:10', 'admin', '2024-03-18 19:45:23');
INSERT INTO `sys_menu` VALUES (57, '修改提问', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 54, 'scas:replies:update', 3, 1, 'admin', '2024-03-18 19:35:25', 'admin', '2024-03-18 19:45:31');
INSERT INTO `sys_menu` VALUES (58, '删除提问回复', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 54, 'scas:replies:delete', 4, 1, 'admin', '2024-03-18 19:35:40', 'admin', '2024-03-18 19:35:40');
INSERT INTO `sys_menu` VALUES (59, '回复提问', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 54, 'scas:replies:reply', 5, 1, 'admin', '2024-03-18 19:45:50', 'admin', '2024-03-18 19:45:50');
INSERT INTO `sys_menu` VALUES (61, '发布作业', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 35, 'scas:assignment:add', 6, 1, 'admin', '2024-03-18 19:56:05', 'admin', '2024-03-18 19:56:05');
INSERT INTO `sys_menu` VALUES (66, '学习资源推荐', 'CFResource', '/CFResource', 'scas/CFResource/index', 'el-icon-picture-outline-round', NULL, 0, 0, 0, '1', 0, NULL, 0, 1, 'admin', '2024-03-19 08:55:16', 'admin', '2024-04-16 12:27:20');
INSERT INTO `sys_menu` VALUES (74, '我的课程', 'student', '/student', 'scas/student/index', 'el-icon-s-promotion', NULL, 0, 0, 0, '1', 0, NULL, 3, 1, 'admin', '2024-04-18 08:38:11', 'admin', '2024-04-18 09:40:27');
INSERT INTO `sys_menu` VALUES (75, '我加入的课程详情', 'courseDetail', '/courseDetail', 'scas/student/detail.vue', 'el-icon-s-operation', NULL, 0, 1, 0, '1', 0, NULL, 4, 1, 'admin', '2024-04-18 09:26:05', 'admin', '2024-04-18 10:53:05');
INSERT INTO `sys_menu` VALUES (77, '我创建的课程详情', 'courseDetailT', '/courseDetailT', 'scas/teacher/detail', 'el-icon-notebook-2', NULL, 0, 1, 0, '1', 0, NULL, 2, 1, 'admin', '2024-04-18 10:44:06', 'admin', '2024-04-18 10:52:58');
INSERT INTO `sys_menu` VALUES (78, '查看课程详情-教师', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, '2', 35, 'scas:course:detail', 2, 1, 'admin', '2024-04-18 10:49:35', 'admin', '2024-04-18 10:49:35');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '公告状态（1正常 0关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------

-- ----------------------------
-- Table structure for sys_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operate_log`;
CREATE TABLE `sys_operate_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `method_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求方法名称',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人',
  `req_ip` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求IP',
  `req_interface` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求接口',
  `req_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求方式',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态（0-失败  1-正常   2-异常）',
  `req_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `json_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '返回参数',
  `browser` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '系统类型',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `expend_time` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执行时长(毫秒)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统操作日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_operate_log
-- ----------------------------
INSERT INTO `sys_operate_log` VALUES (1, '新增我的课程', 'student', '127.0.0.1', '/scas/courses/add', 'POST', 1, '{\"courseId\":1,\"enrollmentDate\":\"2024-04-22 15:08:46.609\",\"id\":1,\"userName\":\"student\"}', '{\"code\":200,\"message\":\"添加成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:08:47', '14ms');
INSERT INTO `sys_operate_log` VALUES (2, '新增部门', 'system', '127.0.0.1', '/dept/add', 'POST', 1, '{\"createBy\":\"system\",\"createTime\":\"2024-04-22 15:11:07.637\",\"deptName\":\"信息科学与工程学院\",\"id\":1,\"parentId\":0,\"status\":true,\"updateBy\":\"system\",\"updateTime\":\"2024-04-22 15:11:07.637\"}', '{\"code\":200,\"message\":\"添加成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:11:08', '4ms');
INSERT INTO `sys_operate_log` VALUES (3, '添加用户', 'system', '127.0.0.1', '/user/add', 'POST', 1, '{\"roleSign\":[],\"user\":{\"className\":\"计算机\",\"createBy\":\"system\",\"createTime\":\"2024-04-22 15:12:07.885\",\"deptId\":1,\"email\":\"\",\"headSrc\":\"\",\"id\":5,\"identity\":\"学生\",\"major\":\"计算机\",\"nickName\":\"student\",\"password\":\"$2a$10$OuSoEjdUnLQythe0iba1N.oDdPfoFr8d/qk5LWsvkkQJd5HEEi0E6\",\"phone\":\"13388894562\",\"sex\":1,\"status\":true,\"userName\":\"202001020133\"}}', '{\"code\":200,\"message\":\"添加成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:12:08', '71ms');
INSERT INTO `sys_operate_log` VALUES (4, '修改用户', 'system', '127.0.0.1', '/user/update', 'POST', 1, '{\"roleSign\":[3],\"user\":{\"className\":\"软件工程\",\"createBy\":\"admin\",\"createTime\":\"2024-03-18 20:45:56\",\"email\":\"\",\"headSrc\":\"https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/manage/img/20240319/ce658dd438cc4f169aeeb6b5ebcd1f3d.jpg\",\"id\":4,\"identity\":\"学生\",\"major\":\"软件工程\",\"nickName\":\"student\",\"password\":\"$2a$10$01pJEly7vLOGkl9fghYcBOtJNJpIK3AT4VXazQZm9uUjg9M3jSVFe\",\"phone\":\"15852652648\",\"sex\":1,\"status\":true,\"updateBy\":\"system\",\"updateTime\":\"2024-04-22 15:14:17.938\",\"userName\":\"student\"}}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:14:18', '11ms');
INSERT INTO `sys_operate_log` VALUES (5, '修改用户', 'system', '127.0.0.1', '/user/update', 'POST', 1, '{\"roleSign\":[3],\"user\":{\"className\":\"计算机\",\"createBy\":\"system\",\"createTime\":\"2024-04-22 15:12:08\",\"deptId\":1,\"email\":\"\",\"headSrc\":\"\",\"id\":5,\"identity\":\"学生\",\"major\":\"计算机\",\"nickName\":\"student\",\"password\":\"$2a$10$OuSoEjdUnLQythe0iba1N.oDdPfoFr8d/qk5LWsvkkQJd5HEEi0E6\",\"phone\":\"13388894562\",\"sex\":1,\"status\":true,\"updateBy\":\"system\",\"updateTime\":\"2024-04-22 15:14:24.287\",\"userName\":\"202001020133\"}}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:14:24', '8ms');
INSERT INTO `sys_operate_log` VALUES (6, '修改个人信息', '202001020133', '127.0.0.1', '/user/updatePersonal', 'POST', 1, '{\"className\":\"计算机\",\"createBy\":\"system\",\"createTime\":\"2024-04-22 15:12:08\",\"deptId\":1,\"email\":\"\",\"headSrc\":\"https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/smart-classroom/img/20240422/af0d151780a549baba8002fe7148e31b.jpg\",\"id\":5,\"identity\":\"学生\",\"major\":\"计算机\",\"nickName\":\"student\",\"password\":\"$2a$10$OuSoEjdUnLQythe0iba1N.oDdPfoFr8d/qk5LWsvkkQJd5HEEi0E6\",\"phone\":\"13388894562\",\"sex\":1,\"status\":true,\"updateBy\":\"202001020133\",\"updateTime\":\"2024-04-22 15:15:02.749\",\"userName\":\"202001020133\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:15:03', '4ms');
INSERT INTO `sys_operate_log` VALUES (7, '修改用户', 'system', '127.0.0.1', '/user/update', 'POST', 1, '{\"roleSign\":[3],\"user\":{\"className\":\"软件工程\",\"createBy\":\"admin\",\"createTime\":\"2024-03-18 20:45:56\",\"deptId\":1,\"email\":\"\",\"headSrc\":\"https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/manage/img/20240319/ce658dd438cc4f169aeeb6b5ebcd1f3d.jpg\",\"id\":4,\"identity\":\"学生\",\"major\":\"软件工程\",\"nickName\":\"student\",\"password\":\"$2a$10$01pJEly7vLOGkl9fghYcBOtJNJpIK3AT4VXazQZm9uUjg9M3jSVFe\",\"phone\":\"15852652648\",\"sex\":1,\"status\":true,\"updateBy\":\"system\",\"updateTime\":\"2024-04-22 15:15:43.861\",\"userName\":\"student\"}}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:15:44', '6ms');
INSERT INTO `sys_operate_log` VALUES (8, '修改用户', 'system', '127.0.0.1', '/user/update', 'POST', 1, '{\"roleSign\":[2],\"user\":{\"className\":\"\",\"createBy\":\"admin\",\"createTime\":\"2024-03-18 16:56:27\",\"deptId\":1,\"email\":\"\",\"headSrc\":\"https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/manage/img/20240319/13d984d220a04b2d9544a021c087303f.jpg\",\"id\":3,\"identity\":\"老师\",\"major\":\"\",\"nickName\":\"teacher\",\"password\":\"$2a$10$mvpJx9V5NQYvrY9Tp4ka8.eTEQVKX4h5OX0EUA9sl9An.nMxy08AK\",\"phone\":\"15852635248\",\"sex\":1,\"status\":true,\"updateBy\":\"system\",\"updateTime\":\"2024-04-22 15:15:48.104\",\"userName\":\"teacher\"}}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:15:48', '6ms');
INSERT INTO `sys_operate_log` VALUES (9, '修改课程', 'teacher', '127.0.0.1', '/scas/course/update', 'PUT', 1, '{\"classTime\":\"[{\\\"dictLabel\\\":\\\"周一\\\",\\\"dictValue\\\":\\\"15:00-17:00\\\"}]\",\"courseDescription\":\"测试\",\"courseName\":\"测试\",\"courseType\":\"必修课\",\"createBy\":\"teacher\",\"createTime\":\"2024-03-19 15:10:29\",\"id\":1,\"status\":1,\"teacherId\":3,\"teacherName\":\"teacher\",\"updateBy\":\"teacher\",\"updateTime\":\"2024-04-22 15:16:35.401\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:16:35', '2ms');
INSERT INTO `sys_operate_log` VALUES (10, '新增我的课程', 'student', '127.0.0.1', '/scas/courses/add', 'POST', 0, '{\"courseId\":1}', '{\"code\":-1,\"message\":\"课程已报名\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:17:14', '2ms');
INSERT INTO `sys_operate_log` VALUES (11, '修改课程', 'teacher', '127.0.0.1', '/scas/course/update', 'PUT', 1, '{\"classTime\":\"[{\\\"dictLabel\\\":\\\"周一\\\",\\\"dictValue\\\":\\\"15:00-17:00\\\"}]\",\"courseDescription\":\"测试\",\"courseName\":\"测试\",\"courseType\":\"必修课\",\"createBy\":\"teacher\",\"createTime\":\"2024-03-19 15:10:29\",\"id\":1,\"status\":2,\"teacherId\":3,\"teacherName\":\"teacher\",\"updateBy\":\"teacher\",\"updateTime\":\"2024-04-22 15:17:25.82\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:17:26', '8ms');
INSERT INTO `sys_operate_log` VALUES (12, '新增实验报告', 'teacher', '127.0.0.1', '/scas/experiment/add', 'POST', 1, '{\"courseId\":1,\"deadline\":\"2024-04-25 00:00:00\",\"experimentDescription\":\"学习运用黑盒测试技术\",\"experimentTitle\":\"黑盒测试\",\"jobNumber\":\"第一次\"}', '{\"code\":200,\"message\":\"添加成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:18:35', '15ms');
INSERT INTO `sys_operate_log` VALUES (13, '修改实验报告-状态', 'student', '127.0.0.1', '/scas/experiment/update', 'PUT', 1, '{\"answer\":\"完成\",\"courseId\":1,\"courseName\":\"测试\",\"deadline\":\"2024-04-25 00:00:00\",\"editFlag\":1,\"experimentDescription\":\"学习运用黑盒测试技术\",\"experimentTitle\":\"黑盒测试\",\"id\":1,\"jobNumber\":\"第一次\",\"uploadDate\":\"2024-04-22 15:19:40.149\",\"uploadUrl\":\"https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/smart-classroom/file/20240422/da49c6c7945345b7bd098c14703aef69.docx\",\"userName\":\"student\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:19:40', '7ms');
INSERT INTO `sys_operate_log` VALUES (14, '新增实验报告', 'teacher', '127.0.0.1', '/scas/experiment/add', 'POST', 1, '{\"courseId\":1,\"deadline\":\"2024-04-30 00:00:00\",\"experimentDescription\":\"运用白盒技术\",\"experimentTitle\":\"白盒测试\",\"jobNumber\":\"第二次\"}', '{\"code\":200,\"message\":\"添加成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:21:24', '4ms');
INSERT INTO `sys_operate_log` VALUES (15, '修改实验报告-状态', 'student', '127.0.0.1', '/scas/experiment/update', 'PUT', 1, '{\"answer\":\"完成\",\"courseId\":1,\"courseName\":\"测试\",\"deadline\":\"2024-04-30 00:00:00\",\"editFlag\":1,\"experimentDescription\":\"运用白盒技术\",\"experimentTitle\":\"白盒测试\",\"id\":2,\"jobNumber\":\"第二次\",\"uploadDate\":\"2024-04-22 15:21:50.754\",\"uploadUrl\":\"https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/smart-classroom/file/20240422/e0e7c330681b448b94fc662419558d94.docx\",\"userName\":\"student\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:21:51', '9ms');
INSERT INTO `sys_operate_log` VALUES (16, '修改课程', 'teacher', '127.0.0.1', '/scas/course/update', 'PUT', 1, '{\"classTime\":\"[{\\\"dictLabel\\\":\\\"周一\\\",\\\"dictValue\\\":\\\"15:00-17:00\\\"}]\",\"courseDescription\":\"测试\",\"courseName\":\"测试\",\"courseType\":\"必修课\",\"createBy\":\"teacher\",\"createTime\":\"2024-03-19 15:10:29\",\"id\":1,\"status\":1,\"teacherId\":3,\"teacherName\":\"teacher\",\"updateBy\":\"teacher\",\"updateTime\":\"2024-04-22 15:22:22.461\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:22:22', '8ms');
INSERT INTO `sys_operate_log` VALUES (17, '新增我的课程', '202001020136', '127.0.0.1', '/scas/courses/add', 'POST', 1, '{\"courseId\":1,\"enrollmentDate\":\"2024-04-22 15:24:02.254\",\"id\":2,\"userName\":\"202001020136\"}', '{\"code\":200,\"message\":\"添加成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:24:02', '10ms');
INSERT INTO `sys_operate_log` VALUES (18, '修改课程', 'teacher', '127.0.0.1', '/scas/course/update', 'PUT', 1, '{\"classTime\":\"[{\\\"dictLabel\\\":\\\"周一\\\",\\\"dictValue\\\":\\\"15:00-17:00\\\"}]\",\"courseDescription\":\"测试\",\"courseName\":\"测试\",\"courseType\":\"必修课\",\"createBy\":\"teacher\",\"createTime\":\"2024-03-19 15:10:29\",\"id\":1,\"status\":2,\"teacherId\":3,\"teacherName\":\"teacher\",\"updateBy\":\"teacher\",\"updateTime\":\"2024-04-22 15:24:46.508\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:24:47', '9ms');
INSERT INTO `sys_operate_log` VALUES (19, '修改个人信息', '202001020136', '127.0.0.1', '/user/updatePersonal', 'POST', 1, '{\"className\":\"计算机\",\"createBy\":\"202001020136\",\"createTime\":\"2024-04-22 15:23:33\",\"deptId\":1,\"email\":\"\",\"headSrc\":\"https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/smart-classroom/img/20240422/81a63bdf17aa496c94a76ec4b5156f16.jpg\",\"id\":6,\"identity\":\"学生\",\"major\":\"计算机\",\"nickName\":\"王同学\",\"password\":\"$2a$10$R/85qJfaOyF3MlYR3rwUv.nDQ6up35LnGcXyvHT91F2WinaoD2v/m\",\"phone\":\"13355559999\",\"sex\":2,\"status\":true,\"updateBy\":\"202001020136\",\"updateTime\":\"2024-04-22 15:27:29.345\",\"userName\":\"202001020136\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:27:29', '8ms');
INSERT INTO `sys_operate_log` VALUES (20, '修改个人信息', '202001020136', '127.0.0.1', '/user/updatePersonal', 'POST', 1, '{\"className\":\"计算机\",\"createBy\":\"202001020136\",\"createTime\":\"2024-04-22 15:23:33\",\"deptId\":1,\"email\":\"\",\"headSrc\":\"https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/smart-classroom/img/20240422/40f96588950042e8a09138109abe0963.png\",\"id\":6,\"identity\":\"学生\",\"major\":\"计算机\",\"nickName\":\"王同学\",\"password\":\"$2a$10$R/85qJfaOyF3MlYR3rwUv.nDQ6up35LnGcXyvHT91F2WinaoD2v/m\",\"phone\":\"13355559999\",\"sex\":2,\"status\":true,\"updateBy\":\"202001020136\",\"updateTime\":\"2024-04-22 15:27:40.703\",\"userName\":\"202001020136\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 15:27:41', '9ms');
INSERT INTO `sys_operate_log` VALUES (21, '修改个人信息', 'student', '127.0.0.1', '/user/updatePersonal', 'POST', 1, '{\"className\":\"软件工程\",\"createBy\":\"admin\",\"createTime\":\"2024-03-18 20:45:56\",\"deptId\":1,\"email\":\"\",\"headSrc\":\"https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/manage/img/20240319/ce658dd438cc4f169aeeb6b5ebcd1f3d.jpg\",\"id\":4,\"identity\":\"学生\",\"major\":\"软件工程\",\"nickName\":\"student\",\"password\":\"$2a$10$01pJEly7vLOGkl9fghYcBOtJNJpIK3AT4VXazQZm9uUjg9M3jSVFe\",\"phone\":\"15852652648\",\"sex\":1,\"status\":true,\"updateBy\":\"student\",\"updateTime\":\"2024-04-22 16:33:12.445\",\"userName\":\"student\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-22 16:33:12', '16ms');
INSERT INTO `sys_operate_log` VALUES (22, '修改课程', 'teacher', '127.0.0.1', '/scas/course/update', 'PUT', 1, '{\"classTime\":\"[{\\\"dictLabel\\\":\\\"周一\\\",\\\"dictValue\\\":\\\"15:00-17:00\\\"}]\",\"courseDescription\":\"测试\",\"courseName\":\"测试\",\"courseType\":\"必修课\",\"createBy\":\"teacher\",\"createTime\":\"2024-03-19 15:10:29\",\"id\":1,\"status\":1,\"teacherId\":3,\"teacherName\":\"teacher\",\"updateBy\":\"teacher\",\"updateTime\":\"2024-04-23 20:51:33.885\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-23 20:51:34', '8ms');
INSERT INTO `sys_operate_log` VALUES (23, '新增我的课程', '202001020136', '127.0.0.1', '/scas/courses/add', 'POST', 0, '{\"courseId\":1}', '{\"code\":-1,\"message\":\"课程已报名\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-23 20:51:58', '3ms');
INSERT INTO `sys_operate_log` VALUES (24, '新增我的课程', '202001020133', '127.0.0.1', '/scas/courses/add', 'POST', 1, '{\"courseId\":1,\"enrollmentDate\":\"2024-04-23 20:53:27.534\",\"id\":3,\"userName\":\"202001020133\"}', '{\"code\":200,\"message\":\"添加成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-23 20:53:28', '12ms');
INSERT INTO `sys_operate_log` VALUES (25, '删除我的课程', 'teacher', '127.0.0.1', '/scas/courses/delete/3', 'DELETE', 1, '3', '{\"code\":200,\"data\":true,\"message\":\"true\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-23 20:54:08', '12ms');
INSERT INTO `sys_operate_log` VALUES (26, '新增我的课程', '202001020133', '127.0.0.1', '/scas/courses/add', 'POST', 1, '{\"courseId\":1,\"enrollmentDate\":\"2024-04-23 20:54:11.523\",\"id\":4,\"userName\":\"202001020133\"}', '{\"code\":200,\"message\":\"添加成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-23 20:54:12', '4ms');
INSERT INTO `sys_operate_log` VALUES (27, '修改课程', 'teacher', '127.0.0.1', '/scas/course/update', 'PUT', 1, '{\"classTime\":\"[{\\\"dictLabel\\\":\\\"周一\\\",\\\"dictValue\\\":\\\"15:00-17:00\\\"}]\",\"courseDescription\":\"测试\",\"courseName\":\"测试\",\"courseType\":\"必修课\",\"createBy\":\"teacher\",\"createTime\":\"2024-03-19 15:10:29\",\"id\":1,\"status\":2,\"teacherId\":3,\"teacherName\":\"teacher\",\"updateBy\":\"teacher\",\"updateTime\":\"2024-04-23 20:54:30.665\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-23 20:54:31', '9ms');
INSERT INTO `sys_operate_log` VALUES (28, '新增提问回复', 'student', '127.0.0.1', '/scas/replies/add', 'POST', 1, '{\"courseId\":1,\"questionDate\":\"2024-04-25 12:38:46.495\",\"questionText\":\"黑盒测试是怎么实现的呢？\",\"replyId\":1,\"userName\":\"student\"}', '{\"code\":200,\"message\":\"添加成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 12:38:47', '7ms');
INSERT INTO `sys_operate_log` VALUES (29, '新增课程评价', 'student', '127.0.0.1', '/scas/reviews/add', 'POST', 1, '{\"courseId\":1,\"id\":1,\"rating\":5,\"reviewDate\":\"2024-04-25 12:39:20.79\",\"reviewText\":\"老师讲课水平非常高。\",\"userName\":\"student\"}', '{\"code\":200,\"message\":\"评价成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 12:39:21', '12ms');
INSERT INTO `sys_operate_log` VALUES (30, '修改实验报告-状态', 'student', '127.0.0.1', '/scas/experiment/update', 'PUT', 1, '{\"answer\":\"完成\",\"courseId\":1,\"courseName\":\"测试\",\"deadline\":\"2024-04-25 00:00:00\",\"editFlag\":1,\"experimentDescription\":\"学习运用黑盒测试技术\",\"experimentTitle\":\"黑盒测试\",\"id\":1,\"jobNumber\":\"第一次\",\"uploadDate\":\"2024-04-25 17:57:07.153\",\"uploadUrl\":\"https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/smart-classroom/file/20240422/da49c6c7945345b7bd098c14703aef69.docx\",\"userName\":\"student\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 17:57:07', '15ms');
INSERT INTO `sys_operate_log` VALUES (31, '修改实验报告-状态', 'student', '127.0.0.1', '/scas/experiment/update', 'PUT', 1, '{\"answer\":\"完成!\",\"courseId\":1,\"courseName\":\"测试\",\"deadline\":\"2024-04-25 00:00:00\",\"editFlag\":1,\"experimentDescription\":\"学习运用黑盒测试技术\",\"experimentTitle\":\"黑盒测试\",\"id\":1,\"jobNumber\":\"第一次\",\"uploadDate\":\"2024-04-25 18:00:46.284\",\"uploadUrl\":\"https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/smart-classroom/file/20240422/da49c6c7945345b7bd098c14703aef69.docx\",\"userName\":\"student\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 18:00:46', '12ms');
INSERT INTO `sys_operate_log` VALUES (32, '添加用户', 'system', '127.0.0.1', '/user/add', 'POST', 1, '{\"roleSign\":[],\"user\":{\"className\":\"\",\"createBy\":\"system\",\"createTime\":\"2024-04-25 18:31:15.901\",\"email\":\"\",\"headSrc\":\"\",\"id\":7,\"identity\":\"老师\",\"major\":\"\",\"nickName\":\"\",\"password\":\"$2a$10$VM1LFg8PBLvZNaBe9Exp9e/vMrhrM/DdKhLM.ILlQgWc.baCqkQLO\",\"phone\":\"13144556677\",\"sex\":1,\"status\":true,\"userName\":\"C\"}}', '{\"code\":200,\"message\":\"添加成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 18:31:16', '140ms');
INSERT INTO `sys_operate_log` VALUES (33, '删除用户', 'system', '127.0.0.1', '/user/delete/7', 'DELETE', 1, '7', '{\"code\":200,\"data\":true,\"message\":\"true\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 18:31:24', '10ms');
INSERT INTO `sys_operate_log` VALUES (34, '修改课程', 'teacher', '127.0.0.1', '/scas/course/update', 'PUT', 1, '{\"classTime\":\"[{\\\"dictLabel\\\":\\\"周一\\\",\\\"dictValue\\\":\\\"15:00-17:00\\\"}]\",\"courseDescription\":\"测试\",\"courseName\":\"测试\",\"courseType\":\"必修课\",\"createBy\":\"teacher\",\"createTime\":\"2024-03-19 15:10:29\",\"id\":1,\"status\":2,\"teacherId\":3,\"teacherName\":\"teacher\",\"updateBy\":\"teacher\",\"updateTime\":\"2024-04-25 18:44:07.07\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 18:44:07', '12ms');
INSERT INTO `sys_operate_log` VALUES (35, '修改课程', 'teacher', '127.0.0.1', '/scas/course/update', 'PUT', 1, '{\"classTime\":\"[{\\\"dictLabel\\\":\\\"周一\\\",\\\"dictValue\\\":\\\"15:00-17:00\\\"}]\",\"courseDescription\":\"测试\",\"courseName\":\"测试\",\"courseType\":\"必修课\",\"createBy\":\"teacher\",\"createTime\":\"2024-03-19 15:10:29\",\"id\":1,\"status\":1,\"teacherId\":3,\"teacherName\":\"teacher\",\"updateBy\":\"teacher\",\"updateTime\":\"2024-04-25 18:44:11.299\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 18:44:11', '9ms');
INSERT INTO `sys_operate_log` VALUES (36, '修改课程', 'teacher', '127.0.0.1', '/scas/course/update', 'PUT', 1, '{\"classTime\":\"[{\\\"dictLabel\\\":\\\"周一\\\",\\\"dictValue\\\":\\\"15:00-17:00\\\"}]\",\"courseDescription\":\"测试\",\"courseName\":\"测试\",\"courseType\":\"必修课\",\"createBy\":\"teacher\",\"createTime\":\"2024-03-19 15:10:29\",\"id\":1,\"status\":2,\"teacherId\":3,\"teacherName\":\"teacher\",\"updateBy\":\"teacher\",\"updateTime\":\"2024-04-25 18:44:16.145\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 18:44:16', '9ms');
INSERT INTO `sys_operate_log` VALUES (37, '修改实验报告-状态', 'admin', '127.0.0.1', '/scas/experiment/update', 'PUT', 1, '{\"answer\":\"完成!\",\"courseId\":1,\"courseName\":\"测试\",\"deadline\":\"2024-04-25 00:00:00\",\"editFlag\":1,\"experimentDescription\":\"学习运用黑盒测试技术\",\"experimentTitle\":\"黑盒测试\",\"id\":1,\"jobNumber\":\"第一次\",\"myCode\":\"csacascacaccascacasc\",\"result\":\"正确\",\"uploadDate\":\"2024-04-25 18:55:06.18\",\"uploadUrl\":\"https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/smart-classroom/file/20240422/da49c6c7945345b7bd098c14703aef69.docx\",\"userName\":\"student\"}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 18:55:06', '8ms');
INSERT INTO `sys_operate_log` VALUES (38, '修改用户', 'admin', '127.0.0.1', '/user/update', 'POST', 1, '{\"roleSign\":[3],\"user\":{\"className\":\"计算机\",\"createBy\":\"202001020136\",\"createTime\":\"2024-04-22 15:23:33\",\"deptId\":1,\"email\":\"\",\"headSrc\":\"https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/smart-classroom/img/20240422/40f96588950042e8a09138109abe0963.png\",\"id\":6,\"identity\":\"学生\",\"major\":\"计算机\",\"nickName\":\"王同学\",\"password\":\"$2a$10$uaf3V8JRJLzu3ALXYkM3n.vgTaiVi9AliBt6GX9k6DCFWfPJKZZPu\",\"phone\":\"13355559999\",\"sex\":2,\"status\":true,\"updateBy\":\"admin\",\"updateTime\":\"2024-04-25 19:00:24.163\",\"userName\":\"202001020136\"}}', '{\"code\":200,\"message\":\"修改成功\"}', 'Lenovo', 'Windows 10 or Windows Server 2016', '2024-04-25 19:00:24', '90ms');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键（角色编号)',
  `role_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名称',
  `data_scope` tinyint NULL DEFAULT 2 COMMENT '数据范围（1：全部数据权限 2：自定数据权限）',
  `remarks` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `show_flag` tinyint(1) NULL DEFAULT 1 COMMENT '是否显示(0-否   1-是)',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_name_UNIQUE`(`role_name` ASC) USING BTREE COMMENT '角色名称唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '系统管理员', 2, '自定义数据权限', 1, 'admin', '2023-09-01 11:01:02', 'admin', '2024-04-16 14:47:34');
INSERT INTO `sys_role` VALUES (2, '老师', 2, '老师', 1, 'admin', '2024-03-18 11:48:32', 'admin', '2024-03-18 11:48:50');
INSERT INTO `sys_role` VALUES (3, '学生', 2, '学生', 1, 'admin', '2024-03-18 11:48:40', 'admin', '2024-03-18 11:48:40');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int NULL DEFAULT NULL COMMENT '角色编号',
  `menu_id` int NULL DEFAULT NULL COMMENT '菜单编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_menu_menu_id`(`menu_id` ASC) USING BTREE,
  INDEX `role_menu_role_id`(`role_id` ASC) USING BTREE,
  CONSTRAINT `role_menu_menu_id` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `role_menu_role_id` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1, 35);
INSERT INTO `sys_role_menu` VALUES (2, 1, 54);
INSERT INTO `sys_role_menu` VALUES (3, 1, 66);
INSERT INTO `sys_role_menu` VALUES (4, 1, 36);
INSERT INTO `sys_role_menu` VALUES (5, 1, 38);
INSERT INTO `sys_role_menu` VALUES (6, 1, 39);
INSERT INTO `sys_role_menu` VALUES (7, 1, 50);
INSERT INTO `sys_role_menu` VALUES (8, 1, 52);
INSERT INTO `sys_role_menu` VALUES (9, 1, 53);
INSERT INTO `sys_role_menu` VALUES (10, 1, 55);
INSERT INTO `sys_role_menu` VALUES (11, 1, 58);
INSERT INTO `sys_role_menu` VALUES (12, 1, 22);
INSERT INTO `sys_role_menu` VALUES (13, 1, 23);
INSERT INTO `sys_role_menu` VALUES (14, 1, 24);
INSERT INTO `sys_role_menu` VALUES (15, 1, 25);
INSERT INTO `sys_role_menu` VALUES (16, 1, 26);
INSERT INTO `sys_role_menu` VALUES (17, 1, 30);
INSERT INTO `sys_role_menu` VALUES (18, 1, 29);
INSERT INTO `sys_role_menu` VALUES (19, 1, 34);
INSERT INTO `sys_role_menu` VALUES (20, 1, 32);
INSERT INTO `sys_role_menu` VALUES (21, 1, 33);
INSERT INTO `sys_role_menu` VALUES (22, 1, 31);
INSERT INTO `sys_role_menu` VALUES (23, 1, 6);
INSERT INTO `sys_role_menu` VALUES (24, 1, 15);
INSERT INTO `sys_role_menu` VALUES (25, 1, 9);
INSERT INTO `sys_role_menu` VALUES (26, 1, 10);
INSERT INTO `sys_role_menu` VALUES (27, 1, 11);
INSERT INTO `sys_role_menu` VALUES (28, 1, 7);
INSERT INTO `sys_role_menu` VALUES (29, 1, 16);
INSERT INTO `sys_role_menu` VALUES (30, 1, 12);
INSERT INTO `sys_role_menu` VALUES (31, 1, 13);
INSERT INTO `sys_role_menu` VALUES (32, 1, 14);
INSERT INTO `sys_role_menu` VALUES (33, 1, 17);
INSERT INTO `sys_role_menu` VALUES (34, 1, 18);
INSERT INTO `sys_role_menu` VALUES (35, 1, 19);
INSERT INTO `sys_role_menu` VALUES (36, 2, 35);
INSERT INTO `sys_role_menu` VALUES (37, 2, 50);
INSERT INTO `sys_role_menu` VALUES (38, 2, 54);
INSERT INTO `sys_role_menu` VALUES (39, 2, 66);
INSERT INTO `sys_role_menu` VALUES (40, 2, 36);
INSERT INTO `sys_role_menu` VALUES (41, 2, 78);
INSERT INTO `sys_role_menu` VALUES (42, 2, 37);
INSERT INTO `sys_role_menu` VALUES (43, 2, 38);
INSERT INTO `sys_role_menu` VALUES (44, 2, 39);
INSERT INTO `sys_role_menu` VALUES (45, 2, 61);
INSERT INTO `sys_role_menu` VALUES (46, 2, 77);
INSERT INTO `sys_role_menu` VALUES (47, 2, 52);
INSERT INTO `sys_role_menu` VALUES (48, 2, 55);
INSERT INTO `sys_role_menu` VALUES (49, 2, 59);
INSERT INTO `sys_role_menu` VALUES (50, 2, 22);
INSERT INTO `sys_role_menu` VALUES (51, 2, 23);
INSERT INTO `sys_role_menu` VALUES (52, 2, 24);
INSERT INTO `sys_role_menu` VALUES (53, 2, 25);
INSERT INTO `sys_role_menu` VALUES (54, 2, 26);
INSERT INTO `sys_role_menu` VALUES (55, 3, 35);
INSERT INTO `sys_role_menu` VALUES (56, 3, 54);
INSERT INTO `sys_role_menu` VALUES (57, 3, 22);
INSERT INTO `sys_role_menu` VALUES (58, 3, 66);
INSERT INTO `sys_role_menu` VALUES (59, 3, 36);
INSERT INTO `sys_role_menu` VALUES (60, 3, 46);
INSERT INTO `sys_role_menu` VALUES (61, 3, 74);
INSERT INTO `sys_role_menu` VALUES (62, 3, 75);
INSERT INTO `sys_role_menu` VALUES (63, 3, 50);
INSERT INTO `sys_role_menu` VALUES (64, 3, 52);
INSERT INTO `sys_role_menu` VALUES (65, 3, 53);
INSERT INTO `sys_role_menu` VALUES (66, 3, 55);
INSERT INTO `sys_role_menu` VALUES (67, 3, 56);
INSERT INTO `sys_role_menu` VALUES (68, 3, 57);
INSERT INTO `sys_role_menu` VALUES (69, 3, 58);
INSERT INTO `sys_role_menu` VALUES (70, 3, 23);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户账户(学工号)',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `nick_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '性别（1-男  2-女）',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `head_src` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像地址',
  `status` tinyint NULL DEFAULT 1 COMMENT '帐号状态（1正常 0停用）',
  `dept_id` int NULL DEFAULT NULL COMMENT '所属部门编号',
  `identity` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份（学生，老师）',
  `major` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专业',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '班级',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_name`(`user_name` ASC) USING BTREE,
  INDEX `dept_id`(`dept_id` ASC) USING BTREE,
  CONSTRAINT `sys_user_ibfk_1` FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '账号表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$zcMl0MS8KOd/QZFfx9/Feuc45llyJyvPpIxQ41EPk0/CYISVo28PS', 'admin', 1, '13313963674', '2448805312@qq.com', 'https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/manage/img/20230901/5f0a9fb906fd434fa755026821af1466.jpg', 1, NULL, '管理员', NULL, NULL, '开发者', '2023-07-07 09:47:38', 'admin', '2023-09-01 15:51:12');
INSERT INTO `sys_user` VALUES (2, 'system', '$2a$10$ykjqahDw.Is8Xt5K/8SyOeH5EuyPZJ8Ney5BR.BIQ7jlHYVclm8Jm', '系统管理员', 1, '13313963685', '758066871@qq.com', 'https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/manage/img/20230728/6a2054b7d35744418e819b4f62ea0b7d.jpg', 1, 7, '管理员', '', '', 'admin', '2023-07-28 08:34:55', 'admin', '2024-03-18 11:43:26');
INSERT INTO `sys_user` VALUES (3, 'teacher', '$2a$10$mvpJx9V5NQYvrY9Tp4ka8.eTEQVKX4h5OX0EUA9sl9An.nMxy08AK', 'teacher', 1, '15852635248', '', 'https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/manage/img/20240319/13d984d220a04b2d9544a021c087303f.jpg', 1, 1, '老师', '', '', 'admin', '2024-03-18 16:56:27', 'system', '2024-04-22 15:15:48');
INSERT INTO `sys_user` VALUES (4, 'student', '$2a$10$01pJEly7vLOGkl9fghYcBOtJNJpIK3AT4VXazQZm9uUjg9M3jSVFe', 'student', 1, '15852652648', '', 'https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/manage/img/20240319/ce658dd438cc4f169aeeb6b5ebcd1f3d.jpg', 1, 1, '学生', '软件工程', '软件工程', 'admin', '2024-03-18 20:45:56', 'student', '2024-04-22 16:33:12');
INSERT INTO `sys_user` VALUES (5, '202001020133', '$2a$10$OuSoEjdUnLQythe0iba1N.oDdPfoFr8d/qk5LWsvkkQJd5HEEi0E6', 'student', 1, '13388894562', '', 'https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/smart-classroom/img/20240422/af0d151780a549baba8002fe7148e31b.jpg', 1, 1, '学生', '计算机', '计算机', 'system', '2024-04-22 15:12:08', '202001020133', '2024-04-22 15:15:03');
INSERT INTO `sys_user` VALUES (6, '202001020136', '$2a$10$uaf3V8JRJLzu3ALXYkM3n.vgTaiVi9AliBt6GX9k6DCFWfPJKZZPu', '王同学', 2, '13355559999', '', 'https://zhedian-00.oss-cn-hangzhou.aliyuncs.com/smart-classroom/img/20240422/40f96588950042e8a09138109abe0963.png', 1, 1, '学生', '计算机', '计算机', '202001020136', '2024-04-22 15:23:33', 'admin', '2024-04-25 19:00:24');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户账号',
  `role_id` int NULL DEFAULT NULL COMMENT '角色编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_user_user_name`(`user_name` ASC) USING BTREE,
  INDEX `role_user_role_id`(`role_id` ASC) USING BTREE,
  CONSTRAINT `role_user_role_id` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `role_user_user_name` FOREIGN KEY (`user_name`) REFERENCES `sys_user` (`user_name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '账号角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 'system', 1);
INSERT INTO `sys_user_role` VALUES (5, '202001020133', 3);
INSERT INTO `sys_user_role` VALUES (6, 'student', 3);
INSERT INTO `sys_user_role` VALUES (7, 'teacher', 2);
INSERT INTO `sys_user_role` VALUES (9, '202001020136', 3);

SET FOREIGN_KEY_CHECKS = 1;
