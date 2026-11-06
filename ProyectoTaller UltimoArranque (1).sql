CREATE DATABASE  IF NOT EXISTS `taller` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `taller`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: taller
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `id_cliente` int NOT NULL AUTO_INCREMENT,
  `nombres` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellidos` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `correo` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `direccion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_cliente`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,'Carlos Leonel','Gómez Pérez','5512-3456','carlos.gomez@gmail.com','Zona 10, Ciudad de Guatemala'),(2,'María Fernanda','López Ruano','4234-8901','mfer.lopez@gmail.com','Zona 4, Mixco'),(4,'Carlos Alberto','Pérez Gómez','5555-9999','carlos.nuevo@gmail.com','Zona 4, Ciudad de Guatemala');
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_orden_repuestos`
--

DROP TABLE IF EXISTS `detalle_orden_repuestos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_orden_repuestos` (
  `id_detalle_repuesto` int NOT NULL AUTO_INCREMENT,
  `id_orden` int NOT NULL,
  `id_repuesto` int NOT NULL,
  `cantidad` int NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_detalle_repuesto`),
  KEY `fk_dor_orden` (`id_orden`),
  KEY `fk_dor_repuesto` (`id_repuesto`),
  CONSTRAINT `fk_dor_orden` FOREIGN KEY (`id_orden`) REFERENCES `ordenes_trabajo` (`id_orden`),
  CONSTRAINT `fk_dor_repuesto` FOREIGN KEY (`id_repuesto`) REFERENCES `repuestos` (`id_repuesto`),
  CONSTRAINT `detalle_orden_repuestos_chk_1` CHECK ((`cantidad` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_orden_repuestos`
--

LOCK TABLES `detalle_orden_repuestos` WRITE;
/*!40000 ALTER TABLE `detalle_orden_repuestos` DISABLE KEYS */;
INSERT INTO `detalle_orden_repuestos` VALUES (3,7,2,1,250.00,250.00),(4,8,1,1,85.00,85.00),(5,7,2,1,250.00,250.00),(6,8,1,1,85.00,85.00),(7,7,1,1,45.00,45.00),(8,7,2,1,250.00,250.00),(9,7,1,1,45.00,45.00);
/*!40000 ALTER TABLE `detalle_orden_repuestos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_orden_servicios`
--

DROP TABLE IF EXISTS `detalle_orden_servicios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_orden_servicios` (
  `id_detalle_servicio` int NOT NULL AUTO_INCREMENT,
  `id_orden` int NOT NULL,
  `id_servicio` int NOT NULL,
  `costo_aplicado` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_detalle_servicio`),
  KEY `fk_dos_orden` (`id_orden`),
  KEY `fk_dos_servicio` (`id_servicio`),
  CONSTRAINT `fk_dos_orden` FOREIGN KEY (`id_orden`) REFERENCES `ordenes_trabajo` (`id_orden`),
  CONSTRAINT `fk_dos_servicio` FOREIGN KEY (`id_servicio`) REFERENCES `servicios` (`id_servicio`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_orden_servicios`
--

LOCK TABLES `detalle_orden_servicios` WRITE;
/*!40000 ALTER TABLE `detalle_orden_servicios` DISABLE KEYS */;
INSERT INTO `detalle_orden_servicios` VALUES (3,7,1,100.00),(4,8,2,175.00),(5,7,1,100.00),(6,8,2,175.00),(7,7,2,175.00);
/*!40000 ALTER TABLE `detalle_orden_servicios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mecanicos`
--

DROP TABLE IF EXISTS `mecanicos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mecanicos` (
  `id_mecanico` int NOT NULL AUTO_INCREMENT,
  `nombres` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellidos` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `especialidad` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_mecanico`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mecanicos`
--

LOCK TABLES `mecanicos` WRITE;
/*!40000 ALTER TABLE `mecanicos` DISABLE KEYS */;
INSERT INTO `mecanicos` VALUES (1,'Juan Carlos','Martínez Morales','3112-9988','Motor y Diagnóstico Electrónico'),(2,'Esteban Raúl','Castillo Fuentes','5844-1122','Frenos y Suspensión'),(4,'Juan Carlos','Martínez Morales','3112-9988','Motor y Diagnóstico Electrónico'),(5,'Esteban Raúl','Castillo Fuentes','5844-1122','Frenos y Suspensión');
/*!40000 ALTER TABLE `mecanicos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordenes_trabajo`
--

DROP TABLE IF EXISTS `ordenes_trabajo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordenes_trabajo` (
  `id_orden` int NOT NULL AUTO_INCREMENT,
  `id_vehiculo` int NOT NULL,
  `id_mecanico` int DEFAULT NULL,
  `fecha_ingreso` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_entrega` datetime DEFAULT NULL,
  `descripcion_problema` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `estado` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDIENTE',
  `costo_total` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`id_orden`),
  KEY `fk_orden_vehiculo` (`id_vehiculo`),
  KEY `fk_orden_mecanico` (`id_mecanico`),
  CONSTRAINT `fk_orden_mecanico` FOREIGN KEY (`id_mecanico`) REFERENCES `mecanicos` (`id_mecanico`),
  CONSTRAINT `fk_orden_vehiculo` FOREIGN KEY (`id_vehiculo`) REFERENCES `vehiculos` (`id_vehiculo`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordenes_trabajo`
--

LOCK TABLES `ordenes_trabajo` WRITE;
/*!40000 ALTER TABLE `ordenes_trabajo` DISABLE KEYS */;
INSERT INTO `ordenes_trabajo` VALUES (7,3,2,'2026-10-20 00:00:00','2026-11-04 22:52:38','Vehículo no enciende por las mañanas, posible revisión de motor y afinación.','FINALIZADA',1170.00),(8,4,2,'2026-10-20 14:10:52',NULL,'El carro vibra al frenar en carretera y rechina.','EN PROCESO',335.00),(12,3,1,'2026-11-01 00:00:00',NULL,'Prueba sencilla de inserción de orden','EN PROCESO',0.00),(13,4,NULL,'2026-11-03 21:50:53','2026-11-03 21:54:09','No enciende por las mañanas ','FINALIZADA',0.00);
/*!40000 ALTER TABLE `ordenes_trabajo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repuestos`
--

DROP TABLE IF EXISTS `repuestos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `repuestos` (
  `id_repuesto` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text COLLATE utf8mb4_unicode_ci,
  `precio` decimal(10,2) NOT NULL,
  `existencia` int NOT NULL,
  PRIMARY KEY (`id_repuesto`),
  CONSTRAINT `repuestos_chk_1` CHECK ((`existencia` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repuestos`
--

LOCK TABLES `repuestos` WRITE;
/*!40000 ALTER TABLE `repuestos` DISABLE KEYS */;
INSERT INTO `repuestos` VALUES (1,'Filtro de Aceite','Filtro estándar para motores 4 cilindros',45.00,26),(2,'Pastillas de Freno','Juego delantero de pastillas cerámicas',250.00,8),(3,'Aceite Sintético 5W-30','Litro de aceite de motor de alta gama',85.00,30),(5,'Bujía Modificada','Descripción actualizada correctamente',95.50,25),(6,'Filtro de Aceite','Filtro estándar para motores 4 cilindros',45.00,15),(7,'Pastillas de Freno','Juego delantero de pastillas cerámicas',250.00,8),(8,'Aceite Sintético 5W-30','Litro de aceite de motor de alta gama',85.00,30);
/*!40000 ALTER TABLE `repuestos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servicios`
--

DROP TABLE IF EXISTS `servicios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `servicios` (
  `id_servicio` int NOT NULL AUTO_INCREMENT,
  `nombre_servicio` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text COLLATE utf8mb4_unicode_ci,
  `costo_mano_obra` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_servicio`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servicios`
--

LOCK TABLES `servicios` WRITE;
/*!40000 ALTER TABLE `servicios` DISABLE KEYS */;
INSERT INTO `servicios` VALUES (1,'Cambio de Aceite y Filtro','Mano de obra para drenaje, sustitución de filtro y llenado',100.00),(2,'Reemplazo de Pastillas de Freno','Desmontaje de ruedas, cambio de pastillas y purgado',175.00),(3,'Cambio de Aceite','Aceite sintético y filtro',150.00),(4,'Reemplazo de Pastillas de Freno','Desmontaje de ruedas, cambio de pastillas y purgado',175.00),(5,'Cambio de Aceite y Filtro','Mano de obra para drenaje, sustitución de filtro y llenado',100.00),(6,'Reemplazo de Pastillas de Freno','Desmontaje de ruedas, cambio de pastillas y purgado',175.00),(7,'Alineación y Balanceo','Servicio de alineación de dirección en 4 ruedas y balanceo de neumáticos',150.00),(8,'Sustitución de Kit de Embrague','Desmontaje de transmisión y cambio completo de disco, plato y collarín',450.00),(9,'Diagnóstico Computarizado','Escaneo general de sensores, borrado de códigos de falla y revisión de testigos',125.00),(10,'Afisado Mayor (Tune-Up)','Limpieza de inyectores, cambio de bujías, filtros de aire y revisión general',275.00),(11,'Mantenimiento del Sistema de Enfriamiento','Lavado de radiador, sustitución de refrigerante y verificación de mangueras',180.00),(13,'Cambio de Aceite','Cambio de aceite sintético y filtro de motor',150.00),(14,'Cambio de Aceite y Filtro','Mano de obra para drenaje, sustitución de filtro y llenado',100.00),(15,'Reemplazo de Pastillas de Freno','Desmontaje de ruedas, cambio de pastillas y purgado',175.00),(16,'Alineación y Balanceo','Servicio de alineación de dirección en 4 ruedas y balanceo de neumáticos',150.00),(17,'Sustitución de Kit de Embrague','Desmontaje de transmisión y cambio completo de disco, plato y collarín',450.00),(18,'Diagnóstico Computarizado','Escaneo general de sensores, borrado de códigos de falla y revisión de testigos',125.00),(19,'Afisado Mayor (Tune-Up)','Limpieza de inyectores, cambio de bujías, filtros de aire y revisión general',275.00),(20,'Mantenimiento del Sistema de Enfriamiento','Lavado de radiador, sustitución de refrigerante y verificación de mangueras',180.00);
/*!40000 ALTER TABLE `servicios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehiculos`
--

DROP TABLE IF EXISTS `vehiculos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehiculos` (
  `id_vehiculo` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int NOT NULL,
  `placa` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `marca` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `modelo` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `anio` int NOT NULL,
  PRIMARY KEY (`id_vehiculo`),
  UNIQUE KEY `placa` (`placa`),
  KEY `fk_vehiculo_cliente` (`id_cliente`),
  CONSTRAINT `fk_vehiculo_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehiculos`
--

LOCK TABLES `vehiculos` WRITE;
/*!40000 ALTER TABLE `vehiculos` DISABLE KEYS */;
INSERT INTO `vehiculos` VALUES (3,1,'P-123ABC','Toyota','Corolla',2018),(4,4,'P-12ABC','Honda','Civic',2024);
/*!40000 ALTER TABLE `vehiculos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-05 21:54:11
