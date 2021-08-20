DROP DATABASE IF EXISTS `challenge`;
CREATE DATABASE `challenge`;
USE `challenge`;


DROP TABLE IF EXISTS `cav`;
CREATE TABLE `cav` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `open_hours` varchar(500) NOT NULL,
  PRIMARY KEY (`id`)
);


DROP TABLE IF EXISTS `car`;
CREATE TABLE `car` (
  `id` int NOT NULL AUTO_INCREMENT,
  `brand` varchar(30) NOT NULL,
  `model` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
);


DROP TABLE IF EXISTS `calendar`;
CREATE TABLE `calendar` (
  `id` int NOT NULL AUTO_INCREMENT,
  `day` date NOT NULL,
  `hour` int NOT NULL,
  `cav_id` int NOT NULL,
  `car_id` int NOT NULL,
  `type` varchar(15) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cav_id` (`cav_id`),
  KEY `car_id` (`car_id`),
  CONSTRAINT `calendar_ibfk_1` FOREIGN KEY (`cav_id`) REFERENCES `cav` (`id`) ON DELETE CASCADE,
  CONSTRAINT `calendar_ibfk_2` FOREIGN KEY (`car_id`) REFERENCES `car` (`id`) ON DELETE CASCADE,
  UNIQUE (day, hour, cav_id, type)
);


INSERT INTO `cav` (`id`, `name`, `open_hours`) VALUES
(1,	'Botafogo',	'{\"monday\":{\"begin\":10, \"end\":17}, \"tuesday\":{\"begin\":10, \"end\":17}, \"wednesday\":{\"begin\":10, \"end\":17}, \"thursday\":{\"begin\":10, \"end\":17}, \"friday\":{\"begin\":10, \"end\":17}, \"saturday\":{\"begin\":10, \"end\":15}, \"domingo\":{} }'),
(2,	'Barra da Tijuca',	'{\"monday\":{\"begin\":10, \"end\":17}, \"tuesday\":{\"begin\":10, \"end\":17}, \"wednesday\":{\"begin\":10, \"end\":17}, \"thursday\":{\"begin\":10, \"end\":17}, \"friday\":{\"begin\":10, \"end\":17}, \"saturday\":{\"begin\":10, \"end\":15}, \"domingo\":{} }'),
(3,	'Norte Shopping',	'{\"monday\":{\"begin\":10, \"end\":17}, \"tuesday\":{\"begin\":10, \"end\":17}, \"wednesday\":{\"begin\":10, \"end\":17}, \"thursday\":{\"begin\":10, \"end\":17}, \"friday\":{\"begin\":10, \"end\":17}, \"saturday\":{\"begin\":10, \"end\":15}, \"domingo\":{} }');


INSERT INTO `car` (`id`, `brand`, `model`) VALUES
(1,	'VW',	'Golf'),
(2,	'Ford',	'Fiesta'),
(3,	'GM',	'Cruze'),
(4,	'GM',	'Cobalt'),
(5,	'GM',	'Cobalt'),
(6,	'Fiat',	'Palio'),
(7,	'GVW',	'Up');


INSERT INTO `calendar` (`id`, `day`, `hour`, `cav_id`, `car_id`, `type`) VALUES
(1,	'2019-07-17',	11,	1,	1,	'VISIT'),
(2,	'2019-07-17',	14,	1,	7,	'VISIT'),
(3,	'2019-07-17',	11,	1,	7,	'INSPECTION'),
(4,	'2019-07-17',	11,	3,	2,	'INSPECTION'),
(5,	'2019-07-17',	11,	2,	3,	'VISIT'),
(6,	'2019-07-17',	10,	2,	3,	'INSPECTION'),
(7,	'2019-07-17',	11,	2,	4,	'INSPECTION'),
(8,	'2019-07-17',	12,	2,	5,	'INSPECTION'),
(9,	'2019-07-18',	10,	3,	2,	'VISIT'),
(10,	'2019-07-18',	14,	3,	2,	'VISIT');


