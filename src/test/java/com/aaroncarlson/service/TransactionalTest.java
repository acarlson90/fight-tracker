package com.aaroncarlson.service;

import com.aaroncarlson.model.City;
import com.aaroncarlson.model.Flight;
import com.aaroncarlson.util.AppConstants;
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
public class TransactionalTest {

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
    public void testSuccessfulCreateFlight() throws Exception {
        final City sanFrancisco = cityService.getCityByName(TestConstants.SAN_FRANCISCO);
        final City barcelona = cityService.getCityByName(TestConstants.BARCELONA);

        flightService.createFlight(new Flight(sanFrancisco, barcelona, TestConstants.NOW));

        assertThat(flightService.getAllFlights(Integer.parseInt(AppConstants.DEFAULT_PAGE_NUMBER),
                Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE)).getContent()).hasSize(1);
    }

    @Test
    public void testUnsuccessfulEmptyCreateFlight() {
        try {
            flightService.createFlight(new Flight());
        } catch (Exception exception) {

        }

        assertThat(flightService.getAllFlights(Integer.parseInt(AppConstants.DEFAULT_PAGE_NUMBER),
                Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE)).getContent()).hasSize(0);
    }

    @Test
    public void testUnsuccessfulCreateFlightSameDepartureAndArrivalCities() throws Exception {
        final City barcelona = cityService.getCityByName(TestConstants.BARCELONA);

        try {
            flightService.createFlight(new Flight(barcelona, barcelona, TestConstants.NOW));
        } catch (Exception exception) {

        }

        assertThat(flightService.getAllFlights(Integer.parseInt(AppConstants.DEFAULT_PAGE_NUMBER),
                Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE)).getContent()).hasSize(0);
    }

    @After
    public void teardown() {
        flightService.deleteAllFlights();
        cityService.deleteAllCities();
    }

}
