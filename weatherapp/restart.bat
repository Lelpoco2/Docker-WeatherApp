@echo off

REM Script per riavviare rapidamente l'applicazione WeatherApp con Docker Compose

echo Riavvio dell'applicazione Weather App con Docker...

docker-compose down
if %errorlevel% neq 0 (
    echo Errore durante l'arresto dei container.
    pause
    exit /b 1
)

docker-compose up --build -d
if %errorlevel% neq 0 (
    echo Errore durante l'avvio dei container.
    pause
    exit /b 1
)

echo L'applicazione è stata riavviata!
docker-compose ps
echo.
echo Per vedere i log: docker-compose logs -f weatherapp
echo.
pause
