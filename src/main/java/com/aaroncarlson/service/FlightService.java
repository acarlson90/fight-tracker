package com.aaroncarlson.service;

import com.aaroncarlson.exception.BadRequestException;
import com.aaroncarlson.exception.ResourceNotFoundException;
import com.aaroncarlson.model.City;
import com.aaroncarlson.model.Flight;
import com.aaroncarlson.payload.PagedResponse;
import com.aaroncarlson.repository.FlightRepository;
import com.aaroncarlson.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class FlightService extends PagedResponseService {

    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private CityService cityService;

    public long count() {
        return flightRepository.count();
    }

    public Flight getFlightById(final long id) {
        return flightRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("Flight", "id", Long.toString(id)));
    }

    public Flight getFlightByDepartureAndArrivalCityNames(final String departureCityName, final String arrivalCityName) {
        City departureCity = cityService.getCityByName(departureCityName);
        City arrivalCity = cityService.getCityByName(arrivalCityName);

        return getFlightByDepartureAndArrivalCities(departureCity, arrivalCity);
    }

    private Flight getFlightByDepartureAndArrivalCities(final City departureCity, final City arrivalCity) {
        return flightRepository.findByDepartureCityAndArrivalCity(departureCity, arrivalCity).orElseThrow(
                () -> new ResourceNotFoundException("Flight", "Departure OR Arrival City",
                        "Departure City: '" + departureCity.getName() + "', Arrival City: '" + arrivalCity.getName() + "'"));
    }

    public PagedResponse<Flight> getAllFlights(final int page, final int size) {
        validatePageNumberAndSize(page, size);

        // Retrieve Flights
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);
        Page<Flight> flightPage = flightRepository.findAll(pageable);

        if (flightPage.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), flightPage.getNumber(),
                    flightPage.getSize(), flightPage.getTotalElements(), flightPage.getTotalPages(), flightPage.isLast());
        }

        List<Flight> flights = flightPage.map(flight -> flight).getContent();

        return new PagedResponse<>(flights, flightPage.getNumber(), flightPage.getSize(),
                flightPage.getTotalElements(), flightPage.getTotalPages(), flightPage.isLast());
    }

    @Transactional
    public Flight createFlight(final Flight flight) {
        City persistedDepartureCity = cityService.getCityByName(flight.getDepartureCity().getName());
        City persistedArrivalCity = cityService.getCityByName(flight.getArrivalCity().getName());

        /**
         * Intentionally SAVING flight BEFORE checking if arrival and departure cities are identical to demonstrate
         * that @Transactional roles back the DB
         */
        Flight returnFlight = flightRepository.save(new Flight(400, persistedDepartureCity, persistedArrivalCity, flight.getTime()));

        if (persistedDepartureCity.equals(persistedArrivalCity)) {
            throw new BadRequestException("Flight departure and arrival city cannot be the same");
        }

        return returnFlight;
    }

    @Transactional
    public Flight editFlight(final long id, final Flight flight) {
        Flight persistedFlight = getFlightById(id);
        City persistedDepartureCity = cityService.getCityByName(flight.getDepartureCity().getName());
        City persistedArrivalCity = cityService.getCityByName(flight.getArrivalCity().getName());

        if (persistedFlight.equals(flight)) {
            throw new BadRequestException("Flight is identical to '" + id + "'");
        }

        if (!persistedFlight.getDepartureCity().equals(persistedDepartureCity)) {
            persistedFlight.setDepartureCity(persistedDepartureCity);
        }

        if (!persistedFlight.getArrivalCity().equals(persistedArrivalCity)) {
            persistedFlight.setArrivalCity(persistedArrivalCity);
        }

        if (!persistedFlight.getTime().equals(flight.getTime())) {
            persistedFlight.setTime(flight.getTime());
        }

        return flightRepository.save(persistedFlight);
    }

    @Transactional
    public void deleteFlight(final long id) {
        final Flight flight = getFlightById(id);
        flightRepository.deleteById(flight.getId());
    }

    public void deleteAllFlights() {
        flightRepository.deleteAll();
    }
}
