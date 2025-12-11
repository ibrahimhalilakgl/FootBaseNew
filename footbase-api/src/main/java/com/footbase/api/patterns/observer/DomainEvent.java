package com.footbase.api.patterns.observer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class DomainEvent {
    private final String name;
    private final Instant occurredAt;
    private final Map<String, Object> payload;
}
