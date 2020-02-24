package org.flights;

import com.herokuapp.flights.CubettechFlightsApplication;
import com.herokuapp.flights.client.HerokuappServiceMonoClient;
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

import java.util.Arrays;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CubettechFlightsApplication.class)
@AutoConfigureWebTestClient(timeout = "1000000000")
public class FlightMonoApiTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    HerokuappServiceMonoClient herokuappServiceMonoClient;
    @Test
    public void testGetAllMonoApi() {
        MockitoAnnotations.initMocks(this);
        Mockito. when(herokuappServiceMonoClient.getBusinessFlightsData()).thenReturn(FlightTestData.getMonoAggregateData());
        Mockito.when(herokuappServiceMonoClient.getCheapFlightsData()).thenReturn(FlightTestData.getMonoAggregateData());

        webTestClient.get().uri("/all").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody(AggregateData[].class)
                .consumeWith(response -> {
                    Assert.assertNotNull(response.getResponseBody());

                    Arrays.asList(response.getResponseBody()).forEach(aggregateData -> {
                        System.out.println(aggregateData.getSource());
                    });
                });
    }

    @Test
    public void testGetAllMonoApiEmpty() {
        MockitoAnnotations.initMocks(this);
        Mockito. when(herokuappServiceMonoClient.getBusinessFlightsData()).thenReturn(FlightTestData.mockMonoResopnseEmpty());
        Mockito.when(herokuappServiceMonoClient.getCheapFlightsData()).thenReturn(FlightTestData.mockMonoResopnseEmpty());

        webTestClient.get().uri("/all").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody(AggregateData[].class)
                .consumeWith(response -> {
                    Assert.assertNotNull(response.getResponseBody());
                    AggregateData[] f= response.getResponseBody();
                   Assert.assertTrue(f.length==0);

                });
    }

    @Test
    public void testGetAllMonoApiException() {
        MockitoAnnotations.initMocks(this);
        Mockito. when(herokuappServiceMonoClient.getBusinessFlightsData()).thenReturn(FlightTestData.mockResponseError());
        Mockito.when(herokuappServiceMonoClient.getCheapFlightsData()).thenReturn(FlightTestData.mockResponseError());

        webTestClient.get().uri("/all").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class)
                .consumeWith(response -> {
                   Assert.assertNull(response.getResponseBody());

                });
    }



}
