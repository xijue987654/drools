/*
 Navicat Premium Data Transfer

 Source Server         : SIT
 Source Server Type    : MySQL
 Source Server Version : 80013
 Source Host           : 10.4.170.53:3308
 Source Schema         : gopher_pd

 Target Server Type    : MySQL
 Target Server Version : 80013
 File Encoding         : 65001

 Date: 20/07/2020 09:40:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rule
-- ----------------------------
DROP TABLE IF EXISTS `rule`;
CREATE TABLE `rule`  (
  `rid` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `id` varchar(36) DEFAULT NULL,
  `add_by` varchar(36) DEFAULT NULL COMMENT '域账号',
  `add_time` datetime(0) DEFAULT NULL COMMENT '添加日期',
  `updated_by` varchar(36) DEFAULT NULL COMMENT '域账号',
  `updated_time` datetime(0) DEFAULT NULL COMMENT '最后更新日期',
  `is_deleted` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `rule_name` varchar(100) DEFAULT NULL,
  `rule_detail` varchar(2000) DEFAULT NULL,
  `package_name` varchar(255) DEFAULT NULL,
  `visible` int(20) NULL DEFAULT 1,
  PRIMARY KEY (`rid`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Records of rule
-- ----------------------------
INSERT INTO `rule` VALUES (1, '13CAB608A6FC459EBC9BFACB08246441', 'sys', '2020-07-16 15:26:07', 'sys', '2020-07-16 15:26:07', 0, 'birthdayPoint', 'package droolRule\r\n\r\nimport com.noahgroup.drools.entity.PointDomain;\r\n\r\nrule birthdayPoint\r\n// 过生日，则加10分，并且将当月交易比数翻倍后再计算积分\r\nsalience 100\r\nlock-on-active true\r\nwhen\r\n    $pointDomain : PointDomain(birthDay == true)\r\nthen\r\n    $pointDomain.setPoint($pointDomain.getPoint()+10);\r\n    $pointDomain.setBuyNums($pointDomain.getBuyNums()*2);\r\n    $pointDomain.setBuyMoney($pointDomain.getBuyMoney()*2);\r\n    $pointDomain.setBillThisMonth($pointDomain.getBillThisMonth()*2);\r\n    $pointDomain.recordPointLog($pointDomain.getUserName(),\"birthdayPoint\");\r\nend', 'droolRule', 1);
INSERT INTO `rule` VALUES (2, 'F811E24F21DD4181B3C17DD14210F615', 'sys', '2020-07-16 15:26:58', 'sys', '2020-07-16 15:26:58', 0, 'billThisMonthPoint', 'package droolRule;\r\nimport com.noahgroup.drools.entity.PointDomain;\r\n\r\nrule billThisMonthPoint\r\n// 2020-04-08 – 2020-08-08每月信用卡还款3次以上，每满3笔赠送30分\r\nsalience 99\r\nlock-on-active true\r\ndate-effective \"2020-04-08 23:59:59\"\r\ndate-expires \"2020-08-08 23:59:59\"\r\nwhen\r\n    $pointDomain : PointDomain(billThisMonth >= 3)\r\nthen\r\n    $pointDomain.setPoint($pointDomain.getPoint()+$pointDomain.getBillThisMonth()/3*30);\r\n    $pointDomain.recordPointLog($pointDomain.getUserName(),\"billThisMonthPoint\");\r\nend', 'droolRule', 1);
INSERT INTO `rule` VALUES (3, 'F14F8A3D8CD944719B51A1447184669E', 'sys', '2020-07-16 15:27:51', 'sys', '2020-07-16 15:27:51', 0, 'buyMoneyPoint', 'package droolRule\r\n\r\nimport com.noahgroup.drools.entity.PointDomain;\r\n\r\nrule buyMoneyPoint\r\n// 当月购物总金额100以上，每100元赠送10分\r\nsalience 98\r\nlock-on-active true\r\nwhen\r\n    $pointDomain : PointDomain(buyMoney >= 100)\r\nthen\r\n    $pointDomain.setPoint($pointDomain.getPoint()+ (int)$pointDomain.getBuyMoney()/100 * 10);\r\n    $pointDomain.recordPointLog($pointDomain.getUserName(),\"buyMoneyPoint\");\r\nend', 'droolRule', 1);
INSERT INTO `rule` VALUES (4, '0CA29A04553749E8935D4FAE946794BA', 'sys', '2020-07-16 15:28:32', 'sys', '2020-07-16 15:28:32', 0, 'buyNumsPoint', 'package droolRule\r\n\r\nimport com.noahgroup.drools.entity.PointDomain;\r\n\r\nrule buyNumsPoint\r\n// 当月购物次数5次以上，每五次赠送50分\r\nsalience 97\r\nlock-on-active true\r\nwhen\r\n    $pointDomain : PointDomain(buyNums >= 5)\r\nthen\r\n    $pointDomain.setPoint($pointDomain.getPoint()+$pointDomain.getBuyNums()/5 * 50);\r\n    $pointDomain.recordPointLog($pointDomain.getUserName(),\"buyNumsPoint\");\r\nend', 'droolRule', 1);
INSERT INTO `rule` VALUES (5, 'AD2DAC523B85443FA997E83D7DE16B75', 'sys', '2020-07-16 15:28:58', 'sys', '2020-07-16 15:28:58', 0, 'allFitPoint', 'package droolRule\r\n\r\nimport com.noahgroup.drools.entity.PointDomain;\r\n\r\nrule allFitPoint\r\n// 特别的，如果全部满足了要求，则额外奖励100分\r\nsalience 96\r\nlock-on-active true\r\nwhen\r\n    $pointDomain:PointDomain(buyNums >= 5 && billThisMonth >= 3 && buyMoney >= 100)\r\nthen\r\n    $pointDomain.setPoint($pointDomain.getPoint()+ 100);\r\n    $pointDomain.recordPointLog($pointDomain.getUserName(),\"allFitPoint\");\r\nend', 'droolRule', 1);
INSERT INTO `rule` VALUES (6, '9A36482E5B3F4E939B061E8932B40EC3', 'sys', '2020-07-16 15:29:46', 'sys', '2020-07-16 15:29:46', 0, 'subBackNumsPoint', 'package droolRule\r\n\r\nimport com.noahgroup.drools.entity.PointDomain;\r\n\r\nrule subBackNumsPoint\r\n// 发生退货，扣减10分\r\nsalience 10\r\nlock-on-active true\r\nwhen\r\n    $pointDomain: PointDomain(backNums >= 1)\r\nthen\r\n    $pointDomain.setPoint($pointDomain.getPoint()-10);\r\n    $pointDomain.recordPointLog($pointDomain.getUserName(),\"subBackNumsPoint\");\r\nend', 'droolRule', 1);
INSERT INTO `rule` VALUES (7, '7A561F2E0F594976B58F8B7EDA487E4A', 'sys', '2020-07-16 15:30:05', 'sys', '2020-07-16 15:30:05', 0, 'subBackMondyPoint', 'package droolRule\r\n\r\nimport com.noahgroup.drools.entity.PointDomain;\r\n\r\nrule subBackMondyPoint\r\n// 退货金额大于100，扣减10分\r\nsalience 9\r\nlock-on-active true\r\nwhen\r\n    $pointDomain: PointDomain(backMondy >= 100)\r\nthen\r\n    $pointDomain.setPoint($pointDomain.getPoint()-10);\r\n    $pointDomain.recordPointLog($pointDomain.getUserName(),\"subBackMondyPoint\");\r\nend', 'droolRule', 1);

SET FOREIGN_KEY_CHECKS = 1;
