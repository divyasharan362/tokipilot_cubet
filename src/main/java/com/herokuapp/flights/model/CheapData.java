package com.herokuapp.flights.model;

import lombok.Data;

import java.util.Optional;
@Data
public class CheapData {
    private String route;
    private long departure;
    private long arrival;

    public Optional<String> getRoute() {
        return Optional.ofNullable(route);
    }

    public  Optional<Long> getDeparture() {
        return Optional.ofNullable(departure);
    }

    public Optional<Long> getArrival() {
        return Optional.ofNullable(arrival);
    }

}
