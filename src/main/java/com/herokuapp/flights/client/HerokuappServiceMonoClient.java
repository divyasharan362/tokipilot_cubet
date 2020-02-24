package com.herokuapp.flights.client;

import com.herokuapp.flights.Exceptions.FlightsException;
import com.herokuapp.flights.model.*;
import com.herokuapp.flights.model.*;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service("herokuappServiceMonoClient")
@Slf4j
public class HerokuappServiceMonoClient {

    @Autowired
    @Qualifier("client")
    private WebClient client;

    @CircuitBreaker(name = "backendB", fallbackMethod = "dataFallBackBusinessMono")
    @TimeLimiter(name = "backendB", fallbackMethod = "dataFallBackBusinessMono")
    @RateLimiter(name = "backendB", fallbackMethod = "dataFallBackBusinessMono")
    @Retry(name = "backendB", fallbackMethod = "dataFallBackBusinessMono")
    @Bulkhead(name = "backendB", fallbackMethod = "dataFallBackBusinessMono")
    public Mono<List<AggregateData>> getBusinessFlightsData() {
        log.info(" getBusinessFlightsData >>>>  ");
        return client.get()
                .uri("/api/flights/business")
                .retrieve()
                .bodyToMono(Business.class)
                .onErrorResume(throwable -> {
                    log.error(throwable.getLocalizedMessage());

                    return Mono.error(new FlightsException(" Resilience4j backendB getBusinessFlightsData failed " + throwable.getMessage(), throwable));
                })
               .flatMap(business -> {

                    return Mono.<List<AggregateData>>just(business.getData().orElse(Collections.emptyList())
                            .stream().map(businessData -> getDataT1(businessData)).collect(Collectors.toList()));


                }) ;

    }

    @CircuitBreaker(name = "backendA", fallbackMethod = "dataFallBackCheap")
    @RateLimiter(name = "backendA", fallbackMethod = "dataFallBackCheap")
    @Retry(name = "backendA", fallbackMethod = "dataFallBackCheap")
    @Bulkhead(name = "backendA", fallbackMethod = "dataFallBackCheap")
    @TimeLimiter(name= "backendA", fallbackMethod = "dataFallBackCheap")
    public Mono<List<AggregateData>> getCheapFlightsData() {
        log.info("getCheapFlightsData >>> ");

        return client.get()
                .uri("/api/flights/cheap")
                .retrieve()
                .bodyToMono(Cheap.class)
                .onErrorResume(throwable -> {
                    log.error(throwable.getMessage());

                    return Mono.error(new FlightsException(" Resilience4j backendA getCheapFlightsData failed " + throwable.getMessage(), throwable));
                })
                .flatMap(business -> {

                    return Mono.<List<AggregateData>>just(business.getData().orElse(Collections.emptyList())
                            .stream().map(cheapData -> getDataT2(cheapData)).collect(Collectors.toList()));


                });
    }

    public Mono<List<AggregateData>> dataFallBackBusinessMono(Exception e){
        log.error("fallback method called.business....");
        return Mono.just(Collections.EMPTY_LIST);
    }

    public Mono<List<AggregateData>> dataFallBackCheap(Exception e){
        log.error("fallback method cheap.....");
        return Mono.just(Collections.EMPTY_LIST);
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
}
