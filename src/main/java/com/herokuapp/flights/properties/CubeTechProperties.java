package com.herokuapp.flights.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Setter
@Getter
@PropertySource("classpath:/application-cubettech.yml")
@ConfigurationProperties(prefix = "com.flights")
@Component
public  class CubeTechProperties {

     @Value("${baseUrl}")
    public String baseUrl;

}
