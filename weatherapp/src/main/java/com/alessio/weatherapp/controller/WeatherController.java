package com.alessio.weatherapp.controller;

import com.alessio.weatherapp.model.WeatherData;
import com.alessio.weatherapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {
    
    @Autowired
    private WeatherService weatherService;
    
    @GetMapping("/api/weather")
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
}
