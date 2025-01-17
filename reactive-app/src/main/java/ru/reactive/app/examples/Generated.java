package ru.reactive.app.examples;

import org.slf4j.Logger;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

public class Generated {
    static Logger log = org.slf4j.LoggerFactory.getLogger(Operators.class);

    public void generate() {
        Flux.generate( // (1)
                        () -> Tuples.of(0L, 1L), // (1.1)
                        (state, sink) -> { //
                            log.info("generated value: {}", state.getT2()); //
                            sink.next(state.getT2()); // (1.2)
                            long newValue = state.getT1() + state.getT2(); //
                            return Tuples.of(state.getT2(), newValue); // (1.3)
                        })
                .delayElements(Duration.ofMillis(1)) // (2)
                .take(7) // (3)
                .subscribe(e -> log.info("onNext: {}", e)); // (4)
    }

    public void using() {
        Flux<String> ioRequestResults = Flux.using( // (1)
                () -> new Connection().newConnection(), // (1.1)
                connection -> Flux.fromIterable(connection.getData()), // (1.2)
                Connection::close // (1.3)
        );
        ioRequestResults.subscribe( // (2)
                data -> log.info("Received data: {}", data), //
                e -> log.info("Error: {}", e.getMessage()), //
                () -> log.info("Stream finished"));
    }

    public class Connection implements AutoCloseable {private final Random rnd = new Random();
        public Iterable<String> getData() { // (2)
            if (rnd.nextInt(10) < 3) { // (2.1)
                throw new RuntimeException("Communication error");
            }
            return Arrays.asList("Some", "data"); // (2.2)
        }
        public void close() { // (3)
            log.info("IO Connection closed");
        }

        public Connection newConnection() { // (4)
            log.info("IO Connection created");
            return new Connection();
        }
    }
}
