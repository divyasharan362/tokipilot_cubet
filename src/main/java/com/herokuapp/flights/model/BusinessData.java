package com.herokuapp.flights.model;

import lombok.Data;

import java.util.Optional;

@Data
public class BusinessData {

	private String arrival;
	private String departure;
	private Long departureTime;
	private Long arrivalTime;

	public Optional<String> getArrival() {
		return Optional.ofNullable(arrival);
	}

	public Optional<String> getDeparture() {
		return Optional.ofNullable(departure);
	}

	public Optional<Long> getDepartureTime() {
		return Optional.ofNullable(departureTime);
	}

	public Optional<Long> getArrivalTime() {
		return Optional.ofNullable(arrivalTime);
	}



}
