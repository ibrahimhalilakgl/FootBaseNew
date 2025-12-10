package com.footbase.api.repository;

import com.footbase.api.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByCode(String code);
    boolean existsByCode(String code);
}

