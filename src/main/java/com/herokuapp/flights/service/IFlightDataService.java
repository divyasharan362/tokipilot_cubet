package com.herokuapp.flights.service;

import com.herokuapp.flights.model.AggregateData;
import reactor.core.publisher.Flux;


public interface IFlightDataService {

    public Flux<AggregateData> getBusinessFlightsData();
    public Flux<AggregateData> getCheapFlightsData();
    public Flux<AggregateData> getAll();
}