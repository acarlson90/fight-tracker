package com.aaroncarlson.controller;

import com.aaroncarlson.model.City;
import com.aaroncarlson.payload.ApiResponse;
import com.aaroncarlson.payload.PagedResponse;
import com.aaroncarlson.service.CityService;
import com.aaroncarlson.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public PagedResponse<City> getCities(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return cityService.getAllCities(page, size);
    }

    @GetMapping("/{id}")
    public City getCityById(@PathVariable(value = "id") final long id) {
        return cityService.getCityById(id);
    }

    @PostMapping
    public ResponseEntity<?> createCity(@Valid @RequestBody final City city) {
        City persistedCity = cityService.createCity(city);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(persistedCity.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "City Created"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editCity(@PathVariable(value = "id") final long id,
                                      @Valid @RequestBody final City city) {
        City persistedCity = cityService.editCity(id, city);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(persistedCity.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "City Edited"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCity(@PathVariable(value = "id") final long id) {

        cityService.deleteCity(id);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "City Deleted"));
    }

}
