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
- **Servizio WeatherApp**: Applicazione Spring Boot
- **Rete**: Comunicazione sicura tra i servizi
- **Volume**: Persistenza dei log dell'applicazione

## 🔧 Configurazione

### Profili Spring Boot
- **dev**: Profilo di sviluppo con H2 database
- **docker**: Profilo Docker con H2 database (ora predefinito)

### Variabili d'ambiente
Puoi personalizzare la configurazione modificando le variabili d'ambiente in `docker-compose.yml`:

```yaml
environment:
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
│   # (nessuna directory mysql, solo log applicazione)
└── logs/                      # Directory log applicazione
```

## 🐛 Troubleshooting

### L'applicazione non si avvia
```bash
# Verifica lo stato dei servizi
docker-compose ps

# Controlla i log
docker-compose logs weatherapp
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
- **File di log**: `./logs/weatherapp.log`

## 🗄️ Persistenza dei dati meteorologici

L'applicazione salva automaticamente i dati meteo recuperati dalle API in un database H2 in modalità file, garantendo la persistenza tra i riavvii del container.

- **Database**: H2 (file, percorso `/app/data/weatherdb` in Docker)
- **Configurazione**: già inclusa nei profili `dev` e `docker`.
- **Console H2**: accessibile su [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - JDBC URL: `jdbc:h2:file:./data/weatherdb` (locale) oppure `jdbc:h2:file:/app/data/weatherdb` (Docker)
  - User: `sa` (senza password)

### Endpoint REST per dati storici

- `GET /api/weather/history?city=...`<br>
  Restituisce la lista dei dati meteo storici salvati per la città.
- `GET /api/weather/latest/{city}`<br>
  Restituisce l'ultimo dato meteo salvato per la città.
- `GET /api/weather/stats/popular-locations`<br>
  Restituisce le statistiche sulle località più cercate.

### Visualizzazione dati storici

- **Pagina web**: [http://localhost:8080/history.html](http://localhost:8080/history.html)
  - Permette di selezionare una città e visualizzare tutti i dati meteo storici registrati.
  - Mostra anche le statistiche delle località più popolari.

### Note
- I dati vengono salvati automaticamente ogni volta che viene effettuata una richiesta meteo tramite l'applicazione.
- La persistenza è abilitata sia in locale che in Docker.
- I dati sono consultabili sia tramite API che tramite interfaccia web.
