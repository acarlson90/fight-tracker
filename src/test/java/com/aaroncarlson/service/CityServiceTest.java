package com.aaroncarlson.service;

import com.aaroncarlson.model.City;
import com.aaroncarlson.payload.PagedResponse;
import com.aaroncarlson.util.AppConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CityServiceTest {

    @Autowired
    private CityService cityService;

    @Test
    public void testCRUDOperations() throws Exception {
        City berkeley = new City(TestConstants.BERKELEY);

        // Create
        cityService.createCity(berkeley);

        // Read
        PagedResponse<City> cityPagedResponse = cityService.getAllCities(Integer.parseInt(AppConstants.DEFAULT_PAGE_NUMBER),
                Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE));
        assertThat(cityPagedResponse.getTotalElements()).isEqualTo(1);
        assertThat(cityPagedResponse.getContent()).hasSize(1);
        assertThat(cityPagedResponse.getSize()).isEqualTo(30);
        assertThat(cityPagedResponse.getPage()).isEqualTo(0);
        assertThat(cityPagedResponse.isLast()).isTrue();

        City persistedCity = cityService.getCityByName(TestConstants.BERKELEY);
        assertThat(persistedCity.getName()).isEqualToIgnoringCase(TestConstants.BERKELEY);
        assertThat(persistedCity.getCreatedAt()).isNotNull();
        assertThat(persistedCity.getUpdatedAt()).isNotNull();
        assertThat(persistedCity.getCreatedAt()).isEqualTo(persistedCity.getUpdatedAt());

        // Update
        final String updateCityName = TestConstants.BERKELEY + " - updated";
        persistedCity.setName(updateCityName);
        final City updatedPersistedCity = cityService.editCity(persistedCity.getId(), persistedCity);
        assertThat(updatedPersistedCity.getName()).isEqualToIgnoringCase(updateCityName);
        assertThat(updatedPersistedCity.getCreatedAt()).isNotEqualTo(updatedPersistedCity.getUpdatedAt());

        // Delete
        cityService.deleteCity(persistedCity.getId());
        assertThat(cityService.count()).isEqualTo(0);

    }

}
