// city.js
// Gestisce la pagina di dettaglio città: tabella + grafico temperature

function getCityFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get('city');
}

function capitalize(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}

document.addEventListener('DOMContentLoaded', function() {
    const city = getCityFromUrl();
    const resultDiv = document.getElementById('result');
    const loader = document.getElementById('loader');
    const cityTitle = document.getElementById('city-title');
    const chartCanvas = document.getElementById('tempChart');

    if (!city) {
        resultDiv.innerHTML = '<p style="color:red;">Città non specificata.</p>';
        return;
    }
    cityTitle.innerHTML = `<span class='icon'>🌆</span> Meteo per ${capitalize(city)}`;
    loader.style.display = 'block';
    fetch(`/api/weather?city=${city}`)
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text || 'Errore nella richiesta'); });
            }
            return response.json();
        })
        .then(data => {
            loader.style.display = 'none';
            if (!data.dailyWeather || data.dailyWeather.length === 0) {
                resultDiv.innerHTML = '<p>Nessun dato disponibile.</p>';
                return;
            }
            let html = `<h2 style='color:#5f27cd;'>Meteo per ${data.location}</h2>`;
            html += '<table><thead><tr><th>Data</th><th>Max (°C)</th><th>Min (°C)</th><th>Media (°C)</th></tr></thead><tbody>';
            const labels = [];
            const maxs = [], mins = [], means = [];
            data.dailyWeather.forEach(day => {
                html += `<tr><td>${day.date}</td><td>${day.temperatureMax.toFixed(1)}</td><td>${day.temperatureMin.toFixed(1)}</td><td>${day.temperatureMean.toFixed(1)}</td></tr>`;
                labels.push(day.date);
                maxs.push(day.temperatureMax);
                mins.push(day.temperatureMin);
                means.push(day.temperatureMean);
            });
            html += '</tbody></table>';
            resultDiv.innerHTML = html;
            // Mostra grafico
            chartCanvas.style.display = 'block';
            new Chart(chartCanvas, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [
                        {
                            label: 'Max (°C)',
                            data: maxs,
                            borderColor: '#e17055',
                            backgroundColor: 'rgba(225,112,85,0.08)',
                            fill: false,
                            tension: 0.2
                        },
                        {
                            label: 'Min (°C)',
                            data: mins,
                            borderColor: '#0984e3',
                            backgroundColor: 'rgba(9,132,227,0.08)',
                            fill: false,
                            tension: 0.2
                        },
                        {
                            label: 'Media (°C)',
                            data: means,
                            borderColor: '#00b894',
                            backgroundColor: 'rgba(0,184,148,0.08)',
                            fill: false,
                            borderDash: [5,5],
                            tension: 0.2
                        }
                    ]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: { position: 'top' },
                        title: { display: true, text: 'Andamento temperature ultime 2 settimane' }
                    },
                    scales: {
                        y: { beginAtZero: false }
                    }
                }
            });
        })
        .catch(err => {
            loader.style.display = 'none';
            resultDiv.innerHTML = `<p style='color:#d63031;font-weight:bold;'>${err.message}</p>`;
        });
});
