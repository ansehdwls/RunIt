-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: run_it
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attendance` (
  `created_at` date NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK46cuxphi3uh5quom51s6i2q8x` (`user_id`),
  CONSTRAINT `FK46cuxphi3uh5quom51s6i2q8x` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendance`
--

LOCK TABLES `attendance` WRITE;
/*!40000 ALTER TABLE `attendance` DISABLE KEYS */;
INSERT INTO `attendance` VALUES ('2024-11-18',1,1),('2024-11-18',2,2),('2024-11-18',3,3);
/*!40000 ALTER TABLE `attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `experience`
--

DROP TABLE IF EXISTS `experience`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `experience` (
  `changed` bigint NOT NULL,
  `create_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `activity` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_create_at` (`create_at`),
  KEY `FK41lup37auw1bvwwqpgn0blbic` (`user_id`),
  CONSTRAINT `FK41lup37auw1bvwwqpgn0blbic` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `experience`
--

LOCK TABLES `experience` WRITE;
/*!40000 ALTER TABLE `experience` DISABLE KEYS */;
INSERT INTO `experience` VALUES (10,'2024-11-18 10:47:45.089478',1,1,'출석'),(10,'2024-11-18 10:47:45.097472',2,1,'거리'),(10,'2024-11-18 10:48:53.424827',3,2,'출석'),(20,'2024-11-18 10:48:53.432798',4,2,'거리'),(10,'2024-11-18 10:57:18.775566',5,1,'거리'),(10,'2024-11-18 10:58:46.792976',6,3,'출석'),(10,'2024-11-18 10:58:46.797477',7,3,'거리'),(10,'2024-11-18 10:59:13.649546',8,3,'거리'),(10,'2024-11-18 10:59:34.745095',9,3,'거리');
/*!40000 ALTER TABLE `experience` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group`
--

DROP TABLE IF EXISTS `group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `league_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4wcfwf6m0974qq891i9dh74i4` (`league_id`),
  CONSTRAINT `FK4wcfwf6m0974qq891i9dh74i4` FOREIGN KEY (`league_id`) REFERENCES `league` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group`
--

LOCK TABLES `group` WRITE;
/*!40000 ALTER TABLE `group` DISABLE KEYS */;
INSERT INTO `group` VALUES (1,1);
/*!40000 ALTER TABLE `group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `league`
--

DROP TABLE IF EXISTS `league`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `league` (
  `league_rank` int NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `league_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `league`
--

LOCK TABLES `league` WRITE;
/*!40000 ALTER TABLE `league` DISABLE KEYS */;
INSERT INTO `league` VALUES (1,1,'알'),(2,2,'나무늘보'),(3,3,'거북이'),(4,4,'토끼'),(5,5,'말'),(6,6,'치타');
/*!40000 ALTER TABLE `league` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `league_summary`
--

DROP TABLE IF EXISTS `league_summary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `league_summary` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `league_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf1jb3t9detgoc8yf6on4wa2q4` (`league_id`),
  CONSTRAINT `FKf1jb3t9detgoc8yf6on4wa2q4` FOREIGN KEY (`league_id`) REFERENCES `league` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `league_summary`
--

LOCK TABLES `league_summary` WRITE;
/*!40000 ALTER TABLE `league_summary` DISABLE KEYS */;
/*!40000 ALTER TABLE `league_summary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `league_summary_advanced_users`
--

DROP TABLE IF EXISTS `league_summary_advanced_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `league_summary_advanced_users` (
  `league_summary_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`league_summary_id`,`user_id`),
  KEY `FKqqxsyb22m7ktdqvpj80rpc897` (`user_id`),
  CONSTRAINT `FKpn70v86ji4gykuox6jummsfe3` FOREIGN KEY (`league_summary_id`) REFERENCES `league_summary` (`id`),
  CONSTRAINT `FKqqxsyb22m7ktdqvpj80rpc897` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `league_summary_advanced_users`
--

LOCK TABLES `league_summary_advanced_users` WRITE;
/*!40000 ALTER TABLE `league_summary_advanced_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `league_summary_advanced_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `league_summary_degraded_users`
--

DROP TABLE IF EXISTS `league_summary_degraded_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `league_summary_degraded_users` (
  `league_summary_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`league_summary_id`,`user_id`),
  KEY `FKbrsjxciujxxav1ohie9e1g134` (`user_id`),
  CONSTRAINT `FK1y7auh22xmbht8p7uy88f39wx` FOREIGN KEY (`league_summary_id`) REFERENCES `league_summary` (`id`),
  CONSTRAINT `FKbrsjxciujxxav1ohie9e1g134` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `league_summary_degraded_users`
--

LOCK TABLES `league_summary_degraded_users` WRITE;
/*!40000 ALTER TABLE `league_summary_degraded_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `league_summary_degraded_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `league_summary_wait_users`
--

DROP TABLE IF EXISTS `league_summary_wait_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `league_summary_wait_users` (
  `league_summary_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`league_summary_id`,`user_id`),
  KEY `FK9plcf11i4n86r95wfjxebhf1m` (`user_id`),
  CONSTRAINT `FK9plcf11i4n86r95wfjxebhf1m` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKlb7jad9rvugciwhuxr54yxby4` FOREIGN KEY (`league_summary_id`) REFERENCES `league_summary` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `league_summary_wait_users`
--

LOCK TABLES `league_summary_wait_users` WRITE;
/*!40000 ALTER TABLE `league_summary_wait_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `league_summary_wait_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `record`
--

DROP TABLE IF EXISTS `record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `record` (
  `bpm` int DEFAULT NULL,
  `distance` double DEFAULT NULL,
  `duration` int DEFAULT NULL,
  `is_practice` bit(1) DEFAULT NULL,
  `end_time` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `start_time` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKeny3549xar8rnrcmdw3hl0la1` (`user_id`),
  CONSTRAINT `FKeny3549xar8rnrcmdw3hl0la1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `record`
--

LOCK TABLES `record` WRITE;
/*!40000 ALTER TABLE `record` DISABLE KEYS */;
INSERT INTO `record` VALUES (0,1,300,_binary '\0','2024-11-18 01:41:08.642000',1,'2024-11-18 01:36:08.642000',1),(0,2,300,_binary '\0','2024-11-18 01:41:08.642000',2,'2024-11-18 01:36:08.642000',2),(0,1,30,_binary '\0','2024-11-18 01:37:08.642000',3,'2024-11-18 01:36:08.642000',1),(0,1,30,_binary '\0','2024-11-18 01:41:08.642000',4,'2024-11-18 01:36:08.642000',3),(0,1,30,_binary '\0','2024-11-18 01:41:08.642000',5,'2024-11-18 01:36:08.642000',3),(0,1,30,_binary '\0','2024-11-18 01:41:08.642000',6,'2024-11-18 01:36:08.642000',3);
/*!40000 ALTER TABLE `record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `split`
--

DROP TABLE IF EXISTS `split`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `split` (
  `bpm` int DEFAULT NULL,
  `pace` int DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbcmx4sami9ng6a2gfkwb20xp8` (`record_id`),
  CONSTRAINT `FKbcmx4sami9ng6a2gfkwb20xp8` FOREIGN KEY (`record_id`) REFERENCES `record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `split`
--

LOCK TABLES `split` WRITE;
/*!40000 ALTER TABLE `split` DISABLE KEYS */;
INSERT INTO `split` VALUES (0,5,1,1),(0,5,2,2),(0,5,3,3),(0,5,4,4),(0,5,5,5),(0,5,6,6);
/*!40000 ALTER TABLE `split` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `track`
--

DROP TABLE IF EXISTS `track`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `track` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` bigint DEFAULT NULL,
  `track_image_url` varchar(255) DEFAULT NULL,
  `path` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8y32j5sldkagjiijsvdvaquak` (`record_id`),
  CONSTRAINT `FKnmwns5ojwdsfat5jqyp2ncng3` FOREIGN KEY (`record_id`) REFERENCES `record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `track`
--

LOCK TABLES `track` WRITE;
/*!40000 ALTER TABLE `track` DISABLE KEYS */;
INSERT INTO `track` VALUES (1,1,'https://project-runit.s3.ap-northeast-2.amazonaws.com/1%3A1%EC%BA%A1%EC%B2%98.PNG','string'),(2,2,'https://project-runit.s3.ap-northeast-2.amazonaws.com/2%3A2%EC%BA%A1%EC%B2%98.PNG','string'),(3,3,'https://project-runit.s3.ap-northeast-2.amazonaws.com/1%3A3%EC%BA%A1%EC%B2%98.PNG','string'),(4,4,'https://project-runit.s3.ap-northeast-2.amazonaws.com/3%3A4%EC%BA%A1%EC%B2%98.PNG','string'),(5,5,'https://project-runit.s3.ap-northeast-2.amazonaws.com/3%3A5%EC%BA%A1%EC%B2%98.PNG','string'),(6,6,'https://project-runit.s3.ap-northeast-2.amazonaws.com/3%3A6%EC%BA%A1%EC%B2%98.PNG','string');
/*!40000 ALTER TABLE `track` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `group_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fcm_token` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `user_number` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6iba98lx96y1gpj6rkfm2onnt` (`group_id`),
  CONSTRAINT `FK6iba98lx96y1gpj6rkfm2onnt` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,1,NULL,'string','test_1','1'),(1,2,NULL,'string','test_2','2'),(1,3,NULL,'string','test_3','3'),(1,4,NULL,'string','test_4','4'),(1,5,NULL,'string','test_5','5'),(1,6,NULL,'string','test_6','6'),(1,7,NULL,'string','test_7','7');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-19 10:09:02
