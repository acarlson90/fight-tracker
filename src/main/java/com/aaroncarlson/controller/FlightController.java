package com.aaroncarlson.controller;

import com.aaroncarlson.model.Flight;
import com.aaroncarlson.payload.ApiResponse;
import com.aaroncarlson.payload.PagedResponse;
import com.aaroncarlson.service.FlightService;
import com.aaroncarlson.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping
    public PagedResponse<Flight> getFlights(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return flightService.getAllFlights(page, size);
    }

    @GetMapping("/{id}")
    public Flight getFlightById(@PathVariable(value = "id") final long id) {
        return flightService.getFlightById(id);
    }

    @PostMapping
    public ResponseEntity<?> createFlight(@Valid @RequestBody final Flight flight) {
        Flight persistedFlight = flightService.createFlight(flight);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(persistedFlight.getId()).toUri();

        System.out.println("INBOUND FLIGHT" + flight.toString());
        System.out.println("PERSISTED FLIGHT" + persistedFlight.toString());

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Flight Created"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editFlight(@PathVariable(value = "id") final long id,
                                        @Valid @RequestBody final Flight flight) {
        Flight persistedFlight = flightService.editFlight(id, flight);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("id")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Flight Edited"));
    }

}
