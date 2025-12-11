package com.footbase.api.patterns.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Basit observer/event-bus: aboneleri saklar ve publish çağrısında tetikler.
 */
public class SimpleEventBus {
    private final List<EventSubscriber> subscribers = new CopyOnWriteArrayList<>();

    public void register(EventSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unregister(EventSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void publish(DomainEvent event) {
        for (EventSubscriber subscriber : subscribers) {
            subscriber.onEvent(event);
        }
    }
}
