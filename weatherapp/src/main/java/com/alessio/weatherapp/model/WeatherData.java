package com.alessio.weatherapp.model;

import java.time.LocalDate;
import java.util.List;

public class WeatherData {
    private String location;
    private List<DailyWeather> dailyWeather;
    
    public WeatherData() {}
    
    public WeatherData(String location, List<DailyWeather> dailyWeather) {
        this.location = location;
        this.dailyWeather = dailyWeather;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public List<DailyWeather> getDailyWeather() {
        return dailyWeather;
    }
    
    public void setDailyWeather(List<DailyWeather> dailyWeather) {
        this.dailyWeather = dailyWeather;
    }
    
    public static class DailyWeather {
        private LocalDate date;
        private double temperatureMax;
        private double temperatureMin;
        private double temperatureMean;
        
        public DailyWeather() {}
        
        public DailyWeather(LocalDate date, double temperatureMax, double temperatureMin, double temperatureMean) {
            this.date = date;
            this.temperatureMax = temperatureMax;
            this.temperatureMin = temperatureMin;
            this.temperatureMean = temperatureMean;
        }
        
        public LocalDate getDate() {
            return date;
        }
        
        public void setDate(LocalDate date) {
            this.date = date;
        }
        
        public double getTemperatureMax() {
            return temperatureMax;
        }
        
        public void setTemperatureMax(double temperatureMax) {
            this.temperatureMax = temperatureMax;
        }
        
        public double getTemperatureMin() {
            return temperatureMin;
        }
        
        public void setTemperatureMin(double temperatureMin) {
            this.temperatureMin = temperatureMin;
        }
        
        public double getTemperatureMean() {
            return temperatureMean;
        }
        
        public void setTemperatureMean(double temperatureMean) {
            this.temperatureMean = temperatureMean;
        }
    }
}
