package com.alessio.weatherapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alessio.weatherapp.entity.DailyWeatherEntity;
import com.alessio.weatherapp.entity.WeatherDataEntity;
import com.alessio.weatherapp.model.WeatherData;
import com.alessio.weatherapp.repository.WeatherDataRepository;

@Service
@Transactional
public class WeatherPersistenceService {
    
    private static final Logger logger = LoggerFactory.getLogger(WeatherPersistenceService.class);
    
    @Autowired
    private WeatherDataRepository weatherDataRepository;
    
    /**
     * Salva i dati meteo nel database
     */
    public WeatherDataEntity saveWeatherData(WeatherData weatherData) {
        logger.info("Salvando dati meteo per la località: {}", weatherData.getLocation());
        
        try {
            // Crea l'entità principale
            WeatherDataEntity entity = new WeatherDataEntity(weatherData.getLocation());
            
            // Converte e aggiunge i dati giornalieri
            if (weatherData.getDailyWeather() != null) {
                for (WeatherData.DailyWeather daily : weatherData.getDailyWeather()) {
                    DailyWeatherEntity dailyEntity = new DailyWeatherEntity(
                        daily.getDate(),
                        daily.getTemperatureMax(),
                        daily.getTemperatureMin(),
                        daily.getTemperatureMean(),
                        daily.getHumidity(),
                        daily.getDescription()
                    );
                    entity.addDailyWeather(dailyEntity);
                }
            }
            
            // Salva nel database
            WeatherDataEntity savedEntity = weatherDataRepository.save(entity);
            logger.info("Dati meteo salvati con successo con ID: {}", savedEntity.getId());
            
            return savedEntity;
            
        } catch (Exception e) {
            logger.error("Errore durante il salvataggio dei dati meteo per {}: {}", 
                        weatherData.getLocation(), e.getMessage(), e);
            throw new RuntimeException("Errore durante il salvataggio dei dati meteo", e);
        }
    }
    
    /**
     * Recupera tutti i dati meteo per una località
     */
    @Transactional(readOnly = true)
    public List<WeatherDataEntity> getWeatherDataByLocation(String location) {
        logger.debug("Recuperando dati meteo per la località: {}", location);
        return weatherDataRepository.findByLocationIgnoreCaseOrderByCreatedAtDesc(location);
    }
    
    /**
     * Recupera i dati meteo più recenti per una località
     */
    @Transactional(readOnly = true)
    public Optional<WeatherDataEntity> getLatestWeatherDataByLocation(String location) {
        logger.debug("Recuperando dati meteo più recenti per la località: {}", location);
        return weatherDataRepository.findFirstByLocationIgnoreCaseOrderByCreatedAtDesc(location);
    }
    
    /**
     * Recupera tutti i dati meteo più recenti di una certa data
     */
    @Transactional(readOnly = true)
    public List<WeatherDataEntity> getWeatherDataAfter(LocalDateTime date) {
        logger.debug("Recuperando dati meteo dopo: {}", date);
        return weatherDataRepository.findByCreatedAtAfter(date);
    }
    
    /**
     * Recupera le statistiche delle località più cercate
     */
    @Transactional(readOnly = true)
    public List<LocationStats> getMostSearchedLocations() {
        logger.debug("Recuperando statistiche località più cercate");
        List<Object[]> results = weatherDataRepository.findMostSearchedLocations();
        
        return results.stream()
                .map(result -> new LocationStats((String) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }
    
    /**
     * Conta le ricerche per una località
     */
    @Transactional(readOnly = true)
    public long countSearchesByLocation(String location) {
        return weatherDataRepository.countByLocationIgnoreCase(location);
    }
    
    /**
     * Elimina i dati più vecchi di una certa data per mantenere il database pulito
     */
    public void cleanOldData(LocalDateTime beforeDate) {
        logger.info("Eliminando dati più vecchi di: {}", beforeDate);
        weatherDataRepository.deleteByCreatedAtBefore(beforeDate);
    }
    
    /**
     * Converte un'entità WeatherDataEntity in WeatherData (modello)
     */
    @Transactional(readOnly = true)
    public WeatherData convertToModel(WeatherDataEntity entity) {
        WeatherData weatherData = new WeatherData();
        weatherData.setLocation(entity.getLocation());
        
        List<WeatherData.DailyWeather> dailyWeatherList = entity.getDailyWeather().stream()
                .map(daily -> new WeatherData.DailyWeather(
                    daily.getDate(),
                    daily.getTemperatureMax(),
                    daily.getTemperatureMin(),
                    daily.getTemperatureMean(),
                    daily.getHumidity(),
                    daily.getDescription()
                ))
                .collect(Collectors.toList());
        
        weatherData.setDailyWeather(dailyWeatherList);
        return weatherData;
    }
    
    /**
     * Classe interna per le statistiche delle località
     */
    public static class LocationStats {
        private final String location;
        private final Long searchCount;
        
        public LocationStats(String location, Long searchCount) {
            this.location = location;
            this.searchCount = searchCount;
        }
        
        public String getLocation() {
            return location;
        }
        
        public Long getSearchCount() {
            return searchCount;
        }
    }
}
