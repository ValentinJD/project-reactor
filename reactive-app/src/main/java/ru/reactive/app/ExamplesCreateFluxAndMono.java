package ru.reactive.app;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

public class ExamplesCreateFluxAndMono {

    public static void main(String[] args) {
        Flux<String> stream1 = Flux.just("Hello", "world");
        Flux<Integer> stream2 = Flux.fromArray(new Integer[]{1, 2, 3});
        Flux<Integer> stream3 = Flux.fromIterable(Arrays.asList(9, 8, 7));
        Flux<Integer> stream4 = Flux.range(2010, 9);
        System.out.println("asdf");
        Mono<String> stream5 = Mono.just("One");
        Mono<String> stream6 = Mono.justOrEmpty(null);
        Mono<String> stream7 = Mono.justOrEmpty(Optional.empty());
        Mono<String> stream8 = Mono.fromCallable(() -> httpRequest());
        Flux<String> empty = Flux.empty();
        Flux<String> never = Flux.never();
        Mono<String> error = Mono.error(new RuntimeException("Unknown id"));
    }

    private static String httpRequest() {
        return null;
    }

    // isValidSession вызывается только после подписки на Flux
    Mono<User> requestUserData(String sessionId) {
        return Mono.defer(() ->
                isValidSession(sessionId)
                        ? Mono.fromCallable(() -> requestUser(sessionId))
                        : Mono.error(new RuntimeException("Invalid user session")));
    }

    // isValidSession вызывается сразу
    Mono<User> requestUserData2(String sessionId) {
        return isValidSession(sessionId)
                ? Mono.fromCallable(() -> requestUser(sessionId))
                : Mono.error(new RuntimeException("Invalid user session"));
    }

    private User requestUser(String sessionId) {
        return null;
    }

    private static boolean isValidSession(String sessionId) {
        return true;
    }

}
