package ru.reactive.app.examples;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
public class Cache4 {

    public static void main(String[] args) throws InterruptedException {
        Cache4 errorHandle = new Cache4();
        errorHandle.cash();
        Thread.sleep(4000);
    }

    void cash() {
        Flux<Integer> source = Flux.range(0, 2) // (1)
                .doOnSubscribe(s ->
                        log.info("new subscription for the cold publisher"));

        Flux<Integer> cachedSource = source.cache(Duration.ofSeconds(1));// (2)

        cachedSource.subscribe(e -> log.info("[S 1] onNext: {}", e)); // (3)
        cachedSource.subscribe(e -> log.info("[S 2] onNext: {}", e)); // (4)
        try {
            Thread.sleep(1200); // (5)
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        cachedSource.subscribe(e -> log.info("[S 3] onNext: {}", e)); // (6)
    }
}
