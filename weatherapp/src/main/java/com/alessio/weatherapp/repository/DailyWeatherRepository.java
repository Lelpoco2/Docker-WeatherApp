package com.alessio.weatherapp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alessio.weatherapp.entity.DailyWeatherEntity;

@Repository
public interface DailyWeatherRepository extends JpaRepository<DailyWeatherEntity, Long> {
    
    /**
     * Trova tutti i dati giornalieri per uno specifico WeatherData ID
     */
    List<DailyWeatherEntity> findByWeatherDataId(Long weatherDataId);
    
    /**
     * Trova i dati giornalieri per una data specifica
     */
    List<DailyWeatherEntity> findByDate(LocalDate date);
    
    /**
     * Trova i dati giornalieri in un range di date
     */
    List<DailyWeatherEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Trova i dati giornalieri per una località e data specifica
     */
    @Query("SELECT d FROM DailyWeatherEntity d " +
           "JOIN d.weatherData w " +
           "WHERE LOWER(w.location) = LOWER(:location) AND d.date = :date")
    List<DailyWeatherEntity> findByLocationAndDate(@Param("location") String location, 
                                                   @Param("date") LocalDate date);
    
    /**
     * Trova i dati giornalieri per una località in un range di date
     */
    @Query("SELECT d FROM DailyWeatherEntity d " +
           "JOIN d.weatherData w " +
           "WHERE LOWER(w.location) = LOWER(:location) " +
           "AND d.date BETWEEN :startDate AND :endDate " +
           "ORDER BY d.date")
    List<DailyWeatherEntity> findByLocationAndDateRange(@Param("location") String location,
                                                        @Param("startDate") LocalDate startDate,
                                                        @Param("endDate") LocalDate endDate);
    
    /**
     * Trova la temperatura media per una località
     */
    @Query("SELECT AVG(d.temperatureMean) FROM DailyWeatherEntity d " +
           "JOIN d.weatherData w " +
           "WHERE LOWER(w.location) = LOWER(:location)")
    Double findAverageTemperatureByLocation(@Param("location") String location);
}
