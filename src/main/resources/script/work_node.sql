DROP TABLE IF EXISTS `worker_node`;

CREATE TABLE `worker_node` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'auto increment id',
  `IP` varchar(128) NOT NULL COMMENT 'host name',
  `PORT` varchar(64) NOT NULL COMMENT 'port',
  `WORK_NODE_ID` int(11) NOT NULL COMMENT 'work node id',
  `LAUNCH_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'launch date',
  `STATUS` varchar(3) NOT NULL COMMENT 'server status, on or off',
  `LAST_UPDATE_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'last update time',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='DB WorkerID Assigner for UID Generator';
