package ru.reactive.app.examples;

import org.slf4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.reactive.app.User;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
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
                            acc[(int) (elem.getT1() % bucketSize)] = elem.getT2(); // (4.2)
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

        Flux.range(1, 13)
                .buffer(4)
                .subscribe(e -> log.info("onNext: {}", e));

        Flux<Flux<Integer>> windowedFlux = Flux.range(101, 20) // (1)
                .windowUntil(integer -> integer % 5 == 0, true); // (2)
        windowedFlux.subscribe(window -> window // (3)
                .collectList() // (4)
                .subscribe(e -> log.info("window: {}", e)));

        Flux.range(1, 7) // (1)
                .groupBy(e -> e % 2 == 0 ? "Even" : "Odd") // (2)
                .subscribe(groupFlux -> groupFlux
                        .scan( // (4)
                                new LinkedList<>(), // (4.1)
                                (list, elem) -> {
                                    list.add(elem); // (4.2)
                                    if (list.size() > 2) {
                                        list.remove(0); // (4.3)
                                    }
                                    return list;
                                })
                        .filter(arr -> !arr.isEmpty()) // (5)
                        .subscribe(data -> // (6)
                                log.info("{}: {}", groupFlux.key(), data)));

        Flux.range(1, 100)
                .delayElements(Duration.ofMillis(1))
                .sample(Duration.ofMillis(20))
                .subscribe(e -> log.info("onNext: {}", e));
    }
}
