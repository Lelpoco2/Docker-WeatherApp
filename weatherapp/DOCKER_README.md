# Weather App - Docker Setup

Questa applicazione Spring Boot è stata configurata per funzionare con Docker e Docker Compose.

## 🚀 Avvio Rapido

### Prerequisiti
- Docker Desktop installato
- Docker Compose installato (incluso in Docker Desktop)

### Avvio dell'applicazione

#### Windows
```bash
# Avvia l'applicazione
./start.bat

# Oppure manualmente:
docker-compose up --build -d
```

#### Linux/macOS
```bash
# Avvia l'applicazione
./start.sh

# Oppure manualmente:
docker-compose up --build -d
```

## 📋 Servizi Disponibili

### 🌐 Applicazione Web
- **URL**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health

### 💾 Database MySQL
- **Host**: localhost
- **Port**: 3306
- **Database**: weatherapp
- **Username**: weatheruser
- **Password**: weatherpass
- **Root Password**: rootpassword

## 🛠️ Comandi Utili

### Gestione dell'applicazione
```bash
# Avvia l'applicazione
docker-compose up -d

# Avvia con rebuild delle immagini
docker-compose up --build -d

# Ferma l'applicazione
docker-compose down

# Ferma e rimuove i volumi
docker-compose down -v

# Visualizza i log
docker-compose logs -f weatherapp

# Visualizza lo stato dei servizi
docker-compose ps
```

### Debug e sviluppo
```bash
# Entra nel container dell'applicazione
docker-compose exec weatherapp bash

# Entra nel container MySQL
docker-compose exec mysql mysql -u weatheruser -p weatherapp

# Visualizza i log in tempo reale
docker-compose logs -f

# Ricostruisci solo l'applicazione
docker-compose build weatherapp
```

## 🏗️ Architettura

### Dockerfile
- **Multi-stage build** per ottimizzare le dimensioni dell'immagine
- **Fase 1**: Build dell'applicazione con Maven
- **Fase 2**: Runtime con OpenJDK 17 slim
- **Sicurezza**: Utente non-root per l'esecuzione
- **Health Check**: Controllo automatico dello stato dell'applicazione

### Docker Compose
- **Servizio MySQL**: Database persistente con volume
- **Servizio WeatherApp**: Applicazione Spring Boot
- **Rete**: Comunicazione sicura tra i servizi
- **Volume**: Persistenza dei dati MySQL e log dell'applicazione

## 🔧 Configurazione

### Profili Spring Boot
- **dev**: Profilo di sviluppo con H2 database
- **docker**: Profilo Docker con MySQL

### Variabili d'ambiente
Puoi personalizzare la configurazione modificando le variabili d'ambiente in `docker-compose.yml`:

```yaml
environment:
  SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/weatherapp
  SPRING_DATASOURCE_USERNAME: weatheruser
  SPRING_DATASOURCE_PASSWORD: weatherpass
  SPRING_PROFILES_ACTIVE: docker
  LOGGING_LEVEL_COM_ALESSIO_WEATHERAPP: DEBUG
```

## 📁 Struttura dei File Docker

```
weatherapp/
├── Dockerfile                 # Configurazione immagine Docker
├── docker-compose.yml         # Orchestrazione servizi
├── .dockerignore              # File da ignorare nella build
├── start.bat                  # Script avvio Windows
├── start.sh                   # Script avvio Linux/macOS
├── stop.bat                   # Script arresto Windows
├── docker/
│   └── mysql/
│       └── init/
│           └── 01-init.sql    # Script inizializzazione MySQL
└── logs/                      # Directory log applicazione
```

## 🐛 Troubleshooting

### L'applicazione non si avvia
```bash
# Verifica lo stato dei servizi
docker-compose ps

# Controlla i log
docker-compose logs weatherapp
docker-compose logs mysql

# Verifica la connessione al database
docker-compose exec mysql mysql -u weatheruser -p weatherapp
```

### Problemi di connessione al database
```bash
# Riavvia solo il database
docker-compose restart mysql

# Verifica che il database sia healthy
docker-compose ps
```

### Reset completo
```bash
# Ferma tutto e rimuovi volumi
docker-compose down -v

# Rimuovi le immagini
docker-compose down --rmi all

# Riavvia da zero
docker-compose up --build -d
```

## 🔄 Aggiornamenti

Per aggiornare l'applicazione:
1. Modifica il codice
2. Ricostruisci l'immagine: `docker-compose build weatherapp`
3. Riavvia il servizio: `docker-compose up -d weatherapp`

## 📊 Monitoraggio

### Health Checks
- **Applicazione**: http://localhost:8080/actuator/health
- **Database**: Controllo automatico tramite Docker Compose

### Log
- **Applicazione**: `docker-compose logs -f weatherapp`
- **Database**: `docker-compose logs -f mysql`
- **File di log**: `./logs/weatherapp.log`
