module cubet.flights {
    requires reactor.core;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.actuator;
    requires spring.boot.starter;
    requires spring.aop;
    requires spring.boot.starter.webflux;
    requires spring.core;
    requires resilience4j.spring.boot.common;
    requires io.github.resilience4j.annotations;
    requires io.github.resilience4j.bulkhead;
    requires io.github.resilience4j.circuitbreaker;
    requires io.github.resilience4j.circularbuffer;
    requires io.github.resilience4j.core;
    requires io.github.resilience4j.retry;
    requires io.github.resilience4j.reactor;
}