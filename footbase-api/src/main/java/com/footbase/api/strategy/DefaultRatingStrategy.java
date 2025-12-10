package com.footbase.api.strategy;

import com.footbase.api.domain.PlayerRating;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class DefaultRatingStrategy implements RatingStrategy {
    @Override
    public double calculateRating(PlayerRating rating) {
        return rating.getScore();
    }

    @Override
    public String getStrategyName() {
        return "DEFAULT";
    }
}

