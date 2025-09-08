-- MySQL dump 10.13  Distrib 8.0.38, for macos14 (arm64)
--
-- Host: 127.0.0.1    Database: foodie
-- ------------------------------------------------------
-- Server version	9.0.1

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
-- Table structure for table `dispense`
--

DROP TABLE IF EXISTS `dispense`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dispense` (
  `username` varchar(255) NOT NULL,
  `alimento` varchar(255) NOT NULL,
  PRIMARY KEY (`username`,`alimento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispense`
--

LOCK TABLES `dispense` WRITE;
/*!40000 ALTER TABLE `dispense` DISABLE KEYS */;
INSERT INTO `dispense` VALUES ('test_utente','aubergines'),('test_utente','basil'),('test_utente','black pepper'),('test_utente','bread'),('test_utente','broccoli'),('test_utente','chili'),('test_utente','garlic'),('test_utente','oil'),('test_utente','parmesan'),('test_utente','pasta'),('test_utente','ricotta salata'),('test_utente','salt'),('test_utente','tomato'),('test_utente','zucchini'),('test_utente2','basil'),('test_utente2','black pepper'),('test_utente2','garlic'),('test_utente2','oil'),('test_utente2','parmesan'),('test_utente2','pasta'),('test_utente2','salt'),('test_utente2','tomato');
/*!40000 ALTER TABLE `dispense` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingredienti`
--

