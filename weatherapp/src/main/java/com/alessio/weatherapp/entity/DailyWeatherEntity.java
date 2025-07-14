package com.alessio.weatherapp.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "daily_weather")
public class DailyWeatherEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(name = "temperature_max", nullable = false)
    private Double temperatureMax;
    
    @Column(name = "temperature_min", nullable = false)
    private Double temperatureMin;
    
    @Column(name = "temperature_mean", nullable = false)
    private Double temperatureMean;
    
    @Column(name = "humidity")
    private Double humidity;
    
    @Column(name = "description")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weather_data_id", nullable = false)
    private WeatherDataEntity weatherData;
    
    // Costruttori
    public DailyWeatherEntity() {}
    
    public DailyWeatherEntity(LocalDate date, Double temperatureMax, Double temperatureMin, 
                              Double temperatureMean, Double humidity, String description) {
        this.date = date;
        this.temperatureMax = temperatureMax;
        this.temperatureMin = temperatureMin;
        this.temperatureMean = temperatureMean;
        this.humidity = humidity;
        this.description = description;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public Double getTemperatureMax() {
        return temperatureMax;
    }
    
    public void setTemperatureMax(Double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }
    
    public Double getTemperatureMin() {
        return temperatureMin;
    }
    
    public void setTemperatureMin(Double temperatureMin) {
        this.temperatureMin = temperatureMin;
    }
    
    public Double getTemperatureMean() {
        return temperatureMean;
    }
    
    public void setTemperatureMean(Double temperatureMean) {
        this.temperatureMean = temperatureMean;
    }
    
    public Double getHumidity() {
        return humidity;
    }
    
    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public WeatherDataEntity getWeatherData() {
        return weatherData;
    }
    
    public void setWeatherData(WeatherDataEntity weatherData) {
        this.weatherData = weatherData;
    }
}
