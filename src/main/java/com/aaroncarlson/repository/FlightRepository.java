package com.aaroncarlson.repository;

import com.aaroncarlson.model.City;
import com.aaroncarlson.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    Optional<Flight> findByDepartureCityAndArrivalCity(City departureCity, City arrivalCity);

}
