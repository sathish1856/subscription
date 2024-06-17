-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: subscriptiondb
-- ------------------------------------------------------
-- Server version	8.0.37

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
-- Table structure for table `hotel`
--

USE subscriptiondb;

DROP TABLE IF EXISTS `hotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hotel` (
  `hotelid` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `hotel_name` varchar(255) NOT NULL,
  `state` varchar(255) DEFAULT NULL,
  `zip_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`hotelid`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotel`
--

LOCK TABLES `hotel` WRITE;
/*!40000 ALTER TABLE `hotel` DISABLE KEYS */;
INSERT INTO `hotel` VALUES (1,'123 Main St','Los Angeles','USA','2024-01-01 00:00:00.000000','Hotel California','CA','90001'),(2,'123 Main St','Frankurt','Germany',NULL,'Hotel German','CA','90001'),(3,'123 Main St','Frankurt','Germany',NULL,'Hotel Europe','CA','90001'),(4,'456 Main St','Frankurt','Germany',NULL,'Hotel Austria','CA','90001'),(5,'456 Main St','Frankurt','Germany',NULL,'Hotel UK','CA','12345'),(6,'456 Main St','Frankurt','Germany',NULL,'Hotel Luxomberg','CA','6677'),(7,'456 Main St','Berlin','Germany',NULL,'Hotel Berlin','CA','6677'),(8,'456 Main St','Berlin','Germany',NULL,'Hotel Bevaria','CA','456456'),(9,'456 Main St','dellort','Austria',NULL,'Hotel Denmark','CA','456456'),(10,'456 Main St','dellort','Austria',NULL,'Hotel Sathish','CA','456456');
/*!40000 ALTER TABLE `hotel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscription`
--

DROP TABLE IF EXISTS `subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subscription` (
  `subscriptionid` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `next_payment` date NOT NULL,
  `start_date` date NOT NULL,
  `status` enum('ACTIVE','CANCELED','EXPIRED') NOT NULL,
  `term` enum('MONTHLY','YEARLY') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `hotelid` bigint NOT NULL,
  PRIMARY KEY (`subscriptionid`),
  KEY `FK5o7ldewiq1m1aury9ipoej351` (`hotelid`),
  CONSTRAINT `FK5o7ldewiq1m1aury9ipoej351` FOREIGN KEY (`hotelid`) REFERENCES `hotel` (`hotelid`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscription`
--

LOCK TABLES `subscription` WRITE;
/*!40000 ALTER TABLE `subscription` DISABLE KEYS */;
INSERT INTO `subscription` VALUES (1,NULL,NULL,'2024-08-01','2024-07-01','ACTIVE','MONTHLY','2024-06-16 19:48:30.471056',1),(2,NULL,'2024-06-16','2024-08-01','2024-07-01','ACTIVE','MONTHLY','2024-06-16 18:55:31.097452',2),(3,NULL,NULL,'2025-07-01','2024-07-01','ACTIVE','MONTHLY','2024-06-16 19:11:38.278162',3),(4,NULL,NULL,'2024-08-01','2024-07-01','EXPIRED','MONTHLY',NULL,4),(5,'2024-06-16 00:51:12.831248',NULL,'2024-08-01','2024-07-01','ACTIVE','MONTHLY','2024-06-16 00:51:12.831248',5),(6,'2024-06-16 08:55:10.141867',NULL,'2024-08-01','2024-07-01','ACTIVE','MONTHLY','2024-06-16 08:55:10.141867',6),(7,'2024-06-16 09:31:55.348023',NULL,'2024-08-01','2024-07-01','ACTIVE','MONTHLY','2024-06-16 09:31:55.348023',7),(8,'2024-06-16 11:00:17.630359','2024-06-16','2024-09-01','2024-07-01','CANCELED','MONTHLY','2024-06-16 11:01:36.246723',8),(10,'2024-06-16 12:28:15.849951',NULL,'2025-06-16','2024-06-16','ACTIVE','YEARLY','2024-06-16 12:28:15.850249',9),(12,'2024-06-16 18:45:35.866727','2024-06-16','2029-06-27','2024-06-27','CANCELED','YEARLY','2024-06-16 19:25:13.043094',10);
/*!40000 ALTER TABLE `subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscription_audit`
--

DROP TABLE IF EXISTS `subscription_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subscription_audit` (
  `auditid` bigint NOT NULL AUTO_INCREMENT,
  `change_date` datetime(6) DEFAULT NULL,
  `field_changed` varchar(255) NOT NULL,
  `new_value` varchar(255) DEFAULT NULL,
  `old_value` varchar(255) DEFAULT NULL,
  `subscriptionid` bigint NOT NULL,
  PRIMARY KEY (`auditid`),
  KEY `FK1lgv4qphgfbwqlnu3j9ted48f` (`subscriptionid`),
  CONSTRAINT `FK1lgv4qphgfbwqlnu3j9ted48f` FOREIGN KEY (`subscriptionid`) REFERENCES `subscription` (`subscriptionid`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscription_audit`
--

LOCK TABLES `subscription_audit` WRITE;
/*!40000 ALTER TABLE `subscription_audit` DISABLE KEYS */;
INSERT INTO `subscription_audit` VALUES (1,'2024-06-16 08:55:10.162877','status','ACTIVE',NULL,6),(2,'2024-06-16 09:18:06.999211','status','ACTIVE','ACTIVE',1),(3,'2024-06-16 09:18:13.287660','status','ACTIVE','ACTIVE',1),(4,'2024-06-16 09:18:14.793567','status','ACTIVE','ACTIVE',1),(5,'2024-06-16 09:31:55.369978','status','ACTIVE',NULL,7),(6,'2024-06-16 11:00:17.641407','status','ACTIVE',NULL,8),(7,'2024-06-16 11:00:51.668170','status','ACTIVE','ACTIVE',8),(8,'2024-06-16 11:01:36.253792','status','CANCELED','ACTIVE',8),(10,'2024-06-16 12:28:15.863101','status','ACTIVE',NULL,10),(11,'2024-06-16 17:11:30.515922','status','CANCELED','ACTIVE',1),(12,'2024-06-16 18:02:46.208602','status','ACTIVE','CANCELED',1),(13,'2024-06-16 18:02:58.782834','status','CANCELED','ACTIVE',1),(15,'2024-06-16 18:16:05.435783','status','CANCELED','ACTIVE',2),(17,'2024-06-16 18:45:35.880487','status','ACTIVE',NULL,12),(19,'2024-06-16 18:55:31.144042','status','ACTIVE',NULL,2),(20,'2024-06-16 19:11:27.624875','status','ACTIVE','ACTIVE',3),(21,'2024-06-16 19:11:30.624721','status','ACTIVE','ACTIVE',3),(22,'2024-06-16 19:11:31.422162','status','ACTIVE','ACTIVE',3),(23,'2024-06-16 19:11:32.381176','status','ACTIVE','ACTIVE',3),(24,'2024-06-16 19:11:32.994213','status','ACTIVE','ACTIVE',3),(25,'2024-06-16 19:11:34.045222','status','ACTIVE','ACTIVE',3),(26,'2024-06-16 19:11:37.569566','status','ACTIVE','ACTIVE',3),(27,'2024-06-16 19:11:37.745074','status','ACTIVE','ACTIVE',3),(28,'2024-06-16 19:11:37.904559','status','ACTIVE','ACTIVE',3),(29,'2024-06-16 19:11:38.065280','status','ACTIVE','ACTIVE',3),(30,'2024-06-16 19:11:38.283923','status','ACTIVE','ACTIVE',3),(31,'2024-06-16 19:24:10.909085','status','CANCELED','ACTIVE',12),(32,'2024-06-16 19:24:22.173187','status','ACTIVE','CANCELED',12),(33,'2024-06-16 19:24:40.465499','status','CANCELED','ACTIVE',12),(34,'2024-06-16 19:24:46.371808','status','ACTIVE','CANCELED',12),(35,'2024-06-16 19:25:09.792599','status','CANCELED','ACTIVE',12),(36,'2024-06-16 19:25:10.861001','status','ACTIVE','CANCELED',12),(37,'2024-06-16 19:25:11.709334','status','CANCELED','ACTIVE',12),(38,'2024-06-16 19:25:12.455474','status','ACTIVE','CANCELED',12),(39,'2024-06-16 19:25:13.059280','status','CANCELED','ACTIVE',12),(40,'2024-06-16 19:48:21.902437','status','ACTIVE','CANCELED',1),(41,'2024-06-16 19:48:24.262412','status','CANCELED','ACTIVE',1),(42,'2024-06-16 19:48:25.118212','status','ACTIVE','CANCELED',1),(43,'2024-06-16 19:48:26.337534','status','CANCELED','ACTIVE',1),(44,'2024-06-16 19:48:27.070548','status','ACTIVE','CANCELED',1),(45,'2024-06-16 19:48:27.956297','status','CANCELED','ACTIVE',1),(46,'2024-06-16 19:48:28.651914','status','ACTIVE','CANCELED',1),(47,'2024-06-16 19:48:29.757766','status','CANCELED','ACTIVE',1),(48,'2024-06-16 19:48:30.483452','status','ACTIVE','CANCELED',1);
/*!40000 ALTER TABLE `subscription_audit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'subscriptiondb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-16 22:23:22
