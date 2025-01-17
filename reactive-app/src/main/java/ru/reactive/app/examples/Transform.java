package ru.reactive.app.examples;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.util.function.Function;

@Slf4j
public class Transform {
    void transform() {
        Function<Flux<String>, Flux<String>> logUserInfo = // (1)
                stream -> stream //
                        .index() // (1.1)
                        .doOnNext(tp -> // (1.2)
                                log.info("[{}] User: {}", tp.getT1(), tp.getT2())) //
                        .map(Tuple2::getT2);
        // (1.3)
        Flux.range(1000, 3) // (2)
                .map(i -> "user-" + i) //
                .transform(logUserInfo) // (3)
                .subscribe(e -> log.info("onNext: {}", e));
    }
}
