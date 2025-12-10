package com.footbase.api.strategy;

import com.footbase.api.domain.PlayerRating;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class WeightedRatingStrategy implements RatingStrategy {
    @Override
    public double calculateRating(PlayerRating rating) {
        double baseScore = rating.getScore();
        
        // Son 30 gün içindeki puanlamalar daha ağırlıklı
        long daysSinceRating = ChronoUnit.DAYS.between(rating.getCreatedAt(), Instant.now());
        double weight = daysSinceRating <= 30 ? 1.2 : 1.0;
        
        return baseScore * weight;
    }

    @Override
    public String getStrategyName() {
        return "WEIGHTED";
    }
}

