-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.7.26 - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for rage_web
CREATE DATABASE IF NOT EXISTS `rage_web` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `rage_web`;

-- Dumping structure for table rage_web.clan
CREATE TABLE IF NOT EXISTS `clan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `owner_id` int(11) NOT NULL,
  `owner_name` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `header_url` varchar(255) DEFAULT NULL,
  `clan_tag` varchar(255) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `cmb_lvl` int(11) NOT NULL DEFAULT '3',
  `is_private` tinyint(1) NOT NULL DEFAULT '0',
  `invite_only` tinyint(1) NOT NULL DEFAULT '0',
  `short_desc` varchar(255) NOT NULL,
  `about_clan` mediumtext NOT NULL,
  `date_created` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

-- Dumping data for table rage_web.clan: 3 rows
/*!40000 ALTER TABLE `clan` DISABLE KEYS */;
INSERT INTO `clan` (`id`, `owner_id`, `owner_name`, `title`, `avatar_url`, `header_url`, `clan_tag`, `type`, `cmb_lvl`, `is_private`, `invite_only`, `short_desc`, `about_clan`, `date_created`) VALUES
	(4, 481, 'Rebirth', 'Legion of Lost Souls', 'https://i.imgur.com/AD4hjQU.png', NULL, 'LoSS', 'pvm', 3, 0, 0, 'Gloom and doom.', '<p class="ql-align-center">Founding Member: <strong>Rebirth</strong></p><p class="ql-align-center"><br /></p><p class="ql-align-center">The clan of infamy, everything is wrong but we aprove.</p>', 1558314157),
	(2, 93, 'Dj Kaz', 'Clan Tribe', NULL, NULL, 'CTT', 'pvp', 3, 0, 1, 'The old Tribe', '<p><br /></p>', 1558168321),
	(5, 1, 'Tamatea', 'Test', NULL, NULL, 'Gang', 'minigames', 3, 0, 1, 'good cunts', '<p>We got some gang shit goin on nigga</p>', 1578050100);
/*!40000 ALTER TABLE `clan` ENABLE KEYS */;

-- Dumping structure for table rage_web.clan_apps
CREATE TABLE IF NOT EXISTS `clan_apps` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `clan_id` int(11) NOT NULL,
  `applied_date` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- Dumping data for table rage_web.clan_apps: 0 rows
/*!40000 ALTER TABLE `clan_apps` DISABLE KEYS */;
/*!40000 ALTER TABLE `clan_apps` ENABLE KEYS */;

-- Dumping structure for table rage_web.clan_members
CREATE TABLE IF NOT EXISTS `clan_members` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `clan` int(11) NOT NULL,
  `rank` varchar(255) NOT NULL DEFAULT 'member',
  `kills` int(11) NOT NULL DEFAULT '0',
  `deaths` int(11) NOT NULL DEFAULT '0',
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `joined` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

-- Dumping data for table rage_web.clan_members: 3 rows
/*!40000 ALTER TABLE `clan_members` DISABLE KEYS */;
INSERT INTO `clan_members` (`id`, `user_id`, `username`, `clan`, `rank`, `kills`, `deaths`, `status`, `joined`) VALUES
	(10, 481, 'Rebirth', 4, 'owner', 0, 0, 1, 1558314140),
	(2, 93, 'Dj Kaz', 2, 'owner', 0, 0, 1, 1558168321),
	(11, 1, 'Tamatea', 5, 'owner', 0, 0, 1, 1578050100);
/*!40000 ALTER TABLE `clan_members` ENABLE KEYS */;

-- Dumping structure for table rage_web.hs_searches
CREATE TABLE IF NOT EXISTS `hs_searches` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `time_searched` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table rage_web.hs_searches: ~0 rows (approximately)
/*!40000 ALTER TABLE `hs_searches` DISABLE KEYS */;
/*!40000 ALTER TABLE `hs_searches` ENABLE KEYS */;

-- Dumping structure for table rage_web.notices
CREATE TABLE IF NOT EXISTS `notices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `controller` varchar(255) DEFAULT NULL,
  `c_action` varchar(255) DEFAULT NULL,
  `type` varchar(255) NOT NULL DEFAULT 'card',
  `sub_type` varchar(255) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `content` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- Dumping data for table rage_web.notices: 5 rows
