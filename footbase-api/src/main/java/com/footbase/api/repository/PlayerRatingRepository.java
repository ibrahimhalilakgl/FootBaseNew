package com.footbase.api.repository;

import com.footbase.api.domain.Player;
import com.footbase.api.domain.PlayerRating;
import com.footbase.api.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlayerRatingRepository extends JpaRepository<PlayerRating, Long> {
    List<PlayerRating> findByPlayer(Player player);
    Optional<PlayerRating> findByPlayerAndUser(Player player, UserAccount user);
    List<PlayerRating> findByUserInOrderByCreatedAtDesc(List<UserAccount> users);
    
    @Query("SELECT AVG(pr.score) FROM PlayerRating pr WHERE pr.player.id = :playerId")
    Optional<Double> findAverageScoreByPlayer(@Param("playerId") Long playerId);
    
    @Query("SELECT COUNT(pr) FROM PlayerRating pr WHERE pr.player.id = :playerId")
    Long countByPlayer(@Param("playerId") Long playerId);
    
    long countByPlayer(Player player);
}

