#!/bin/bash

# Script per avviare l'applicazione con Docker Compose

echo "🚀 Avvio dell'applicazione Weather App con Docker..."

# Verifica se Docker è installato
if ! command -v docker &> /dev/null; then
    echo "❌ Docker non è installato. Per favore installa Docker prima di continuare."
    exit 1
fi

# Verifica se Docker Compose è installato
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose non è installato. Per favore installa Docker Compose prima di continuare."
    exit 1
fi

# Crea le directory necessarie
mkdir -p logs

# Avvia i servizi
echo "📦 Avvio dei container..."
docker-compose up --build -d

# Attendi che i servizi siano pronti
echo "⏳ Attesa dell'avvio dei servizi..."
sleep 10

# Verifica lo stato dei servizi
echo "📊 Stato dei servizi:"
docker-compose ps

# Mostra i log dell'applicazione
echo ""
echo "📋 Log dell'applicazione (premi Ctrl+C per interrompere):"
echo "🌐 L'applicazione sarà disponibile su: http://localhost:8080"
echo "🗄️  Console H2 disponibile su: http://localhost:8080/h2-console"
echo "💾 Database MySQL disponibile su: localhost:3306"
echo ""

# Segui i log
docker-compose logs -f weatherapp
