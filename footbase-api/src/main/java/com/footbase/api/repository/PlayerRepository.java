package com.footbase.api.repository;

import com.footbase.api.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    java.util.List<Player> findTop3ByOrderByIdDesc();
}

