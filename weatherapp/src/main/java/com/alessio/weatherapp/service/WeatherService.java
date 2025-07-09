package com.alessio.weatherapp.service;

import java.time.LocalDate; // Modello per la risposta grezza dell'API Open-Meteo
import java.time.format.DateTimeFormatter; // Modello per i dati meteo usati dal frontend
import java.util.ArrayList; // Permette di dichiarare la classe come service Spring
import java.util.List; // Usa WebClient

import org.springframework.stereotype.Service; // Per costruire URL con parametri
import org.springframework.web.reactive.function.client.WebClient; // Per gestire le date
import org.springframework.web.util.UriComponentsBuilder; // Per formattare le date

import com.alessio.weatherapp.model.OpenMeteoResponse; // Lista dinamica
import com.alessio.weatherapp.model.WeatherData; // Interfaccia lista

@Service // Indica che questa classe è un service Spring
public class WeatherService {
    
    private final WebClient webClient; // Usa WebClient invece di RestTemplate
    private static final String OPEN_METEO_API_URL = "https://api.open-meteo.com/v1/forecast"; // URL base dell'API Open-Meteo
    
    public WeatherService() {
        this.webClient = WebClient.create(); // Inizializza WebClient
    }
    
    /**
     * Recupera i dati meteo delle ultime due settimane per una località.
     * @param location Nome della località
     * @param latitude Latitudine
     * @param longitude Longitudine
     * @return Oggetto WeatherData con i dati meteo
     */
    public WeatherData getWeatherData(String location, double latitude, double longitude) {
        // Calcola la data di fine (oggi)
        LocalDate endDate = LocalDate.now();
        // Calcola la data di inizio (due settimane fa)
        LocalDate startDate = endDate.minusWeeks(2);

        // Costruisce l'URL per la chiamata all'API OpenMeteo con i parametri richiesti
        String url = UriComponentsBuilder.fromUriString(OPEN_METEO_API_URL)
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("start_date", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .queryParam("end_date", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .queryParam("daily", "temperature_2m_max,temperature_2m_min,temperature_2m_mean,relative_humidity_2m_mean,weathercode")
                .queryParam("timezone", "auto")
                .build()
                .toUriString();
        
        try {
            // Effettua la chiamata HTTP GET e deserializza la risposta in OpenMeteoResponse
            OpenMeteoResponse response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(OpenMeteoResponse.class)
                    .block(); // Chiamata sincrona
            
            // Se la risposta e i dati giornalieri sono validi, converte in WeatherData
            if (response != null && response.getDaily() != null) {
                return convertToWeatherData(location, response);
            }
            // Se non ci sono dati, restituisce un oggetto vuoto
            return new WeatherData(location, new ArrayList<>());
            
        } catch (Exception e) {
            // In caso di errore, lancia un'eccezione con il messaggio
            throw new RuntimeException("Errore nel recupero dei dati meteorologici: " + e.getMessage());
        }
    }
    
    /**
     * Converte la risposta grezza di OpenMeteo in un oggetto WeatherData più semplice.
     * @param location Nome della località
     * @param response Risposta grezza dell'API
     * @return Oggetto WeatherData con lista di DailyWeather
     */
    private WeatherData convertToWeatherData(String location, OpenMeteoResponse response) {
        List<WeatherData.DailyWeather> dailyWeatherList = new ArrayList<>();
        OpenMeteoResponse.Daily daily = response.getDaily();
        for (int i = 0; i < daily.getTime().size(); i++) {
            LocalDate date = LocalDate.parse(daily.getTime().get(i));
            double maxTemp = daily.getTemperatureMax().get(i);
            double minTemp = daily.getTemperatureMin().get(i);
            double meanTemp = daily.getTemperatureMean().get(i);
            Double humidity = null;
            if (daily.getHumidityMean() != null && daily.getHumidityMean().size() > i) {
                humidity = daily.getHumidityMean().get(i);
            }
            String description = null;
            if (daily.getWeatherCode() != null && daily.getWeatherCode().size() > i) {
                description = mapWeatherCodeToDescription(daily.getWeatherCode().get(i));
            }
            WeatherData.DailyWeather dailyWeather = new WeatherData.DailyWeather(
                date, maxTemp, minTemp, meanTemp, humidity, description
            );
            dailyWeatherList.add(dailyWeather);
        }
        return new WeatherData(location, dailyWeatherList);

    }

    // Mappa i codici weathercode di Open-Meteo in descrizioni testuali
    private String mapWeatherCodeToDescription(Integer code) {
        if (code == null) return "";
        // Codici principali Open-Meteo: https://open-meteo.com/en/docs#api_form
        return switch (code) {
            case 0 -> "Sereno";
            case 1, 2, 3 -> "Parzialmente nuvoloso";
            case 45, 48 -> "Nebbia";
            case 51, 53, 55, 56, 57 -> "Pioviggine";
            case 61, 63, 65, 66, 67 -> "Pioggia";
            case 71, 73, 75, 77, 85, 86 -> "Neve";
            case 80, 81, 82 -> "Rovesci";
            case 95, 96, 99 -> "Temporale";
            default -> "Nuvoloso";
        };
    }
    
    /**
     * Restituisce le coordinate di una città italiana predefinita.
     * @param cityName Nome della città
     * @return Oggetto LocationInfo con nome, latitudine e longitudine
     */
    public LocationInfo getLocationInfo(String cityName) {
        // Switch sulle città supportate
        switch (cityName.toLowerCase()) {
            case "roma" -> {
                return new LocationInfo("Roma", 41.9028, 12.4964); // Roma
            }
            case "milano" -> {
                return new LocationInfo("Milano", 45.4642, 9.1900); // Milano
            }
            case "napoli" -> {
                return new LocationInfo("Napoli", 40.8518, 14.2681); // Napoli
            }
            case "torino" -> {
                return new LocationInfo("Torino", 45.0703, 7.6869); // Torino
            }
            case "firenze" -> {
                return new LocationInfo("Firenze", 43.7696, 11.2558); // Firenze
            }
            case "venezia" -> {
                return new LocationInfo("Venezia", 45.4408, 12.3155); // Venezia
            }
            case "bologna" -> {
                return new LocationInfo("Bologna", 44.4949, 11.3426); // Bologna
            }
            case "palermo" -> {
                return new LocationInfo("Palermo", 38.1157, 13.3615); // Palermo
            }
            case "genova" -> {
                return new LocationInfo("Genova", 44.4056, 8.9463); // Genova
            }
            case "bari" -> {
                return new LocationInfo("Bari", 41.1171, 16.8719); // Bari
            }
            default -> // Se la città non è supportata, lancia un'eccezione
                throw new IllegalArgumentException("Città non supportata. Città supportate: Roma, Milano, Napoli, Torino, Firenze, Venezia, Bologna, Palermo, Genova, Bari");
        }
    }
    
    /**
     * Classe che rappresenta le informazioni di una località (nome, latitudine, longitudine)
     */
    public static class LocationInfo {
        private final String name; // Nome della località
        private final double latitude; // Latitudine
        private final double longitude; // Longitudine
        
        public LocationInfo(String name, double latitude, double longitude) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }
        
        public String getName() {
            return name;
        }
        
        public double getLatitude() {
            return latitude;
        }
        
        public double getLongitude() {
            return longitude;
        }
    }
}
