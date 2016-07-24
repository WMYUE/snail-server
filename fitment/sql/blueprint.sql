/*
Navicat MySQL Data Transfer

Source Server         : blueprint
Source Server Version : 50625
Source Host           : audi01.e.lanxin.cn:3306
Source Database       : blueprint

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2016-07-08 09:43:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `app_bill_t`
-- ----------------------------
DROP TABLE IF EXISTS `app_bill_t`;
CREATE TABLE `app_bill_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `number_of_person` int(11) DEFAULT NULL COMMENT '使用app的总人数',
  `number_of_messages` int(11) DEFAULT NULL COMMENT '已付费用',
  `paid_money` double DEFAULT NULL COMMENT '已缴纳费用',
  `fees` double DEFAULT NULL COMMENT '应缴纳费用',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0：初始状态，1：到了账单日期初始统计状态，2：还款状态，3：已还款状态，4：逾期停止使用app状态',
  `paid_time` timestamp NULL DEFAULT NULL COMMENT '还款时间',
  `repayment_time` timestamp NULL DEFAULT NULL COMMENT '最后还款日期',
  `bill_time` timestamp NULL DEFAULT NULL COMMENT '账单日期',
  `app_order_id` bigint(20) DEFAULT NULL,
  `start_bill_time` timestamp NULL DEFAULT NULL COMMENT '开始记账日期',
  `app_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='收费情况记录';

-- ----------------------------
-- Records of app_bill_t
-- ----------------------------

-- ----------------------------
-- Table structure for `app_category_t`
-- ----------------------------
DROP TABLE IF EXISTS `app_category_t`;
CREATE TABLE `app_category_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL COMMENT '分类名称',
  `category_desc` varchar(45) DEFAULT NULL COMMENT '描述信息',
  `p_id` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '分类,0:业务类型,1:行业类型',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='行业分类';

-- ----------------------------
-- Records of app_category_t
-- ----------------------------
INSERT INTO `app_category_t` VALUES ('1', '互联网/通信', '互联网/通信', '0', '0', '2016-04-25 17:29:04', '2016-04-25 17:29:07');
INSERT INTO `app_category_t` VALUES ('2', '计算机软件', '计算机软件', '1', '0', '2016-04-25 17:29:12', '2016-04-25 17:29:14');
INSERT INTO `app_category_t` VALUES ('3', '实时工具', '实时工具', '1', '0', null, null);
INSERT INTO `app_category_t` VALUES ('4', '计算机硬件', '计算机硬件', '1', '0', null, null);
INSERT INTO `app_category_t` VALUES ('5', '网络游戏', '网络游戏', '1', '0', null, null);
INSERT INTO `app_category_t` VALUES ('6', '金融业', '金融业', '0', '0', null, null);
INSERT INTO `app_category_t` VALUES ('7', '基金/证券', '基金/证券', '6', '0', null, null);
INSERT INTO `app_category_t` VALUES ('8', '信托/拍卖', '信托/拍卖', '6', '0', null, null);
INSERT INTO `app_category_t` VALUES ('9', '银行', '银行', '6', null, null, null);
INSERT INTO `app_category_t` VALUES ('10', '保险', '保险', '6', null, null, null);
INSERT INTO `app_category_t` VALUES ('11', '房地产/建筑业', '房地产/建筑业', '0', null, '2016-04-25 05:33:16', '2016-04-25 05:33:16');
INSERT INTO `app_category_t` VALUES ('12', '建材/工程', '建材/工程', '11', null, '2016-04-25 05:33:20', '2016-04-25 05:33:20');
INSERT INTO `app_category_t` VALUES ('13', '家具/室内', '家具/室内', '11', null, '2016-04-25 05:33:57', '2016-04-25 05:33:57');
INSERT INTO `app_category_t` VALUES ('14', '物业管理', '物业管理', '11', null, null, null);
INSERT INTO `app_category_t` VALUES ('15', '生产/加工', '生产/加工', '0', null, null, null);
INSERT INTO `app_category_t` VALUES ('16', '汽车/摩托车', '汽车/摩托车', '15', null, null, null);
INSERT INTO `app_category_t` VALUES ('17', '大型设备', '大型设备', '15', null, null, null);
INSERT INTO `app_category_t` VALUES ('18', '加工制造', '加工制造', '15', null, null, null);
INSERT INTO `app_category_t` VALUES ('19', '办公用品', '办公用品', '15', null, null, null);
INSERT INTO `app_category_t` VALUES ('20', '服务业', '服务业', '0', null, null, null);
INSERT INTO `app_category_t` VALUES ('21', '医疗/护理', '医疗/护理', '20', null, null, null);
INSERT INTO `app_category_t` VALUES ('22', '旅游/度假', '旅游/度假', '20', null, null, null);
INSERT INTO `app_category_t` VALUES ('23', '酒店/餐饮', '酒店/餐饮', '20', null, null, null);

-- ----------------------------
-- Table structure for `app_dependency_t`
-- ----------------------------
DROP TABLE IF EXISTS `app_dependency_t`;
CREATE TABLE `app_dependency_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_info_id` bigint(20) DEFAULT NULL,
  `app_id` varchar(45) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '依赖关系标识:0为直接依赖,1:间接依赖',
  PRIMARY KEY (`id`),
  KEY `fk_app_dependency_application_info_t1_idx` (`app_info_id`),
  CONSTRAINT `fk_app_dependency_application_info_t1` FOREIGN KEY (`app_info_id`) REFERENCES `application_info_t` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='app依赖关系表';

-- ----------------------------
-- Records of app_dependency_t
-- ----------------------------

-- ----------------------------
-- Table structure for `app_order_t`
-- ----------------------------
DROP TABLE IF EXISTS `app_order_t`;
CREATE TABLE `app_order_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_time` timestamp NULL DEFAULT NULL COMMENT '账单日期',
  `repayment_method` bigint(20) DEFAULT NULL COMMENT '生成账单日期方式',
  `status` int(11) DEFAULT NULL COMMENT '0:需进行扫描;1:已停止订阅,但本月仍进行扫描;2:已停止订阅,不再进行统计扫描',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `order_deadline` timestamp NULL DEFAULT NULL COMMENT '截至时间',
  `org_sub_info_id` bigint(20) DEFAULT NULL,
  `bill_day` int(11) DEFAULT NULL,
  `p_id` bigint(20) DEFAULT NULL COMMENT '父级节点,默认为0,在更改订阅方式时生成子级',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='app订阅产生订单';

-- ----------------------------
-- Records of app_order_t
-- ----------------------------

-- ----------------------------
-- Table structure for `app_publish_info_t`
-- ----------------------------
DROP TABLE IF EXISTS `app_publish_info_t`;
CREATE TABLE `app_publish_info_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_uni_id` varchar(45) DEFAULT NULL COMMENT '发布者id',
  `user_name` varchar(45) DEFAULT NULL,
  `domain` varchar(45) DEFAULT NULL COMMENT '发布者所属组织',
  `scope` varchar(300) DEFAULT NULL COMMENT '发布应用可见组织范围，1）ALL:全组织可见,2)多个组织之间用逗号分隔',
  `app_info_id` bigint(20) DEFAULT NULL COMMENT '发布app的Id',
  `app_name` varchar(100) DEFAULT NULL COMMENT '发布的应用名称',
  `app_pub_version` varchar(25) DEFAULT NULL COMMENT '应用发布版本',
  `app_desc` varchar(400) DEFAULT NULL COMMENT '应用描述信息',
  `app_icon_resource_id` varchar(75) DEFAULT NULL COMMENT '应用icon资源id',
  `screenshot_resource_id` varchar(300) DEFAULT NULL COMMENT '应用截图资源,以逗号分隔的资源id列表',
  `app_version_feature` varchar(255) DEFAULT NULL,
  `probation` int(11) DEFAULT NULL COMMENT '试用期,单位为月,默认为0,即没有试用期',
  `business_type` int(11) DEFAULT NULL COMMENT 'app所属行业类型',
  `profession_type` int(11) DEFAULT NULL COMMENT 'app所属业务场景类型',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `app_op_resource_id` varchar(45) DEFAULT NULL,
  `support_equipment` varchar(45) DEFAULT NULL,
  `project_file_resource_id` varchar(45) DEFAULT NULL,
  `recommend_type` int(11) DEFAULT NULL COMMENT '-1没有推荐属性，0:热门，1:精品',
  PRIMARY KEY (`id`),
  KEY `fk_app_publish_info_application_info1_idx` (`app_info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COMMENT='应用发布信息';

-- ----------------------------
-- Records of app_publish_info_t
-- ----------------------------

-- ----------------------------
-- Table structure for `app_push_record_t`
-- ----------------------------
DROP TABLE IF EXISTS `app_push_record_t`;
CREATE TABLE `app_push_record_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `grant_u_id` varchar(80) DEFAULT NULL COMMENT '推送人',
  `current_u_id` varchar(80) DEFAULT NULL,
  `current_u_name` varchar(50) DEFAULT NULL,
  `domain` varchar(100) DEFAULT NULL COMMENT '推送域',
  `branch` varchar(100) DEFAULT NULL COMMENT '推送分支',
  `app_pub_info_id` bigint(50) DEFAULT NULL COMMENT '对应订单信息',
  `app_id` varchar(45) DEFAULT NULL,
  `app_version` int(11) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL COMMENT '订阅状态',
  `create_serial_id` bigint(20) DEFAULT NULL,
  `operate_serial_id` bigint(20) DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '再次分发时上级分发记录id',
  `send_again` int(11) DEFAULT NULL COMMENT '是否可以转发',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=243 DEFAULT CHARSET=utf8 COMMENT='订阅app后产生app推送记录';

-- ----------------------------
-- Records of app_push_record_t
-- ----------------------------

-- ----------------------------
-- Table structure for `app_push_viewers_t`
-- ----------------------------
DROP TABLE IF EXISTS `app_push_viewers_t`;
CREATE TABLE `app_push_viewers_t` (
  `id` bigint(20) NOT NULL,
  `app_push_id` bigint(20) DEFAULT NULL,
  `user_uni_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='app查看者映射表';

-- ----------------------------
-- Records of app_push_viewers_t
-- ----------------------------

-- ----------------------------
-- Table structure for `app_usage_impact_t`
-- ----------------------------
DROP TABLE IF EXISTS `app_usage_impact_t`;
CREATE TABLE `app_usage_impact_t` (
  `id` bigint(20) NOT NULL,
  `domain` varchar(125) DEFAULT NULL COMMENT '对应的组织',
  `number_of_person` int(11) DEFAULT NULL,
  `number_of_messages` int(11) DEFAULT NULL,
  `org_sub_id` bigint(20) DEFAULT NULL COMMENT '订阅appId',
  `app_id` varchar(45) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

-- ----------------------------
-- Records of app_usage_impact_t
-- ----------------------------

-- ----------------------------
-- Table structure for `app_usage_log_t`
-- ----------------------------
DROP TABLE IF EXISTS `app_usage_log_t`;
CREATE TABLE `app_usage_log_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `domain` varchar(45) DEFAULT NULL COMMENT '组织域',
  `user_uni_id` varchar(45) DEFAULT NULL COMMENT '用户id',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `org_sub_info_id` bigint(20) DEFAULT NULL,
  `app_bill_id` bigint(20) DEFAULT NULL,
  `app_id` varchar(45) DEFAULT NULL COMMENT '使用的app应用的id',
  `app_usage_impact_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_usage_of_app_app_bill1_idx` (`app_bill_id`),
  KEY `fk_usage_of_app_org_subscription_info1_idx` (`org_sub_info_id`),
  KEY `fk_app_usage_log_app_usage_impact1_idx` (`app_usage_impact_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8 COMMENT='总线传输的app使用情况';

-- ----------------------------
-- Records of app_usage_log_t
-- ----------------------------
INSERT INTO `app_usage_log_t` VALUES ('1001', '2.audi-lanxin', '144@2.audi-lanxin', '1464088369570', '1464088369570', '0', '26', '0', '52DDC0947E65429DAC41986A5A81D567', '0');
INSERT INTO `app_usage_log_t` VALUES ('1002', '2.audi-lanxin', '144@2.audi-lanxin', '1464106369610', '1464106369610', '0', '26', '0', '52DDC0947E65429DAC41986A5A81D567', '0');
INSERT INTO `app_usage_log_t` VALUES ('1003', '2.audi-lanxin', '144@2.audi-lanxin', '1464221569658', '1464221569658', '0', '26', '0', '52DDC0947E65429DAC41986A5A81D567', '0');

-- ----------------------------
-- Table structure for `application_info_t`
-- ----------------------------
DROP TABLE IF EXISTS `application_info_t`;
CREATE TABLE `application_info_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` int(11) DEFAULT '0' COMMENT '应用类型\n0 : 库\n1：原始URL\n2：预定义H5（蓝图一期）\n3：重组页面（蓝图二期）\n4：原生自定义页面（蓝图三期）\n5 : 唤醒外部应用',
  `app_id` varchar(45) NOT NULL DEFAULT '' COMMENT '应用ID',
  `name` varchar(45) DEFAULT NULL COMMENT '应用名称',
  `resource_id` varchar(45) DEFAULT NULL COMMENT '应用资源信息id',
  `app_desc` varchar(255) DEFAULT NULL COMMENT '应用描述',
  `icon` varchar(255) DEFAULT NULL COMMENT '应用图标',
  `version` int(11) DEFAULT NULL COMMENT '应用版本',
  `min_runtime_version` int(11) DEFAULT NULL COMMENT '最小运行时版本',
  `min_framework_version` int(11) DEFAULT NULL COMMENT '最小Framework版本',
  `start_page` varchar(255) DEFAULT NULL COMMENT '首页',
  `valid_urls` varchar(500) DEFAULT NULL COMMENT '合法地址列表，只有在这里的地址，才可以使用IDriver',
  `error_report` varchar(255) DEFAULT NULL COMMENT '错误日志上报地址',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `domain` varchar(100) DEFAULT NULL,
  `file_version` int(11) DEFAULT NULL,
  `file_path` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=184 DEFAULT CHARSET=utf8 COMMENT='app基础信息表';

-- ----------------------------
-- Records of application_info_t
-- ----------------------------

-- ----------------------------
-- Table structure for `behavior_sycn_data_t`
-- ----------------------------
DROP TABLE IF EXISTS `behavior_sycn_data_t`;
CREATE TABLE `behavior_sycn_data_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `user_uni_id` varchar(200) NOT NULL COMMENT '用户唯一id',
  `behavior_data_id` varchar(200) NOT NULL COMMENT '行为数据唯一id',
  `behavior_data_table_name` varchar(200) NOT NULL COMMENT '行为数据对应表',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=571 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of behavior_sycn_data_t
-- ----------------------------

-- ----------------------------
-- Table structure for `bind_relationship_t`
-- ----------------------------
DROP TABLE IF EXISTS `bind_relationship_t`;
CREATE TABLE `bind_relationship_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '蓝图唯一id',
  `ex_platform_flag` varchar(100) NOT NULL DEFAULT 'BLUEPRINT' COMMENT '外部系统标志，如lanxin',
  `ex_platform_name` varchar(100) NOT NULL DEFAULT '蓝信' COMMENT '外部系统名称',
  `ex_domain_flag` varchar(100) NOT NULL COMMENT '外部组织标识',
  `ex_domain_name` varchar(100) NOT NULL COMMENT '外部组织名称',
  `ex_user_uni_id` varchar(100) NOT NULL COMMENT '外部系统的userUniId',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of bind_relationship_t
-- ----------------------------
INSERT INTO `bind_relationship_t` VALUES ('1', '1', 'BLUEPRINT', '蓝信', '3.conference-lanxin', '蓝图演示组织', '114@3.conference-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('2', '2', 'BLUEPRINT', '蓝信', '3.conference-lanxin', '蓝图演示组织', '148@3.conference-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('3', '3', 'BLUEPRINT', '蓝信', '3.conference-lanxin', '蓝图演示组织', '86@3.conference-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('4', '4', 'BLUEPRINT', '蓝信', '3.conference-lanxin', '蓝图演示组织', '94@3.conference-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('5', '5', 'BLUEPRINT', '蓝信', '3.conference-lanxin', '蓝图演示组织', '87@3.conference-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('6', '6', 'BLUEPRINT', '蓝信', '3.conference-lanxin', '蓝图演示组织', '222@3.conference-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('7', '7', 'BLUEPRINT', '蓝信', '3.conference-lanxin', '蓝图演示组织', '145@3.conference-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('8', '8', 'BLUEPRINT', '蓝信', '3.conference-lanxin', '蓝图演示组织', '79@3.conference-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('9', '9', 'BLUEPRINT', '蓝信', '3.conference-lanxin', '蓝图演示组织', '111@3.conference-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('10', '10', 'BLUEPRINT', '蓝信', '3.conference-lanxin', '蓝图演示组织', '239@3.conference-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('11', '11', 'BLUEPRINT', '蓝信', '3.conference-lanxin', '蓝图演示组织', '283@3.conference-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('12', '12', 'BLUEPRINT', '蓝信', '3.conference-lanxin', '蓝图演示组织', '112@3.conference-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('13', '13', 'BLUEPRINT', '蓝信', '3.conference-lanxin', '蓝图演示组织', '96@3.conference-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('14', '14', 'BLUEPRINT', '蓝信', '3.conference-lanxin', '蓝图演示组织', '240@3.conference-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('15', '15', 'BLUEPRINT', '蓝信', '3.conference-lanxin', '蓝图演示组织', '113@3.conference-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('16', '16', 'BLUEPRINT', '蓝信', '3.conference-lanxin', '蓝图演示组织', '77@3.conference-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('17', '17', 'BLUEPRINT', '蓝信', '3.conference-lanxin', '蓝图演示组织', '131@3.conference-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('18', '1', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '2@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('19', '2', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '137@1.72-lanxin ');
INSERT INTO `bind_relationship_t` VALUES ('20', '3', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '125@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('21', '4', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '126@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('22', '5', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '127@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('23', '6', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '128@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('24', '7', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '129@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('25', '8', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '130@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('26', '9', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '131@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('27', '10', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '132@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('28', '11', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '133@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('29', '12', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '134@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('30', '13', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '135@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('31', '14', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '136@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('32', '15', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '138@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('33', '16', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '74@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('34', '17', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '5@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('35', '18', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '139@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('36', '19', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '140@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('37', '20', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '141@1.72-lanxin');
INSERT INTO `bind_relationship_t` VALUES ('38', '21', 'BLUEPRINT', '蓝信', '1.72-lanxin', '蓝图测试组织', '142@1.72-lanxin');

-- ----------------------------
-- Table structure for `bp_user_t`
-- ----------------------------
DROP TABLE IF EXISTS `bp_user_t`;
CREATE TABLE `bp_user_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `login_name` varchar(100) NOT NULL COMMENT '登录名称（手机号）',
  `path` varchar(200) NOT NULL COMMENT '路径',
  `role_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '蓝图唯一id',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of bp_user_t
-- ----------------------------
INSERT INTO `bp_user_t` VALUES ('1', 'dengyiping', '', '0', '0', '2016-06-24 16:53:43', '2016-06-24 16:50:39');
INSERT INTO `bp_user_t` VALUES ('2', 'chenlei', '', '0', '0', '2016-06-24 16:53:47', '2016-06-24 16:50:42');
INSERT INTO `bp_user_t` VALUES ('3', 'lize', '', '0', '0', '2016-06-24 16:53:45', '2016-06-24 16:50:40');
INSERT INTO `bp_user_t` VALUES ('4', 'dengsitian', '', '0', '0', '2016-06-24 16:53:49', '2016-06-24 16:50:44');
INSERT INTO `bp_user_t` VALUES ('5', 'liweijun', '', '0', '0', '2016-06-24 16:53:50', '2016-06-24 16:50:45');
INSERT INTO `bp_user_t` VALUES ('6', 'yubeiming', '', '0', '0', '2016-06-24 16:53:51', '2016-06-24 16:50:47');
INSERT INTO `bp_user_t` VALUES ('7', 'xiabo', '', '0', '0', '2016-06-24 16:53:53', '2016-06-24 16:50:48');
INSERT INTO `bp_user_t` VALUES ('8', 'sunhaopeng', '', '0', '0', '2016-06-24 16:53:54', '2016-06-24 16:50:50');
INSERT INTO `bp_user_t` VALUES ('9', 'zhuliang', '', '0', '0', '2016-06-24 16:53:56', '2016-06-24 16:50:51');
INSERT INTO `bp_user_t` VALUES ('10', 'duwenyan', '', '0', '0', '2016-06-24 16:53:57', '2016-06-24 16:50:53');
INSERT INTO `bp_user_t` VALUES ('11', 'zhusiyang', '', '0', '0', '2016-06-24 16:53:59', '2016-06-24 16:50:54');
INSERT INTO `bp_user_t` VALUES ('12', 'xiexin', '', '0', '0', '2016-06-24 16:54:00', '2016-06-24 16:50:56');
INSERT INTO `bp_user_t` VALUES ('13', 'guopeili', '', '0', '0', '2016-06-24 16:54:02', '2016-06-24 16:50:57');
INSERT INTO `bp_user_t` VALUES ('14', 'jinmangang', '', '0', '0', '2016-06-24 16:54:03', '2016-06-24 16:50:58');
INSERT INTO `bp_user_t` VALUES ('15', 'handongtao', '', '0', '0', '2016-06-24 16:54:04', '2016-06-24 16:51:00');
INSERT INTO `bp_user_t` VALUES ('16', 'liujian', '', '0', '0', '2016-06-24 16:54:06', '2016-06-24 16:51:01');
INSERT INTO `bp_user_t` VALUES ('17', 'yuzhichun', '', '0', '0', '2016-06-24 16:54:08', '2016-06-24 16:51:02');
INSERT INTO `bp_user_t` VALUES ('18', 'shenhao', '', '0', '0', '2016-07-05 17:03:37', '2016-07-05 17:03:38');
INSERT INTO `bp_user_t` VALUES ('19', 'ranyaning', '', '0', '0', '2016-07-05 17:07:04', '2016-07-05 17:03:54');
INSERT INTO `bp_user_t` VALUES ('20', 'houqingfeng', '', '0', '0', '2016-07-05 17:04:08', '2016-07-05 17:04:10');
INSERT INTO `bp_user_t` VALUES ('21', 'shichao', '', '0', '0', '2016-07-05 17:04:19', '2016-07-05 17:04:21');

-- ----------------------------
-- Table structure for `charge_plan_t`
-- ----------------------------
DROP TABLE IF EXISTS `charge_plan_t`;
CREATE TABLE `charge_plan_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `serial` int(11) DEFAULT NULL COMMENT '区间序列',
  `min` int(11) DEFAULT NULL COMMENT '区间最小值',
  `max` int(11) DEFAULT NULL COMMENT '区间最大值',
  `price` float DEFAULT NULL COMMENT '价格',
  `charge_id` bigint(20) DEFAULT NULL COMMENT '对应的收费模式',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_chage_plan_charge1_idx` (`charge_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4244 DEFAULT CHARSET=utf8 COMMENT='具体收费方式区间';

-- ----------------------------
-- Records of charge_plan_t
-- ----------------------------

-- ----------------------------
-- Table structure for `charge_t`
-- ----------------------------
DROP TABLE IF EXISTS `charge_t`;
CREATE TABLE `charge_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `charge_name` varchar(45) DEFAULT NULL COMMENT '收费方式名称',
  `charge_desc` varchar(255) DEFAULT NULL COMMENT '收费方式描述信息',
  `scope` varchar(400) DEFAULT NULL COMMENT '收费方式针对的组织域，默认为：ALL',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `app_pub_info_id` bigint(20) DEFAULT NULL COMMENT '应用发布id',
  `status` int(11) DEFAULT NULL,
  `charge_type` int(11) DEFAULT NULL COMMENT '-1:免费，0:按人数，1:按流量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=235 DEFAULT CHARSET=utf8 COMMENT='收费模式信息';

-- ----------------------------
-- Records of charge_t
-- ----------------------------

-- ----------------------------
-- Table structure for `config_resource_t`
-- ----------------------------
DROP TABLE IF EXISTS `config_resource_t`;
CREATE TABLE `config_resource_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `key_str` varchar(100) NOT NULL COMMENT '配置关键字',
  `file_path` varchar(100) NOT NULL COMMENT '下载地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of config_resource_t
-- ----------------------------
INSERT INTO `config_resource_t` VALUES ('2', 'blueprint_h5_v1.444', 'group1\\M01/00/01/wKgHH1c0V8uEYoECAAAAABL3F4Y302.444');
INSERT INTO `config_resource_t` VALUES ('3', 'blueprint_h5_v1.50', 'group1/M00/00/68/2vYEDlc1p6eEGCkZAAAAADWYUq01931.50');
INSERT INTO `config_resource_t` VALUES ('4', 'pcide_v1111', 'group1/M00/00/01/wKgHH1c0X6CEY84iAAAAABL3F4Y89.data');
INSERT INTO `config_resource_t` VALUES ('5', 'framework_v1111', 'group1/M00/00/01/wKgHIFc0YAKEf9wuAAAAABL3F4Y70.data');
INSERT INTO `config_resource_t` VALUES ('6', 'pcide_v1234', 'group1/M01/00/01/wKgHH1c0YraEVnE2AAAAABL3F4Y32.data');

-- ----------------------------
-- Table structure for `config_t`
-- ----------------------------
DROP TABLE IF EXISTS `config_t`;
CREATE TABLE `config_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `key_str` varchar(100) NOT NULL COMMENT '关键字',
  `value` varchar(100) NOT NULL COMMENT '值',
  `description` varchar(200) DEFAULT NULL COMMENT '配置描述',
  `version` varchar(100) NOT NULL COMMENT '版本信息',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of config_t
-- ----------------------------
INSERT INTO `config_t` VALUES ('1', '1234', '1235', 'aaaa', '3.0.0.1', '0', '2016-05-12 16:06:10', '2016-05-12 16:06:10');
INSERT INTO `config_t` VALUES ('2', 'dagasd', '1235', 'aaaadsaga', '3.0.0.1', '0', '2016-05-12 16:27:26', '2016-05-12 16:27:26');
INSERT INTO `config_t` VALUES ('3', 'blueprint_h5', '1.50', 'h5配置文件', '3.0.0.1', '0', '2016-05-12 04:50:19', '2016-05-12 04:50:19');
INSERT INTO `config_t` VALUES ('4', 'pcide', '1234', 'dagag', '3.0.0.1', '0', '2016-05-12 06:49:04', '2016-05-12 06:49:04');
INSERT INTO `config_t` VALUES ('5', 'framework', '1111', '1111', '3.0.0.1', '0', '2016-05-12 06:50:42', '2016-05-12 06:50:42');

-- ----------------------------
-- Table structure for `developer_t`
-- ----------------------------
DROP TABLE IF EXISTS `developer_t`;
CREATE TABLE `developer_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `user_name` varchar(200) NOT NULL COMMENT '用户名',
  `password` varchar(200) NOT NULL COMMENT '密码',
  `email` varchar(100) NOT NULL COMMENT '邮箱',
  `mobile` varchar(60) NOT NULL COMMENT '手机号',
  `checked` int(11) NOT NULL DEFAULT '0' COMMENT '是否确认，0未确认，1已确认',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of developer_t
-- ----------------------------
INSERT INTO `developer_t` VALUES ('4', 'dengyiping', 'e10adc3949ba59abbe56e057f20f883e', 'dengyiping@comisys.net', '13811552350', '1', '2016-05-14 12:07:35', '2016-05-12 23:07:30', '0');
INSERT INTO `developer_t` VALUES ('8', 'chenlei', 'e10adc3949ba59abbe56e057f20f883e', 'chenlei@comisys.net', '15822407937', '1', '2016-05-17 13:54:06', '2016-05-17 13:54:06', '0');
INSERT INTO `developer_t` VALUES ('9', 'dengsitian', 'e10adc3949ba59abbe56e057f20f883e', 'dengsitian@comisys.net', '13311111111', '0', '2016-05-18 15:37:36', '2016-05-18 15:37:36', '0');

-- ----------------------------
-- Table structure for `hook_t`
-- ----------------------------
DROP TABLE IF EXISTS `hook_t`;
CREATE TABLE `hook_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `domain` varchar(100) NOT NULL COMMENT '组织域',
  `app_id` varchar(100) NOT NULL COMMENT '应用id',
  `conditions` varchar(200) NOT NULL COMMENT '回调条件参数',
  `call_back_url` varchar(200) NOT NULL COMMENT '回调地址',
  `creator` varchar(100) NOT NULL COMMENT '创建者userUniId',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `status` int(11) NOT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hook_t
-- ----------------------------

-- ----------------------------
-- Table structure for `login_info_t`
-- ----------------------------
DROP TABLE IF EXISTS `login_info_t`;
CREATE TABLE `login_info_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `login_name` varchar(200) NOT NULL COMMENT '用户名',
  `password` varchar(200) NOT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(60) DEFAULT NULL COMMENT '手机号',
  `user_id` bigint(20) NOT NULL COMMENT '蓝图用户唯一iid',
  `checked` int(11) NOT NULL DEFAULT '0' COMMENT '是否确认，0未确认，1已确认',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `loginNameIndex` (`login_name`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of login_info_t
-- ----------------------------
INSERT INTO `login_info_t` VALUES ('1', 'dengyiping', 'e10adc3949ba59abbe56e057f20f883e', 'dengyiping@comisys.net', '13811552350', '1', '1', '2016-06-24 16:53:06', '2016-06-24 16:50:00', '0');
INSERT INTO `login_info_t` VALUES ('2', 'chenlei', 'e10adc3949ba59abbe56e057f20f883e', 'chenlei@comisys.net', '15822407937', '2', '1', '2016-06-24 16:53:07', '2016-06-24 16:50:02', '0');
INSERT INTO `login_info_t` VALUES ('3', 'lize', 'e10adc3949ba59abbe56e057f20f883e', 'lize@comisys.net', '13466718118', '3', '1', '2016-06-24 16:53:09', '2016-06-24 16:50:04', '0');
INSERT INTO `login_info_t` VALUES ('4', 'dengsitian', 'e10adc3949ba59abbe56e057f20f883e', 'dengsitian@comisys.net', '17601651121', '4', '0', '2016-06-24 16:53:11', '2016-06-24 16:50:06', '0');
INSERT INTO `login_info_t` VALUES ('5', 'liweijun', 'e10adc3949ba59abbe56e057f20f883e', 'liweijun@comisys.net', '15801595614', '5', '1', '2016-06-24 16:53:13', '2016-06-24 16:50:08', '0');
INSERT INTO `login_info_t` VALUES ('6', 'yubeiming', 'e10adc3949ba59abbe56e057f20f883e', 'yubeiming@comisys.net', '18610102212', '6', '1', '2016-06-24 16:53:14', '2016-06-24 16:50:09', '0');
INSERT INTO `login_info_t` VALUES ('7', 'xiabo', 'e10adc3949ba59abbe56e057f20f883e', 'xiabo@comisys.net', '13520381446', '7', '1', '2016-06-24 16:53:15', '2016-06-24 16:50:11', '0');
INSERT INTO `login_info_t` VALUES ('8', 'sunhaopeng', 'e10adc3949ba59abbe56e057f20f883e', 'sunhaopeng@comisys.net', '15810254520', '8', '1', '2016-06-24 16:53:17', '2016-06-24 16:50:12', '0');
INSERT INTO `login_info_t` VALUES ('9', 'zhuliang', 'e10adc3949ba59abbe56e057f20f883e', 'zhuliang@comisys.net', '18612147350', '9', '1', '2016-06-24 16:53:19', '2016-06-24 16:50:14', '0');
INSERT INTO `login_info_t` VALUES ('10', 'duwenyan', 'e10adc3949ba59abbe56e057f20f883e', 'duwenyan@comisys.net', '13230413016', '10', '1', '2016-06-24 16:53:21', '2016-06-24 16:50:16', '0');
INSERT INTO `login_info_t` VALUES ('11', 'zhusiyang', 'e10adc3949ba59abbe56e057f20f883e', 'zhusiyang@comisys.net', '18610048839', '11', '1', '2016-06-24 16:53:22', '2016-06-24 16:50:17', '0');
INSERT INTO `login_info_t` VALUES ('12', 'xiexin', 'e10adc3949ba59abbe56e057f20f883e', 'xiexin@comisys.net', '13520109964', '12', '1', '2016-06-24 16:53:24', '2016-06-24 16:50:19', '0');
INSERT INTO `login_info_t` VALUES ('13', 'guopeili', 'e10adc3949ba59abbe56e057f20f883e', 'guopeili@comisys.net', '13552408773', '13', '1', '2016-06-24 16:53:25', '2016-06-24 16:50:20', '0');
INSERT INTO `login_info_t` VALUES ('14', 'jinmangang', 'e10adc3949ba59abbe56e057f20f883e', 'jinmangang@comisys.net', '15011347568', '14', '1', '2016-06-24 16:53:27', '2016-06-24 16:50:22', '0');
INSERT INTO `login_info_t` VALUES ('15', 'handongtao', 'e10adc3949ba59abbe56e057f20f883e', 'handongtao@comisys.net', '15810802918', '15', '1', '2016-06-24 16:53:28', '2016-06-24 16:50:23', '0');
INSERT INTO `login_info_t` VALUES ('16', 'liujian', 'e10adc3949ba59abbe56e057f20f883e', 'liujian@comisys.net', '13661334463', '16', '1', '2016-06-24 16:53:29', '2016-06-24 16:50:25', '0');
INSERT INTO `login_info_t` VALUES ('17', 'yuzhichun', 'e10adc3949ba59abbe56e057f20f883e', 'yuzhichun@comisys.net', '18612382370', '17', '1', '2016-06-24 16:53:32', '2016-06-24 16:50:26', '0');
INSERT INTO `login_info_t` VALUES ('18', 'shenhao', 'e10adc3949ba59abbe56e057f20f883e', 'shenhao@comisys.net', '13126550257', '18', '1', '2016-07-05 17:01:19', '2016-07-05 17:01:21', '0');
INSERT INTO `login_info_t` VALUES ('19', 'ranyaning', 'e10adc3949ba59abbe56e057f20f883e', 'ranyaning@comisys.net', '18515338050', '19', '1', '2016-07-05 17:05:06', '2016-07-05 17:01:55', '0');
INSERT INTO `login_info_t` VALUES ('20', 'houqingfeng', 'e10adc3949ba59abbe56e057f20f883e', 'houqingfeng@comisys.net', '13810729952', '20', '1', '2016-07-05 17:02:26', '2016-07-05 17:02:33', '0');
INSERT INTO `login_info_t` VALUES ('21', 'shichao', 'e10adc3949ba59abbe56e057f20f883e', 'shichao@comisys.net', '13811894257', '21', '1', '2016-07-05 17:03:01', '2016-07-05 17:03:03', '0');

-- ----------------------------
-- Table structure for `org_subscription_info_t`
-- ----------------------------
DROP TABLE IF EXISTS `org_subscription_info_t`;
CREATE TABLE `org_subscription_info_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `domain` varchar(45) DEFAULT NULL COMMENT '订阅组织',
  `user_uni_id` varchar(45) DEFAULT NULL COMMENT '组织内个人id',
  `sub_deadline` timestamp NULL DEFAULT NULL COMMENT '订阅截至时间',
  `is_deleted` int(11) DEFAULT NULL COMMENT '订阅状态0:订阅中,1:已取消订阅',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `status` int(11) DEFAULT NULL COMMENT '预留',
  `app_pub_info_id` bigint(20) DEFAULT NULL COMMENT '用户选择的app发布id',
  `charge_id` bigint(20) DEFAULT NULL COMMENT '用户选择的收费方式',
  `app_id` varchar(45) DEFAULT NULL,
  `trial_date` timestamp NULL DEFAULT NULL COMMENT '截至试用日期,即账单日期',
  PRIMARY KEY (`id`),
  KEY `fk_org_subscription_info_app_publish_info1_idx` (`app_pub_info_id`),
  KEY `fk_org_subscription_info_charge1_idx` (`charge_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COMMENT='组织订阅app信息';

-- ----------------------------
-- Records of org_subscription_info_t
-- ----------------------------

-- ----------------------------
-- Table structure for `permission_mapper_t`
-- ----------------------------
DROP TABLE IF EXISTS `permission_mapper_t`;
CREATE TABLE `permission_mapper_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `permission_id` bigint(20) NOT NULL COMMENT '权限id',
  `permission_name` varchar(200) NOT NULL COMMENT '权限名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of permission_mapper_t
-- ----------------------------
INSERT INTO `permission_mapper_t` VALUES ('3', '6', '1', '');
INSERT INTO `permission_mapper_t` VALUES ('4', '6', '7', '');
INSERT INTO `permission_mapper_t` VALUES ('7', '5', '7', '组织域发布');
INSERT INTO `permission_mapper_t` VALUES ('8', '5', '9', '发布');
INSERT INTO `permission_mapper_t` VALUES ('9', '4', '1', '全网发布');
INSERT INTO `permission_mapper_t` VALUES ('10', '4', '7', '组织域发布');
INSERT INTO `permission_mapper_t` VALUES ('11', '1', '1', '全网发布');
INSERT INTO `permission_mapper_t` VALUES ('12', '1', '8', '订阅');

-- ----------------------------
-- Table structure for `permission_t`
-- ----------------------------
DROP TABLE IF EXISTS `permission_t`;
CREATE TABLE `permission_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(50) NOT NULL COMMENT '权限key，字符串形势',
  `name` varchar(100) NOT NULL COMMENT '权限名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of permission_t
-- ----------------------------
INSERT INTO `permission_t` VALUES ('1', 'PLATFORM-PUBLISH', '全网发布', '2016-05-05 14:17:35', '2016-05-05 14:17:35', '0');
INSERT INTO `permission_t` VALUES ('7', 'DOMAIN-PUBLISH', '组织域发布', '2016-05-05 21:53:01', '2016-05-05 21:53:01', '0');
INSERT INTO `permission_t` VALUES ('8', 'SUBCRIBLE', '订阅', '2016-07-05 10:27:32', '2016-07-05 10:27:32', '0');
INSERT INTO `permission_t` VALUES ('9', 'PUBLISH', '发布', '2016-07-05 10:27:41', '2016-07-05 10:27:41', '0');

-- ----------------------------
-- Table structure for `real_server_info_t`
-- ----------------------------
DROP TABLE IF EXISTS `real_server_info_t`;
CREATE TABLE `real_server_info_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `domain` varchar(45) DEFAULT NULL COMMENT '组织域',
  `url` varchar(45) DEFAULT NULL COMMENT '组织服务接口地址',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '预留',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of real_server_info_t
-- ----------------------------

-- ----------------------------
-- Table structure for `recommend_app_info_t`
-- ----------------------------
DROP TABLE IF EXISTS `recommend_app_info_t`;
CREATE TABLE `recommend_app_info_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_info_id` bigint(20) DEFAULT NULL,
  `state` int(11) DEFAULT NULL COMMENT 'app特殊定义：0:精品推荐,1:热门推荐',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `reco_desc` varchar(200) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_recommend_app_info_app_publish_info_t1_idx` (`app_info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='推荐app信息';

-- ----------------------------
-- Records of recommend_app_info_t
-- ----------------------------
INSERT INTO `recommend_app_info_t` VALUES ('1', '1', '0', '2016-05-07 11:30:28', '2016-05-07 11:30:28', '精品推荐', '0');
INSERT INTO `recommend_app_info_t` VALUES ('2', '2', '1', '2016-05-07 11:32:20', '2016-05-07 11:32:20', '热门推荐', '0');

-- ----------------------------
-- Table structure for `resource_t`
-- ----------------------------
DROP TABLE IF EXISTS `resource_t`;
CREATE TABLE `resource_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `resource_id` varchar(50) NOT NULL COMMENT '资源id',
  `file_name` varchar(200) NOT NULL COMMENT '文件名称',
  `system_file_name` varchar(200) NOT NULL COMMENT '系统名称',
  `user_uni_id` varchar(100) NOT NULL COMMENT '用户唯一id',
  `mime_type` varchar(100) NOT NULL COMMENT '文件类型',
  `size` bigint(20) NOT NULL DEFAULT '0' COMMENT '文件大小',
  `file_path` varchar(200) NOT NULL COMMENT '文件保存路径',
  `md5` varchar(50) NOT NULL COMMENT 'md5值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=923 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of resource_t
-- ----------------------------

-- ----------------------------
-- Table structure for `role_mapping_t`
-- ----------------------------
DROP TABLE IF EXISTS `role_mapping_t`;
CREATE TABLE `role_mapping_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `role_name` bigint(20) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of role_mapping_t
-- ----------------------------

-- ----------------------------
-- Table structure for `role_t`
-- ----------------------------
DROP TABLE IF EXISTS `role_t`;
CREATE TABLE `role_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(50) NOT NULL COMMENT '角色key，不同类型可以重复',
  `name` varchar(100) NOT NULL COMMENT '角色名称，不同类型可以重复',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `status` int(11) DEFAULT '0' COMMENT '状态，0，正常，1，删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of role_t
-- ----------------------------
INSERT INTO `role_t` VALUES ('1', 'PLATFORM-DEVELOPER', '平台开发者', '2016-04-24 23:03:21', '2016-04-25 11:00:40', '0');
INSERT INTO `role_t` VALUES ('2', 'DOMAIN-DEVELOPER', '组织域开发者', '2016-04-24 23:03:19', '2016-04-25 11:00:38', '0');
INSERT INTO `role_t` VALUES ('4', 'DEVELOPER', '开发者', '2016-07-05 10:31:37', '2016-07-05 10:31:37', '0');
INSERT INTO `role_t` VALUES ('5', 'EnterPriseManager', '企业管理员', '2016-07-05 10:31:55', '2016-07-05 10:31:55', '0');

-- ----------------------------
-- Table structure for `user_account_t`
-- ----------------------------
DROP TABLE IF EXISTS `user_account_t`;
CREATE TABLE `user_account_t` (
  `id` bigint(20) NOT NULL,
  `domain` varchar(45) DEFAULT NULL,
  `userUniId` varchar(45) DEFAULT NULL,
  `userName` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `phone` varchar(18) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

-- ----------------------------
-- Records of user_account_t
-- ----------------------------
INSERT INTO `user_account_t` VALUES ('0', '3.conference-lanxin', '148@3.conference-lanxin', '陈蕾', 'chenlei@comisys.net', '2016-05-03 16:37:20', '2016-05-03 16:37:22', '0', '15822407937');
