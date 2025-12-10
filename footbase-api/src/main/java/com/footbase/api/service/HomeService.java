package com.footbase.api.service;

import com.footbase.api.domain.MatchComment;
import com.footbase.api.domain.MatchFixture;
import com.footbase.api.domain.Player;
import com.footbase.api.dto.HomePageDto;
import com.footbase.api.repository.MatchCommentRepository;
import com.footbase.api.repository.MatchRepository;
import com.footbase.api.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final MatchCommentRepository matchCommentRepository;

    @Transactional(readOnly = true)
    public HomePageDto getHome() {
        List<Player> topPlayers = playerRepository.findTop3ByOrderByIdDesc();
        List<MatchFixture> topMatches = matchRepository.findTop3ByOrderByIdDesc();
        List<MatchComment> topComments = matchCommentRepository.findTop3ByOrderByIdDesc();

        return HomePageDto.builder()
                .players(topPlayers.stream().map(this::toPlayerDto).collect(Collectors.toList()))
                .matches(topMatches.stream().map(this::toMatchDto).collect(Collectors.toList()))
                .comments(topComments.stream().map(this::toCommentDto).collect(Collectors.toList()))
                .build();
    }

    private HomePageDto.SimplePlayerDto toPlayerDto(Player p) {
        return HomePageDto.SimplePlayerDto.builder()
                .id(p.getId())
                .fullName(p.getFullName())
                .position(p.getPosition())
                .team(p.getTeam() != null ? p.getTeam().getName() : null)
                .build();
    }

    private HomePageDto.SimpleMatchDto toMatchDto(MatchFixture m) {
        return HomePageDto.SimpleMatchDto.builder()
                .id(m.getId())
                .homeTeam(m.getHomeTeam() != null ? m.getHomeTeam().getName() : null)
                .awayTeam(m.getAwayTeam() != null ? m.getAwayTeam().getName() : null)
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

