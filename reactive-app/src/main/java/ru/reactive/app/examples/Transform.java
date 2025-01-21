package ru.reactive.app.examples;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.util.Random;
import java.util.function.Function;

@Slf4j
public class Transform {// (1)
    public final Function<Flux<String>, Flux<String>> logUserInfo = stream -> stream //
            .index() // (1.1)
            .doOnNext(tp -> // (1.2)
                    log.info("[{}] User: {}", tp.getT1(), tp.getT2())) //
            .map(Tuple2::getT2);

    public static void main(String[] args) throws InterruptedException {
        Transform errorHandle = new Transform();
        errorHandle.compose();
        Thread.sleep(4000);
    }

    void transform() {
        // (1.3)
        Flux.range(1000, 3) // (2)
                .map(i -> "user-" + i) //
                .transform(logUserInfo) // (3)
                .subscribe(e -> log.info("onNext: {}", e));
    }

    void compose() {
        Function<Flux<String>, Flux<String>> logUserInfo = (stream) -> { // (1)
            Random random = new Random();
            if (random.nextBoolean()) {
                return stream
                        .doOnNext(e -> log.info("[path A] User: {}", e));
            } else {
                return stream
                        .doOnNext(e -> log.info("[path B] User: {}", e));
            }
        };

        Flux<String> publisher = Flux.just("1", "2", "3","4") // (2)
                .compose(logUserInfo); // (3)
        publisher.subscribe(); // (4)
        publisher.subscribe();
    }

}
