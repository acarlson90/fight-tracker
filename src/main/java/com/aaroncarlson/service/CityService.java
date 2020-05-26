package com.aaroncarlson.service;

import com.aaroncarlson.exception.BadRequestException;
import com.aaroncarlson.exception.ResourceNotFoundException;
import com.aaroncarlson.model.City;
import com.aaroncarlson.payload.PagedResponse;
import com.aaroncarlson.repository.CityRepository;
import com.aaroncarlson.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class CityService extends PagedResponseService {

    @Autowired
    private CityRepository cityRepository;

    public long count() {
        return cityRepository.count();
    }

    public PagedResponse<City> getAllCities(final int page, final int size) {
        validatePageNumberAndSize(page, size);

        // Retrieve Cities
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);
        Page<City> cityPage = cityRepository.findAll(pageable);

        if (cityPage.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), cityPage.getNumber(),
                    cityPage.getSize(), cityPage.getTotalElements(), cityPage.getTotalPages(), cityPage.isLast());
        }

        List<City> cities = cityPage.map(city -> city).getContent();

        return new PagedResponse<>(cities, cityPage.getNumber(), cityPage.getSize(),
                cityPage.getTotalElements(), cityPage.getTotalPages(), cityPage.isLast());
    }

    public City getCityById(final Long id) {
        City city = cityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("City", "id", Long.toString(id)));

        return city;
    }

    public City getCityByName(final String name) {
        City city = cityRepository.findByNameIgnoreCase(name).orElseThrow(
                () -> new ResourceNotFoundException("City", "name", name));

        return city;
    }

    @Transactional
    public City createCity(final City city) {
        String cityName = city.getName();

        if (cityRepository.findByNameIgnoreCase(cityName).isPresent()) {
            throw new BadRequestException("City already exists with name: " + cityName);
        }
        return cityRepository.save(city);
    }

    @Transactional
    public City editCity(final long id, final City city) {
        City persistedCity = getCityById(id);

        if (persistedCity.equals(city)) {
            throw new BadRequestException("City is identical to '" + id + "'");
        }

        persistedCity.setName(city.getName());

        //TODO: Finish Update

//        if (!persistedCity.getName().equalsIgnoreCase(city.getName())) {
//            persistedCity.setName(city.getName());
//        }

        return cityRepository.save(persistedCity);
    }

    public void deleteCity(final long id) {
        final City city = getCityById(id);
        cityRepository.delete(city);
    }

    public void deleteAllCities() {
        cityRepository.deleteAll();
    }

}
