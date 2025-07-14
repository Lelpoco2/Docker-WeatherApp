package com.alessio.weatherapp.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alessio.weatherapp.entity.WeatherDataEntity;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherDataEntity, Long> {
    
    /**
     * Trova tutti i dati meteo per una specifica località
     */
    List<WeatherDataEntity> findByLocationIgnoreCase(String location);
    
    /**
     * Trova tutti i dati meteo per una località ordinati per data di creazione (più recenti prima)
     */
    List<WeatherDataEntity> findByLocationIgnoreCaseOrderByCreatedAtDesc(String location);
    
    /**
     * Trova il dato meteo più recente per una località
     */
    Optional<WeatherDataEntity> findFirstByLocationIgnoreCaseOrderByCreatedAtDesc(String location);
    
    /**
     * Trova tutti i dati meteo creati dopo una certa data
     */
    List<WeatherDataEntity> findByCreatedAtAfter(LocalDateTime date);
    
    /**
     * Trova tutti i dati meteo per località e creati dopo una certa data
     */
    List<WeatherDataEntity> findByLocationIgnoreCaseAndCreatedAtAfter(String location, LocalDateTime date);
    
    /**
     * Query personalizzata per trovare le località più frequentemente ricercate
     */
    @Query("SELECT w.location, COUNT(w) as count " +
           "FROM WeatherDataEntity w " +
           "GROUP BY w.location " +
           "ORDER BY COUNT(w) DESC")
    List<Object[]> findMostSearchedLocations();
    
    /**
     * Conta quante ricerche sono state fatte per una località
     */
    long countByLocationIgnoreCase(String location);
    
    /**
     * Elimina tutti i dati più vecchi di una certa data
     */
    void deleteByCreatedAtBefore(LocalDateTime date);
}