/*!40000 ALTER TABLE `notices` DISABLE KEYS */;
INSERT INTO `notices` (`id`, `name`, `controller`, `c_action`, `type`, `sub_type`, `active`, `content`) VALUES
	(1, 'homepage', 'index', 'index', 'alert', 'info', 0, '<h4>Testing Notice System</h4><p>This is just a test of a newly added CMS. Work in progress, dun hate.</p>'),
	(2, 'store_top', 'store', 'index', 'card', 'border-0', 0, '<p>Test</p>'),
	(3, 'store_rsgp', 'store', 'index', 'text', 'border-0', 1, '<p>Donations we accept:</p><ul><li>Amazon Gift Card</li><li>OSGP ($0.70 per 1m, ie. 14m = $10)</li><li>CS:GO Skins, .80 per $</li></ul><p>Staff that can handle donations:</p><ul><li>Reaper</li><li>Caliphate - <span style="color: rgb(255, 153, 0);">Caliphate#3930</span></li><li>Talon<span style="color: rgb(255, 153, 0);"> - Fahqu420#0687</span></li></ul><p>Web-Store and Payment issues:</p><p>Post a ticket on https://support.ss.getvm.co/</p><p><br></p>'),
	(4, 'terms', 'pages', 'terms', 'text', 'border-0', 1, '<h3><span style="color: rgb(255, 194, 102);">SoulSplit Terms and Conditions</span></h3><p>Your use of this website ("ss.getvm.co" and all of it\'s subdomains, including but not limited to "forums.ss.getvm.co") is subject to the following terms and conditions. By using this website, you show your acceptance of these terms.</p><p>Please note that different terms and conditions apply for each of the products or services we supply through this website. We recommend that you print out and retain a copy of these terms and conditions and of any terms and conditions relating to any products or services we supply through this website.</p><h6><strong style="color: rgb(255, 194, 102);">Terms and Conditions of the products or services we supply through this website</strong></h6><ol><li>If you apply for any products or services from this website, the terms and conditions relating to the products or services will apply, rather than these Website Terms and Conditions.</li><li>The contents of this paragraph do not constitute an offer by us to sell products or services to you. Your request to purchase a product or services represents an offer by you that we may accept or reject. After you ask through this website to purchase the product or services, then assuming such product or services are available and your offer is accepted, you will receive confirmation of your purchase.</li><li>We reserve the right at any time, and without prior notice, to cease to supply any of the products or services referred to in this website and will not be liable to you in any way if we cease such supply.</li></ol><h6><strong style="color: rgb(255, 194, 102);">Copyright and other Intellectual Property rights</strong></h6><ol><li>The images and information contained in this website are copyright works and our exclusive property. Elements of this website may also be protected by trade mark, unfair competition, passing off and other intellectual property rights. Except for making a hard copy print for personal use only, or downloading the material for personal use only, the material on this website may not be copied, reproduced, transmitted, distributed or displayed by any means, without our express written consent. Nothing in this website shall confer on any person any licence or right to use any such image, logo, name or trademark.</li></ol><h6><strong style="color: rgb(255, 194, 102);">Changes to these Website Terms and Conditions</strong></h6><ol><li>The material contained in this website is provided for general information purposes only. Although we strive to ensure that the information contained is accurate and complete at the date of publication, no representation or warranties are made (whether express or implied) as to the reliability, accuracy or completeness of such information.</li></ol><h6><strong style="color: rgb(255, 194, 102);">Protection and security of your data</strong></h6><ol><li>By accepting our Website Terms and Conditions, you consent that we may use the information you provide to conduct appropriate anti-fraud checks. Your personal and financial information will not be shared with anyone, and will not be stored on our servers All information you provide will be treated securely.</li></ol><h6><strong style="color: rgb(255, 194, 102);">Disclaimers, limitation and exclusion of liability</strong></h6><ol><li>While we have taken care in the preparation of this website, the website and any information contained in it relating to its products or services are provided on an ???????as is??????? basis, without any representation or endorsement being made and without any warranty of any kind, including but not limited to, any implied warranties or satisfactory quality, fitness for a particular person, non-infringement, compatibility, security and accuracy.</li><li>In no event shall we be liable for any loss, damages or costs arising out of your use of this website or any action taken in reliance on information appearing on it. Nor shall we be liable for any loss, damages or costs arising out of any defamatory posting which may be made in the forum. We refer you to the forum terms and conditions which govern your use of the forum, which are set out at your entry to the forum.</li><li>We do not warrant that this website is free from infection by viruses or other matter that may contaminate or destroy your computer system. We do not warrant that the materials accessible from this website will be uninterrupted or error free or that this website, or the server that makes it available, are virus or bug free.</li></ol><h6><strong style="color: rgb(255, 194, 102);">Links to other websites</strong></h6><ol><li>Certain links may lead you to websites that are not under our control. When you activate any of these links, you will leave this website and we have no control over, and will accept no responsibility or liability for, the material on any other website.</li></ol><h6><strong style="color: rgb(255, 194, 102);">Waiver</strong></h6><ol><li>If you breach these website terms and conditions of use, and we do not take immediate action against you, we are still entitled to enforce our rights and remedies for any such breach or subsequent breach.</li></ol><h6><strong style="color: rgb(255, 194, 102);">Jurisdiction and law</strong></h6><ol><li>Unless otherwise specified, the products and services described in this website are available to residents throughout the world. The information contained in this website may not satisfy the laws of other countries and those who choose to access this website from other countries are responsible for compliance with local laws if they apply.</li><li>These Website Terms and Conditions and any terms and conditions relating to products or services described in this website are governed by the laws of England. Disputes arising in relation to them shall be subject to the exclusive jurisdiction of the courts of England.</li></ol><h6><strong style="color: rgb(255, 194, 102);">Your use of the forum</strong></h6><ol><li>Your use of the forum is governed by the forum terms and conditions which are set out on the first page of the forum. These terms and conditions exclude liability for any defamatory matter posted by any third party in the forum. We refer you to the disclaimer in paragraph 5 above.</li></ol><h6><strong style="color: rgb(255, 194, 102);">Sales and subscriptions</strong></h6><ol><li>All sales are final. Under no circumstance any refunds refunds will be given. If any problems occur with a payment our 24/7 support team will gladly help you on????<a href="https://support.ss.getvm.co/" target="_blank" style="color: rgb(255, 226, 97);">our support page</a>.</li></ol><h6><strong style="color: rgb(255, 194, 102);">Copyright infringement notification</strong></h6><ol><li>If you are a copyright owner or an agent of copyright owner and believe that any user content or other content that is being made available on or through this website infringes your copyright, you may send a written notice of copyright infringement (???????Notice???????) to our Copyright Agent via email or by post with the following information:</li><li class="ql-indent-1">A statement that you have identified material on this website which infringes your copyright or the copyright of a third party on whose behalf you are entitled to act;</li><li class="ql-indent-1">A description of the copyrighted work that you claim has been infringed;</li><li class="ql-indent-1">A description specifying material you claim is infringing and the location of the material on this website (including, for example, a URL and/or screen shot);</li><li class="ql-indent-1">Your full name, telephone number and email address on which you can be contacted;</li><li class="ql-indent-1">A statement that you have a good faith belief that the disputed use of the material is not authorised by the copyright owner, its agent, or by law;</li><li class="ql-indent-1">A statement that the information in the Notice is accurate and that you are authorised to act on behalf of the owner of the exclusive right that is allegedly infringed; and</li><li class="ql-indent-1">An electronic or physical signature (which may be a scanned copy) of a person authorised to act on behalf of the owner of the work that is allegedly infringed.</li></ol><h6><strong style="color: rgb(255, 194, 102);">Users under 18</strong></h6><ul><li class="ql-indent-1">Some Soulsplit Products may have age restrictions which prevent users under 13 creating an account. If a Soulsplit Product is not subject to age restrictions, a user under 13 may create an account. However, they must get consent from their parents/guardians to use the Soulsplit Product in accordance with our terms and conditions and privacy policy. We may Stop any account if we are not satisfied that such consent has been given or that such consent does not comply with local laws regarding users who are under 13 years of age.</li><li class="ql-indent-1">We do not knowingly collect or store any personally identifiable information from children under 13. If we find out that any personally identifiable information we have collected is from someone under 13, we will automatically delete it, and we may Stop the source account for that information.</li><li class="ql-indent-1">We recommend that parents/guardians should play together with their children who are under the age of 13.</li><li class="ql-indent-1">If you are under 18, by using our website you are representing (i.e. making a promise) that you have consent from your parents/guardian to use Soulsplit Products in accordance with our terms and conditions and privacy policy. We may Stop any account if we are not satisfied that such consent has been given.</li><li class="ql-indent-1">If we are told by a parent/guardian that their child has lied about their age when registering for Soulsplit Products, we will Stop the account provided we have information which satisfies us that the reporting person is indeed the parent or guardian.</li></ul><p><br></p>'),
	(6, 'rules', 'pages', 'rules', 'text', 'border-0', 1, '<p>Basic bitch rules here</p>');
