package ru.reactive.app.examples;

import org.slf4j.Logger;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;


public class Subscribe {

    static Logger log = org.slf4j.LoggerFactory.getLogger(Subscribe.class);

    public static void main(String[] args) throws InterruptedException {
        Transaction transaction = new Transaction(1);
        transaction.updateData();
        Thread.sleep(6000);
    }

    public static void subscribe() {
        Flux.just("A", "B", "C")
                .subscribe(
                        data -> log.info("onNext: {}", data),
                        err -> { /* игнорируется */ },
                        () -> log.info("onComplete"));
    }

    // Управляем количеством обрабатываемых элементов
    public static void hand() {
        Flux.range(1, 100) //(1)
                .subscribe( // (2)
                        data -> log.info("onNext: {}", data),
                        err -> { /* игнорируется */ },
                        () -> log.info("onComplete"),
                        subscription -> { // (3)
                            subscription.request(4); // (3.1)
                            subscription.cancel(); // (3.2)
                        }
                );
    }

    // Отмена потока
    public static void cancel() {
        Disposable disposable = Flux.interval(Duration.ofMillis(50)) // (1)
                .subscribe( // (2)
                        data -> {
                            log.info("onNext: {}", data);
                            if (data == 3) {
                                throw new RuntimeException();
                            }
                        },
                        err -> {
                            log.error("onError: {}", err);
                        }
                );
        try {
            Thread.sleep(200); // (3)
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        disposable.dispose();
    }
}
