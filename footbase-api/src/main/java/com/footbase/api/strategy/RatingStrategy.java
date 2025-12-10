package com.footbase.api.strategy;

import com.footbase.api.domain.PlayerRating;

public interface RatingStrategy {
    double calculateRating(PlayerRating rating);
    String getStrategyName();
}

