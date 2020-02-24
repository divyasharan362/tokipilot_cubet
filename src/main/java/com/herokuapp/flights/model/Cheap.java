package com.herokuapp.flights.model;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Cheap {
    @JsonProperty("data")
    private List<CheapData> data;

	public Optional<List<CheapData>> getData() {
		return Optional.ofNullable(data);
	}

}