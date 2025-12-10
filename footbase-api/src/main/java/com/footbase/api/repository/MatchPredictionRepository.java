package com.footbase.api.repository;

import com.footbase.api.domain.MatchFixture;
import com.footbase.api.domain.MatchPrediction;
import com.footbase.api.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchPredictionRepository extends JpaRepository<MatchPrediction, Long> {
    Optional<MatchPrediction> findByMatchAndUser(MatchFixture match, UserAccount user);
}

