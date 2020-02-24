package com.herokuapp.flights.api;

import com.herokuapp.flights.model.AggregateData;
import com.herokuapp.flights.service.FlightMonoDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
@Slf4j
public class FlightMonoController {

    @Autowired
    FlightMonoDataService flightMonoDataService;

    @GetMapping("/all")
    public Mono<List<AggregateData>> getAllFlights(){
        return flightMonoDataService.getAll().subscribeOn(Schedulers.parallel());
    }

}
