package com.aaroncarlson.model;

import com.aaroncarlson.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "flight")
public class Flight extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @NotNull(message = "Departing City is required")
    private City departureCity;
    @ManyToOne
    @NotNull(message = "Arriving City is required")
    private City arrivalCity;
    @NotNull(message = "Departure Time is required")
    private LocalDateTime departureTime;

    public Flight() {
    }

    public Flight(City departureCity, City arrivalCity, LocalDateTime departureTime) {
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureTime = departureTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public City getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(City departureCity) {
        this.departureCity = departureCity;
    }

    public City getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(City arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", departureCity=" + departureCity +
                ", arrivalCity=" + arrivalCity +
                ", departureTime=" + departureTime +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        // If the object is compared with itself return true
        if (object == this) {
            return true;
        }
        /* Check if object is an instance of City or not
           "null instance [type]" asl return false */
        if (!(object instanceof City)) {
            return false;
        }
        /* Typecast of Object to City so that we can compare
           data members */
        Flight flight = (Flight)object;
        return flight.getArrivalCity().equals(this.arrivalCity) &&
                flight.getDepartureCity().equals(this.departureCity)&&
                flight.getDepartureTime().equals(this.departureTime);
    }

}
