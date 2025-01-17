package ru.reactive.app.examples;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;

@Slf4j
public class ErrorHandle {

    public Flux<String> recommendedBooks(String userId) {
        return Flux.defer(() -> { // (1)
            Random random = new Random();
            if (random.nextInt(10) < 7) {
                return Flux.<String>error(new RuntimeException("Err")) // (2)
                        .delaySequence(Duration.ofMillis(100));
            } else {
                return Flux.just("Blue Mars", "The Expanse") // (3)
                        .delayElements(Duration.ofMillis(50));
            }
        }).doOnSubscribe(s -> log.info("Request for {}", userId)); // (4)
    }

    public void call() {
        Flux.just("user-1") // (1)
                .flatMap(user -> // (2)
                        recommendedBooks(user) // (2.1)
                                .retryBackoff(5, Duration.ofMillis(100)) // (2.2)
                                .timeout(Duration.ofSeconds(3)) // (2.3)
                                .onErrorResume(e -> Flux.just("The Martian"))) // (2.4)
                .subscribe( // (3)
                        b -> log.info("onNext: {}", b),
                        e -> log.warn("onError: {}", e.getMessage()),
                        () -> log.info("onComplete")
                );
    }

}
