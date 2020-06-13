package com.aaroncarlson.model;

import com.aaroncarlson.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "city")
public class City extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name is required")
    @Column(unique = true)
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "departureCity",
            fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            orphanRemoval = false)
    private Set<Flight> departureFlights = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "arrivalCity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = false)
    private Set<Flight> arrivalFlights = new HashSet<>();

    public City() {
    }

    public City(final String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Flight> getDepartureFlights() {
        return departureFlights;
    }

    public void setDepartureFlights(Set<Flight> departureFlights) {
        this.departureFlights = departureFlights;
    }

    public Set<Flight> getArrivalFlights() {
        return arrivalFlights;
    }

    public void setArrivalFlights(Set<Flight> arrivalFlights) {
        this.arrivalFlights = arrivalFlights;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
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
        City city = (City)object;
        return city.getName().equalsIgnoreCase(this.name);
    }
}
