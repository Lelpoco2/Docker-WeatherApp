// city.js - Gestione della pagina di dettaglio città

// Configurazione
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

const CITY_NAMES = {
    'roma': 'Roma',
    'milano': 'Milano',
    'napoli': 'Napoli',
    'torino': 'Torino',
    'firenze': 'Firenze',
    'venezia': 'Venezia',
    'bologna': 'Bologna',
    'palermo': 'Palermo',
    'genova': 'Genova',
    'bari': 'Bari'
};

// Stato dell'applicazione
let weatherData = null;
let chart = null;

// Inizializzazione
document.addEventListener('DOMContentLoaded', function() {
    initializeCityPage();
});

function initializeCityPage() {
    const cityId = getCityFromUrl();
    
    if (!cityId) {
        showError('Città non specificata nell\'URL');
        return;
    }
    
    const cityName = CITY_NAMES[cityId];
    if (!cityName) {
        showError('Città non riconosciuta');
        return;
    }
    
    // Aggiorna il titolo e breadcrumb
    updatePageTitle(cityName);
    updateBreadcrumb(cityName);
    
    // Carica i dati meteo
    loadWeatherData(cityId);
}

function getCityFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('city');
}

function updatePageTitle(cityName) {
    const titleElement = document.getElementById('cityTitle');
    if (titleElement) {
        titleElement.innerHTML = `
            <span class="weather-icon">🏙️</span>
            Meteo ${cityName}
        `;
    }
    
    // Aggiorna anche il titolo della pagina
    document.title = `Weather App - ${cityName}`;
}

function updateBreadcrumb(cityName) {
    const breadcrumbElement = document.getElementById('breadcrumbCity');
    if (breadcrumbElement) {
        breadcrumbElement.textContent = cityName;
    }
}

async function loadWeatherData(cityId) {
    showLoading();
    
    try {
        const response = await fetch(`/api/weather?city=${cityId}`);
        
        if (!response.ok) {
            throw new Error(`Errore HTTP: ${response.status} ${response.statusText}`);
        }
        
        const data = await response.json();
        
        if (!data || !data.dailyWeather || data.dailyWeather.length === 0) {
            throw new Error('Nessun dato meteorologico disponibile per questa città');
        }
        
        weatherData = data;
        hideLoading();
        displayWeatherData(data);
        
    } catch (error) {
        console.error('Errore nel caricamento dei dati:', error);
        hideLoading();
        showError(error.message);
    }
}

function showLoading() {
    const loadingContainer = document.getElementById('loadingContainer');
    const weatherDetail = document.getElementById('weatherDetail');
    const errorMessage = document.getElementById('errorMessage');
    
    if (loadingContainer) loadingContainer.style.display = 'flex';
    if (weatherDetail) weatherDetail.style.display = 'none';
    if (errorMessage) errorMessage.style.display = 'none';
}

function hideLoading() {
    const loadingContainer = document.getElementById('loadingContainer');
    if (loadingContainer) loadingContainer.style.display = 'none';
}

function showError(message) {
    const errorMessage = document.getElementById('errorMessage');
    const errorText = document.getElementById('errorText');
    const weatherDetail = document.getElementById('weatherDetail');
    
    if (errorText) errorText.textContent = message;
    if (errorMessage) errorMessage.style.display = 'block';
    if (weatherDetail) weatherDetail.style.display = 'none';
}

function displayWeatherData(data) {
    const weatherDetail = document.getElementById('weatherDetail');
    if (!weatherDetail) return;
    
    // Mostra la sezione dettagli
    weatherDetail.style.display = 'block';
    
    // Genera e mostra il riassunto
    displayWeatherSummary(data);
    
    // Genera e mostra la tabella
    displayWeatherTable(data);
    
    // Genera e mostra il grafico
    displayWeatherChart(data);
}