/*!40000 ALTER TABLE `notices` ENABLE KEYS */;

-- Dumping structure for table rage_web.referrals
CREATE TABLE IF NOT EXISTS `referrals` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ref_id` int(11) DEFAULT '-1',
  `ref_name` varchar(255) DEFAULT NULL,
  `ref_url` varchar(255) DEFAULT NULL,
  `ip_address` varchar(255) DEFAULT NULL,
  `clicks` int(11) DEFAULT '0',
  `dateline` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table rage_web.referrals: ~0 rows (approximately)
/*!40000 ALTER TABLE `referrals` DISABLE KEYS */;
/*!40000 ALTER TABLE `referrals` ENABLE KEYS */;

-- Dumping structure for table rage_web.referrers
CREATE TABLE IF NOT EXISTS `referrers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ref_name` varchar(255) DEFAULT NULL,
  `referrals` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;

-- Dumping data for table rage_web.referrers: ~19 rows (approximately)
/*!40000 ALTER TABLE `referrers` DISABLE KEYS */;
INSERT INTO `referrers` (`id`, `ref_name`, `referrals`) VALUES
	(3, 'wet', 0),
	(4, 'nyan', 0),
	(5, 'row', 0),
	(6, 'sprad', 0),
	(7, 'sohan', 0),
	(8, 'wr3cked', 0),
	(9, 'eggy', 0),
	(11, 'did', 0),
	(12, 'vihtic', 0),
	(13, 'fpk', 0),
	(14, 'fewb', 0),
	(15, 'artz', 0),
	(16, 'zach', 0),
	(17, 'jordan', 0),
	(19, 'luksa', 0),
	(20, 'osbot', 0),
	(21, 'rspslist', 0),
	(22, 'runeserver', 0),
	(23, 'rl', 0);
/*!40000 ALTER TABLE `referrers` ENABLE KEYS */;

-- Dumping structure for table rage_web.site_logs
CREATE TABLE IF NOT EXISTS `site_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `staff_id` int(11) NOT NULL,
  `staff_name` varchar(255) NOT NULL,
  `other_id` int(11) NOT NULL,
  `other_name` varchar(255) NOT NULL,
  `log_type` varchar(255) NOT NULL,
  `page_no` int(11) NOT NULL DEFAULT '0',
  `lookup_time` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table rage_web.site_logs: ~0 rows (approximately)
/*!40000 ALTER TABLE `site_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `site_logs` ENABLE KEYS */;

-- Dumping structure for table rage_web.spins_log
CREATE TABLE IF NOT EXISTS `spins_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `item_id` int(11) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT '1',
  `rarity` int(11) NOT NULL,
  `tier` int(11) DEFAULT NULL,
  `claimed` tinyint(1) NOT NULL DEFAULT '0',
  `date_added` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table rage_web.spins_log: ~0 rows (approximately)
/*!40000 ALTER TABLE `spins_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `spins_log` ENABLE KEYS */;

-- Dumping structure for table rage_web.spins_table
CREATE TABLE IF NOT EXISTS `spins_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_id` int(11) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT '1',
  `rarity` int(11) NOT NULL,
  `tier` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8;

