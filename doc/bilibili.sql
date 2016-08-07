/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50712
Source Host           : 127.0.0.1:3306
Source Database       : bilibili

Target Server Type    : MYSQL
Target Server Version : 50712
File Encoding         : 65001

Date: 2016-07-17 00:10:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for aid
-- ----------------------------
DROP TABLE IF EXISTS `aid`;
CREATE TABLE `aid` (
  `aid` int(11) NOT NULL,
  `tid` int(11) DEFAULT NULL,
  `typename` varchar(255) DEFAULT NULL,
  `arctype` varchar(255) DEFAULT NULL,
  `play` int(11) DEFAULT NULL,
  `review` int(11) DEFAULT NULL,
  `video_review` int(11) DEFAULT NULL,
  `favorites` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `allow_bp` int(11) DEFAULT NULL,
  `allow_feed` int(11) DEFAULT NULL,
  `allow_download` int(11) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `pic` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `mid` int(11) DEFAULT NULL,
  `face` varchar(255) DEFAULT NULL,
  `pages` int(11) DEFAULT NULL,
  `instant_server` varchar(255) DEFAULT NULL,
  `created` int(11) DEFAULT NULL,
  `created_at` varchar(255) DEFAULT NULL,
  `credit` int(11) DEFAULT NULL,
  `coins` int(11) DEFAULT NULL,
  `spid` int(11) DEFAULT NULL,
  `src` varchar(255) DEFAULT NULL,
  `cid` int(11) DEFAULT NULL,
  `partname` varchar(255) DEFAULT NULL,
  `offsite` varchar(255) DEFAULT NULL,
  `typename2` varchar(255) DEFAULT NULL,
  `partid` int(11) DEFAULT NULL,
  `season_id` int(11) DEFAULT NULL,
  `season_index` varchar(255) DEFAULT NULL,
  `season_episode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for aidcid
-- ----------------------------
DROP TABLE IF EXISTS `aidcid`;
CREATE TABLE `aidcid` (
  `aid` int(11) DEFAULT NULL,
  `cid` int(11) NOT NULL,
  `partid` int(11) DEFAULT NULL,
  `partname` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for avcount
-- ----------------------------
DROP TABLE IF EXISTS `avcount`;
CREATE TABLE `avcount` (
  `date` date NOT NULL,
  `count` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for avplay
-- ----------------------------
DROP TABLE IF EXISTS `avplay`;
CREATE TABLE `avplay` (
  `title` varchar(255) NOT NULL,
  `play` int(255) DEFAULT NULL,
  `timestamp` datetime NOT NULL,
  `ranking` int(11) DEFAULT NULL,
  PRIMARY KEY (`title`,`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bangumi
-- ----------------------------
DROP TABLE IF EXISTS `bangumi`;
CREATE TABLE `bangumi` (
  `bangumi_id` int(11) NOT NULL,
  `season_id` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `allow_download` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`bangumi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for cid
-- ----------------------------
DROP TABLE IF EXISTS `cid`;
CREATE TABLE `cid` (
  `cid` int(11) NOT NULL,
  `maxlimit` int(11) DEFAULT NULL,
  `chatid` int(11) DEFAULT NULL,
  `server` varchar(255) DEFAULT NULL,
  `vtype` varchar(255) DEFAULT NULL,
  `oriurl` varchar(255) DEFAULT NULL,
  `aid` int(11) DEFAULT NULL,
  `typeid` int(11) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  `click` int(11) DEFAULT NULL,
  `favourites` int(11) DEFAULT NULL,
  `credits` int(11) DEFAULT NULL,
  `coins` int(11) DEFAULT NULL,
  `fw_click` int(255) DEFAULT NULL,
  `duration` varchar(255) DEFAULT NULL,
  `arctype` varchar(255) DEFAULT NULL,
  `danmu` int(11) DEFAULT NULL,
  `bottom` int(11) DEFAULT NULL,
  `sinapi` int(11) DEFAULT NULL,
  `acceptguest` varchar(255) DEFAULT NULL,
  `acceptaccel` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for data
-- ----------------------------
DROP TABLE IF EXISTS `data`;
CREATE TABLE `data` (
  `dp_done_mp4` tinyint(1) DEFAULT NULL,
  `letv_vu` varchar(255) DEFAULT NULL,
  `dp_done_flv` tinyint(1) DEFAULT NULL,
  `upload_meta` int(11) DEFAULT NULL,
  `aid` int(11) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `vp` int(11) DEFAULT NULL,
  `upload` int(11) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `cover` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `page` int(11) DEFAULT NULL,
  `dispatch` int(11) DEFAULT NULL,
  `vid` varchar(255) DEFAULT NULL,
  `backup_vid` varchar(255) DEFAULT NULL,
  `files` int(11) DEFAULT NULL,
  `dispatch_servers` int(11) DEFAULT NULL,
  `cache` varchar(255) DEFAULT NULL,
  `storage_server` int(11) DEFAULT NULL,
  `cid` int(11) NOT NULL,
  `dp_done` tinyint(1) DEFAULT NULL,
  `duration` float DEFAULT NULL,
  `mid` int(11) DEFAULT NULL,
  `dp_done_hdmp4` tinyint(1) DEFAULT NULL,
  `letv_vid` int(11) DEFAULT NULL,
  `storage` int(11) DEFAULT NULL,
  `letv_addr` varchar(255) DEFAULT NULL,
  `subtitle` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dispatch_servers
-- ----------------------------
DROP TABLE IF EXISTS `dispatch_servers`;
CREATE TABLE `dispatch_servers` (
  `is_active` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `done` int(11) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `done_at` datetime DEFAULT NULL,
  `create_at` datetime DEFAULT NULL,
  `dms_id` int(11) DEFAULT NULL,
  `server` int(11) DEFAULT NULL,
  `format` varchar(255) DEFAULT NULL,
  `create` int(11) DEFAULT NULL,
  `cid` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for files
-- ----------------------------
DROP TABLE IF EXISTS `files`;
CREATE TABLE `files` (
  `filesize` mediumtext,
  `order` int(11) DEFAULT NULL,
  `md5` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `length` int(11) DEFAULT NULL,
  `storage_state` int(11) DEFAULT NULL,
  `format` varchar(255) DEFAULT NULL,
  `cid` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for node_server
-- ----------------------------
DROP TABLE IF EXISTS `node_server`;
CREATE TABLE `node_server` (
  `sid` varchar(255) DEFAULT NULL,
  `server_id` varchar(255) DEFAULT NULL,
  `perferred_zones` varchar(255) DEFAULT NULL,
  `domain` varchar(255) DEFAULT NULL,
  `allow_upload` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `node_id` varchar(255) DEFAULT NULL,
  `cid` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for param
-- ----------------------------
DROP TABLE IF EXISTS `param`;
CREATE TABLE `param` (
  `key` varchar(255) NOT NULL,
  `value` varchar(1024) DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for save
-- ----------------------------
DROP TABLE IF EXISTS `save`;
CREATE TABLE `save` (
  `id` int(11) NOT NULL,
  `bilibili` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for upload
-- ----------------------------
DROP TABLE IF EXISTS `upload`;
CREATE TABLE `upload` (
  `encode_server` varchar(255) DEFAULT NULL,
  `done` int(11) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `create_at` datetime DEFAULT NULL,
  `done_at` datetime DEFAULT NULL,
  `uploaded` int(11) DEFAULT NULL,
  `convert` int(11) DEFAULT NULL,
  `error_reason` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `timestamp` int(11) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `uploaded_at` datetime DEFAULT NULL,
  `convert_at` datetime DEFAULT NULL,
  `node_server` int(11) DEFAULT NULL,
  `cid` int(11) NOT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for upload_meta
-- ----------------------------
DROP TABLE IF EXISTS `upload_meta`;
CREATE TABLE `upload_meta` (
  `storage_server` int(11) DEFAULT NULL,
  `done` int(11) DEFAULT NULL,
  `done_at` datetime DEFAULT NULL,
  `create_at` datetime DEFAULT NULL,
  `storage_state` int(11) DEFAULT NULL,
  `format` varchar(255) DEFAULT NULL,
  `vp` int(11) DEFAULT NULL,
  `create` int(11) DEFAULT NULL,
  `cid` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  PRIMARY KEY (`cid`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for vstorage
-- ----------------------------
DROP TABLE IF EXISTS `vstorage`;
CREATE TABLE `vstorage` (
  `id` int(11) NOT NULL,
  `code` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- View structure for 番剧
-- ----------------------------
DROP VIEW IF EXISTS `番剧`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `番剧` AS select `c`.`aid` AS `aid`,`c`.`cid` AS `cid`,`c`.`pid` AS `pid`,`c`.`typeid` AS `typeid`,`d`.`title` AS `title`,`d`.`subtitle` AS `subtitle` from (`cid` `c` join `data` `d` on(((`c`.`cid` = `d`.`cid`) and ((`c`.`typeid` = 32) or (`c`.`typeid` = 33))))) ;
