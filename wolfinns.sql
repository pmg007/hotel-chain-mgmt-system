-- MySQL dump 10.16  Distrib 10.2.14-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: wolfinns
-- ------------------------------------------------------
-- Server version	10.2.14-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin` (
  `AdminID` int(5) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  PRIMARY KEY (`AdminID`)
) ENGINE=InnoDB AUTO_INCREMENT=10006 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (10001,'Abhishek Arya','aarya'),(10002,'Tushar Pahuja','tpahuja'),(10003,'Pragam Gandhi','pmgandh2'),(10004,'Pavneet Singh Anand','panand4'),(10005,'Guest','guest1');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bill` (
  `BillNumber` bigint(10) NOT NULL AUTO_INCREMENT,
  `PaymentMode` varchar(20) NOT NULL,
  `CardNumber` bigint(16) DEFAULT NULL,
  `BillAddress` varchar(255) NOT NULL,
  `SSN` bigint(9) NOT NULL,
  `TotalAmount` decimal(12,2) NOT NULL,
  `BookingID` int(9) NOT NULL,
  PRIMARY KEY (`BillNumber`),
  KEY `Bill_fk_id` (`BookingID`),
  CONSTRAINT `Bill_fk_id` FOREIGN KEY (`BookingID`) REFERENCES `booking` (`BookingID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill` DISABLE KEYS */;
INSERT INTO `bill` VALUES (11,'CREDIT CARD',1052,'980 TRT ST., Raleigh NC',5939846,431.00,25),(12,'WOLFINN CREDIT CARD',3020,'7720 MHT St Greensboro NC',7778352,774.25,26),(13,'CREDIT CARD',2497,'231 DRY St ROchester NY 78',8589430,510.00,27),(14,'CASH',0,'24 bst st dallas tx 14',4409328,3005.00,28),(17,'CASH',0,'123 crest',6987423,15000.00,31),(18,'cash',0,'123 crest road',34567,20015.00,32);
/*!40000 ALTER TABLE `bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `booking` (
  `BookingID` int(9) NOT NULL AUTO_INCREMENT,
  `GuestNumber` int(1) NOT NULL,
  `StartDate` date NOT NULL,
  `EndDate` date NOT NULL,
  `CheckInTime` time NOT NULL,
  `CheckOutTime` time DEFAULT NULL,
  `CustomerEmail` varchar(60) NOT NULL,
  `HotelID` int(4) NOT NULL,
  `RoomNumber` int(4) NOT NULL,
  PRIMARY KEY (`BookingID`),
  KEY `Booking_fk_id_Room` (`HotelID`,`RoomNumber`),
  KEY `Booking_fk_id_Customer` (`CustomerEmail`),
  CONSTRAINT `Booking_fk_id_Customer` FOREIGN KEY (`CustomerEmail`) REFERENCES `customer` (`Email`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Booking_fk_id_Room` FOREIGN KEY (`HotelID`, `RoomNumber`) REFERENCES `room` (`HotelID`, `RoomNumber`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
INSERT INTO `booking` VALUES (25,1,'2017-05-10','2017-05-13','15:17:00','12:12:17','david@gmail.com',1,1),(26,2,'2017-05-10','2017-05-13','16:11:00','12:37:58','sarah@gmail.com',1,2),(27,1,'2016-05-10','2016-05-14','15:45:00','12:45:07','joseph@gmail.com',2,3),(28,2,'2018-05-10','2018-05-12','14:30:00','12:53:15','lucy@gmail.com',3,2),(31,1,'2018-01-01','2018-01-03','10:00:00','13:32:40','tempuser@gmail.com',4,1),(32,2,'2018-04-17','2018-04-20','15:32:00','15:46:10','lucy@gmail.com',4,1);
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `Name` varchar(255) NOT NULL,
  `Email` varchar(60) NOT NULL,
  `DOB` date NOT NULL,
  `Phone` varchar(10) NOT NULL,
  PRIMARY KEY (`Email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES ('David','david@gmail.com','1980-01-30','123'),('Joseph','joseph@gmail.com','1987-01-30','789'),('Lucy','lucy@gmail.com','1985-01-30','213'),('Sarah','sarah@gmail.com','1971-01-30','456'),('new user','tempuser@gmail.com','2000-01-01','8180');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hotel`
--

DROP TABLE IF EXISTS `hotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hotel` (
  `HotelID` int(4) NOT NULL AUTO_INCREMENT,
  `ManagerID` int(6) DEFAULT NULL,
  `Name` varchar(255) NOT NULL,
  `Address` varchar(255) NOT NULL,
  `City` varchar(15) NOT NULL,
  `State` varchar(20) NOT NULL,
  `Email` varchar(60) NOT NULL,
  `Phone` varchar(10) NOT NULL,
  PRIMARY KEY (`HotelID`),
  UNIQUE KEY `ManagerID` (`ManagerID`)
) ENGINE=InnoDB AUTO_INCREMENT=1018 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotel`
--

LOCK TABLES `hotel` WRITE;
/*!40000 ALTER TABLE `hotel` DISABLE KEYS */;
INSERT INTO `hotel` VALUES (1,100,'Hotel A','21 ABC St, Raleigh NC 27','Raleigh','NC','hotela@gmail.com','919'),(2,101,'Hotel B','25 XYZ St, Rochester NY 54','Rochester','NY','hotelb@gmail.com','718'),(3,102,'Hotel C','29 PQR St, Greensboro NC 27','Greensboro','NC','hotelc@gmail.com','984'),(4,105,'Hotel D','28 GHW St, Raleigh NC 32','Raleigh','NC','hoteld@gmail.com','920');
/*!40000 ALTER TABLE `hotel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manage`
--

DROP TABLE IF EXISTS `manage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `manage` (
  `AdminID` int(5) NOT NULL,
  `HotelID` int(4) NOT NULL,
  PRIMARY KEY (`AdminID`,`HotelID`),
  KEY `Manage_fk_id_Hotel` (`HotelID`),
  CONSTRAINT `Manage_fk_id_Admin` FOREIGN KEY (`AdminID`) REFERENCES `admin` (`AdminID`) ON DELETE CASCADE,
  CONSTRAINT `Manage_fk_id_Hotel` FOREIGN KEY (`HotelID`) REFERENCES `hotel` (`HotelID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manage`
--

LOCK TABLES `manage` WRITE;
/*!40000 ALTER TABLE `manage` DISABLE KEYS */;
/*!40000 ALTER TABLE `manage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `presidential_room`
--

DROP TABLE IF EXISTS `presidential_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `presidential_room` (
  `RoomNumber` int(4) NOT NULL DEFAULT 0,
  `HotelID` int(4) NOT NULL DEFAULT 0,
  `RoomServiceStaffID` int(6) DEFAULT NULL,
  `CateringStaffID` int(6) DEFAULT NULL,
  PRIMARY KEY (`RoomNumber`,`HotelID`),
  KEY `Presidential_fk_id` (`HotelID`),
  CONSTRAINT `Presidential_fk_id` FOREIGN KEY (`HotelID`) REFERENCES `hotel` (`HotelID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `presidential_room`
--

LOCK TABLES `presidential_room` WRITE;
/*!40000 ALTER TABLE `presidential_room` DISABLE KEYS */;
INSERT INTO `presidential_room` VALUES (1,4,NULL,NULL);
/*!40000 ALTER TABLE `presidential_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room` (
  `RoomNumber` int(4) NOT NULL DEFAULT 0,
  `HotelID` int(4) NOT NULL DEFAULT 0,
  `ServiceDesc` text NOT NULL,
  `MaxOccupancy` int(1) NOT NULL,
  `Category` varchar(20) NOT NULL,
  `Availability` tinyint(1) NOT NULL,
  `Rate` int(4) NOT NULL,
  PRIMARY KEY (`RoomNumber`,`HotelID`),
  KEY `Room_fk_id` (`HotelID`),
  CONSTRAINT `Room_fk_id` FOREIGN KEY (`HotelID`) REFERENCES `hotel` (`HotelID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,1,'TV',1,'Economy',1,100),(1,4,'TV, AC, REFRIGERATOR, WIFI',4,'PRESIDENTIAL SUITE',1,5000),(2,1,'TV, AC',2,'Deluxe',1,200),(2,3,'TV, AC, REFRIGERATOR',3,'Executive',0,1000),(3,2,'TV',1,'Economy',1,100),(5,1,'TV, AC',2,'Deluxe',1,200);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service` (
  `ServiceID` int(3) NOT NULL,
  `Cost` int(4) NOT NULL,
  `ServiceName` varchar(20) NOT NULL,
  `HotelId` int(6) NOT NULL,
  PRIMARY KEY (`ServiceID`,`HotelId`),
  KEY `Service_fk_id` (`HotelId`),
  CONSTRAINT `Service_fk_id` FOREIGN KEY (`HotelId`) REFERENCES `hotel` (`HotelID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
INSERT INTO `service` VALUES (0,0,'presidential',1),(0,0,'presidential',2),(0,0,'presidential',3),(0,0,'presidential',4),(1,5,'phone bills',1),(1,5,'phone bills',2),(1,5,'phone bills ',3),(1,5,'phone bills',4),(2,16,'dry cleaning',1),(2,16,'dry cleaning',2),(2,16,'dry cleaning',3),(2,16,'dry cleaning',4),(3,15,'gyms',1),(3,15,'gyms',2),(3,15,'gyms',3),(3,15,'gyms',4),(4,10,'room service',1),(4,10,'room service',2),(4,10,'room service',3),(4,10,'room service',4),(5,20,'special requests',1),(5,20,'special requests',2),(5,20,'special requests',3),(5,20,'special requests',4);
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_booking`
--

DROP TABLE IF EXISTS `service_booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_booking` (
  `ServiceNumber` int(4) NOT NULL DEFAULT 0,
  `BookingID` int(9) NOT NULL DEFAULT 0,
  PRIMARY KEY (`ServiceNumber`,`BookingID`),
  KEY `Service_booking_fk2` (`BookingID`),
  CONSTRAINT `Service_booking_fk1` FOREIGN KEY (`ServiceNumber`) REFERENCES `service_requested` (`ServiceNumber`) ON DELETE CASCADE,
  CONSTRAINT `Service_booking_fk2` FOREIGN KEY (`BookingID`) REFERENCES `booking` (`BookingID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_booking`
--

LOCK TABLES `service_booking` WRITE;
/*!40000 ALTER TABLE `service_booking` DISABLE KEYS */;
INSERT INTO `service_booking` VALUES (10,25),(11,25),(12,26),(13,27),(14,28),(18,31),(19,32),(20,32);
/*!40000 ALTER TABLE `service_booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_requested`
--

DROP TABLE IF EXISTS `service_requested`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_requested` (
  `ServiceNumber` int(4) NOT NULL AUTO_INCREMENT,
  `ServiceStaffID` int(6) NOT NULL,
  `ReceptingStaffID` int(6) NOT NULL,
  `ServiceID` int(4) NOT NULL,
  `HotelID` int(4) NOT NULL,
  PRIMARY KEY (`ServiceNumber`),
  KEY `Service_req_fk_id_Service` (`ServiceID`,`HotelID`),
  CONSTRAINT `Service_req_fk_id_Service` FOREIGN KEY (`ServiceID`, `HotelID`) REFERENCES `service` (`ServiceID`, `HotelId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_requested`
--

LOCK TABLES `service_requested` WRITE;
/*!40000 ALTER TABLE `service_requested` DISABLE KEYS */;
INSERT INTO `service_requested` VALUES (10,100023,100023,2,1),(11,100024,100024,3,1),(12,100023,100024,3,1),(13,100025,100026,4,2),(14,100027,100028,1,3),(18,100029,100030,0,4),(19,100030,100031,0,4),(20,100031,100031,3,4);
/*!40000 ALTER TABLE `service_requested` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `staff` (
  `StaffID` int(6) NOT NULL AUTO_INCREMENT,
  `HotelID` int(4) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `Age` int(2) NOT NULL,
  `Address` varchar(255) NOT NULL,
  `Phone` varchar(10) NOT NULL,
  `Availability` tinyint(1) NOT NULL,
  `Department` varchar(20) NOT NULL,
  `IsActive` tinyint(1) NOT NULL,
  `Password` varchar(255) NOT NULL,
  PRIMARY KEY (`StaffID`),
  KEY `Staff_fk_id` (`HotelID`),
  CONSTRAINT `Staff_fk_id` FOREIGN KEY (`HotelID`) REFERENCES `hotel` (`HotelID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=100032 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES (100,1,'Mary',40,'90 ABC St, Raleigh NC 27','654',1,'Management',1,'qwerty'),(101,2,'John',45,'798 XYZ St, Rochester NY 54','564',1,'Management',1,'qwerty'),(102,3,'Carol',55,'351 MH St, Greensboro NC 27','546',1,'Management',1,'qwerty'),(103,1,'Emma',55,'49 ABC St, Raleigh NC 27','547',1,'Receptionist',1,'qwerty'),(104,1,'Ava',55,'425 RG St, Raleigh NC 27','777',1,'Catering',1,'qwerty'),(105,4,'Peter',52,'475 RG St, Raleigh NC 27','724',1,'Management',1,'qwerty'),(106,4,'Olivia',27,'325 PD St, Raleigh NC 27','799',1,'Receptionist',1,'qwerty'),(100023,1,'Server1',21,'27 avx','123',1,'Service',1,'qwerty'),(100024,1,'Server2',22,'28 avx','21111',1,'Service',1,'qwerty'),(100025,2,'Server1',27,'28 avx','1234',1,'Service',1,'qwerty'),(100026,2,'Server 2',25,'29 aww','890',1,'Service',1,'qwerty'),(100027,3,'Server 1',30,'30 crest','6789',1,'Service',1,'qwerty'),(100028,3,'Server 2',32,'31 xrest','5673',1,'Service',1,'qwerty'),(100029,4,'Server 1',33,'123 abc','101',1,'Service',1,'qwerty'),(100030,4,'Server 2',35,'127 xyz','109',1,'Service',1,'qwerty'),(100031,4,'Server 3',36,'123 abcd','110',1,'Service',1,'qwerty');
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-05-12  0:37:10
