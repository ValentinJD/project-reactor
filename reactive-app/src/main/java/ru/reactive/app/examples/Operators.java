package ru.reactive.app.examples;

import org.slf4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.reactive.app.User;

import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

public class Operators {
    static Logger log = org.slf4j.LoggerFactory.getLogger(Operators.class);

    public static void main(String[] args) {
        Flux.range(2018, 5) // (1)
                .timestamp() // (2)
                .index() // (3)
                .subscribe(e -> log.info("index: {}, ts: {}, value: {}", // (4)
                        e.getT1(), // (4.1)
                        Instant.ofEpochMilli(e.getT2().getT1()), // (4.2)
                        e.getT2().getT2()));

        //Skip values from this Flux until a specified Publisher signals an onNext or onComplete.
        Mono<?> startCommand = Mono.empty();
        Mono<?> stopCommand = Mono.empty();
        Flux<User> streamOfData = Flux.empty();
        streamOfData
                .skipUntilOther(startCommand)
                .takeUntilOther(stopCommand)
                .subscribe(System.out::println);

        Flux.just(1, 6, 2, 8, 3, 1, 5, 1)
                .collectSortedList(Comparator.reverseOrder())
                .subscribe(System.out::println);
        Flux.just(3, 5, 7, 9, 11, 15, 16, 17)
                .any(e -> e % 2 == 0)
                .subscribe(hasEvens -> log.info("Has evens: {}", hasEvens));
        Flux.range(1, 5)
                .reduce(0, (acc, elem) -> acc + elem)
                .subscribe(result -> log.info("Result: {}", result));
        Flux.range(1, 5)
                .scan(0, (acc, elem) -> acc + elem)
                .subscribe(result -> log.info("Result: {}", result));

        int bucketSize = 5; // (1)
        Flux.range(1, 500) // (2)
                .index() // (3)
                .scan( // (4)
                        new int[bucketSize], // (4.1)
                        (acc, elem) -> { //
                            acc[(int)(elem.getT1() % bucketSize)] = elem.getT2(); // (4.2)
                            return acc; // (4.3)
                        })
                .skip(bucketSize) // (5)
                .map(array -> Arrays.stream(array).sum() * 1.0 / bucketSize) // (6)
                .subscribe(av -> log.info("Running average: {}", av)); // (7)

        Flux.just(1, 2, 3)
                .thenMany(Flux.just(4, 5))
                .subscribe(e -> log.info("onNext: {}", e));
        Flux.concat(
                Flux.range(1, 3),
                Flux.range(4, 2),
                Flux.range(6, 5)
        ).subscribe(e -> log.info("onNext: {}", e));
    }
}