-- Dumping data for table rage_web.spins_table: ~115 rows (approximately)
/*!40000 ALTER TABLE `spins_table` DISABLE KEYS */;
INSERT INTO `spins_table` (`id`, `item_id`, `item_name`, `quantity`, `rarity`, `tier`) VALUES
	(1, 441, 'Iron Ore', 75, 0, 0),
	(2, 448, 'Mithril Ore', 75, 0, 1),
	(3, 450, 'Adamant Ore', 75, 0, 2),
	(4, 452, 'Rune Ore', 100, 0, 2),
	(5, 318, 'Raw Shrimp', 150, 0, 0),
	(6, 378, 'Raw Lobster', 150, 0, 0),
	(7, 384, 'Raw Shark', 150, 0, 1),
	(8, 7945, 'Raw Monkfish', 150, 0, 2),
	(9, 18831, 'Frost Dragon Bones', 5, 0, 3),
	(10, 11212, 'Dragon Arrows', 75, 0, 0),
	(11, 6737, 'Berserker Ring', 1, 0, 0),
	(12, 6733, 'Archers Ring', 1, 0, 0),
	(13, 15031, 'Seers Ring', 1, 0, 0),
	(14, 2579, 'Wizard Boots', 1, 0, 0),
	(15, 868, 'Rune Knife', 150, 0, 0),
	(16, 6585, 'Amulet of Fury', 1, 0, 2),
	(17, 4151, 'Abyssal Whip', 1, 0, 2),
	(18, 995, 'Coins', 2500000, 0, 0),
	(19, 9244, 'Dragon Bolts (e)', 150, 0, 0),
	(20, 3140, 'Dragon Chainbody', 1, 0, 1),
	(21, 4087, 'Dragon Platelegs', 1, 0, 1),
	(22, 11732, 'Dragon Boots', 1, 0, 1),
	(23, 987, 'Loop Half of Key', 1, 1, 0),
	(24, 985, 'Tooth Half of Key', 1, 1, 0),
	(25, 6889, 'Mage\'s Book', 1, 1, 0),
	(26, 11335, 'Dragon Full Helm', 1, 1, 1),
	(27, 11726, 'Bandos Tasset', 1, 1, 2),
	(28, 11724, 'Bandos Chestplate', 1, 1, 2),
	(29, 995, 'Coins', 5000000, 1, 0),
	(30, 13113, 'Cat Mask', 1, 1, 0),
	(31, 2364, 'Rune Bar', 50, 1, 0),
	(32, 2362, 'Adamant Bar', 100, 1, 1),
	(33, 1516, 'Yew Logs', 100, 1, 0),
	(34, 1514, 'Magic Logs', 300, 1, 2),
	(35, 1618, 'Uncut Diamonds', 50, 1, 0),
	(36, 1618, 'Uncut Dragonstone', 25, 1, 0),
	(37, 15332, 'Overloads', 10, 1, 1),
	(38, 2581, 'Robin Hood Hat', 1, 1, 1),
	(39, 11730, 'Saradomin Sword', 1, 1, 2),
	(40, 10858, 'Shadow Sword', 1, 1, 3),
	(41, 15126, 'Amulet of Ranging', 1, 1, 0),
	(42, 10034, 'Red Chinchompa', 150, 1, 0),
	(43, 6570, 'Fire Cape', 1, 2, 2),
	(44, 2577, 'Ranger Boots', 1, 2, 0),
	(45, 10033, 'Chinchompa', 500, 2, 0),
	(46, 10034, 'Red Chinchompa', 400, 2, 0),
	(47, 18831, 'Frost Dragon Bones', 100, 2, 0),
	(48, 4566, 'Rubber Chicken', 1, 2, 1),
	(49, 2460, 'Flowers (Purple)', 1, 2, 0),
	(50, 2472, 'Flowers (Tri-Color)', 1, 2, 0),
	(51, 11852, 'Karil\'s Set', 1, 2, 1),
	(52, 11854, 'Torag\'s Set', 1, 2, 1),
	(53, 11848, 'Dharok\'s Set', 1, 2, 1),
	(54, 11846, 'Ahrim\'s Set', 1, 2, 1),
	(55, 11850, 'Guthan\'s Set', 1, 2, 1),
	(56, 11854, 'Verac\'s Set', 1, 2, 1),
	(57, 11718, 'Armadyl Helmet', 1, 2, 2),
	(58, 11720, 'Armadyl Chestplate', 1, 2, 2),
	(59, 11722, 'Armadyl Plateskirt', 1, 2, 2),
	(60, 15220, 'Berserker Ring (i)', 1, 2, 0),
	(61, 15019, 'Archer Ring (i)', 1, 2, 0),
	(62, 15018, 'Seers Ring (i)', 1, 2, 0),
	(63, 995, 'Coins', 10000000, 2, 0),
	(64, 13744, 'Spectral Spirit', 1, 3, 1),
	(65, 13738, 'Arcane Spirit Shield', 1, 3, 1),
	(66, 13742, 'Elysian Spirit SHield', 1, 3, 1),
	(67, 21787, 'Steadfast Boots', 1, 3, 1),
	(68, 21790, 'Glaiven Boots', 1, 3, 1),
	(69, 21793, 'Ragefire Boots', 1, 3, 1),
	(70, 14479, 'Dragon Platebody', 1, 3, 0),
	(71, 11716, 'Zamorakian Spear', 1, 3, 0),
	(72, 18831, 'Frost Dragon Bones', 250, 3, 0),
	(73, 18349, 'Chaotic Rapier', 1, 3, 2),
	(74, 18351, 'Chaotic Longsword', 1, 3, 2),
	(75, 18353, 'Chaotic Maul', 1, 3, 2),
	(76, 18355, 'Chaotic Staff', 1, 3, 2),
	(77, 18357, 'Chaotic Crossbow', 1, 3, 2),
	(78, 18359, 'Chaotic Kiteshield', 1, 3, 2),
	(79, 6543, 'Antique Lamp', 1, 3, 0),
	(80, 995, 'Coins', 15000000, 3, 0),
	(81, 14484, 'Dragon Claws', 1, 3, 1),
	(82, 5607, 'Grain', 1, 3, 0),
	(83, 5608, 'Fox', 1, 3, 0),
	(84, 5609, 'Chicken', 1, 3, 0),
	(85, 10836, 'Silly Jester Hat', 1, 3, 0),
	(86, 10837, 'Silly Jester Top', 1, 3, 0),
	(87, 10838, 'Silly Jester Tights', 1, 3, 0),
	(88, 10839, 'Silly Jester Boots', 1, 3, 0),
	(89, 10840, 'A Jester Stick', 1, 3, 1),
	(90, 13740, 'Divine Spirit Shield', 1, 3, 3),
	(91, 20163, 'Virtus robe top', 1, 4, 1),
	(92, 20167, 'Virtus robe legs', 1, 4, 1),
	(93, 20147, 'Pernix Cowl', 1, 4, 1),
	(94, 20151, 'Pernix body', 1, 4, 1),
	(95, 20155, 'Pernix chaps', 1, 4, 1),
	(96, 7927, 'Easter ring', 1, 4, 0),
	(97, 11858, 'Third-age melee set', 1, 4, 2),
	(98, 11860, 'Third-age ranger set', 1, 4, 2),
	(99, 11862, 'Third-age mage set', 1, 4, 2),
	(100, 19580, 'Third-age prayer set', 1, 4, 3),
	(101, 1050, 'Santa Hat', 1, 4, 1),
	(102, 1419, 'Scythe', 1, 4, 1),
	(103, 1037, 'Bunny Ears', 1, 4, 0),
	(104, 4084, 'Sled', 1, 4, 1),
	(106, 18768, 'Mystery Box', 5, 4, 0),
	(107, 2476, 'Flowers (Black)', 1, 3, 3),
	(108, 2474, 'Flowers (White)', 1, 3, 3),
	(111, 6861, 'Scarf (Green/Red)', 1, 3, 2),
	(112, 6859, 'Scarf (Red/White)', 1, 3, 2),
	(113, 995, 'Coins', 100000000, 4, 1),
	(114, 20159, 'Virtus Mask', 1, 4, 1),
	(115, 962, 'Christmas Cracker', 1, 4, 3),
	(116, 20135, 'Torva Full Helm', 1, 4, 2),
	(117, 20139, 'Torva Platebody', 1, 4, 2),
	(118, 20143, 'Torva Platelegs', 1, 4, 2);
/*!40000 ALTER TABLE `spins_table` ENABLE KEYS */;

-- Dumping structure for table rage_web.store_bans
CREATE TABLE IF NOT EXISTS `store_bans` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`id`,`email`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- Dumping data for table rage_web.store_bans: 0 rows
/*!40000 ALTER TABLE `store_bans` DISABLE KEYS */;
/*!40000 ALTER TABLE `store_bans` ENABLE KEYS */;

