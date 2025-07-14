package com.alessio.weatherapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alessio.weatherapp.model.WeatherData;
import com.alessio.weatherapp.service.WeatherPersistenceService;
import com.alessio.weatherapp.service.WeatherService;

@RestController
public class WeatherController {
    
    @Autowired
    private WeatherService weatherService;
    
    @GetMapping("/api/weather")
    @SuppressWarnings("CallToPrintStackTrace")
    public WeatherData getWeather(@RequestParam("city") String city) {
        try {
            WeatherService.LocationInfo locationInfo = weatherService.getLocationInfo(city);
            return weatherService.getWeatherData(
                    locationInfo.getName(), 
                    locationInfo.getLatitude(), 
                    locationInfo.getLongitude()
            );
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Endpoint per recuperare i dati meteo storici di una città dal database
     */
    @GetMapping("/api/weather/history")
    public ResponseEntity<List<WeatherData>> getWeatherHistory(@RequestParam("city") String city) {
        try {
            List<WeatherData> historicalData = weatherService.getHistoricalWeatherData(city);
            return ResponseEntity.ok(historicalData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    
    /**
     * Endpoint per recuperare gli ultimi dati meteo salvati di una città
     */
    @GetMapping("/api/weather/latest/{city}")
    public ResponseEntity<WeatherData> getLatestWeatherData(@PathVariable String city) {
        try {
            WeatherData latestData = weatherService.getLatestWeatherDataFromDB(city);
            if (latestData != null) {
                return ResponseEntity.ok(latestData);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    
    /**
     * Endpoint per recuperare le statistiche delle località più cercate
     */
    @GetMapping("/api/weather/stats/popular-locations")
    public ResponseEntity<List<WeatherPersistenceService.LocationStats>> getPopularLocations() {
        try {
            List<WeatherPersistenceService.LocationStats> stats = weatherService.getMostSearchedLocations();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
