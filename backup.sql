-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: social_network
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` datetime(6) DEFAULT NULL,
  `modifiedby` varchar(255) DEFAULT NULL,
  `modifieddate` datetime(6) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `post_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs1slvnkuemjsq2kj4h3vhx7i1` (`post_id`),
  KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`),
  CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKs1slvnkuemjsq2kj4h3vhx7i1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,NULL,'2025-02-24 16:06:04.635000',NULL,'2025-02-24 16:06:04.635000','Quá đẹp',2,1),(2,NULL,'2025-02-25 13:37:13.548000',NULL,'2025-02-25 14:46:36.118000','Chỉnh sửa',2,1);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `like_seq`
--

DROP TABLE IF EXISTS `like_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `like_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `like_seq`
--

LOCK TABLES `like_seq` WRITE;
/*!40000 ALTER TABLE `like_seq` DISABLE KEYS */;
INSERT INTO `like_seq` VALUES (1);
/*!40000 ALTER TABLE `like_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `likes`
--

DROP TABLE IF EXISTS `likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `likes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `post_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` datetime(6) DEFAULT NULL,
  `modifiedby` varchar(255) DEFAULT NULL,
  `modifieddate` datetime(6) DEFAULT NULL,
  `story_id` bigint DEFAULT NULL,
  `reel_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKowd6f4s7x9f3w50pvlo6x3b41` (`post_id`),
  KEY `FKljubol38ivojon0a216b7yhu7` (`story_id`),
  KEY `FKe2uwyugawlwn3whxn6xm92ay5` (`reel_id`),
  KEY `FKi2wo4dyk4rok7v4kak8sgkwx0` (`user_id`),
  CONSTRAINT `FKe2uwyugawlwn3whxn6xm92ay5` FOREIGN KEY (`reel_id`) REFERENCES `reel` (`id`),
  CONSTRAINT `FKi2wo4dyk4rok7v4kak8sgkwx0` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKljubol38ivojon0a216b7yhu7` FOREIGN KEY (`story_id`) REFERENCES `story` (`id`),
  CONSTRAINT `FKowd6f4s7x9f3w50pvlo6x3b41` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=129 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `likes`
--

LOCK TABLES `likes` WRITE;
/*!40000 ALTER TABLE `likes` DISABLE KEYS */;
INSERT INTO `likes` VALUES (54,4,1,NULL,NULL,NULL,NULL,NULL,NULL),(55,4,2,NULL,NULL,NULL,NULL,NULL,NULL),(56,3,2,NULL,NULL,NULL,NULL,NULL,NULL),(103,202,2,NULL,NULL,NULL,NULL,NULL,NULL),(106,2,1,NULL,NULL,NULL,NULL,NULL,NULL),(117,202,1,NULL,NULL,NULL,NULL,NULL,NULL),(128,3,1,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `likes_seq`
--

DROP TABLE IF EXISTS `likes_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `likes_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `likes_seq`
--

LOCK TABLES `likes_seq` WRITE;
/*!40000 ALTER TABLE `likes_seq` DISABLE KEYS */;
INSERT INTO `likes_seq` VALUES (201);
/*!40000 ALTER TABLE `likes_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(500) NOT NULL,
  `receiver_id` bigint NOT NULL,
  `sender_id` bigint NOT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` datetime(6) DEFAULT NULL,
  `modifiedby` varchar(255) DEFAULT NULL,
  `modifieddate` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (1,'Duy dep trai qua',2,1,'2025-02-12 16:16:50.953948',NULL,NULL,NULL,NULL),(2,'Đúng rồi',1,2,'2025-02-13 15:03:49.878085',NULL,NULL,NULL,NULL),(3,'Yes sir',2,1,'2025-02-13 15:08:57.948754',NULL,NULL,NULL,NULL),(5,'hello',2,1,'2025-02-13 15:48:34.718003',NULL,NULL,NULL,NULL),(7,'123',2,1,'2025-02-13 16:11:24.226558',NULL,NULL,NULL,NULL),(8,'123',2,1,'2025-02-14 15:43:15.569521',NULL,NULL,NULL,NULL),(9,'Oke bạn',1,2,'2025-02-18 22:18:46.764110',NULL,NULL,NULL,NULL),(10,'Hello',2,1,'2025-02-18 22:19:03.408632',NULL,NULL,NULL,NULL),(11,'123',2,1,'2025-02-18 22:21:34.319107',NULL,NULL,NULL,NULL),(12,'hello',1,3,'2025-02-27 07:22:19.498077',NULL,'2025-02-27 07:22:19.499000',NULL,'2025-02-27 07:22:19.499000'),(13,'hi',3,1,'2025-02-27 07:32:19.553435',NULL,'2025-02-27 07:32:19.563000',NULL,'2025-02-27 07:32:19.563000'),(14,'oke',1,3,'2025-02-27 07:32:54.892205',NULL,'2025-02-27 07:32:54.892000',NULL,'2025-02-27 07:32:54.892000'),(15,'123',3,1,'2025-02-27 07:35:06.437144',NULL,'2025-02-27 07:35:06.437000',NULL,'2025-02-27 07:35:06.437000'),(16,'123',3,1,'2025-02-27 07:36:17.834185',NULL,'2025-02-27 07:36:17.834000',NULL,'2025-02-27 07:36:17.834000'),(17,'allo',1,3,'2025-02-27 07:36:25.977775',NULL,'2025-02-27 07:36:25.978000',NULL,'2025-02-27 07:36:25.978000'),(18,'123',3,1,'2025-02-28 07:04:14.686822',NULL,'2025-02-28 07:04:14.688000',NULL,'2025-02-28 07:04:14.688000'),(19,'123',3,1,'2025-02-28 07:08:33.359551',NULL,'2025-02-28 07:08:33.360000',NULL,'2025-02-28 07:08:33.360000'),(20,'123',3,1,'2025-02-28 07:11:32.463730',NULL,'2025-02-28 07:11:32.463000',NULL,'2025-02-28 07:11:32.463000'),(21,'hello',3,1,'2025-02-28 07:12:26.720284',NULL,'2025-02-28 07:12:26.720000',NULL,'2025-02-28 07:12:26.720000'),(22,'123',3,1,'2025-02-28 07:13:54.139360',NULL,'2025-02-28 07:13:54.140000',NULL,'2025-02-28 07:13:54.140000'),(23,'123',3,1,'2025-02-28 07:14:44.999830',NULL,'2025-02-28 07:14:45.000000',NULL,'2025-02-28 07:14:45.000000'),(24,'123',3,1,'2025-02-28 07:15:55.111500',NULL,'2025-02-28 07:15:55.112000',NULL,'2025-02-28 07:15:55.112000'),(25,'123',3,1,'2025-02-28 07:16:00.335506',NULL,'2025-02-28 07:16:00.335000',NULL,'2025-02-28 07:16:00.335000'),(26,'123',3,1,'2025-02-28 07:19:08.873498',NULL,'2025-02-28 07:19:08.873000',NULL,'2025-02-28 07:19:08.873000'),(27,'123',3,1,'2025-02-28 07:20:27.589634',NULL,'2025-02-28 07:20:27.590000',NULL,'2025-02-28 07:20:27.590000'),(28,'123',3,1,'2025-02-28 07:23:38.633065',NULL,'2025-02-28 07:23:38.633000',NULL,'2025-02-28 07:23:38.633000'),(29,'123',3,1,'2025-02-28 07:23:51.123820',NULL,'2025-02-28 07:23:51.123000',NULL,'2025-02-28 07:23:51.123000'),(30,'hello',3,1,'2025-02-28 07:24:01.760323',NULL,'2025-02-28 07:24:01.760000',NULL,'2025-02-28 07:24:01.760000'),(31,'hello',3,1,'2025-02-28 07:44:23.335269',NULL,'2025-02-28 07:44:23.336000',NULL,'2025-02-28 07:44:23.336000'),(32,'hello',3,1,'2025-02-28 07:45:43.017947',NULL,'2025-02-28 07:45:43.019000',NULL,'2025-02-28 07:45:43.019000'),(33,'hello',3,1,'2025-02-28 07:46:37.372647',NULL,'2025-02-28 07:46:37.372000',NULL,'2025-02-28 07:46:37.372000'),(34,'okee',1,3,'2025-02-28 07:56:36.133967',NULL,'2025-02-28 07:56:36.134000',NULL,'2025-02-28 07:56:36.134000'),(35,'hello',2,1,'2025-02-28 07:58:27.315804',NULL,'2025-02-28 07:58:27.315000',NULL,'2025-02-28 07:58:27.315000');
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `is_post` bit(1) NOT NULL,
  `is_reply` bit(1) NOT NULL,
  `id` bigint NOT NULL,
  `reply_for_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `video` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` datetime(6) DEFAULT NULL,
  `modifiedby` varchar(255) DEFAULT NULL,
  `modifieddate` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpy8yy4h36uoio8lko5jcenf1v` (`reply_for_id`),
  KEY `FK72mt33dhhs48hf9gcqrq4fxte` (`user_id`),
  CONSTRAINT `FK72mt33dhhs48hf9gcqrq4fxte` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKpy8yy4h36uoio8lko5jcenf1v` FOREIGN KEY (`reply_for_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (_binary '',_binary '\0',2,NULL,1,'Duy đẳng cấp quá alo alo alo 123!','http://res.cloudinary.com/dbkgbh9kl/image/upload/10cfce57-9e23-4462-973f-5636d5574867_4.jpg',NULL,'2025-02-11 14:44:35.561231',NULL,NULL,NULL,NULL),(_binary '',_binary '\0',3,NULL,1,'hello','http://res.cloudinary.com/dbkgbh9kl/image/upload/561092d0-fbb7-4399-a514-ef640ccafa34_Screenshot%202024-12-19%20221350.png',NULL,'2025-02-11 14:57:37.194168',NULL,NULL,NULL,NULL),(_binary '',_binary '\0',4,NULL,1,'Tết vui !!!','http://res.cloudinary.com/dbkgbh9kl/image/upload/2e897af7-084a-4e39-a631-ca9b9b1fb5ca_Screenshot%202025-01-14%20133750.png',NULL,'2025-02-11 16:03:33.746365',NULL,NULL,NULL,NULL),(_binary '\0',_binary '',102,4,1,'đẳng cấp quá ',NULL,NULL,'2025-02-12 15:33:23.941760',NULL,NULL,NULL,NULL),(_binary '\0',_binary '',152,4,1,'đẳng cấp quá ',NULL,NULL,'2025-02-12 15:39:42.092607',NULL,NULL,NULL,NULL),(_binary '',_binary '\0',202,NULL,2,'Đẳng cấp quá','http://res.cloudinary.com/dbkgbh9kl/image/upload/dd084196-4e1d-401f-b2d4-14f7e2d8fc3d_Screenshot%202025-02-13%20004053.png',NULL,'2025-02-14 16:18:21.592779',NULL,NULL,NULL,NULL),(_binary '',_binary '\0',252,NULL,1,'Hello1234',NULL,NULL,'2025-02-19 16:32:05.164822',NULL,NULL,NULL,'2025-02-23 05:17:18.647000');
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_comments`
--

DROP TABLE IF EXISTS `post_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_comments` (
  `post_id` bigint NOT NULL,
  `comments_id` bigint NOT NULL,
  UNIQUE KEY `UKgq9be62nx9c9hc0uyhakey771` (`comments_id`),
  KEY `FKmws3vvhl5o4t7r7sajiqpfe0b` (`post_id`),
  CONSTRAINT `FKmws3vvhl5o4t7r7sajiqpfe0b` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FKrvgf8o4dg5kamt01me5gjqodf` FOREIGN KEY (`comments_id`) REFERENCES `comment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_comments`
--

LOCK TABLES `post_comments` WRITE;
/*!40000 ALTER TABLE `post_comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `post_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_re_post_users`
--

DROP TABLE IF EXISTS `post_re_post_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_re_post_users` (
  `post_id` bigint NOT NULL,
  `re_post_users_id` bigint NOT NULL,
  KEY `FKdjxdmowcwb43yv7wdpxlk50b1` (`re_post_users_id`),
  KEY `FKmk18orbfly2n8m2hywyol4yjr` (`post_id`),
  CONSTRAINT `FKdjxdmowcwb43yv7wdpxlk50b1` FOREIGN KEY (`re_post_users_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKmk18orbfly2n8m2hywyol4yjr` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_re_post_users`
--

LOCK TABLES `post_re_post_users` WRITE;
/*!40000 ALTER TABLE `post_re_post_users` DISABLE KEYS */;
INSERT INTO `post_re_post_users` VALUES (202,1);
/*!40000 ALTER TABLE `post_re_post_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_reply_post`
--

DROP TABLE IF EXISTS `post_reply_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_reply_post` (
  `post_id` bigint NOT NULL,
  `reply_post_id` bigint NOT NULL,
  UNIQUE KEY `UKnhqfdj0wcs2gqv63o4g3ccdry` (`reply_post_id`),
  KEY `FKaxp9jfr5rmtsv0wr2weeoyt8m` (`post_id`),
  CONSTRAINT `FK1e150ppgo7edat5w0awmewi0q` FOREIGN KEY (`reply_post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FKaxp9jfr5rmtsv0wr2weeoyt8m` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_reply_post`
--

LOCK TABLES `post_reply_post` WRITE;
/*!40000 ALTER TABLE `post_reply_post` DISABLE KEYS */;
INSERT INTO `post_reply_post` VALUES (4,152),(102,102);
/*!40000 ALTER TABLE `post_reply_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_seq`
--

DROP TABLE IF EXISTS `post_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_seq`
--

LOCK TABLES `post_seq` WRITE;
/*!40000 ALTER TABLE `post_seq` DISABLE KEYS */;
INSERT INTO `post_seq` VALUES (351);
/*!40000 ALTER TABLE `post_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reel`
--

DROP TABLE IF EXISTS `reel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reel` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` datetime(6) DEFAULT NULL,
  `modifiedby` varchar(255) DEFAULT NULL,
  `modifieddate` datetime(6) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKilfe7hcd7lxnyc7hmapuctp2g` (`user_id`),
  CONSTRAINT `FKilfe7hcd7lxnyc7hmapuctp2g` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reel`
--

LOCK TABLES `reel` WRITE;
/*!40000 ALTER TABLE `reel` DISABLE KEYS */;
INSERT INTO `reel` VALUES (1,NULL,'2025-02-27 06:58:51.496000',NULL,'2025-02-27 06:58:51.496000','undefined','2025-02-27 06:58:51.474421','http://res.cloudinary.com/dbkgbh9kl/video/upload/398046fa-46c4-47db-911a-169299411068_253998_tiny.mp4.mp4',_binary '\0',1),(2,NULL,'2025-02-28 06:44:52.079000',NULL,'2025-02-28 06:44:52.079000','undefined','2025-02-28 06:44:52.077931','http://res.cloudinary.com/dbkgbh9kl/image/upload/b84a60b9-c955-472f-b98f-39c679501929_Screenshot%202025-02-20%20153104.png.png',_binary '\0',1);
/*!40000 ALTER TABLE `reel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `story`
--

DROP TABLE IF EXISTS `story`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `story` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` datetime(6) DEFAULT NULL,
  `modifiedby` varchar(255) DEFAULT NULL,
  `modifieddate` datetime(6) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`id`),
  KEY `FKisa0vjg7u7066r4phxuniy6kh` (`user_id`),
  CONSTRAINT `FKisa0vjg7u7066r4phxuniy6kh` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `story`
--

LOCK TABLES `story` WRITE;
/*!40000 ALTER TABLE `story` DISABLE KEYS */;
INSERT INTO `story` VALUES (8,NULL,'2025-02-20 20:25:57.597000',NULL,'2025-02-24 07:28:32.630000','123','http://res.cloudinary.com/dbkgbh9kl/image/upload/9c9c2787-529f-4f8c-b0a1-7aa84009b520_images.jpeg',1,_binary '','2025-02-20 14:55:50.208053'),(9,NULL,'2025-02-20 20:54:03.809000',NULL,'2025-02-24 07:28:32.715000','123','http://res.cloudinary.com/dbkgbh9kl/image/upload/f383ff69-6900-417e-ab55-1a60ab904ff6_5.png',1,_binary '','2025-02-20 14:55:50.208053'),(10,NULL,'2025-02-20 21:03:19.051000',NULL,'2025-02-24 07:28:32.763000','Hello','http://res.cloudinary.com/dbkgbh9kl/image/upload/5fa84763-f510-49b0-b728-c7e685992134_470052838_584543200849909_7752056687164020175_n.png',1,_binary '','2025-02-20 14:55:50.208053'),(11,NULL,'2025-02-27 06:17:15.074000',NULL,'2025-02-28 06:41:47.160000','Hehe','http://res.cloudinary.com/dbkgbh9kl/video/upload/fc9dd19c-6329-4f19-a19b-88ff78c5debf_253998_tiny.mp4.mp4',1,_binary '','2025-02-26 23:17:15.040601');
/*!40000 ALTER TABLE `story` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `login_with_google` bit(1) NOT NULL,
  `req_user` bit(1) NOT NULL,
  `status` bit(1) DEFAULT NULL,
  `ends_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `started_at` datetime(6) DEFAULT NULL,
  `background_image` varchar(255) DEFAULT NULL,
  `bio` varchar(255) DEFAULT NULL,
  `birth_date` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `plan_type` varchar(255) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` datetime(6) DEFAULT NULL,
  `modifiedby` varchar(255) DEFAULT NULL,
  `modifieddate` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (_binary '\0',_binary '\0',_binary '\0',NULL,1,NULL,'','','27/10/2004','nguyenduy123@gmail.com','Nguyễn Duy','','Quảng Trị',NULL,'$2a$10$XmcBAzGHFoNV/MPfe3pC2.w.BP38vV1kyxHib7ND.n82/6dEjRoS2',NULL,'',NULL,NULL,NULL,NULL),(_binary '\0',_binary '\0',_binary '\0',NULL,2,NULL,NULL,NULL,'18/9/2008','nguyenkiet123@gmail.com','Nguyễn Kiệt',NULL,NULL,NULL,'$2a$10$uyLNRQWxd2N7AfpwoSclKO9XLn7klpMZB8j8hardh4vTuifFMxHgq',NULL,NULL,NULL,NULL,NULL,NULL),(_binary '\0',_binary '\0',_binary '\0',NULL,3,NULL,NULL,NULL,'1/1/2025','nguyenvana123@gmail.com','nguyenvana',NULL,NULL,NULL,'$2a$10$YdFc8LRy/GWdoROEAiGRn.MuOLLbn/kG7IygNgtz99vI8QAEMrFCG',NULL,NULL,NULL,'2025-02-27 07:21:06.303000',NULL,'2025-02-27 07:21:06.303000'),(_binary '\0',_binary '\0',_binary '\0',NULL,4,NULL,NULL,NULL,'27/10/2004','bogiaoffline@gmail.com','Nguyen Duy',NULL,NULL,NULL,'$2a$10$KChbSwuV7AZN2f3g.D5faOPwdHcEZnuQzw8ZkjqdjjBkXq7FWUuwq',NULL,NULL,NULL,'2025-03-11 21:45:04.824000',NULL,'2025-03-11 22:22:34.770000');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_followers`
--

DROP TABLE IF EXISTS `user_followers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_followers` (
  `followers_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FKokc5w6fibhnthvwnxjxyrlfc1` (`user_id`),
  KEY `FKpvkdr9tjpc96kdwe7591oixnj` (`followers_id`),
  CONSTRAINT `FKokc5w6fibhnthvwnxjxyrlfc1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKpvkdr9tjpc96kdwe7591oixnj` FOREIGN KEY (`followers_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_followers`
--

LOCK TABLES `user_followers` WRITE;
/*!40000 ALTER TABLE `user_followers` DISABLE KEYS */;
INSERT INTO `user_followers` VALUES (1,2),(1,3);
/*!40000 ALTER TABLE `user_followers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_followings`
--

DROP TABLE IF EXISTS `user_followings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_followings` (
  `followings_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FKj2a8435v8kbuogf5d8aaudfrp` (`user_id`),
  KEY `FK1f1kxtjhmrvlvrhqmuwf9r7ls` (`followings_id`),
  CONSTRAINT `FK1f1kxtjhmrvlvrhqmuwf9r7ls` FOREIGN KEY (`followings_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKj2a8435v8kbuogf5d8aaudfrp` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_followings`
--

LOCK TABLES `user_followings` WRITE;
/*!40000 ALTER TABLE `user_followings` DISABLE KEYS */;
INSERT INTO `user_followings` VALUES (2,1),(3,1);
/*!40000 ALTER TABLE `user_followings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_like_seq`
--

DROP TABLE IF EXISTS `user_like_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_like_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_like_seq`
--

LOCK TABLES `user_like_seq` WRITE;
/*!40000 ALTER TABLE `user_like_seq` DISABLE KEYS */;
INSERT INTO `user_like_seq` VALUES (1);
/*!40000 ALTER TABLE `user_like_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_seq`
--

DROP TABLE IF EXISTS `user_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_seq`
--

LOCK TABLES `user_seq` WRITE;
/*!40000 ALTER TABLE `user_seq` DISABLE KEYS */;
INSERT INTO `user_seq` VALUES (101);
/*!40000 ALTER TABLE `user_seq` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-12 22:30:04
