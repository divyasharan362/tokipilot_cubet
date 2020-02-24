package com.herokuapp.flights.service;

import com.herokuapp.flights.Exceptions.FlightsException;
import com.herokuapp.flights.client.HerokuappServiceMonoClient;
import com.herokuapp.flights.model.AggregateData;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class FlightMonoDataService {


    @Autowired
    HerokuappServiceMonoClient herokuappServiceMonoClient;

    Comparator<AggregateData> bySource =
            (AggregateData o1, AggregateData o2) -> o1.getSource().orElse("").compareTo(o2.getSource().orElse(""));
    Comparator<AggregateData> byDestinatino =
            (AggregateData o1, AggregateData o2) -> o1.getDestination().orElse("").compareTo(o2.getDestination().orElse(""));

    @CircuitBreaker(name = "backendA", fallbackMethod = "dataFallBack")
    @RateLimiter(name = "backendA", fallbackMethod = "dataFallBack")
    @Retry(name = "backendA", fallbackMethod = "dataFallBack")
    @TimeLimiter(name = "backendA", fallbackMethod = "dataFallBack")
    public Mono<List<AggregateData>> getAll() {
        Mono<List<AggregateData>> businessFlightsData = herokuappServiceMonoClient.getBusinessFlightsData()
                .onErrorResume(throwable -> Mono.empty());
        Mono<List<AggregateData>> cData = herokuappServiceMonoClient.getCheapFlightsData().onErrorResume(throwable -> Mono.empty());
        return Mono.zip(businessFlightsData, cData)
                .flatMap(tuples -> {
                    log.info("Size.... T1 : " + tuples.getT1().size() + "    T2 : " + tuples.getT2().size());

                    tuples.getT1().addAll(tuples.getT2());
                    tuples.getT1().sort(bySource.thenComparing(byDestinatino));

                    log.info("Size.... T1 : " + tuples.getT1().size() + "    T2 : " + tuples.getT2().size());
                    return Mono.just(tuples.getT1());
                }).onErrorResume(throwable -> {
                    log.error(throwable.getMessage());

                    return Mono.error(new FlightsException(" Resilience4j backendA getCheapFlightsData failed " + throwable.getMessage(), throwable));
                }).subscribeOn(Schedulers.parallel());

    }


    public Mono dataFallBack(Exception e) {
        log.error("fallback method cheap.....");

        return Mono.just("Resilience4j fallaback " + e.getMessage());
    }
}