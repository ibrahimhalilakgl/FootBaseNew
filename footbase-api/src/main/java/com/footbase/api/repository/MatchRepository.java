package com.footbase.api.repository;

import com.footbase.api.domain.MatchFixture;
import com.footbase.api.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface MatchRepository extends JpaRepository<MatchFixture, Long> {
    @Query("SELECT m FROM MatchFixture m WHERE " +
           "(:teamId IS NULL OR m.homeTeam.id = :teamId OR m.awayTeam.id = :teamId) AND " +
           "(:status IS NULL OR m.status = :status) AND " +
           "(:fromDate IS NULL OR m.kickoffAt >= :fromDate) AND " +
           "(:toDate IS NULL OR m.kickoffAt <= :toDate)")
    List<MatchFixture> searchMatches(
            @Param("teamId") Long teamId,
            @Param("status") String status,
            @Param("fromDate") Instant fromDate,
            @Param("toDate") Instant toDate
    );
    
    List<MatchFixture> findByHomeTeamOrAwayTeam(Team homeTeam, Team awayTeam);
    List<MatchFixture> findByStatus(String status);
    List<MatchFixture> findTop3ByKickoffAtAfterOrderByKickoffAtAsc(Instant now);
    List<MatchFixture> findTop3ByOrderByKickoffAtAsc();
}
