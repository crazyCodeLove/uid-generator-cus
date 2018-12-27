CREATE DATABASE /*!32312 IF NOT EXISTS*/`snowflakecus` /*!40100 DEFAULT CHARACTER SET utf8 */;

/*Table structure for table `work_node` */

DROP TABLE IF EXISTS `work_node`;

CREATE TABLE `work_node` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'auto increment id',
  `IP` varchar(128) NOT NULL COMMENT 'host name',
  `PORT` varchar(64) NOT NULL COMMENT 'port',
  `WORK_NODE_ID` int(11) NOT NULL COMMENT 'work node id',
  `LAUNCH_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'launch date',
  `STATUS` varchar(10) NOT NULL COMMENT 'server status, on or off',
  `LAST_UPDATE_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'last update time',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `WORK_NODE_ID` (`WORK_NODE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='DB WorkerID Assigner for UID Generator';