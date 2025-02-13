package ru.reactive.app.examples;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.reactive.app.User;

import java.util.Arrays;
import java.util.Optional;

public class CreateFluxAndMono {

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
        CustomSubscriber subscriber = new CustomSubscriber();
        Flux<String> stream = Flux.just("Hello", "world", "!"); // (4)
        stream.subscribe(subscriber);
    }

    private static String httpRequest() {
        return null;
    }

    // isValidSession вызывается только после подписки на Flux
    Mono<User> requestUserData(String sessionId) {
        Mono<User> invalidUserSession = Mono.defer(() ->
                isValidSession(sessionId)
                        ? Mono.fromCallable(() -> requestUser(sessionId))
                        : Mono.error(new RuntimeException("Invalid user session")));
        Disposable subscribe = invalidUserSession.subscribe();
        return invalidUserSession;
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
