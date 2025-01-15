package ru.reactive.app.examples;

import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import reactor.core.publisher.BaseSubscriber;

class MySubscriber<T> extends BaseSubscriber<T> {
    static Logger log = org.slf4j.LoggerFactory.getLogger(MySubscriber.class);

    @Override
    public void hookOnSubscribe(Subscription subscription) {
        log.info("initial request for 1 element");
        request(1);
    }

    @Override
    public void hookOnNext(T value) {
        log.info("onNext: {}", value);
        log.info("requesting 1 more element");
        request(1);
    }
}
