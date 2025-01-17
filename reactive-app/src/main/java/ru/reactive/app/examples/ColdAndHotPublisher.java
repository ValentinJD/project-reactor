package ru.reactive.app.examples;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Slf4j
public class ColdAndHotPublisher {

    void cold() {
        Flux<String> coldPublisher = Flux.defer(() -> {
            log.info("Generating new items");
            return Flux.just(UUID.randomUUID().toString());
        });
        log.info("No data was generated so far");
        coldPublisher.subscribe(e -> log.info("onNext: {}", e));
        coldPublisher.subscribe(e -> log.info("onNext: {}", e));
        log.info("Data was generated twice for two subscribers");
    }

    //just расчет один раз
    void hot() {
        Flux<String> hotPublisher = Flux.just(UUID.randomUUID().toString())
                .map(s -> {
                    String upperCase = s.toLowerCase();
                    log.info("upperCase: {}", s);
                    return upperCase;
                });
        log.info("data was generated so far");
        hotPublisher.subscribe(e -> log.info("onNext: {}", e));
        hotPublisher.subscribe(e -> log.info("onNext: {}", e));
        log.info("Data was generated one for two subscribers");
// если завернуть в defer то будет холодный Publisher
        Flux<String> cold = Flux.defer(()->
                Flux.just(UUID.randomUUID().toString())
                .map(s -> {
                    String upperCase = s.toLowerCase();
                    log.info("upperCase: {}", s);
                    return upperCase;
                }));
        log.info("data was generated so far");
        cold.subscribe(e -> log.info("onNext: {}", e));
        cold.subscribe(e -> log.info("onNext: {}", e));
        log.info("Data was generated twice for two subscribers");
    }
}
