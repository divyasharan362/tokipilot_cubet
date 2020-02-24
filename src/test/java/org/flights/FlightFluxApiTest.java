package org.flights;

import com.herokuapp.flights.CubettechFlightsApplication;
import com.herokuapp.flights.client.HerokuappServiceFluxClient;
import com.herokuapp.flights.model.AggregateData;
import org.junit.Assert;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CubettechFlightsApplication.class)
@AutoConfigureWebTestClient(timeout = "1000000000")
public class FlightFluxApiTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    HerokuappServiceFluxClient herokuappServiceFluxClient;
    @Test
    public void testGetAllFluxApi() {
        MockitoAnnotations.initMocks(this);
        Mockito. when(herokuappServiceFluxClient.getBusinessFlightsData()).thenReturn(FlightTestData.mockResBusiness());
        Mockito.when(herokuappServiceFluxClient.getCheapFlightsData()).thenReturn(FlightTestData.mockCheapData());

        webTestClient.get().uri("/allFlux").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody(AggregateData[].class)
                .consumeWith(response -> {
                    Assert.assertNotNull(response.getResponseBody());

                    Arrays.asList(response.getResponseBody()).forEach(aggregateData -> {
                        System.out.println(aggregateData.getSource());
                    });
                });
    }

    @Test
    public void testGetAllFluxApiEmpty() {
        MockitoAnnotations.initMocks(this);
        Mockito. when(herokuappServiceFluxClient.getBusinessFlightsData()).thenReturn(FlightTestData.mockResBusinessEmpty());
        Mockito.when(herokuappServiceFluxClient.getCheapFlightsData()).thenReturn(FlightTestData.mockResCheapEmpty());

        webTestClient.get().uri("/allFlux").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody(AggregateData[].class)
                .consumeWith(response -> {
                    Assert.assertNotNull(response.getResponseBody());
                    AggregateData[] f= response.getResponseBody();
                   Assert.assertTrue(f.length==0);

                });
    }

    @Test
    public void testGetAllFluxApiException() {
        MockitoAnnotations.initMocks(this);
        Mockito. when(herokuappServiceFluxClient.getBusinessFlightsData()).thenReturn(FlightTestData.mockResBusinessError());
        Mockito.when(herokuappServiceFluxClient.getCheapFlightsData()).thenReturn(FlightTestData.mockResCheapError());

        webTestClient.get().uri("/allFlux").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class)
                .consumeWith(response -> {
                    Assert.assertNotNull(response.getResponseBody());
                   Assert.assertEquals(
                           "[\"Resilience4j fallaback  Resilience4j backendA getCheapFlightsData " +
                                   "failed External service not available\"]"
                   ,response.getResponseBody().toString());
                });
    }



}