function displayWeatherSummary(data) {
    const summaryContainer = document.getElementById('weatherSummary');
    if (!summaryContainer) return;
    
    const latestData = data.dailyWeather[data.dailyWeather.length - 1];
    const averages = calculateAverages(data.dailyWeather);
    
    summaryContainer.innerHTML = `
        <div class="summary-card">
            <span class="summary-icon">${getWeatherIcon(latestData.description)}</span>
            <div class="summary-label">Condizioni Attuali</div>
            <div class="summary-value">${latestData.description || 'N/A'}</div>
        </div>
        <div class="summary-card">
            <span class="summary-icon">🌡️</span>
            <div class="summary-label">Temperatura Media</div>
            <div class="summary-value">${averages.temp.toFixed(1)}°C</div>
        </div>
        <div class="summary-card">
            <span class="summary-icon">⬆️</span>
            <div class="summary-label">Massima Assoluta</div>
            <div class="summary-value">${averages.maxTemp.toFixed(1)}°C</div>
        </div>
        <div class="summary-card">
            <span class="summary-icon">⬇️</span>
            <div class="summary-label">Minima Assoluta</div>
            <div class="summary-value">${averages.minTemp.toFixed(1)}°C</div>
        </div>
        <div class="summary-card">
            <span class="summary-icon">💧</span>
            <div class="summary-label">Umidità Media</div>
            <div class="summary-value">${averages.humidity ? averages.humidity.toFixed(0) + '%' : 'N/A'}</div>
        </div>
    `;
}

function calculateAverages(dailyWeather) {
    const validTemp = dailyWeather.filter(day => day.temperatureMean != null);
    const validHumidity = dailyWeather.filter(day => day.humidity != null);
    const allMaxTemps = dailyWeather.map(day => day.temperatureMax).filter(temp => temp != null);
    const allMinTemps = dailyWeather.map(day => day.temperatureMin).filter(temp => temp != null);
    
    return {
        temp: validTemp.length > 0 
            ? validTemp.reduce((sum, day) => sum + day.temperatureMean, 0) / validTemp.length 
            : 0,
        humidity: validHumidity.length > 0 
            ? validHumidity.reduce((sum, day) => sum + day.humidity, 0) / validHumidity.length 
            : null,
        maxTemp: allMaxTemps.length > 0 ? Math.max(...allMaxTemps) : 0,
        minTemp: allMinTemps.length > 0 ? Math.min(...allMinTemps) : 0
    };
}

function displayWeatherTable(data) {
    const tableWrapper = document.getElementById('tableWrapper');
    if (!tableWrapper) return;
    
    const tableHTML = `
        <table class="weather-table">
            <thead>
                <tr>
                    <th>Data</th>
                    <th>Condizioni</th>
                    <th>Max (°C)</th>
                    <th>Min (°C)</th>
                    <th>Media (°C)</th>
                    <th>Umidità (%)</th>
                </tr>
            </thead>
            <tbody>
                ${data.dailyWeather.map(day => `
                    <tr>
                        <td>${formatDate(day.date)}</td>
                        <td>${getWeatherIcon(day.description)} ${day.description || 'N/A'}</td>
                        <td>${day.temperatureMax ? day.temperatureMax.toFixed(1) : 'N/A'}</td>
                        <td>${day.temperatureMin ? day.temperatureMin.toFixed(1) : 'N/A'}</td>
                        <td>${day.temperatureMean ? day.temperatureMean.toFixed(1) : 'N/A'}</td>
                        <td>${day.humidity ? Math.round(day.humidity) : 'N/A'}</td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;
    
    tableWrapper.innerHTML = tableHTML;
}

