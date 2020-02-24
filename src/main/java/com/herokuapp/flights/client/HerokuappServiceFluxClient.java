package com.herokuapp.flights.client;

import com.herokuapp.flights.Exceptions.FlightsException;
import com.herokuapp.flights.model.Business;
import com.herokuapp.flights.model.Cheap;
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
import reactor.core.publisher.Flux;


@Service("herokuappServiceFluxClient")
@Slf4j
public class HerokuappServiceFluxClient {

    @Autowired
    @Qualifier("client")
    private WebClient client;

    @CircuitBreaker(name = "backendB", fallbackMethod = "dataFallBackBusiness")
    @TimeLimiter(name= "backendB", fallbackMethod = "dataFallBackBusiness")
    @RateLimiter(name = "backendB", fallbackMethod = "dataFallBackBusiness")
    @Retry(name = "backendB", fallbackMethod = "dataFallBackBusiness")
    @Bulkhead(name = "backendB", fallbackMethod = "dataFallBackBusiness")
    public Flux<Business> getBusinessFlightsData() {
        log.info("ïts  came hereeeeeeee    eeee  ee");
        return client.get()
                .uri("/api/flights/business")
                .retrieve()
                .bodyToFlux(Business.class)
                .onErrorResume(throwable -> {
                    log.error(throwable.getLocalizedMessage());

                    return Flux.error(new FlightsException(" Resilience4j backendB getBusinessFlightsData failed " + throwable.getMessage(), throwable));
                });

    }

    @CircuitBreaker(name = "backendA", fallbackMethod = "dataFallBackCheao")
    @RateLimiter(name = "backendA", fallbackMethod = "dataFallBackCheao")
    @Retry(name = "backendA", fallbackMethod = "dataFallBackCheao")
    @Bulkhead(name = "backendA", fallbackMethod = "dataFallBackCheao")
    @TimeLimiter(name= "backendA", fallbackMethod = "dataFallBackCheao")
    public Flux<Cheap> getCheapFlightsData() {
        log.info("ïts  came hereeeeeeee    eeee  ee");

        return client.get()
                .uri("/api/flights/cheap")
                .retrieve()
                .bodyToFlux(Cheap.class)
                .onErrorResume(throwable -> {
                    log.error(throwable.getMessage());

                    return Flux.error(new FlightsException(" Resilience4j backendA getCheapFlightsData failed " + throwable.getMessage(), throwable));
                });
    }

    public Flux dataFallBackBusiness(Exception e){
        log.error("fallback method called.business....");
        return Flux.just("Resilience4j fallaback dataFallBackBusiness "+e.getMessage());
    }

    public Flux dataFallBackCheao(Exception e){
        log.error("fallback method cheap.....");
        return Flux.just("Resilience4j fallaback dataFallBackCheao "+e.getMessage());
    }
}
