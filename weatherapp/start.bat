@echo off

REM Script per avviare l'applicazione con Docker Compose su Windows

echo Avvio dell'applicazione Weather App con Docker...

REM Verifica se Docker è installato
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Errore: Docker non è installato. Per favore installa Docker Desktop prima di continuare.
    pause
    exit /b 1
)

REM Verifica se Docker Compose è installato
docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Errore: Docker Compose non è installato. Per favore installa Docker Compose prima di continuare.
    pause
    exit /b 1
)

REM Crea le directory necessarie
if not exist "logs" mkdir logs

REM Avvia i servizi
echo Avvio dei container...
docker-compose up --build -d

REM Attendi che i servizi siano pronti
echo Attesa dell'avvio dei servizi...
timeout /t 10 /nobreak >nul

REM Verifica lo stato dei servizi
echo Stato dei servizi:
docker-compose ps

REM Mostra informazioni utili
echo.
echo INFO - Informazioni dell'applicazione:
echo NETWORK - L'applicazione sarà disponibile su: http://localhost:8080
echo CONSOLE - Console H2 disponibile su: http://localhost:8080/h2-console
echo DATABASE - Database MySQL disponibile su: localhost:3306
echo.
echo LOGS - Per vedere i log: docker-compose logs -f weatherapp
echo STOP - Per fermare l'applicazione: docker-compose down
echo.

pause
