package com.alessio.weatherapp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenMeteoResponse {
    private double latitude;
    private double longitude;
    @JsonProperty("timezone")
    private String timezone;
    @JsonProperty("daily")
    private Daily daily;
    
    public OpenMeteoResponse() {}
    
    public double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public String getTimezone() {
        return timezone;
    }
    
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    
    public Daily getDaily() {
        return daily;
    }
    
    public void setDaily(Daily daily) {
        this.daily = daily;
    }
    
    public static class Daily {
        @JsonProperty("time")
        private List<String> time;
        @JsonProperty("temperature_2m_max")
        private List<Double> temperatureMax;
        @JsonProperty("temperature_2m_min")
        private List<Double> temperatureMin;
        @JsonProperty("temperature_2m_mean")
        private List<Double> temperatureMean;
        @JsonProperty("relative_humidity_2m_mean")
        private List<Double> humidityMean;
        @JsonProperty("weathercode")
        private List<Integer> weatherCode;
        public Daily() {}
        
        public List<String> getTime() { return time; }
        public void setTime(List<String> time) { this.time = time; }
        public List<Double> getTemperatureMax() { return temperatureMax; }
        public void setTemperatureMax(List<Double> temperatureMax) { this.temperatureMax = temperatureMax; }
        public List<Double> getTemperatureMin() { return temperatureMin; }
        public void setTemperatureMin(List<Double> temperatureMin) { this.temperatureMin = temperatureMin; }
        public List<Double> getTemperatureMean() { return temperatureMean; }
        public void setTemperatureMean(List<Double> temperatureMean) { this.temperatureMean = temperatureMean; }
        public List<Double> getHumidityMean() { return humidityMean; }
        public void setHumidityMean(List<Double> humidityMean) { this.humidityMean = humidityMean; }
        public List<Integer> getWeatherCode() { return weatherCode; }
        public void setWeatherCode(List<Integer> weatherCode) { this.weatherCode = weatherCode; }
    }
}
