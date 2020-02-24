package org.flights;

import com.herokuapp.flights.model.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class FlightTestData {


    public static Flux<Cheap> mockCheapData(){
        Cheap cheap = new Cheap();
        List<CheapData> cheapDataList = new ArrayList<>();

        IntStream.range(1,3).forEach(num->{
            int v = new Random().nextInt(9);
            CheapData cheapData = new CheapData();
            cheapData.setArrival(1l);
            cheapData.setDeparture(2l);
            cheapData.setRoute("P"+v+"a"+v+"ris-Tizi");
            cheapDataList.add(cheapData);
        });
        cheap.setData(cheapDataList);
        return Flux.just(cheap);
        //return cheap;//gson.toJson(cheap);
    }

    public static Flux<Business> mockResBusiness(){
        Business business = new Business();
        List<BusinessData> businessDataList = new ArrayList<>();
        IntStream.range(1,3).forEach(num-> {
            int v = new Random().nextInt(9);

            BusinessData businessData= new BusinessData();
            businessData.setArrival("hyderbad"+v);
            businessData.setArrivalTime(1l);
            businessData.setDeparture("delhi"+v);
            businessData.setDepartureTime(3l);

            businessDataList.add(businessData);
        });
        business.setData(businessDataList);
        return Flux.just(business);

    }

    public static Flux<Business> mockResBusinessEmpty(){
       return Flux.empty();
    }
    public static Flux<Cheap> mockResCheapEmpty(){
        return Flux.empty();
    }

    public static Flux<Business> mockResBusinessError(){
        return Flux.error(new RuntimeException("External service not available"));
    }

    public static Flux<Cheap> mockResCheapError(){
        return Flux.error(new RuntimeException("External service not available"));
    }

    public static Mono<List<AggregateData>> getMonoAggregateData(){

        List<AggregateData> dataList = new ArrayList<>();
        IntStream.range(1,5).forEach(num->{
            int v = new Random().nextInt(9);
            AggregateData aggregateData = new AggregateData();
            aggregateData.setSource("kochi"+v);
            aggregateData.setDestination("hyderabad"+v);
            aggregateData.setDepartureTime(1l);
            aggregateData.setArrivalTime(2l);
            dataList.add(aggregateData);
        });
    return Mono.just(dataList);

    }

    public static Mono<List<AggregateData>> mockMonoResopnseEmpty(){
        return Mono.just(Collections.EMPTY_LIST);
    }

    public static Mono<List<AggregateData>> mockResponseError(){
        return Mono.error(new RuntimeException("External service not available"));
    }
}
