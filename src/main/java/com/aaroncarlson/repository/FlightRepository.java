package com.aaroncarlson.repository;

import com.aaroncarlson.model.City;
import com.aaroncarlson.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByDepartureCityIn(List<City> cities);
    List<Flight> findByDepartureCityNameIn(List<String> cityNames);
    Optional<Flight> findByDepartureCityAndArrivalCity(City departureCity, City arrivalCity);
    Optional<Flight> findByDepartureCityAndArrivalCityAndTime(City departureCity, City arrivalCity, LocalDateTime time);
    List<Flight> findByTimeBetween(LocalDateTime startRange, LocalDateTime endRange);
    List<Flight> findByTimeBefore(LocalDateTime time);
    List<Flight> findByTime(LocalDateTime time);
    List<Flight> findByTimeAfter(LocalDateTime time);
    List<Flight> findByPriceLessThan(double price);
    List<Flight> findByPriceLessThanEqual(double price);
    List<Flight> findByPriceGreaterThan(double price);
    List<Flight> findByPriceGreaterThanEqual(double price);
    List<Flight> findByPriceGreaterThanEqualAndPriceLessThanEqual(double startRange, double endRange);
    // Method name and parameter names is up to the developer, only thing that matters is the actual JQuery
    @Query("SELECT f FROM Flight f WHERE f.price >= :startRange AND f.price <= :endRange AND f.departureCity.name LIKE :departureCityName")
    Page<Flight> queryByPriceRangeAndDepartureCityName(@Param("startRange") double startRange,
                                                       @Param("endRange") double endRange,
                                                       @Param("departureCityName") String departureCityName,
                                                       Pageable pageable);
}
