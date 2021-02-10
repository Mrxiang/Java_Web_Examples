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

CREATE DATABASE IF NOT EXISTS db_netExam;
USE db_netExam;

DROP TABLE IF EXISTS `tb_student`;
CREATE TABLE `tb_student` (
  `id`  bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar (20) ,
  `pwd` varchar(20) ,
  `sex` varchar(20) ,
  `joinTime` datetime(6) ,
  `question` varchar(50) ,
  `answer` varchar(50) ,
  `profession` varchar (50) ,
  `cardNo` varchar(20),
  PRIMARY KEY (`id`)
) ;

DROP TABLE IF EXISTS `tb_taoTi`;
CREATE TABLE `tb_taoTi`
(
    `id`         bigint(20) NOT NULL AUTO_INCREMENT,
    `Name`       varchar(50),
    `LessonId`   int(20),
    `joinTime`   datetime(6),
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `tb_questions`;
CREATE TABLE `tb_questions`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `subject`       varchar(50),
    `type`          varchar(20),
    `joinTime`      datetime(6),
    `lessonId`      int(20),
    `taoTiId`       int(20),
    `optionA`       varchar(50),
    `optionB`       varchar(50),
    `optionC`       varchar(50),
    `optionD`       varchar(50),
    `answer`        varchar(50),
    `note`          varchar(50),
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `tb_stuResult`;
CREATE TABLE `tb_stuResult`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `stuId`         varchar(50),
    `whichLesson`   varchar(20),
    `resSingle`     int(8),
    `resMore`       int(8),
    `resTotal`      int(8),
    `joinTime`      datetime(6),
    PRIMARY KEY (`id`)
);