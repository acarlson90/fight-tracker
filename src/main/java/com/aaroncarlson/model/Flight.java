package com.aaroncarlson.model;

import com.aaroncarlson.model.audit.UserDateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "flight")
public class Flight extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private double price;
    @ManyToOne
    @NotNull(message = "Departing City is required")
    private City departureCity;
    @ManyToOne
    @NotNull(message = "Arriving City is required")
    private City arrivalCity;
    @NotNull(message = "Departure Time is required")
    private LocalDateTime time;

    public Flight() {
    }

    public Flight(double price, City departureCity, City arrivalCity, LocalDateTime time) {
        this.price = price;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", departureCity=" + departureCity +
                ", arrivalCity=" + arrivalCity +
                ", time=" + time +
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
        if (!(object instanceof Flight)) {
            return false;
        }
        /* Typecast of Object to City so that we can compare
           data members */
        Flight flight = (Flight)object;
        return flight.getPrice() == this.price &&
                flight.getArrivalCity().equals(this.arrivalCity) &&
                flight.getDepartureCity().equals(this.departureCity)&&
                flight.getTime().equals(this.time);
    }

}
