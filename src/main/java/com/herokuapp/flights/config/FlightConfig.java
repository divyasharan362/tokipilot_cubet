package com.herokuapp.flights.config;


import com.herokuapp.flights.properties.CubeTechProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FlightConfig {

    @Autowired
    CubeTechProperties cubeTechProperties;

    @Bean("client")
    public WebClient fluxClient(WebClient.Builder builder) {
        WebClient webClient = builder
                //.defaultHeader("Connection", "close")
                .baseUrl(cubeTechProperties.getBaseUrl())
                .build();

        return webClient;
    }

}