function displayWeatherChart(data) {
    const canvas = document.getElementById('temperatureChart');
    if (!canvas) return;
    
    // Distruggi il grafico esistente se presente
    if (chart) {
        chart.destroy();
    }
    
    const ctx = canvas.getContext('2d');
    
    const labels = data.dailyWeather.map(day => formatDateShort(day.date));
    const maxTemps = data.dailyWeather.map(day => day.temperatureMax);
    const minTemps = data.dailyWeather.map(day => day.temperatureMin);
    const avgTemps = data.dailyWeather.map(day => day.temperatureMean);
    
    chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [
                {
                    label: 'Temperatura Massima',
                    data: maxTemps,
                    borderColor: '#ff6b6b',
                    backgroundColor: 'rgba(255, 107, 107, 0.1)',
                    borderWidth: 3,
                    fill: false,
                    tension: 0.4,
                    pointBackgroundColor: '#ff6b6b',
                    pointBorderColor: '#ffffff',
                    pointBorderWidth: 2,
                    pointRadius: 5
                },
                {
                    label: 'Temperatura Media',
                    data: avgTemps,
                    borderColor: '#4ecdc4',
                    backgroundColor: 'rgba(78, 205, 196, 0.1)',
                    borderWidth: 3,
                    fill: false,
                    tension: 0.4,
                    pointBackgroundColor: '#4ecdc4',
                    pointBorderColor: '#ffffff',
                    pointBorderWidth: 2,
                    pointRadius: 5
                },
                {
                    label: 'Temperatura Minima',
                    data: minTemps,
                    borderColor: '#667eea',
                    backgroundColor: 'rgba(102, 126, 234, 0.1)',
                    borderWidth: 3,
                    fill: false,
                    tension: 0.4,
                    pointBackgroundColor: '#667eea',
                    pointBorderColor: '#ffffff',
                    pointBorderWidth: 2,
                    pointRadius: 5
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            plugins: {
                title: {
                    display: true,
                    text: `Andamento Temperature - ${data.location}`,
                    font: {
                        size: 16,
                        weight: '600'
                    },
                    color: '#2d3748'
                },
                legend: {
                    position: 'top',
                    labels: {
                        usePointStyle: true,
                        padding: 20,
                        font: {
                            size: 12,
                            weight: '500'
                        }
                    }
                },
                tooltip: {
                    mode: 'index',
                    intersect: false,
                    backgroundColor: 'rgba(0, 0, 0, 0.8)',
                    titleColor: '#ffffff',
                    bodyColor: '#ffffff',
                    borderColor: '#667eea',
                    borderWidth: 1,
                    cornerRadius: 8,
                    displayColors: true
                }
            },
            scales: {
                x: {
                    display: true,
                    title: {
                        display: true,
                        text: 'Data',
                        font: {
                            size: 14,
                            weight: '600'
                        }
                    },
                    grid: {
                        color: 'rgba(0, 0, 0, 0.1)'
                    }
                },
                y: {
                    display: true,
                    title: {
                        display: true,
                        text: 'Temperatura (°C)',
                        font: {
                            size: 14,
                            weight: '600'
                        }
                    },
                    grid: {
                        color: 'rgba(0, 0, 0, 0.1)'
                    }
                }
            },
            interaction: {
                mode: 'nearest',
                axis: 'x',
                intersect: false
            }
        }
    });
}

function getWeatherIcon(description) {
    if (!description) return WEATHER_ICONS.default;
    return WEATHER_ICONS[description] || WEATHER_ICONS.default;
}

function formatDate(dateString) {
    const date = new Date(dateString);
    const options = { 
        weekday: 'short', 
        year: 'numeric', 
        month: 'short', 
        day: 'numeric' 
    };
    return date.toLocaleDateString('it-IT', options);
}

function formatDateShort(dateString) {
    const date = new Date(dateString);
    const options = { 
        month: 'short', 
        day: 'numeric' 
    };
    return date.toLocaleDateString('it-IT', options);
}

// Utilità per il debug
function getWeatherData() {
    return weatherData;
}

// Cleanup quando si lascia la pagina
window.addEventListener('beforeunload', function() {
    if (chart) {
        chart.destroy();
    }
});

// Esporta per debug (se necessario)
if (typeof window !== 'undefined') {
    window.CityWeatherApp = {
        getWeatherData,
        chart: () => chart,
        loadWeatherData
    };
}
