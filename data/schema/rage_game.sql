CREATE DATABASE IF NOT EXISTS `rage_game`;

USE `rage_game`;

CREATE TABLE IF NOT EXISTS `bought_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) unsigned NOT NULL,
  `username` varchar(12) NOT NULL,
  `ip_address` text NOT NULL,
  `uid` text NOT NULL,
  `shop_id` smallint(6) NOT NULL,
  `shop_name` text NOT NULL,
  `item_id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `currency` text NOT NULL,
  `total_cost` int(11) NOT NULL,
  `x` smallint(6) NOT NULL,
  `y` smallint(6) NOT NULL,
  `z` smallint(6) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `chats` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) unsigned NOT NULL,
  `username` varchar(12) NOT NULL,
  `message` text NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `clan_chats` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) unsigned NOT NULL,
  `channel` text NOT NULL,
  `username` varchar(12) NOT NULL,
  `message` text NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `commands` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) unsigned NOT NULL,
  `username` varchar(12) NOT NULL,
  `ip_address` text NOT NULL,
  `uid` text NOT NULL,
  `command` varchar(20) NOT NULL,
  `arguments` text NOT NULL,
  `x` smallint(6) NOT NULL,
  `y` smallint(6) NOT NULL,
  `z` smallint(6) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `dropped_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) unsigned NOT NULL,
  `username` varchar(12) NOT NULL,
  `ip_address` text NOT NULL,
  `uid` text NOT NULL,
  `item_id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `x` smallint(6) NOT NULL,
  `y` smallint(6) NOT NULL,
  `z` smallint(6) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `looted_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) unsigned NOT NULL,
  `username` varchar(12) NOT NULL,
  `ip_address` text NOT NULL,
  `uid` text NOT NULL,
  `dropped_by` varchar(12) NOT NULL,
  `item_id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `x` smallint(6) NOT NULL,
  `y` smallint(6) NOT NULL,
  `z` smallint(6) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `npc_deaths` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) unsigned NOT NULL,
  `killer` varchar(12) NOT NULL,
  `ip_address` text NOT NULL,
  `uid` text NOT NULL,
  `npc_name` varchar(36) NOT NULL,
  `npc_id` int(11) NOT NULL,
  `items_dropped` text NOT NULL,
  `x` smallint(6) NOT NULL,
  `y` smallint(6) NOT NULL,
  `z` smallint(6) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `player_deaths` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) unsigned NOT NULL,
  `username` varchar(12) NOT NULL,
  `ip_address` text NOT NULL,
  `uid` text NOT NULL,
  `killer` text NOT NULL,
  `items_dropped` text NOT NULL,
  `items_kept` text NOT NULL,
  `x` smallint(6) NOT NULL,
  `y` smallint(6) NOT NULL,
  `z` smallint(6) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `pm_chats` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) unsigned NOT NULL,
  `sender` varchar(12) NOT NULL,
  `receiver` varchar(12) NOT NULL,
  `message` text NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `received_rewards` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) unsigned NOT NULL,
  `username` varchar(12) NOT NULL,
  `ip_address` text NOT NULL,
  `uid` text NOT NULL,
  `reward_type` text NOT NULL,
  `item_id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `x` smallint(6) NOT NULL,
  `y` smallint(6) NOT NULL,
  `z` smallint(6) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `sanctions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sanction` enum('mute','address_mute','perm_mute','ban','address_ban','perm_ban') NOT NULL,
  `moderator` varchar(12) NOT NULL,
  `offender` varchar(12) NOT NULL,
  `ip_address` text,
  `serial_number` text,
  `issue` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expire` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `reason` text NOT NULL,
  `closed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `sold_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) unsigned NOT NULL,
  `username` varchar(12) NOT NULL,
  `ip_address` text NOT NULL,
  `uid` text NOT NULL,
  `shop_id` smallint(6) NOT NULL,
  `shop_name` text NOT NULL,
  `item_id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `currency_received` text NOT NULL,
  `currency_amount_received` int(11) NOT NULL,
  `x` smallint(6) NOT NULL,
  `y` smallint(6) NOT NULL,
  `z` smallint(6) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `stakes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) unsigned NOT NULL,
  `username` varchar(12) NOT NULL,
  `ip_address` text NOT NULL,
  `uid` text NOT NULL,
  `other` varchar(12) NOT NULL,
  `won` tinyint(4) NOT NULL,
  `items_staked` text NOT NULL,
  `items_received` text NOT NULL,
  `x` smallint(6) NOT NULL,
  `y` smallint(6) NOT NULL,
  `z` smallint(6) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `trades` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) unsigned NOT NULL,
  `username` varchar(12) NOT NULL,
  `ip_address` text NOT NULL,
  `uid` text NOT NULL,
  `other` varchar(12) NOT NULL,
  `other_session_id` bigint(20) unsigned NOT NULL,
  `items_gave` text NOT NULL,
  `items_received` text NOT NULL,
  `x` smallint(6) NOT NULL,
  `y` smallint(6) NOT NULL,
  `z` smallint(6) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `wogw` (
  `name` varchar(50) NOT NULL DEFAULT '0',
  `session_id` bigint(20) unsigned NOT NULL,
  `amount` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `session_id` (`session_id`)
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `daily_statistical_entries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(12) DEFAULT NULL,
  `host_address` varchar(45) DEFAULT NULL,
  `timestamp` timestamp(6) NULL DEFAULT NULL,
  `new_account` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `host_address_UNIQUE` (`host_address`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



CREATE TABLE `daily_statistical_records` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NULL DEFAULT NULL,
  `new_accounts` int(11) NOT NULL DEFAULT '0',
  `return_accounts` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `lottery_entries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hash` bigint(32) DEFAULT NULL,
  `username` varchar(12) DEFAULT NULL,
  `session` int(11) DEFAULT NULL,
  `tickets` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `hash_UNIQUE` (`hash`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `lottery_sessions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ticket_cost` int(11) DEFAULT NULL,
  `end_date` timestamp(6) NULL DEFAULT NULL,
  `first_place` int(11) DEFAULT '-1',
  `second_place` int(11) DEFAULT '-1',
  `third_place` int(11) DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `lottery_state` (
  `id` int(11) NOT NULL,
  `session_id` int(11) DEFAULT NULL,
  `last_session_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `player_shop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `owner` bigint(12) DEFAULT NULL,
  `last_interaction` bigint(12) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `owner_UNIQUE` (`owner`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `player_shop_advertisement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `owner` bigint(12) DEFAULT NULL,
  `end_date` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `owner_UNIQUE` (`owner`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `player_shop_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `owner` bigint(12) DEFAULT NULL,
  `slot` varchar(45) DEFAULT NULL,
  `item_id` int(11) DEFAULT NULL,
  `item_amount` int(11) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `poll` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `end_date` varchar(256) DEFAULT NULL,
  `question` varchar(256) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `option_one` varchar(128) DEFAULT NULL,
  `option_two` varchar(128) DEFAULT NULL,
  `option_three` varchar(128) DEFAULT NULL,
  `option_four` varchar(128) DEFAULT NULL,
  `state` varchar(128) DEFAULT 'OPEN',
  `result_if_any` varchar(128) DEFAULT 'TO_BE_DETERMINED',
  `option_if_successful` varchar(128) DEFAULT NULL,
  `placement` varchar(128) DEFAULT 'FIFTH',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


CREATE TABLE `polls` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first` int(11) DEFAULT '-1',
  `second` int(11) DEFAULT '-1',
  `third` int(11) DEFAULT '-1',
  `fourth` int(11) DEFAULT '-1',
  `fifth` int(11) DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO polls (first, second, third, fourth, fifth) VALUES(-1, -1, -1, -1, -1);

CREATE TABLE `poll_votes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `poll_id` int(11) DEFAULT NULL,
  `id_username_hash` bigint(32) DEFAULT NULL,
  `username` varchar(12) DEFAULT NULL,
  `vote_option` enum('FIRST','SECOND','THIRD','FOURTH') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_username_hash_UNIQUE` (`id_username_hash`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `game_attributes` (
	`id` INT(11) NOT NULL,
	`key` TEXT NOT NULL,
	`value` TEXT NOT NULL,
	`timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `received_starters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(12) NOT NULL,
  `game_mode` enum('NORMAL','PK_MODE') NOT NULL,
  `ip_address` text NOT NULL,
  `uid` text NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
