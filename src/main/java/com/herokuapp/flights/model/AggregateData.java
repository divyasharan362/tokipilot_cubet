package com.herokuapp.flights.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.Setter;

import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AggregateData {
	@JsonProperty("source")
	private String source;
	@JsonProperty("destination")
	private String destination;
	@JsonProperty("departureTime")
	private long departureTime;
	@JsonProperty("arrivalTime")
	private long arrivalTime;

	/*public Data(String source, String destination, long departureTime, long arrivalTime) {
		super();
		this.source = source;
		this.destination = destination;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
	}*/

	public Optional<String> getSource() {
		return Optional.ofNullable(source);
	}

	public Optional<String> getDestination() {
		return Optional.ofNullable(destination);
	}

	public Optional<Long> getDepartureTime() {
		return Optional.ofNullable(departureTime);
	}

	public Optional<Long> getArrivalTime() {
		return Optional.ofNullable(arrivalTime);
	}

}
