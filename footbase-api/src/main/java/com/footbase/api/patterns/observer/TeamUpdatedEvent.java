package com.footbase.api.patterns.observer;

import com.footbase.api.domain.Team;

import java.time.Instant;
import java.util.Map;

/**
 * Örnek domain olayı: takım bilgisi güncellendi.
 */
public class TeamUpdatedEvent extends DomainEvent {
    public TeamUpdatedEvent(Team team) {
        super(
                "team.updated",
                Instant.now(),
                Map.of(
                        "teamId", team.getId(),
                        "name", team.getName(),
                        "code", team.getCode()
                )
        );
    }
}
