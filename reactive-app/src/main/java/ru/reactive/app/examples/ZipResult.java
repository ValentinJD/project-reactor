package ru.reactive.app.examples;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

public class ZipResult {
    //1 Создаем Mono
    //2 Проверяем если он пустой возвращаем  Моно с пустым списком если
    // размер больше 100 возвращаем пустой Моно
    // 3 Создаем Flux
    // 4 Объединяем их в один Flux
    //5 В методе зип внутри лямбды обрабатываем второй результат

    public static void main(String[] args) {
        ZipResult zipResult = new ZipResult();
        ZipResults zip = zipResult.zip(Mono.just(Arrays.asList(1, 2, 3, 4, 5)),
                List.of("1", "2", "3", "4", "5"));
    }

    public ZipResults zip(Mono<List<Integer>> gosbis, List<String> ucpids) {
        return Mono.zip(getUcpIds(ucpids)
                                .log(),
                        gosbis.defaultIfEmpty(Collections.emptyList())
                                .log())
                .map(tuple -> {
                    List<String> strings = tuple.getT1().isEmpty() ? null : tuple.getT1();
                    List<Integer> integers = tuple.getT2();
                    return new ZipResults(strings, integers);
                }).block();
    }

    public Mono<List<String>> getUcpIds(List<String> request) {
        Mono<List<String>> firstUcpidsInMono = getFirstUcpidsInMono(request);
        return firstUcpidsInMono
                .flatMap(ucpIdsFirst -> {
                    if (Objects.isNull(ucpIdsFirst) || ucpIdsFirst.isEmpty()) {
                        return Mono.just(Collections.emptyList());
                    }
                    if (ucpIdsFirst.size() > 100) {
                        return Mono.empty();
                    }
                    Flux<List<String>> nextUcpidsInFlux = nextUcpids(ucpIdsFirst);
                    return mergeInMono(ucpIdsFirst, nextUcpidsInFlux);
                });
    }

    public Mono<List<String>> getFirstUcpidsInMono(List<String> strings) {
        return Mono.just(strings);
    }

    private Flux<List<String>> nextUcpids(List<String> strings) {
        return Flux.just(strings);
    }

    @AllArgsConstructor
    @Data
    public static class ZipResults {
        private List<String> strings;
        private List<Integer> integers;
    }

    public Mono<List<String>> getUcpIds2(List<String> request) {
        Mono<List<String>> firstUcpidsInMono = getFirstUcpidsInMono(request);
        Flux<List<String>> firstUcpidsInMono2 = nextUcpids(Arrays.asList("1", "2"));
        return Flux.merge(firstUcpidsInMono, firstUcpidsInMono2)
                .flatMap(Flux::fromIterable)
                .distinct()
                .collectList();
    }

    public Mono<List<String>> getUcpIds3(List<String> request) {
        Mono<List<String>> firstUcpidsInMono = getFirstUcpidsInMono(request);
        Flux<List<String>> nextUcpidsInFlux = nextUcpids(Arrays.asList("1", "2"));
        return firstUcpidsInMono.flatMap(strings -> mergeInMono(strings, nextUcpidsInFlux));
    }

    private static Mono<List<String>> mergeInMono(List<String> strings, Flux<List<String>> nextUcpidsInFlux) {
        Flux<String> ucpIdsFlux = Flux.fromIterable(strings);
        Flux<String> stringFlux = nextUcpidsInFlux.flatMap(Flux::fromIterable);
        return Flux.merge(ucpIdsFlux, stringFlux)
                .distinct()
                .collectList();
    }
}