-- Dumping structure for table rage_web.store_categories
CREATE TABLE IF NOT EXISTS `store_categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=66 DEFAULT CHARSET=latin1;

-- Dumping data for table rage_web.store_categories: 7 rows
/*!40000 ALTER TABLE `store_categories` DISABLE KEYS */;
INSERT INTO `store_categories` (`id`, `title`, `enabled`, `image`) VALUES
	(64, 'Card Game ', 1, ''),
	(53, 'Equipment', 1, 'https://vignette.wikia.nocookie.net/runescape2/images/f/f5/Rune_armour_set_%28lg%29.png/revision/latest?cb=20160502071026'),
	(54, 'Cosmetics', 1, 'https://vignette.wikia.nocookie.net/runescape2/images/e/e9/White_partyhat.png/revision/latest?cb=20160502091714'),
	(55, 'Supplies', 1, 'img/store/categories/6685.png'),
	(51, 'General', 1, 'http://www.runescape.com/img/rs3/forums/icons/general.png'),
	(62, 'PVP', 0, 'https://vignette.wikia.nocookie.net/runescape2/images/4/4a/Skull.png/revision/latest?cb=20140804045038'),
	(65, 'Membership', 0, '');
/*!40000 ALTER TABLE `store_categories` ENABLE KEYS */;

-- Dumping structure for table rage_web.store_codes
CREATE TABLE IF NOT EXISTS `store_codes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `amount` double NOT NULL DEFAULT '0',
  `expires` varchar(255) NOT NULL,
  `product_id` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table rage_web.store_codes: ~1 rows (approximately)
/*!40000 ALTER TABLE `store_codes` DISABLE KEYS */;
INSERT INTO `store_codes` (`id`, `code`, `amount`, `expires`, `product_id`) VALUES
	(2, 'HAPPYEASTER', 15, '2019-04-23 00:00', -1);
/*!40000 ALTER TABLE `store_codes` ENABLE KEYS */;

-- Dumping structure for table rage_web.store_credits
CREATE TABLE IF NOT EXISTS `store_credits` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  `price` float NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

-- Dumping data for table rage_web.store_credits: 5 rows
/*!40000 ALTER TABLE `store_credits` DISABLE KEYS */;
INSERT INTO `store_credits` (`id`, `title`, `description`, `image_url`, `price`, `amount`) VALUES
	(11, '312,500 SSC', '250,000 credits with +25% bonus credits.', 'img/store/chest5.png', 249.99, 312500),
	(10, '120,000 SSC', '100,000 credits with +20% bonus credits.', 'img/store/chest4.png', 99.99, 120000),
	(5, '57,500 SSC', '50,000 credits with +15% bonus credits.', 'img/store/chest3.png', 49.99, 57500),
	(3, '27,500 SSC', '25,000 credits with +10% bonus credits.', 'img/store/chest2.png', 24.99, 27500),
	(1, '10,500 SSC', '10,000 credits with +5% bonus credits', 'img/store/chest1.png', 9.99, 10500);
/*!40000 ALTER TABLE `store_credits` ENABLE KEYS */;

-- Dumping structure for table rage_web.store_items
CREATE TABLE IF NOT EXISTS `store_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_id` int(11) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT '1',
  `price` int(11) NOT NULL,
  `discount` int(11) NOT NULL DEFAULT '0',
  `category` int(11) NOT NULL DEFAULT '-1',
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `iron_mode` tinyint(1) NOT NULL DEFAULT '0',
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=361 DEFAULT CHARSET=latin1;

