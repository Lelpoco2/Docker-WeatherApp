// app.js - Gestione della pagina principale con le città

// Configurazione città
const CITIES = [
    { id: 'roma', name: 'Roma', displayName: 'Roma' },
    { id: 'milano', name: 'Milano', displayName: 'Milano' },
    { id: 'napoli', name: 'Napoli', displayName: 'Napoli' },
    { id: 'torino', name: 'Torino', displayName: 'Torino' },
    { id: 'firenze', name: 'Firenze', displayName: 'Firenze' },
    { id: 'venezia', name: 'Venezia', displayName: 'Venezia' },
    { id: 'bologna', name: 'Bologna', displayName: 'Bologna' },
    { id: 'palermo', name: 'Palermo', displayName: 'Palermo' },
    { id: 'genova', name: 'Genova', displayName: 'Genova' },
    { id: 'bari', name: 'Bari', displayName: 'Bari' }
];

// Mappatura icone meteo
const WEATHER_ICONS = {
    'Sereno': '☀️',
    'Parzialmente nuvoloso': '⛅',
    'Nuvoloso': '☁️',
    'Nebbia': '🌫️',
    'Pioviggine': '🌦️',
    'Pioggia': '🌧️',
    'Neve': '❄️',
    'Rovesci': '⛈️',
    'Temporale': '⛈️',
    'default': '🌤️'
};

// Stato dell'applicazione
let weatherData = new Map();
let loadingCities = new Set();

// Inizializzazione
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

function initializeApp() {
    const citiesGrid = document.getElementById('citiesGrid');
    if (!citiesGrid) {
        console.error('Elemento citiesGrid non trovato');
        return;
    }

    // Genera le card delle città
    generateCityCards();
    
    // Carica i dati meteo per tutte le città
    loadAllWeatherData();
}

function generateCityCards() {
    const citiesGrid = document.getElementById('citiesGrid');
    
    CITIES.forEach(city => {
        const cityCard = createCityCard(city);
        citiesGrid.appendChild(cityCard);
    });
}

function createCityCard(city) {
    const card = document.createElement('div');
    card.className = 'city-card';
    card.dataset.city = city.id;
    
    card.innerHTML = `
        <div class="city-card-header">
            <h3 class="city-name">${city.displayName}</h3>
            <div class="city-weather-icon" id="icon-${city.id}">🌤️</div>
        </div>
        <div class="city-weather-info">
            <div class="weather-metric">
                <div class="metric-label">Temperatura</div>
                <div class="metric-value temperature" id="temp-${city.id}">--°C</div>
            </div>
            <div class="weather-metric">
                <div class="metric-label">Umidità</div>
                <div class="metric-value humidity" id="humidity-${city.id}">--%</div>
            </div>
        </div>
    `;
    
    // Aggiungi event listener per il click
    card.addEventListener('click', () => {
        navigateToCity(city.id);
    });
    
    // Aggiungi effetto hover
    card.addEventListener('mouseenter', () => {
        card.style.transform = 'translateY(-8px) scale(1.02)';
    });
    
    card.addEventListener('mouseleave', () => {
        card.style.transform = 'translateY(0) scale(1)';
    });
    
    return card;
}

function navigateToCity(cityId) {
    // Aggiungi una leggera animazione prima della navigazione
    const card = document.querySelector(`[data-city="${cityId}"]`);
    if (card) {
        card.style.transform = 'scale(0.95)';
        setTimeout(() => {
            window.location.href = `/city.html?city=${cityId}`;
        }, 150);
    } else {
        window.location.href = `/city.html?city=${cityId}`;
    }
}

function loadAllWeatherData() {
    CITIES.forEach(city => {
        loadWeatherData(city.id);
    });
}

async function loadWeatherData(cityId) {
    if (loadingCities.has(cityId)) {
        return; // Evita chiamate multiple
    }
    
    loadingCities.add(cityId);
    
    try {
        const response = await fetch(`/api/weather?city=${cityId}`);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        
        if (data && data.dailyWeather && data.dailyWeather.length > 0) {
            weatherData.set(cityId, data);
            updateCityCard(cityId, data);
        } else {
            throw new Error('Dati non validi ricevuti');
        }
        
    } catch (error) {
        console.error(`Errore nel caricamento dati per ${cityId}:`, error);
        handleWeatherError(cityId, error);
    } finally {
        loadingCities.delete(cityId);
    }
}

function updateCityCard(cityId, data) {
    const iconElement = document.getElementById(`icon-${cityId}`);
    const tempElement = document.getElementById(`temp-${cityId}`);
    const humidityElement = document.getElementById(`humidity-${cityId}`);
    
    if (!iconElement || !tempElement || !humidityElement) {
        console.error(`Elementi non trovati per la città ${cityId}`);
        return;
    }
    
    // Prendi i dati del giorno più recente
    const latestData = data.dailyWeather[data.dailyWeather.length - 1];
    
    // Aggiorna temperatura
    const temperature = latestData.temperatureMean;
    tempElement.textContent = temperature ? `${temperature.toFixed(1)}°C` : '--°C';
    
    // Aggiorna umidità
    const humidity = latestData.humidity;
    humidityElement.textContent = humidity ? `${Math.round(humidity)}%` : '--%';
    
    // Aggiorna icona
    const weatherIcon = getWeatherIcon(latestData.description);
    iconElement.textContent = weatherIcon;
    
    // Aggiungi animazione di aggiornamento
    animateUpdate(iconElement);
    animateUpdate(tempElement);
    animateUpdate(humidityElement);
}

function getWeatherIcon(description) {
    if (!description) return WEATHER_ICONS.default;
    
    return WEATHER_ICONS[description] || WEATHER_ICONS.default;
}

function handleWeatherError(cityId, error) {
    console.error(`Errore per ${cityId}:`, error);
    
    const tempElement = document.getElementById(`temp-${cityId}`);
    const humidityElement = document.getElementById(`humidity-${cityId}`);
    const iconElement = document.getElementById(`icon-${cityId}`);
    
    if (tempElement) tempElement.textContent = 'N/A';
    if (humidityElement) humidityElement.textContent = 'N/A';
    if (iconElement) iconElement.textContent = '❌';
}

function animateUpdate(element) {
    if (!element) return;
    
    element.style.transform = 'scale(1.1)';
    element.style.transition = 'transform 0.2s ease';
    
    setTimeout(() => {
        element.style.transform = 'scale(1)';
    }, 200);
}

// Utilità per il debug
function getWeatherData(cityId) {
    return weatherData.get(cityId);
}

function getAllWeatherData() {
    return Object.fromEntries(weatherData);
}

// Esporta per debug (se necessario)
if (typeof window !== 'undefined') {
    window.WeatherApp = {
        getWeatherData,
        getAllWeatherData,
        loadWeatherData,
        CITIES
    };
}
