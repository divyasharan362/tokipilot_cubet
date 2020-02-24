package com.herokuapp.flights.model;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Business {
	@JsonProperty("data")
    private List<BusinessData> data;

	public Optional<List<BusinessData>> getData() {
		return Optional.ofNullable(data);
	}

}
