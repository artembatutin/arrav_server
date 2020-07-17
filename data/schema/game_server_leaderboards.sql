CREATE TABLE IF NOT EXISTS `overall` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(12) DEFAULT NULL,
  `total_level` mediumint(9) DEFAULT NULL,
  `total_experience` bigint(20) DEFAULT NULL,
  `game_mode` enum('NORMAL','IRONMAN','HARDCORE_IRONMAN','ULTIMATE_IRONMAN') DEFAULT NULL,
  `experience_rate` enum('NORMAL','EXTREME','SUPREME','REALISM') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `overall_skill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(12) DEFAULT NULL,
  `last_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `skill` tinyint(4) DEFAULT NULL,
  `level` smallint(6) DEFAULT NULL,
  `experience` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_skill` (`username`,`skill`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `wilderness` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(12) DEFAULT NULL,
  `kills` int(11) DEFAULT NULL,
  `deaths` int(11) DEFAULT NULL,
  `current_killstreak` int(11) DEFAULT NULL,
  `highest_killstreak` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `top_pker_entries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `session` int(11) DEFAULT NULL,
  `session_hash` bigint(16) DEFAULT NULL,
  `username` varchar(12) DEFAULT NULL,
  `kills` int(11) DEFAULT NULL,
  `deaths` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `session_hash_UNIQUE` (`session_hash`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `top_pker_rewards` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `session_id` int(11) DEFAULT NULL,
  `predefined_reward` enum('NONE','MYSTERY_BOX','SUPER_MYSTERY_BOX','BLOOD_MONEY','LARGE_BLOOD_MONEY','ONE_TO_FIVE_TIER_ONE_EBMLEM','THREE_TO_FIVE_TIER_ONE_EMBLEM') DEFAULT 'NONE',
  `item_id` int(11) DEFAULT '-1',
  `item_amount` int(11) DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `top_pker_sessions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `winner` bigint(32) DEFAULT '-1',
  `end_date` timestamp(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `end_date_UNIQUE` (`end_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `top_pker_state` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `session` int(11) NOT NULL DEFAULT '0',
  `last_session` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO top_pker_state (id, session, last_session) VALUES (0, 0, -1);