-- Dumping data for table rage_web.store_items: 121 rows
/*!40000 ALTER TABLE `store_items` DISABLE KEYS */;
INSERT INTO `store_items` (`id`, `item_id`, `item_name`, `quantity`, `price`, `discount`, `category`, `enabled`, `iron_mode`, `description`) VALUES
	(335, 13867, 'Zuriel\'s staff', 1, 5030, 0, 62, 0, 0, ''),
	(54, 21777, 'Armadyl battlestaff', 1, 37499, 0, 53, 1, 0, 'The Armadyl battlestaff is a very powerful staff that requires 77 magic to wield. The staff is mainly used for casting the Storm of Armadyl spell.'),
	(164, 19669, 'Ring of vigour', 1, 7589, 0, 53, 1, 0, ''),
	(50, 6543, 'Antique Lamp', 1, 15765, 5, 51, 1, 0, 'Antique lamps, also known as a \'EXP lamps\' or \'xp lamps\', are used by many players as a way to elevate training skills they do not enjoy or do not have time for.'),
	(39, 21371, 'Abyssal Vine Whip', 1, 8590, 0, 53, 1, 0, 'The Abyssal Vine Whip is an Abyssal Whip interlaced with a Whip vine. This turns the Abyssal Whip into a much more powerful weapon. It requires 75 Attack and 80 slayer to wield.  '),
	(38, 6585, 'Amulet of fury', 1, 4876, 0, 53, 1, 0, 'The Amulet of Fury is an amulet used by many players on Soulsplit who desire an overall bonus in all stats.'),
	(37, 11846, 'Barrows - ahrims set', 1, 13390, 0, 53, 1, 0, 'Ahrim\'s set offers some of the best Magic bonuses available and is normally recommended for fighting various high level monsters. It is one of the few sets of magical robes that give Defence bonuses against things other than Magic.'),
	(41, 4151, 'Abyssal Whip', 1, 5100, 0, 53, 1, 0, 'The Abyssal whip is a powerful one-handed Melee weapon that requires level 70 Attack to wield.'),
	(43, 15126, 'Amulet of ranging', 1, 4286, 0, 53, 1, 0, 'The Amulet of ranging is an amulet dropped by Aquanites. It boosts the player\'s Ranged accuracy and is the best amulet in-game for Ranged attack.'),
	(46, 7807, 'Anger battleaxe', 1, 56925, 0, 54, 1, 1, 'Anger weaponry is generally considered quite rare, as you do not see many players who have one in-game.'),
	(47, 7808, 'Anger mace', 1, 56925, 0, 54, 1, 1, 'Anger weaponry is generally considered quite rare, as you do not see many players who have one in-game.'),
	(48, 7809, 'Anger spear', 1, 56925, 0, 54, 1, 1, 'Anger weaponry is generally considered quite rare, as you do not see many players who have one in-game.'),
	(49, 7806, 'Anger sword', 1, 56925, 0, 54, 1, 1, 'Anger weaponry is generally considered quite rare, as you do not see many players who have one in-game.'),
	(55, 11694, 'Armadyl godsword', 1, 26799, 0, 53, 1, 0, 'The Armadyl Godsword is one of the four legendary godswords that was fought over during the God Wars. It requires 75 Attack to wield. '),
	(57, 11696, 'Bandos godsword', 1, 14325, 0, 53, 1, 0, 'The Bandos godsword is one of the four legendary Godswords. Requiring 75 Attack to wield.'),
	(158, 15018, 'Seers\' Ring (i)', 1, 4896, 0, 53, 1, 0, ''),
	(159, 15020, 'Warrior Ring (i)', 1, 4896, 0, 53, 1, 0, ''),
	(62, 667, 'Blurite Sword', 1, 3565, 0, 62, 0, 0, 'The Blurite sword is currently used by 1 attack pures during PvP (Player vs Player) as it is good for rich 1 Attack pures who want to use something to add a risk to their fight!'),
	(63, 18337, 'Bonecrusher', 1, 6324, 0, 53, 1, 0, 'It is used to train Prayer. When a player kills a monster who drops bones (any kind of bones) the Bonecrusher crushes the bones to gain Prayer experience. The player must have the Bonecrusher in the inventory to make use of the effect.'),
	(71, 12160, 'Crimson Charms (x2500)', 2500, 8324, 0, 55, 1, 0, 'Charms are used to train the Summoning skill.'),
	(72, 12158, 'Gold Charms (x2500)', 2500, 8589, 0, 55, 1, 0, 'Charms are used to train the Summoning skill.'),
	(73, 12159, 'Green Charms (x2500)', 2500, 9854, 0, 55, 1, 0, 'Charms are used to train the Summoning skill.'),
	(74, 12163, 'Blue Charms (x2500)', 2500, 12119, 0, 55, 1, 0, 'Charms are used to train the Summoning skill.'),
	(76, 7462, 'Culinaromancer\'s gloves 10', 1, 2439, 0, 53, 1, 0, 'Culinaromancer\'s Gloves 10, also known as Barrows Gloves.'),
	(77, 6748, 'Demonic sigil', 1, 18649, 0, 53, 1, 0, 'The Demonic sigil is used to travel to the well known Tormented demon\'s Lair.'),
	(78, 11848, 'Barrows - dharoks set', 1, 13276, 0, 53, 1, 0, 'Dharok\'s equipment is one of the five armour sets obtained from the Barrows Minigame.'),
	(83, 15084, 'Dice bag bundle (all 3-bags)', 1, 250000, 0, 51, 0, 0, 'This bundle will include the Purple, Red, and Blue dice bags!'),
	(86, 11732, 'Dragon boots', 1, 4276, 0, 53, 1, 0, 'Dragon Boots, or commonly known as "D Boots", are often used by Low-Wealth and Mid-Wealth players.'),
	(87, 14484, 'Dragon claws', 1, 29999, 0, 53, 1, 0, 'Dragon Claws, referred to as Claws or D Claws, are a very strong Melee weapon. Dragon Claws are well known for their Special Attack "slice and dice".'),
	(92, 11283, 'Dragonfire shield', 1, 5675, 0, 53, 1, 0, 'The dragonfire shield (also known as DFS) is an anti-dragon shield that has been forged with a draconic visage. Requiring 75 Defence to equip.'),
	(93, 18361, 'Eagle-eye kiteshield', 1, 15650, 0, 53, 1, 0, 'A shield used by archers, this shield provides great Range bonuses as well as a decent defence bonus, it is not seen often in-game.'),
	(95, 18363, 'Farseer kiteshield', 1, 15649, 0, 53, 1, 0, 'A kiteshield used by mages, it has high mage attack bonus as well as a good overall defence bonus.'),
	(96, 6570, 'Fire cape', 1, 16649, 0, 53, 1, 0, 'The Fire Cape is a famous and popular cape that is common for its Strength bonuses as well as its cosmetic look.'),
	(97, 14573, 'Fremennik Sea Boots 3', 1, 5500, 0, 54, 1, 1, 'They are mainly used for looks as it is mostly a cosmetic item.'),
	(99, 212, 'Grimy avantoe (x1000)', 1000, 3784, 0, 55, 1, 0, 'A Grimy avantoe is a mid level herb used in Herblore.'),
	(100, 218, 'Grimy dwarf weed (x1000)', 1000, 3593, 0, 55, 1, 0, 'A Grimy dwarf weed is a high level herb used in Herblore at level 70.'),
	(101, 2486, 'Grimy lantadyme (x1000)', 1000, 3502, 0, 55, 1, 0, 'A Grimy lantadyme is a high leveled herb used in Herblore.'),
	(102, 11850, 'Barrows - guthans set', 1, 12390, 0, 53, 1, 0, 'Guthan\'s the Infested is one of the six Barrows brothers, the guthan\'s armor is very strong in PvM.'),
	(105, 2359, 'Mithril bar (x1000)', 1000, 3021, 0, 55, 1, 0, 'The Mithril bar is the finished product after using an Mithril ore on a Furnace with 50 Smithing.'),
	(106, 18768, 'Mystery box', 1, 15765, 0, 51, 1, 0, 'Mystery boxes are one of the original gambling items, which give the player the opportunity to acquire rare items.'),
	(156, 15220, 'Berserker ring (i)', 1, 5496, 0, 53, 1, 0, ''),
	(157, 15019, 'Archers\' ring (i)', 1, 4896, 0, 53, 1, 0, ''),
	(108, 15332, 'Overload (4) (x50)', 50, 4795, 0, 55, 1, 0, 'Overloads are a combination of Extreme potions. They boost the players stats significantly, making it the best stat boosting potion.'),
	(112, 23597, 'Bank Boost - 25 Slots', 1, 10119, 0, 51, 0, 0, ''),
	(113, 23597, 'Bank Boost - 50 Slots', 2, 16444, 0, 51, 0, 0, ''),
	(125, 21630, 'Prayer Renewal Potions (4) (x50)', 50, 3529, 0, 55, 1, 0, ''),
	(126, 15300, 'Special Recover Potions (4) (x20)', 20, 3529, 0, 55, 1, 0, ''),
	(128, 384, 'Raw Sharks (x1000)', 1000, 2678, 0, 55, 1, 0, ''),
	(130, 390, 'Raw Manta Ray (x1000)', 1000, 2759, 0, 55, 1, 0, ''),
	(131, 15271, 'Raw Rocktail (x1000)', 1000, 4250, 0, 55, 1, 0, ''),
	(132, 15273, 'Cooked Rocktail (x1000)', 1000, 4794, 0, 55, 1, 0, ''),
	(133, 20429, 'Fury Shark (heals to 99 HP) (x10)', 150, 4079, 0, 55, 1, 0, ''),
	(146, 21773, 'Armadyl Rune (x1000)', 1000, 11589, 0, 55, 1, 0, ''),
	(148, 6920, 'Infinity Boots', 1, 4385, 0, 53, 1, 0, ''),
	(155, 8842, 'Void Knight Gloves', 1, 11011, 0, 53, 1, 0, ''),
	(166, 15349, 'Ardougne cloak 3', 1, 10119, 0, 53, 1, 0, ''),
	(167, 23659, 'TokHaar-Kal', 1, 64274, 0, 53, 1, 0, ''),
	(169, 11852, 'Barrows - karils set', 1, 12390, 0, 53, 1, 0, ''),
	(170, 11854, 'Barrows - torags set', 1, 12390, 0, 53, 1, 0, ''),
	(171, 11856, 'Barrows - veracs set', 1, 12390, 0, 53, 1, 0, ''),
	(172, 21768, 'Barrows - akrisaes set', 1, 13276, 0, 53, 1, 0, ''),
	(175, 11061, 'Ancient Mace', 1, 14649, 0, 53, 1, 0, ''),
	(176, 11698, 'Saradomin godsword', 1, 14060, 0, 53, 1, 0, ''),
	(177, 11700, 'Zamorak godsword', 1, 12060, 0, 53, 1, 0, ''),
	(178, 11730, 'Saradomin sword', 1, 5120, 0, 53, 1, 0, ''),
	(179, 11716, 'Zamorakian spear', 1, 6896, 0, 53, 1, 0, ''),
	(187, 11235, 'Dark Bow', 1, 5100, 0, 53, 1, 0, ''),
	(188, 13879, 'Morrigan\'s javelins (x50)', 50, 1633, 0, 55, 1, 0, ''),
	(189, 13883, 'Morrigan\'s throwin axes (x50)', 50, 1633, 0, 55, 1, 0, ''),
	(190, 20171, 'Zaryte bow', 1, 58500, 0, 53, 1, 0, ''),
	(192, 11212, 'Dragon arrows (x1000)', 1000, 4794, 0, 55, 1, 0, ''),
	(193, 8841, 'Void knight mace', 1, 11264, 0, 53, 1, 0, ''),
	(334, 13864, 'Zuriel\'s hood', 1, 6923, 0, 62, 0, 0, ''),
	(196, 15486, 'Staff of light', 1, 5100, 0, 53, 1, 0, ''),
	(198, 10551, 'Fighter torso', 1, 6264, 0, 53, 1, 0, ''),
	(333, 13861, 'Zuriel\'s robe bottom', 1, 6300, 0, 62, 0, 0, ''),
	(328, 13890, 'Statius\'s platelegs', 1, 6850, 0, 62, 0, 0, ''),
	(330, 13896, 'Statius\'s full helm', 1, 6923, 0, 62, 0, 0, ''),
	(331, 13902, 'Statius\'s warhammer', 1, 8950, 0, 62, 0, 0, ''),
	(332, 13858, 'Zuriel\'s robe top', 1, 6300, 0, 62, 0, 0, ''),
	(214, 8839, 'Void knight top', 1, 11265, 0, 53, 1, 0, ''),
	(346, 15031, 'Commander Boots', 1, 5500, 0, 54, 1, 1, ''),
	(323, 13887, 'Vesta\'s chainbody', 1, 6850, 0, 62, 0, 0, ''),
	(336, 4428, 'Donator Status', 1, 10000, 0, 51, 1, 1, 'Membership Rank'),
	(224, 11663, 'Void Mage Helm', 1, 11264, 0, 53, 1, 0, ''),
	(226, 8840, 'Void knight robe', 1, 11265, 0, 53, 1, 0, ''),
	(312, 299, 'Mithril seeds', 100, 5125, 0, 51, 1, 0, ''),
	(338, 15098, 'Dice (up to 100)', 1, 90000, 0, 51, 1, 0, 'Dice (up to 100)'),
	(337, 15088, 'Dice (2, 6 sides)', 1, 90000, 0, 51, 1, 0, 'Dice (2, 6 sides)'),
	(316, 989, 'Crystal key', 25, 3750, 0, 51, 1, 0, ''),
	(234, 11664, 'Void Ranged Helm', 1, 11264, 0, 53, 1, 0, ''),
	(239, 10548, 'Fighter hat', 1, 3633, 0, 53, 1, 0, ''),
	(240, 11665, 'Void Melee Helm ', 1, 11264, 0, 53, 1, 0, ''),
	(246, 20072, 'Dragon defender', 1, 3265, 0, 53, 1, 0, ''),
	(252, 19712, 'Void Knight Deflector', 1, 13629, 0, 53, 1, 0, ''),
	(251, 6889, 'Mage\'s Book', 1, 5643, 0, 53, 1, 0, ''),
	(254, 18344, 'Scroll of Augury', 1, 12649, 0, 53, 1, 0, ''),
	(255, 18839, 'Scroll of Rigour', 1, 12649, 0, 53, 1, 0, ''),
	(309, 2572, 'Ring of wealth (4)', 1, 4189, 0, 51, 1, 0, ''),
	(258, 10735, 'Scythe', 1, 187000, 0, 54, 1, 2, ''),
	(327, 13884, 'Statius\'s platebody', 1, 6850, 0, 62, 0, 0, ''),
	(324, 13893, 'Vesta\'s plateskirt', 1, 6850, 0, 62, 0, 0, ''),
	(325, 13899, 'Vesta\'s longsword', 1, 7950, 0, 62, 0, 0, ''),
	(326, 13905, 'Vesta\'s spear', 1, 6530, 0, 62, 0, 0, ''),
	(345, 4566, 'Rubber Chicken', 1, 41000, 0, 54, 1, 1, ''),
	(344, 7927, 'Easter Ring', 1, 40000, 0, 54, 1, 1, ''),
	(343, 15100, 'Die (4 sides)', 1, 90000, 0, 51, 1, 0, ''),
	(347, 1037, 'Bunny Ears', 1, 41000, 0, 54, 1, 1, ''),
	(348, 3481, 'Gilded Platebody', 1, 4500, 0, 54, 1, 1, ''),
	(349, 3483, 'Gilded Platelegs', 1, 4500, 0, 54, 1, 1, ''),
	(350, 3485, 'Gilded Plateskirt', 1, 4500, 0, 54, 1, 1, ''),
	(351, 3486, 'Gilded Full Helm', 1, 4500, 0, 54, 1, 1, ''),
	(352, 621, 'Card Flips', 1, 7500, 0, 64, 1, 0, 'Chances for the new web-based mini-game! '),
	(353, 621, 'Card Flips (Enhanced)', 1, 10500, 0, 64, 1, 0, 'Unique flips guarantee better chances for higher tier cards.'),
	(358, 621, '50 x Card Flips', 50, 300000, 100000, 64, 1, 0, 'Card flips! WITH A 20% DISCOUNT!'),
	(354, 4083, 'Sled', 1, 83000, 0, 54, 1, 2, ''),
	(355, 4565, 'Basket of Eggs', 1, 83000, 0, 54, 1, 1, ''),
	(356, 621, '20 x Card Flips', 20, 127500, 0, 64, 1, 0, 'Card Flips WITH A 15% DISCOUNT!'),
	(357, 621, '20 x Card Flips (Enhanced)', 20, 178500, 0, 64, 1, 0, 'Unique card flips! WITH A 15% DISCOUNT!'),
	(360, 621, 'Membership Status', 1, 10000, 0, 65, 0, 0, 'SoulSplit Membership Status?? AWESOME!'),
	(359, 621, '50 x Card Flips (Enhanced)', 50, 420000, 0, 64, 1, 0, 'Unique card flips! WITH A 20% DISCOUNT!');
