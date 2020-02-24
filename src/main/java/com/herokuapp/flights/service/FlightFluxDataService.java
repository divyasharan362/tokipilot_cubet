package com.herokuapp.flights.service;

import com.herokuapp.flights.Exceptions.FlightsException;
import com.herokuapp.flights.client.HerokuappServiceFluxClient;
import com.herokuapp.flights.model.*;

import com.herokuapp.flights.model.AggregateData;
import com.herokuapp.flights.model.BusinessData;
import com.herokuapp.flights.model.CheapData;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FlightFluxDataService implements IFlightDataService{


    @Autowired
    private HerokuappServiceFluxClient herokuappServiceFluxClient;

    Comparator<AggregateData> bySource =
            (AggregateData o1, AggregateData o2)-> o1.getSource().orElse("").compareTo(o2.getSource().orElse(""));

    @Override
    @CircuitBreaker(name = "backendA", fallbackMethod = "dataFallBack")
    @RateLimiter(name = "backendA", fallbackMethod = "dataFallBack")
    @Retry(name = "backendA", fallbackMethod = "dataFallBack")
    @TimeLimiter(name= "backendA", fallbackMethod = "dataFallBack")
    public Flux<AggregateData> getAll(){
        Flux<AggregateData> cData = getCheapFlightsData();
        Flux<AggregateData> bData = getBusinessFlightsData();
        return cData.concatWith(bData).parallel(2).sorted(bySource).subscribeOn(Schedulers.parallel());
    }
    @Override
    public Flux<AggregateData> getBusinessFlightsData() {
       return herokuappServiceFluxClient.getBusinessFlightsData()
                .flatMap(business -> {
                    System.out.println("business  .... "+business);
                 return Flux.fromIterable(business.getData().orElse(Collections.emptyList()).stream().map(businessData -> {
                       return getDataT1(businessData);
                    }).collect(Collectors.toList()));

               }).onErrorResume(throwable ->  {throw new RuntimeException("dataFallBackBusiness");});
    }


    public Flux<AggregateData> getCheapFlightsData() {
        log.info("ïts  came hereeeeeeee    eeee  ee");

       return herokuappServiceFluxClient.getCheapFlightsData()
        .flatMap(cheap -> {

                    return Flux.fromIterable(cheap.getData().orElse(Collections.emptyList()).stream().map(businessData -> {
                        return getDataT2(businessData);
                    }).collect(Collectors.toList()));

                }).onErrorResume(throwable -> {
                    log.error(throwable.getMessage());

                    return Flux.error(new FlightsException(" Resilience4j backendA getCheapFlightsData failed " + throwable.getMessage(), throwable));
                });
    }

    public AggregateData getDataT1(BusinessData businessData) {
        AggregateData aggregateData = new AggregateData();
        businessData.getArrival().ifPresent(aggregateData::setSource);
        businessData.getArrivalTime().ifPresent(aggregateData::setArrivalTime);
        businessData.getDeparture().ifPresent(aggregateData::setDestination);
        businessData.getDepartureTime().ifPresent(aggregateData::setDepartureTime);
        return aggregateData;
    }

    public AggregateData getDataT2(CheapData cData) {
        String[] sourceDestion = cData.getRoute().orElse("").split("-");
        AggregateData aggregateData = new AggregateData();
        Optional.of(sourceDestion[0]).ifPresent(aggregateData::setSource);
        Optional.of(sourceDestion[1]).ifPresent(aggregateData::setDestination);
        cData.getArrival().ifPresent(aggregateData::setArrivalTime);
        cData.getDeparture().ifPresent(aggregateData::setDepartureTime);
        return aggregateData;
    }

    public Flux dataFallBack(Exception e){
        log.error("fallback method cheap.....");

        return Flux.just("Resilience4j fallaback "+e.getMessage());
    }
}