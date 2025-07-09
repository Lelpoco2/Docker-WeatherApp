-- Script di inizializzazione per MySQL
-- Questo script viene eseguito automaticamente quando il container MySQL si avvia per la prima volta

-- Creazione delle tabelle (se necessario)
-- Spring Boot si occuperà automaticamente della creazione delle tabelle con DDL auto

-- Dati di esempio (opzionale)
USE weatherapp;

-- Puoi aggiungere qui eventuali dati di inizializzazione
-- INSERT INTO esempio_tabella (colonna1, colonna2) VALUES ('valore1', 'valore2');

-- Configurazione di base del database
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

COMMIT;
