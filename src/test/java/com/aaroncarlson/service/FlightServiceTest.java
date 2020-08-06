package com.aaroncarlson.service;

import com.aaroncarlson.model.City;
import com.aaroncarlson.model.Flight;
import com.aaroncarlson.payload.PagedResponse;
import com.aaroncarlson.util.AppConstants;
import com.aaroncarlson.util.TestConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlightServiceTest {

    @Autowired
    private FlightService flightService;
    @Autowired
    private CityService cityService;

    @Before
    public void setup() {
        final City sanFrancisco = new City(TestConstants.SAN_FRANCISCO);
        final City barcelona = new City(TestConstants.BARCELONA);
        List<City> cities = new ArrayList<>();
        cities.add(sanFrancisco);
        cities.add(barcelona);

        for (City city : cities) {
            cityService.createCity(city);
        }
    }

    @Test
    public void testCRUDOperations() throws Exception {
        final City sanFrancisco = cityService.getCityByName(TestConstants.SAN_FRANCISCO);
        final City barcelona = cityService.getCityByName(TestConstants.BARCELONA);

        // Create
        final Flight departureFlight = new Flight(450, sanFrancisco, barcelona, TestConstants.NOW_MINUS_ONE_MONTH);
        final Flight returnFlight = new Flight(500, barcelona, sanFrancisco, TestConstants.NOW_PLUS_ONE_MONTH);

        flightService.createFlight(departureFlight);
        flightService.createFlight(returnFlight);

        // Read
        PagedResponse<Flight> flightPagedResponse = flightService.getAllFlights(Integer.parseInt(AppConstants.DEFAULT_PAGE_NUMBER),
                Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE));
        assertThat(flightPagedResponse.getTotalElements()).isEqualTo(2);
        assertThat(flightPagedResponse.getContent()).hasSize(2);

        final Flight persistedDepartureFlight = flightService
                .getFlightByDepartureAndArrivalCityNames(TestConstants.SAN_FRANCISCO, TestConstants.BARCELONA);
        final Flight persistedReturnFlight = flightService
                .getFlightByDepartureAndArrivalCityNames(TestConstants.BARCELONA, TestConstants.SAN_FRANCISCO);
        assertThat(persistedDepartureFlight.getDepartureCity().getName()).isEqualTo(TestConstants.SAN_FRANCISCO);
        assertThat(persistedDepartureFlight.getArrivalCity().getName()).isEqualTo(TestConstants.BARCELONA);
        assertThat(persistedReturnFlight.getDepartureCity().getName()).isEqualTo(TestConstants.BARCELONA);
        assertThat(persistedReturnFlight.getArrivalCity().getName()).isEqualTo(TestConstants.SAN_FRANCISCO);

        // Delete
        flightService.deleteFlight(persistedReturnFlight.getId());

        assertThat(flightService.count()).isOne();

    }

    @After
    public void cleanup() {
        flightService.deleteAllFlights();
        cityService.deleteAllCities();
    }

}
