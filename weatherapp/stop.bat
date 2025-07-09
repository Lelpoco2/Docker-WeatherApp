@echo off

REM Script per fermare l'applicazione Docker

echo Arresto dell'applicazione Weather App...

REM Ferma e rimuove i container
docker-compose down

REM Opzionale: rimuovi anche i volumi (decommentare se necessario)
REM docker-compose down -v

echo SUCCESS Applicazione arrestata con successo.

pause
