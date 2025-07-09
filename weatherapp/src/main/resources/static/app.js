

// Lista delle città
const cities = [
    'roma', 'milano', 'napoli', 'torino', 'firenze', 'venezia', 'bologna', 'palermo', 'genova', 'bari'
];

// Funzione per scegliere icona meteo base (puoi migliorare con dati reali)
function getWeatherIcon(desc) {
    if (!desc) return '☁️';
    desc = desc.toLowerCase();
    if (desc.includes('pioggia')) return '🌧️';
    if (desc.includes('sereno')) return '☀️';
    if (desc.includes('nuvol')) return '☁️';
    if (desc.includes('neve')) return '❄️';
    return '☁️';
}

document.addEventListener('DOMContentLoaded', function() {
    // Click sulle card
    const cards = document.querySelectorAll('.city-card');
    cards.forEach(card => {
        card.addEventListener('click', function() {
            const city = card.getAttribute('data-city');
            window.location.href = `/city.html?city=${city}`;
        });
    });

    // Carica dati meteo per ogni città
    cities.forEach(city => {
        fetch(`/api/weather?city=${city}`)
            .then(response => response.ok ? response.json() : null)
            .then(data => {
                if (!data || !data.dailyWeather || data.dailyWeather.length === 0) return;
                // Prendi il giorno più recente
                const today = data.dailyWeather[data.dailyWeather.length - 1];
                // Temperatura media
                const temp = today.temperatureMean ? today.temperatureMean.toFixed(1) : '--';
                // Umidità (se disponibile)
                const hum = today.humidity !== undefined ? today.humidity + '%' : '--%';
                // Icona (se disponibile)
                let icon = '☁️';
                if (today.description) icon = getWeatherIcon(today.description);
                // Aggiorna card
                const tempEl = document.getElementById(`temp-${city}`);
                const humEl = document.getElementById(`hum-${city}`);
                const iconEl = document.getElementById(`icon-${city}`);
                if (tempEl) tempEl.textContent = temp + '°C';
                if (humEl) humEl.textContent = hum + ' 💧';
                if (iconEl) iconEl.textContent = icon;
            })
            .catch(() => {});
    });
});
