-- MySQL dump 10.13  Distrib 5.7.9, for osx10.9 (x86_64)
--
-- Host: localhost    Database: blueprint
-- ------------------------------------------------------
-- Server version	5.6.25-73.1-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `QRTZ_BLOB_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(120) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_CALENDARS`
--

DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_CALENDARS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(120) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_CRON_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(120) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_FIRED_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(120) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(120) DEFAULT NULL,
  `JOB_GROUP` varchar(120) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_JOB_DETAILS`
--

DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(120) NOT NULL,
  `JOB_GROUP` varchar(120) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_LOCKS`
--

DROP TABLE IF EXISTS `QRTZ_LOCKS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_LOCKS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_PAUSED_TRIGGER_GRPS`
--

DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(120) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_SCHEDULER_STATE`
--

DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(120) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_SIMPLE_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(120) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_SIMPROP_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(120) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(120) NOT NULL,
  `JOB_NAME` varchar(120) NOT NULL,
  `JOB_GROUP` varchar(120) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(120) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app_bill_t`
--

DROP TABLE IF EXISTS `app_bill_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app_category_t`
--

DROP TABLE IF EXISTS `app_category_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_category_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL COMMENT '分类名称',
  `category_desc` varchar(45) DEFAULT NULL COMMENT '描述信息',
  `p_id` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '分类,0:业务类型,1:行业类型',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_app_category1_idx` (`p_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='行业分类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app_dependency_t`
--

DROP TABLE IF EXISTS `app_dependency_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app_order_t`
--

DROP TABLE IF EXISTS `app_order_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8 COMMENT='app订阅产生订单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app_publish_info_t`
--

DROP TABLE IF EXISTS `app_publish_info_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_publish_info_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(45) DEFAULT NULL COMMENT '发布者id',
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
  `app_resource_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_app_publish_info_application_info1_idx` (`app_info_id`),
  KEY `fk_app_publish_info_application_info2_idx` (`app_name`) USING BTREE,
  KEY `fk_app_publish_info_application_info3_idx` (`recommend_type`) USING BTREE,
  KEY `fk_app_publish_info_application_info4_idx` (`business_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8 COMMENT='应用发布信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app_push_record_t`
--

DROP TABLE IF EXISTS `app_push_record_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  PRIMARY KEY (`id`),
  KEY `fk_app_push_record1_idx` (`current_u_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=984 DEFAULT CHARSET=utf8 COMMENT='订阅app后产生app推送记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app_push_viewers_t`
--

DROP TABLE IF EXISTS `app_push_viewers_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_push_viewers_t` (
  `id` bigint(20) NOT NULL,
  `app_push_id` bigint(20) DEFAULT NULL,
  `user_uni_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='app查看者映射表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app_usage_impact_t`
--

DROP TABLE IF EXISTS `app_usage_impact_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app_usage_log_t`
--

DROP TABLE IF EXISTS `app_usage_log_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `application_info_t`
--

DROP TABLE IF EXISTS `application_info_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `category` varchar(70) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_application_info1_idx` (`app_id`),
  KEY `fk_application_info2_idx` (`version`),
  KEY `fk_application_info3_idx` (`app_id`,`version`)
) ENGINE=InnoDB AUTO_INCREMENT=859 DEFAULT CHARSET=utf8 COMMENT='app基础信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `behavior_data_relationship_t`
--

DROP TABLE IF EXISTS `behavior_data_relationship_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `behavior_data_relationship_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `parent_table_data_id` varchar(200) NOT NULL COMMENT '父表数据id',
  `child_data_id` varchar(200) NOT NULL COMMENT '子行为数据唯一id',
  `child_table_name` varchar(200) NOT NULL COMMENT '子行为数据对应表',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_behavior_data_relationship_t_child_data_id_child_table_name` (`child_data_id`,`child_table_name`),
  KEY `idx_behavior_data_relationship_t_parent_table_data_id` (`parent_table_data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `behavior_sycn_data_t`
--

DROP TABLE IF EXISTS `behavior_sycn_data_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `behavior_sycn_data_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `user_uni_id` varchar(200) NOT NULL COMMENT '用户唯一id',
  `behavior_data_id` varchar(200) NOT NULL COMMENT '行为数据唯一id',
  `behavior_data_table_name` varchar(200) NOT NULL COMMENT '行为数据对应表',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43956 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bind_relationship_t`
--

DROP TABLE IF EXISTS `bind_relationship_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bind_relationship_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '蓝图唯一id',
  `ex_platform_flag` varchar(100) NOT NULL DEFAULT 'BLUEPRINT' COMMENT '外部系统标志，如lanxin',
  `ex_platform_name` varchar(100) NOT NULL DEFAULT '蓝信' COMMENT '外部系统名称',
  `ex_domain_flag` varchar(100) NOT NULL COMMENT '外部组织标识',
  `ex_domain_name` varchar(100) NOT NULL COMMENT '外部组织名称',
  `ex_user_uni_id` varchar(100) NOT NULL COMMENT '外部系统的userUniId',
  PRIMARY KEY (`id`),
  KEY `fk_bind_relationship1_idx` (`user_id`),
  KEY `fk_bind_relationship2_idx` (`user_id`,`ex_platform_flag`,`ex_domain_flag`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bp_user_t`
--

DROP TABLE IF EXISTS `bp_user_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bp_user_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `login_name` varchar(100) NOT NULL COMMENT '登录名称（手机号）',
  `path` varchar(200) DEFAULT NULL COMMENT '路径',
  `role_id` bigint(20) DEFAULT '0' COMMENT '蓝图唯一id',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `mobile` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_bp_user1_idx` (`login_name`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `charge_plan_t`
--

DROP TABLE IF EXISTS `charge_plan_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=5662 DEFAULT CHARSET=utf8 COMMENT='具体收费方式区间';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `charge_t`
--

DROP TABLE IF EXISTS `charge_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  PRIMARY KEY (`id`),
  KEY `fk_charge1_idx` (`app_pub_info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1653 DEFAULT CHARSET=utf8 COMMENT='收费模式信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `config_resource_t`
--

DROP TABLE IF EXISTS `config_resource_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config_resource_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `key_str` varchar(100) NOT NULL COMMENT '配置关键字',
  `file_path` varchar(100) NOT NULL COMMENT '下载地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `config_t`
--

DROP TABLE IF EXISTS `config_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `developer_t`
--

DROP TABLE IF EXISTS `developer_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hook_t`
--

DROP TABLE IF EXISTS `hook_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `login_info_t`
--

DROP TABLE IF EXISTS `login_info_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `icon_resource_id` varchar(75) DEFAULT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  `job` varchar(100) DEFAULT NULL,
  `signature` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `loginNameIndex` (`login_name`),
  KEY `fk_login_info1_idx` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `org_subscription_info_t`
--

DROP TABLE IF EXISTS `org_subscription_info_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  KEY `fk_org_subscription_info_charge1_idx` (`charge_id`),
  KEY `fk_org_subscription_info3_idx` (`user_uni_id`),
  KEY `fk_org_subscription_info4_idx` (`is_deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8 COMMENT='组织订阅app信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permission_mapper_t`
--

DROP TABLE IF EXISTS `permission_mapper_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission_mapper_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `permission_id` bigint(20) NOT NULL COMMENT '权限id',
  `permission_code` varchar(200) NOT NULL COMMENT '权限code',
  `permission_name` varchar(200) NOT NULL COMMENT '权限名称',
  PRIMARY KEY (`id`),
  KEY `fk_permission_mapper1_idx` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permission_t`
--

DROP TABLE IF EXISTS `permission_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(50) NOT NULL COMMENT '权限key，字符串形势',
  `name` varchar(100) NOT NULL COMMENT '权限名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`),
  KEY `fk_permission1_idx` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `real_server_info_t`
--

DROP TABLE IF EXISTS `real_server_info_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `real_server_info_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `domain` varchar(45) DEFAULT NULL COMMENT '组织域',
  `url` varchar(45) DEFAULT NULL COMMENT '组织服务接口地址',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '预留',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `recommend_app_info_t`
--

DROP TABLE IF EXISTS `recommend_app_info_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `resource_t`
--

DROP TABLE IF EXISTS `resource_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resource_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `resource_id` varchar(50) NOT NULL COMMENT '资源id',
  `file_name` varchar(200) NOT NULL COMMENT '文件名称',
  `system_file_name` varchar(200) NOT NULL COMMENT '系统名称',
  `user_uni_id` varchar(100) NOT NULL COMMENT '用户唯一id',
  `mime_type` varchar(100) NOT NULL COMMENT '文件类型',
  `size` bigint(20) NOT NULL DEFAULT '0' COMMENT '文件大小',
  `file_path` varchar(800) NOT NULL COMMENT '文件保存路径',
  `md5` varchar(50) NOT NULL COMMENT 'md5值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `fk_resource1_idx` (`resource_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13355 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_mapping_t`
--

DROP TABLE IF EXISTS `role_mapping_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_mapping_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `platform_flag` varchar(100) DEFAULT NULL COMMENT '平台标志',
  `platform_name` varchar(100) DEFAULT NULL COMMENT '平台名称',
  `domain_flag` varchar(100) DEFAULT NULL COMMENT '组织域标志',
  `domain_name` varchar(100) DEFAULT NULL COMMENT '组织域名称',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `role_name` varchar(100) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`),
  KEY `fk_org_role_mapping1_idx` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_t`
--

DROP TABLE IF EXISTS `role_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(50) NOT NULL COMMENT '角色key，不同类型可以重复',
  `name` varchar(100) NOT NULL COMMENT '角色名称，不同类型可以重复',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `status` int(11) DEFAULT '0' COMMENT '状态，0，正常，1，删除',
  `role_desc` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_role1_idx` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `test`
--

DROP TABLE IF EXISTS `test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test` (
  `name` varchar(255) DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_account_t`
--

DROP TABLE IF EXISTS `user_account_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_behavior_data_t`
--

DROP TABLE IF EXISTS `user_behavior_data_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_behavior_data_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `user_uni_id` varchar(200) NOT NULL COMMENT '用户唯一id',
  `behavior_data_id` varchar(200) NOT NULL COMMENT '行为数据唯一id',
  `behavior_data_table_name` varchar(200) NOT NULL COMMENT '行为数据对应表',
  `status` bigint(2) NOT NULL COMMENT '行为数据状态',
  `plog_id` bigint(20) NOT NULL COMMENT '行为数据plogid',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_behavior_data_t_user_uni_id_behavior_data_id` (`user_uni_id`,`behavior_data_id`),
  KEY `idx_user_behavior_data_t_plog_id` (`plog_id`)
) ENGINE=InnoDB AUTO_INCREMENT=69805 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed
