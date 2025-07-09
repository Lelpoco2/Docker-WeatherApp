document.getElementById('getWeather').addEventListener('click', function() {
    const city = document.getElementById('city').value;
    const resultDiv = document.getElementById('result');
    resultDiv.innerHTML = 'Caricamento...';
    fetch(`/api/weather?city=${city}`)
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text || 'Errore nella richiesta'); });
            }
            return response.json();
        })
        .then(data => {
            if (!data.dailyWeather || data.dailyWeather.length === 0) {
                resultDiv.innerHTML = '<p>Nessun dato disponibile.</p>';
                return;
            }
            let html = `<h2>Meteo per ${data.location}</h2>`;
            html += '<table><thead><tr><th>Data</th><th>Max (°C)</th><th>Min (°C)</th><th>Media (°C)</th></tr></thead><tbody>';
            data.dailyWeather.forEach(day => {
                html += `<tr><td>${day.date}</td><td>${day.temperatureMax.toFixed(1)}</td><td>${day.temperatureMin.toFixed(1)}</td><td>${day.temperatureMean.toFixed(1)}</td></tr>`;
            });
            html += '</tbody></table>';
            resultDiv.innerHTML = html;
        })
        .catch(err => {
            resultDiv.innerHTML = `<p style='color:red;'>${err.message}</p>`;
        });
});
