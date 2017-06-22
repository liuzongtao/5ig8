-- MySQL dump 10.13  Distrib 5.6.34, for Win64 (x86_64)
--
-- Host: localhost    Database: 5igu8
-- ------------------------------------------------------
-- Server version	5.6.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `eova_menu`
--

DROP TABLE IF EXISTS `eova_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eova_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL COMMENT '编码',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `type` varchar(20) NOT NULL COMMENT '菜单类型',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `order_num` int(11) DEFAULT '0' COMMENT '序号',
  `parent_id` int(11) DEFAULT '0' COMMENT '父节点',
  `is_collapse` tinyint(1) DEFAULT '0' COMMENT '是否折叠',
  `biz_intercept` varchar(255) DEFAULT NULL COMMENT '自定义业务拦截器',
  `url` varchar(255) DEFAULT NULL COMMENT '自定义URL',
  `menuConfig` varchar(500) DEFAULT NULL COMMENT '菜单配置JSON',
  `diy_js` varchar(255) DEFAULT NULL COMMENT '依赖JS文件',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '是否隐藏',
  `filter` varchar(500) DEFAULT NULL COMMENT '初始数据过滤条件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `igpcontent`
--

DROP TABLE IF EXISTS `igpcontent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `igpcontent` (
  `lid` bigint(32) NOT NULL AUTO_INCREMENT,
  `teacherId` bigint(32) NOT NULL,
  `id` bigint(16) NOT NULL,
  `kind` varchar(10) DEFAULT NULL,
  `title` varchar(64) DEFAULT NULL,
  `brief` varchar(2048) CHARACTER SET utf8mb4 DEFAULT NULL,
  `price` float(8,2) DEFAULT NULL,
  `vip_price` float(8,2) DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4,
  `image_thumb` varchar(1024) DEFAULT NULL,
  `image` varchar(1024) DEFAULT NULL,
  `image_size` varchar(128) DEFAULT NULL,
  `audio` varchar(128) DEFAULT NULL,
  `audio_len` int(6) DEFAULT NULL,
  `rec_time` bigint(16) DEFAULT NULL,
  `rec_time_desc` varchar(24) DEFAULT NULL,
  `url` varchar(128) DEFAULT NULL,
  `state2` varchar(12) DEFAULT NULL,
  `view_self` tinyint(1) DEFAULT NULL,
  `vip_show` tinyint(1) DEFAULT NULL,
  `not_vip_show` tinyint(1) DEFAULT NULL,
  `unshow_group` varchar(16) DEFAULT NULL,
  `vip_group_info` varchar(16) DEFAULT NULL,
  `o_id` varchar(64) DEFAULT NULL,
  `o_kind` varchar(32) DEFAULT NULL,
  `zh_id` varchar(64) DEFAULT NULL,
  `b_id` varchar(64) DEFAULT NULL,
  `g_id` varchar(64) DEFAULT NULL,
  `menus_id` varchar(64) DEFAULT NULL,
  `v_id` varchar(64) DEFAULT NULL,
  `touid` varchar(64) DEFAULT NULL,
  `stock_info` text CHARACTER SET utf8mb4,
  `thunimg` varchar(128) DEFAULT NULL,
  `o_c_id` varchar(32) DEFAULT NULL,
  `label_kind` varchar(128) DEFAULT NULL,
  `menus` varchar(1024) DEFAULT NULL,
  `forward_info` varchar(1024) DEFAULT NULL,
  `do_forward` tinyint(4) DEFAULT NULL,
  `to_info` varchar(255) DEFAULT NULL,
  `comment_num` int(11) DEFAULT NULL,
  `support_num` int(11) DEFAULT NULL,
  `reward_num` int(11) DEFAULT NULL,
  `reward_list` varchar(1024) DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `content_new` text CHARACTER SET utf8mb4,
  PRIMARY KEY (`lid`),
  UNIQUE KEY `lidIndex` (`lid`),
  KEY `id_tidIndex` (`teacherId`,`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rechargelog`
--

DROP TABLE IF EXISTS `rechargelog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rechargelog` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT,
  `uid` bigint(32) NOT NULL,
  `nickname` varchar(26) DEFAULT NULL,
  `creatTime` bigint(13) DEFAULT NULL,
  `phoneNumber` bigint(13) DEFAULT NULL,
  `curTime` bigint(13) DEFAULT NULL,
  `oldVipEndTime` bigint(13) DEFAULT NULL,
  `newVipEndTime` bigint(13) DEFAULT NULL,
  `disCountInfosStr` varchar(1024) DEFAULT NULL,
  `vipTypeId` bigint(32) DEFAULT NULL,
  `vipTypeDescr` varchar(128) DEFAULT NULL,
  `costMoney` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teacher` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT,
  `pfId` int(5) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `buyEndTime` bigint(13) DEFAULT '0',
  `pfVipUid` int(11) DEFAULT '0',
  `vipTypeId` int(3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(24) CHARACTER SET utf8mb4 NOT NULL,
  `passwd` varchar(64) CHARACTER SET utf8mb4 NOT NULL,
  `creatTime` bigint(13) NOT NULL,
  `phoneNumber` bigint(13) DEFAULT NULL,
  `email` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
  `inviterUid` bigint(32) DEFAULT NULL,
  `roleType` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `userdiscountinfo`
--

DROP TABLE IF EXISTS `userdiscountinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userdiscountinfo` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT,
  `uid` bigint(32) NOT NULL,
  `discountMoney` int(11) NOT NULL,
  `discountEndTime` bigint(13) NOT NULL,
  `discountFromUid` bigint(32) DEFAULT NULL,
  `curTime` bigint(13) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uservipinfo`
--

DROP TABLE IF EXISTS `uservipinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uservipinfo` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT,
  `uid` bigint(32) NOT NULL,
  `vipTypeId` int(3) DEFAULT NULL,
  `vipEndTime` bigint(13) DEFAULT NULL,
  `concernedTeacherId` bigint(32) DEFAULT NULL,
  `sendEmail` varchar(32) CHARACTER SET utf8mb4 DEFAULT '""',
  `sendSms` tinyint(1) DEFAULT '0',
  `showUrl` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `viptype`
--

DROP TABLE IF EXISTS `viptype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `viptype` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT,
  `descr` varchar(64) DEFAULT NULL,
  `costMoney` int(8) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-06-22 18:21:17


INSERT INTO `eova_menu` VALUES (1,'user','用户管理','dir','icon-bricks',1,0,0,NULL,NULL,NULL,NULL,0,NULL),(2,'sys','系统管理','dir','icon-cog',2,0,0,NULL,NULL,NULL,NULL,0,NULL),(3,'biz','综合信息','dir','icon-plugin',3,0,0,NULL,NULL,NULL,NULL,0,NULL),(20,'user_list','用户列表','diy','icon-applicationsidetree',1,1,0,'','/user/list','',NULL,0,NULL),(21,'user_add','添加用户','diy','icon-layout',2,1,0,NULL,'/user/toAdd','',NULL,0,NULL),(22,'user_pwd','修改密码','diy','icon-databasetable',3,1,0,'','/user/toUpdatePwd','',NULL,0,NULL),(51,'sys_log','系统日志','diy','icon-tablemultiple',1,2,0,NULL,NULL,'',NULL,0,NULL);