/*!40000 ALTER TABLE `store_items` ENABLE KEYS */;

-- Dumping structure for table rage_web.store_payments
CREATE TABLE IF NOT EXISTS `store_payments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `pid` int(11) NOT NULL,
  `pname` varchar(255) NOT NULL,
  `paid` float NOT NULL,
  `status` varchar(255) NOT NULL,
  `transId` varchar(255) DEFAULT NULL,
  `date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- Dumping data for table rage_web.store_payments: 0 rows
/*!40000 ALTER TABLE `store_payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `store_payments` ENABLE KEYS */;

-- Dumping structure for table rage_web.store_purchases
CREATE TABLE IF NOT EXISTS `store_purchases` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `total_paid` double NOT NULL,
  `purchases` text NOT NULL,
  `claimed` tinyint(1) NOT NULL DEFAULT '0',
  `date_bought` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rage_web.store_purchases: ~0 rows (approximately)
/*!40000 ALTER TABLE `store_purchases` DISABLE KEYS */;
/*!40000 ALTER TABLE `store_purchases` ENABLE KEYS */;

-- Dumping structure for table rage_web.topic_viewedby
CREATE TABLE IF NOT EXISTS `topic_viewedby` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `tid` int(10) NOT NULL DEFAULT '0',
  `member_id` mediumint(8) NOT NULL DEFAULT '0',
  `dateview` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table rage_web.topic_viewedby: ~0 rows (approximately)
