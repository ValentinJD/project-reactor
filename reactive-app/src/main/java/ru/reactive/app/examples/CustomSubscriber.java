package ru.reactive.app.examples;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;

public class CustomSubscriber implements Subscriber<String> {

    static Logger log = org.slf4j.LoggerFactory.getLogger(CustomSubscriber.class);

    volatile Subscription subscription; // (1)

    @Override
    public void onSubscribe(Subscription s) { // (2)
        subscription = s; // (2.1)
        log.info("initial request for 1 element"); //
        subscription.request(1); // (2.2)
    }

    @Override
    public void onNext(String s) { // (3)
        log.info("onNext: {}", s); //
        log.info("requesting 1 more element"); //
        subscription.request(1); // (3.1)
    }

    @Override
    public void onComplete() {
        log.info("onComplete");
    }

    @Override
    public void onError(Throwable t) {
        log.warn("onError: {}", t.getMessage());
    }

}
