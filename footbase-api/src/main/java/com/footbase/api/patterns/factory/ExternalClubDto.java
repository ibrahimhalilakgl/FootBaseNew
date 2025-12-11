package com.footbase.api.patterns.factory;

import lombok.Builder;
import lombok.Value;

/**
 * Harici kaynaktan (ör. SoccerWiki) gelen kulüp verisinin iç modelde kullanılacak sade DTO'su.
 */
@Value
@Builder
public class ExternalClubDto {
    Long externalId;
    String name;
    String shortName;
    String logoUrl;
    String city;
    String stadium;
    String league;
}
