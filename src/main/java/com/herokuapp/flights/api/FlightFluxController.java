package com.herokuapp.flights.api;

import com.herokuapp.flights.model.AggregateData;
import com.herokuapp.flights.service.FlightFluxDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RestController
public class FlightFluxController {


    @Autowired
    FlightFluxDataService flightFluxDataService;

   @GetMapping("/bdata")
    public Flux<AggregateData> getFlightsData(){

        return flightFluxDataService.getBusinessFlightsData().subscribeOn(Schedulers.parallel());
    }

    @GetMapping("/cdata")
    public Flux<AggregateData> getCFlightsData(){

        return flightFluxDataService.getCheapFlightsData();
    }

    @GetMapping("/allFlux")
    public Flux<AggregateData> getAllFlights(){

        return flightFluxDataService.getAll();
    }

}
