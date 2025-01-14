package ru.reactive.app;

import reactor.core.publisher.Flux;

import java.util.Arrays;

public class ExamplesCreateFluxAndMono {

    public static void main(String[] args) {
        Flux<String> stream1 = Flux.just("Hello", "world");
        Flux<Integer> stream2 = Flux.fromArray(new Integer[]{1, 2, 3});
        Flux<Integer> stream3 = Flux.fromIterable(Arrays.asList(9, 8, 7));
        Flux<Integer> stream4 = Flux.range(2010, 9);
        System.out.println("asdf");
    }
}
