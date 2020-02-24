package com.herokuapp.flights;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableConfigurationProperties

public class CubettechFlightsApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(CubettechFlightsApplication.class).web(WebApplicationType.REACTIVE).run(args);

	}

}
