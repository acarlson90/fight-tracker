package com.aaroncarlson.repository;

import com.aaroncarlson.model.City;
import com.aaroncarlson.model.Flight;
import com.aaroncarlson.util.AppConstants;
import com.aaroncarlson.util.TestConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlightRepositoryTest {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private FlightRepository flightRepository;
    private City sanFrancisco;
    private City barcelona;
    private Flight sanFrancisco2BarcelonaFlight;
    private Flight barcelona2SanFranciscoFlight;
    private Optional flightOptional;
    private List<Flight> flights;

    @Before
    public void setup() {
        sanFrancisco = cityRepository.save(new City(TestConstants.SAN_FRANCISCO));
        barcelona = cityRepository.save(new City(TestConstants.BARCELONA));
        sanFrancisco2BarcelonaFlight = flightRepository.save(new Flight (450,sanFrancisco, barcelona, TestConstants.NOW_MINUS_ONE_MONTH));
        barcelona2SanFranciscoFlight = flightRepository.save(new Flight(500, barcelona, sanFrancisco, TestConstants.NOW_PLUS_ONE_MONTH));

    }

    @Test
    public void testFindFlightByArrivalAndDepartureCity() throws Exception {
        // POSITIVE TEST
        flightOptional = flightRepository.findByDepartureCityAndArrivalCity(sanFrancisco, barcelona);
        assertThat((Flight) flightOptional.get()).isEqualToComparingFieldByField(sanFrancisco2BarcelonaFlight);

        flightOptional = flightRepository.findByDepartureCityAndArrivalCity(barcelona, sanFrancisco);
        assertThat((Flight) flightOptional.get()).isEqualToComparingFieldByField(barcelona2SanFranciscoFlight);

        flightOptional = flightRepository.findByDepartureCityAndArrivalCityAndTime(sanFrancisco, barcelona, TestConstants.NOW_MINUS_ONE_MONTH);
        assertThat((Flight) flightOptional.get()).isEqualToComparingFieldByField(sanFrancisco2BarcelonaFlight);

        flightOptional = flightRepository.findByDepartureCityAndArrivalCityAndTime(barcelona, sanFrancisco, TestConstants.NOW_PLUS_ONE_MONTH);
        assertThat((Flight) flightOptional.get()).isEqualToComparingFieldByField(barcelona2SanFranciscoFlight);

        // NEGATIVE TEST
        flightOptional = flightRepository.findByDepartureCityAndArrivalCity(sanFrancisco, sanFrancisco);
        assertThat(flightOptional.isPresent()).isFalse();

        flightOptional = flightRepository.findByDepartureCityAndArrivalCityAndTime(sanFrancisco, barcelona, TestConstants.NOW_PLUS_ONE_MONTH);
        assertThat(flightOptional.isPresent()).isFalse();

        // Test Name In
        flights = flightRepository.findByDepartureCityNameIn(Arrays.asList(TestConstants.SAN_FRANCISCO, "Pleasanton"));
        assertThat(flights)
                .hasSize(1)
                .contains(sanFrancisco2BarcelonaFlight);

        flights = flightRepository.findByDepartureCityNameIn(Arrays.asList("Pleasanton", "Dublin"));
        assertThat(flights).hasSize(0);

        flights = flightRepository.findByDepartureCityNameIn(Arrays.asList(TestConstants.SAN_FRANCISCO, TestConstants.BARCELONA));
        assertThat(flights)
                .hasSize(2)
                .contains(sanFrancisco2BarcelonaFlight)
                .contains(barcelona2SanFranciscoFlight);

        flights = flightRepository.findByDepartureCityIn(Arrays.asList(sanFrancisco, barcelona));
        assertThat(flights)
                .hasSize(2)
                .contains(sanFrancisco2BarcelonaFlight)
                .contains(barcelona2SanFranciscoFlight);
    }

    @Test
    public void testFindByTime() throws Exception {
        flights = flightRepository.findByTime(TestConstants.NOW_MINUS_ONE_MONTH);
        assertThat(flights).hasSize(1);
        assertThat(flights).contains(sanFrancisco2BarcelonaFlight);

        flights = flightRepository.findByTimeAfter(TestConstants.NOW);
        assertThat(flights).hasSize(1);
        assertThat(flights).contains(barcelona2SanFranciscoFlight);

        flights = flightRepository.findByTimeBefore(TestConstants.NOW);
        assertThat(flights).hasSize(1);
        assertThat(flights).contains(sanFrancisco2BarcelonaFlight);

        flights = flightRepository.findByTimeBetween(TestConstants.NOW_MINUS_TWO_MONTH, TestConstants.NOW_PLUS_TWO_MONTH);
        assertThat(flights).hasSize(2);
        assertThat(flights)
                .contains(sanFrancisco2BarcelonaFlight)
                .contains(barcelona2SanFranciscoFlight);
    }

    @Test
    public void testFindByPrice() throws Exception {
        flights = flightRepository.findByPriceLessThan(475);
        assertThat(flights).hasSize(1);
        assertThat(flights.contains(sanFrancisco2BarcelonaFlight));

        flights = flightRepository.findByPriceLessThanEqual(450);
        assertThat(flights).hasSize(1);
        assertThat(flights.contains(sanFrancisco2BarcelonaFlight));

        flights = flightRepository.findByPriceLessThan(450);
        assertThat(flights).hasSize(0);

        flights = flightRepository.findByPriceGreaterThan(475);
        assertThat(flights).hasSize(1);
        assertThat(flights.contains(barcelona2SanFranciscoFlight));

        flights = flightRepository.findByPriceGreaterThanEqual(500);
        assertThat(flights).hasSize(1);
        assertThat(flights.contains(barcelona2SanFranciscoFlight));

        flights = flightRepository.findByPriceGreaterThan(500);
        assertThat(flights).hasSize(0);

        flights = flightRepository.findByPriceGreaterThanEqualAndPriceLessThanEqual(400, 550);
        assertThat(flights).hasSize(2);
        assertThat(flights)
                .contains(sanFrancisco2BarcelonaFlight)
                .contains(barcelona2SanFranciscoFlight);

        flights = flightRepository.findByPriceGreaterThanEqualAndPriceLessThanEqual(450, 500);
        assertThat(flights).hasSize(2);
        assertThat(flights)
                .contains(sanFrancisco2BarcelonaFlight)
                .contains(barcelona2SanFranciscoFlight);
    }

    @Test
    public void testJavaPersistenceQueryLanguage() {
        Page<Flight> flightPage;
        Pageable pageable;

         pageable = PageRequest.of(Integer.parseInt(AppConstants.DEFAULT_PAGE_NUMBER),
                Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE), Sort.Direction.DESC, AppConstants.CREATED_AT);

        flightPage = flightRepository.queryByPriceRangeAndDepartureCityName(425, 475, "San%", pageable);
        assertThat(flightPage)
                .contains(sanFrancisco2BarcelonaFlight)
                .hasSize(1);

        City sanDiego = cityRepository.save(new City(TestConstants.SAN_DIEGO));
        City oakland = cityRepository.save(new City(TestConstants.OAKLAND));
        Flight sanDiego2OaklandFlight = flightRepository.save(new Flight (450, sanDiego, oakland, TestConstants.NOW_MINUS_ONE_MONTH));
        flightPage = flightRepository.queryByPriceRangeAndDepartureCityName(425, 475, "San%", pageable);
        // Order MATTERS (due to sorting)
        assertThat(flightPage).containsSequence(Arrays.asList(sanDiego2OaklandFlight, sanFrancisco2BarcelonaFlight));

        pageable = PageRequest.of(0, 1, Sort.Direction.DESC, AppConstants.CREATED_AT);
        flightPage = flightRepository.queryByPriceRangeAndDepartureCityName(425, 475, "San%", pageable);
        assertThat(flightPage)
                .containsSequence(sanDiego2OaklandFlight)
                .doesNotContain(sanFrancisco2BarcelonaFlight);
    }

    @After
    public void cleanup() {
        cityRepository.deleteAll();
        flightRepository.deleteAll();
    }

}