/*!40000 ALTER TABLE `topic_viewedby` DISABLE KEYS */;
/*!40000 ALTER TABLE `topic_viewedby` ENABLE KEYS */;

-- Dumping structure for table rage_web.transfer_logs
CREATE TABLE IF NOT EXISTS `transfer_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender_id` int(11) DEFAULT NULL,
  `sender_name` varchar(255) DEFAULT NULL,
  `receiver_id` int(11) DEFAULT NULL,
  `receiver_name` varchar(255) DEFAULT NULL,
  `amount_sent` int(11) DEFAULT '0',
  `date_sent` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rage_web.transfer_logs: ~0 rows (approximately)
/*!40000 ALTER TABLE `transfer_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `transfer_logs` ENABLE KEYS */;

-- Dumping structure for table rage_web.user_data
CREATE TABLE IF NOT EXISTS `user_data` (
  `user_id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `regular_flips` int(11) NOT NULL,
  `enhanced_flips` int(11) NOT NULL,
  `credits` int(11) NOT NULL DEFAULT '0',
  `discord` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rage_web.user_data: ~1 rows (approximately)
/*!40000 ALTER TABLE `user_data` DISABLE KEYS */;
INSERT INTO `user_data` (`user_id`, `username`, `regular_flips`, `enhanced_flips`, `credits`, `discord`) VALUES
	(1, 'Tamatea', 0, 0, 0, NULL);
/*!40000 ALTER TABLE `user_data` ENABLE KEYS */;

-- Dumping structure for table rage_web.user_data_old
CREATE TABLE IF NOT EXISTS `user_data_old` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `card_keys` int(11) NOT NULL,
  `unique_keys` int(11) NOT NULL,
  `store_credits` int(11) NOT NULL DEFAULT '0',
  `pending_credits` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rage_web.user_data_old: ~0 rows (approximately)
/*!40000 ALTER TABLE `user_data_old` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_data_old` ENABLE KEYS */;

-- Dumping structure for table rage_web.user_sessions
CREATE TABLE IF NOT EXISTS `user_sessions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT '-1',
  `username` varchar(255) DEFAULT NULL,
  `group_id` int(11) NOT NULL DEFAULT '-1',
  `controller` varchar(255) DEFAULT NULL,
  `action` varchar(255) DEFAULT NULL,
  `ip_address` varchar(255) NOT NULL,
  `last_active` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- Dumping data for table rage_web.user_sessions: 1 rows
/*!40000 ALTER TABLE `user_sessions` DISABLE KEYS */;
INSERT INTO `user_sessions` (`id`, `user_id`, `username`, `group_id`, `controller`, `action`, `ip_address`, `last_active`) VALUES
	(1, 1, 'Tamatea', 4, 'index', 'index', '::1', 1578125130);
/*!40000 ALTER TABLE `user_sessions` ENABLE KEYS */;

-- Dumping structure for table rage_web.votes
CREATE TABLE IF NOT EXISTS `votes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `ip_address` varchar(255) DEFAULT NULL,
  `vote_key` varchar(255) NOT NULL,
  `site_id` int(11) NOT NULL,
  `voted_on` int(11) DEFAULT '-1',
  `started_on` int(11) NOT NULL DEFAULT '-1',
  `claimed` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- Dumping data for table rage_web.votes: 0 rows
/*!40000 ALTER TABLE `votes` DISABLE KEYS */;
/*!40000 ALTER TABLE `votes` ENABLE KEYS */;

-- Dumping structure for table rage_web.vote_sites
CREATE TABLE IF NOT EXISTS `vote_sites` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `voteid` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  `visible` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

-- Dumping data for table rage_web.vote_sites: 3 rows
/*!40000 ALTER TABLE `vote_sites` DISABLE KEYS */;
INSERT INTO `vote_sites` (`id`, `title`, `voteid`, `url`, `visible`) VALUES
	(1, 'Rune-Locus', '45600', 'http://www.runelocus.com/top-rsps-list/vote-{id}/?id2={incentive}', 1),
	(8, 'RSPS-List', 'SoulSplitPs', 'http://www.rsps-list.com/index.php?a=in&u={id}&id={incentive}', 1),
	(9, 'TopG', '505673', 'http://topg.org/Runescape/in-{id}-{incentive}', 1);
/*!40000 ALTER TABLE `vote_sites` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
