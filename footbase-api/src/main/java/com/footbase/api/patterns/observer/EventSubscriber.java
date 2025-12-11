package com.footbase.api.patterns.observer;

public interface EventSubscriber {
    void onEvent(DomainEvent event);
}
