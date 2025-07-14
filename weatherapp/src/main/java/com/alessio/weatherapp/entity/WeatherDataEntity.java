package com.alessio.weatherapp.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "weather_data")
public class WeatherDataEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String location;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "weatherData", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DailyWeatherEntity> dailyWeather = new ArrayList<>();
    
    // Costruttori
    public WeatherDataEntity() {
        this.createdAt = LocalDateTime.now();
    }
    
    public WeatherDataEntity(String location) {
        this();
        this.location = location;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public List<DailyWeatherEntity> getDailyWeather() {
        return dailyWeather;
    }
    
    public void setDailyWeather(List<DailyWeatherEntity> dailyWeather) {
        this.dailyWeather = dailyWeather;
        // Imposta la relazione bidirezionale
        for (DailyWeatherEntity daily : dailyWeather) {
            daily.setWeatherData(this);
        }
    }
    
    // Metodo di utilità per aggiungere dati giornalieri
    public void addDailyWeather(DailyWeatherEntity dailyWeather) {
        this.dailyWeather.add(dailyWeather);
        dailyWeather.setWeatherData(this);
    }
}
