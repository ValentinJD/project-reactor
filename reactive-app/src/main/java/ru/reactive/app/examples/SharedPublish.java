package ru.reactive.app.examples;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

@Slf4j
public class SharedPublish {

    void share() {
        Flux<Integer> source = Flux.range(0, 3)
                .doOnSubscribe(s ->
                        log.info("new subscription for the cold publisher"));

        ConnectableFlux<Integer> conn = source.publish();

        conn.subscribe(e -> log.info("[Subscriber 1] onNext: {}", e));
        conn.subscribe(e -> log.info("[Subscriber 2] onNext: {}", e));
        log.info("all subscribers are ready, connecting");
        conn.connect();
    }
}
