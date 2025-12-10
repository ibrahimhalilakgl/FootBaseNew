package com.footbase.api.service;

import com.footbase.api.domain.Player;
import com.footbase.api.domain.PlayerRating;
import com.footbase.api.repository.PlayerRatingRepository;
import com.footbase.api.strategy.RatingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingCalculationService {

    private final PlayerRatingRepository playerRatingRepository;
    @Qualifier("defaultRatingStrategy")
    private final RatingStrategy defaultStrategy;

    @Transactional(readOnly = true)
    public Double calculateAverageRating(Long playerId, RatingStrategy strategy) {
        Player player = new Player();
        player.setId(playerId);
        
        List<PlayerRating> ratings = playerRatingRepository.findByPlayer(player);
        
        if (ratings.isEmpty()) {
            return null;
        }

        RatingStrategy usedStrategy = strategy != null ? strategy : defaultStrategy;
        
        double sum = ratings.stream()
                .mapToDouble(usedStrategy::calculateRating)
                .sum();
        
        return sum / ratings.size();
    }

    @Transactional(readOnly = true)
    public Double calculateAverageRating(Long playerId) {
        return calculateAverageRating(playerId, null);
    }
}