DROP TABLE IF EXISTS `ingredienti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingredienti` (
  `nome_ricetta` varchar(255) NOT NULL,
  `autore_ricetta` varchar(255) NOT NULL,
  `alimento` varchar(255) NOT NULL,
  `quantita` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`nome_ricetta`,`autore_ricetta`,`alimento`),
  KEY `nome_ricetta` (`nome_ricetta`,`autore_ricetta`),
  CONSTRAINT `fk_ingredienti_ricette` FOREIGN KEY (`nome_ricetta`, `autore_ricetta`) REFERENCES `ricette` (`nome`, `autore`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingredienti`
--

LOCK TABLES `ingredienti` WRITE;
/*!40000 ALTER TABLE `ingredienti` DISABLE KEYS */;
INSERT INTO `ingredienti` VALUES ('odcj','test_chef','pasta','100g'),('pasta aglio olio e peperoncino','test_chef','chili','qb'),('pasta aglio olio e peperoncino','test_chef','garlic','1'),('pasta aglio olio e peperoncino','test_chef','oil','qb'),('pasta aglio olio e peperoncino','test_chef','pasta','100g'),('pasta al pesto','test_chef2','basil','70g'),('pasta al pesto','test_chef2','garlic','1'),('pasta al pesto','test_chef2','oil','qb'),('pasta al pesto','test_chef2','parmesan','40g'),('pasta al pesto','test_chef2','pasta','100g'),('pasta al pesto','test_chef2','pine nuts','30g'),('pasta al pesto','test_chef2','salt','qb'),('pasta al pomodoro','test_chef','basil','10g'),('pasta al pomodoro','test_chef','garlic','1'),('pasta al pomodoro','test_chef','oil','qb'),('pasta al pomodoro','test_chef','pasta','100g'),('pasta al pomodoro','test_chef','salt','qb'),('pasta al pomodoro','test_chef','tomato','200g'),('pasta alla norma','test_chef2','aubergines','200g'),('pasta alla norma','test_chef2','garlic','1'),('pasta alla norma','test_chef2','oil','qb'),('pasta alla norma','test_chef2','pasta','100g'),('pasta alla norma','test_chef2','ricotta salata','20g'),('pasta alla norma','test_chef2','salt','qb'),('pasta alla norma','test_chef2','tomato','200g'),('Pasta cacio e pepe','chef carlo cracco','black pepper','qb'),('Pasta cacio e pepe','chef carlo cracco','pecorino romano','200 g'),('Pasta cacio e pepe','chef carlo cracco','salt','qb'),('Pasta cacio e pepe','chef carlo cracco','spaghetti','400 g'),('pasta cacio e pepe','test_chef','black pepper','qb'),('pasta cacio e pepe','test_chef','parmesan','70g'),('pasta cacio e pepe','test_chef','pasta','100g'),('pasta cacio e pepe','test_chef','salt','qb'),('Spaghetti con le vongole','chef carlo cracco','chili','qb'),('Spaghetti con le vongole','chef carlo cracco','clams','500 g'),('Spaghetti con le vongole','chef carlo cracco','extra virgin olive oil','qb'),('Spaghetti con le vongole','chef carlo cracco','garlic','2'),('Spaghetti con le vongole','chef carlo cracco','parsley','qb'),('Spaghetti con le vongole','chef carlo cracco','salt','qb'),('Spaghetti con le vongole','chef carlo cracco','spaghetti','400'),('test pasta ','test_chef','pasta','100g');
/*!40000 ALTER TABLE `ingredienti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingredienti_da_approvare`
--

DROP TABLE IF EXISTS `ingredienti_da_approvare`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingredienti_da_approvare` (
  `nome_ricetta` varchar(255) NOT NULL,
  `autore_ricetta` varchar(255) NOT NULL,
  `alimento` varchar(255) NOT NULL,
  `quantita` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`nome_ricetta`,`autore_ricetta`,`alimento`),
  CONSTRAINT `fk_ricetta` FOREIGN KEY (`nome_ricetta`, `autore_ricetta`) REFERENCES `ricette_da_approvare` (`nome`, `autore`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingredienti_da_approvare`
--

LOCK TABLES `ingredienti_da_approvare` WRITE;
/*!40000 ALTER TABLE `ingredienti_da_approvare` DISABLE KEYS */;
INSERT INTO `ingredienti_da_approvare` VALUES ('pasta test','test_chef','pasta','100g');
/*!40000 ALTER TABLE `ingredienti_da_approvare` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ricette`
--

DROP TABLE IF EXISTS `ricette`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ricette` (
  `nome` varchar(255) NOT NULL,
  `descrizione` longtext,
  `difficolta` int DEFAULT NULL,
  `autore` varchar(255) NOT NULL,
  PRIMARY KEY (`nome`,`autore`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ricette`
--

LOCK TABLES `ricette` WRITE;
/*!40000 ALTER TABLE `ricette` DISABLE KEYS */;
INSERT INTO `ricette` VALUES ('odcj','ojcojoj',1,'test_chef'),('pasta aglio olio e peperoncino','Metti a bollire acqua in una pentola e aggiungi il sale. Quando l acqua bolle aggiungi la pasta e cuoci fino a quando è pronta. In una padella scalda l olio e aggiungi l aglio tritato e il peperoncino. Lascia insaporire la pasta scolata, aggiungila al condimento e mescola bene. Servi calda e gusta.',1,'test_chef'),('pasta al pesto','Metti a bollire acqua in una pentola e aggiungi il sale. Quando l acqua bolle, aggiungi la pasta e cuoci fino a quando e pronta. In un frullatore, prepara il pesto con basilico, pinoli, aglio, parmigiano e olio. Scola la pasta e condiscila con il pesto. Mescola bene e servi calda.',1,'test_chef2'),('pasta al pomodoro','Metti a bollire acqua in una pentola e aggiungi il sale. Quando l acqua bolle, aggiungi la pasta e cuoci fino a quando e pronta. In una padella, scalda l olio e aggiungi l aglio tritato. Aggiungi i pomodori tagliati e lascia cuocere fino a quando diventano morbidi. Aggiungi il basilico e mescola bene. Scola la pasta e aggiungila al sugo. Mescola la pasta con il sugo per farla insaporire. Servi calda e gusta.',1,'test_chef'),('pasta alla norma','Metti a bollire acqua in una pentola e aggiungi il sale. Quando l acqua bolle aggiungi la pasta e cuoci fino a quando e pronta. In una padella scalda l olio e aggiungi aglio e melanzane tagliate a cubetti. Aggiungi pomodoro e lascia cuocere fino a quando diventa morbido. Scola la pasta e aggiungila al sugo. Cospargi con ricotta salata grattugiata e servi calda.',3,'test_chef2'),('Pasta cacio e pepe','Porta a ebollizione una pentola di acqua salata e cuoci gli spaghetti seguendo le istruzioni sulla confezione. Ricorda di tenere da parte un po di acqua di cottura prima di scolare la pasta.\nMentre la pasta cuoce, in una ciotola, mescola il Pecorino Romano grattugiato con una generosa quantità di pepe nero.\nScola gli spaghetti al dente e trasferiscili nella ciotola con il formaggio e il pepe.\nMescola bene gli ingredienti finché il formaggio non si fonde con la pasta, aggiungendo gradualmente l acqua di cottura della pasta per ottenere una consistenza cremosa.\nAssaggia e aggiusta di sale e pepe secondo il tuo gusto.\nServi immediatamente, guarnendo con ulteriore pepe nero a piacere.',1,'chef carlo cracco'),('pasta cacio e pepe','Metti a bollire acqua in una pentola e aggiungi il sale. Quando l acqua bolle, aggiungi la pasta e cuoci fino a quando e pronta. In una ciotola, grattugia il parmigiano e mescolalo con il pepe. In una padella, scalda un po di acqua di cottura della pasta. Aggiungi la pasta scolata e mescola bene con il composto di parmigiano e pepe fino a ottenere una crema. Servi calda e gusta.',2,'test_chef'),('Spaghetti con le vongole','Porta a ebollizione una pentola di acqua salata e cuoci le linguine seguendo le istruzioni sulla confezione. Ricorda di tenere da parte un po di acqua di cottura prima di scolare la pasta.\nNel frattempo, pulisci accuratamente le vongole sotto acqua corrente.\nIn una padella capiente, scalda un filo di olio d oliva extravergine a fuoco medio e aggiungi gli spicchi d aglio tritati e il peperoncino.\nAppena l aglio inizia a dorare, aggiungi le vongole alla padella e coprile con un coperchio. Cuoci a fuoco medio-basso finché le vongole non si aprono. Rimuovi quelle chiuse, in quanto potrebbero non essere commestibili.\nScola le linguine al dente e aggiungile nella padella con le vongole. Mescola bene per far assorbire i sapori.\nAggiungi prezzemolo fresco tritato, sale e pepe a piacere. Se necessario, puoi aggiungere un po di acqua di cottura della pasta per ottenere una consistenza più cremosa.\nServi la pasta con le vongole calda, guarnendo con un filo di olio d oliva e prezzemolo fresco.',3,'chef carlo cracco'),('test pasta ','procedimento test',1,'test_chef');
/*!40000 ALTER TABLE `ricette` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ricette_da_approvare`
--

DROP TABLE IF EXISTS `ricette_da_approvare`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ricette_da_approvare` (
  `nome` varchar(255) NOT NULL,
  `autore` varchar(255) NOT NULL,
  `difficolta` int DEFAULT NULL,
  `descrizione` longtext,
  PRIMARY KEY (`nome`,`autore`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ricette_da_approvare`
--

LOCK TABLES `ricette_da_approvare` WRITE;
/*!40000 ALTER TABLE `ricette_da_approvare` DISABLE KEYS */;
INSERT INTO `ricette_da_approvare` VALUES ('pasta test','test_chef',1,'procedimento pasta test');
/*!40000 ALTER TABLE `ricette_da_approvare` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_account`
--

DROP TABLE IF EXISTS `user_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_account` (
  `account_id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `cognome` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `ruolo` int NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `account_id_UNIQUE` (`account_id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_account`
--

LOCK TABLES `user_account` WRITE;
/*!40000 ALTER TABLE `user_account` DISABLE KEYS */;
INSERT INTO `user_account` VALUES (1,'test','test','test_moderatore',2,'moderatore'),(2,'test','test','test_utente',0,'test'),(3,'test','test','test_chef',1,'test'),(4,'Bruno','Barbieri','Chef Bruno Barbieri',1,'bruno00'),(5,'Carlo','Cracco','chef carlo cracco',1,'cracco00'),(16,'mario','rossi','test_utente2',0,'test'),(18,'test','test','test_moderatore2',2,'test'),(19,'test','test','test_chef2',1,'test');
/*!40000 ALTER TABLE `user_account` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-08 19:23:37
