package com.footbase.api.service;

import com.footbase.api.domain.MatchComment;
import com.footbase.api.domain.MatchFixture;
import com.footbase.api.dto.HomePageDto;
import com.footbase.api.repository.MatchCommentRepository;
import com.footbase.api.repository.MatchRepository;
import com.footbase.api.repository.PlayerRepository;
import com.footbase.api.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final MatchRepository matchRepository;
    private final MatchCommentRepository matchCommentRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public HomePageDto getHome() {
        Instant now = Instant.now();
        List<MatchFixture> upcoming = matchRepository.findTop3ByKickoffAtAfterOrderByKickoffAtAsc(now);
        if (upcoming.isEmpty()) {
            upcoming = matchRepository.findTop3ByOrderByKickoffAtAsc();
        }
        List<MatchComment> topComments = matchCommentRepository.findTop3ByOrderByIdDesc();

        return HomePageDto.builder()
                .upcomingMatches(upcoming.stream().map(this::toMatchDto).collect(Collectors.toList()))
                .comments(topComments.stream().map(this::toCommentDto).collect(Collectors.toList()))
                .playerCount(playerRepository.count())
                .teamCount(teamRepository.count())
                .build();
    }

    private HomePageDto.SimpleMatchDto toMatchDto(MatchFixture m) {
        return HomePageDto.SimpleMatchDto.builder()
                .id(m.getId())
                .homeTeam(m.getHomeTeam() != null ? m.getHomeTeam().getName() : null)
                .awayTeam(m.getAwayTeam() != null ? m.getAwayTeam().getName() : null)
                .homeTeamLogo(m.getHomeTeam() != null ? m.getHomeTeam().getLogoUrl() : null)
                .awayTeamLogo(m.getAwayTeam() != null ? m.getAwayTeam().getLogoUrl() : null)
                .homeScore(m.getHomeScore())
                .awayScore(m.getAwayScore())
                .status(m.getStatus())
                .kickoffAt(m.getKickoffAt())
                .build();
    }

    private HomePageDto.SimpleCommentDto toCommentDto(MatchComment c) {
        return HomePageDto.SimpleCommentDto.builder()
                .id(c.getId())
                .author(c.getUser() != null ? c.getUser().getDisplayName() : null)
                .message(c.getMessage())
                .matchId(c.getMatch() != null ? c.getMatch().getId() : null)
                .createdAt(c.getCreatedAt())
                .build();
    }
}
