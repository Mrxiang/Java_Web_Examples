-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.1.40-community


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema db_wlgl
--

CREATE DATABASE IF NOT EXISTS db_CityInfo;
USE db_CityInfo;

DROP TABLE IF EXISTS `tb_info`;
CREATE TABLE `tb_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `info_type` int(10) ,
  `info_title` varchar(80) ,
  `info_content` varchar(1000) ,
  `info_linkman` varchar(50) ,
  `info_phone` varchar(100) ,
  `info_email` varchar(100) ,
  `info_date` datetime(6) ,
  `info_state` varchar(1) default 0,
  `info_payfor` varchar(1) default 0,
  PRIMARY KEY (`id`)

) ;

DROP TABLE IF EXISTS `tb_type`;
CREATE TABLE `tb_type`
(
    `id`         int(10) NOT NULL AUTO_INCREMENT,
    `type_sign`  int(10),
    `type_name`  varchar(20),
    `type_intro` varchar(20),
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`
(
    `id`            int(10) NOT NULL AUTO_INCREMENT,
    `user_name`     varchar(20),
    `user_password` varchar(20),
    PRIMARY KEY (`id`)
);