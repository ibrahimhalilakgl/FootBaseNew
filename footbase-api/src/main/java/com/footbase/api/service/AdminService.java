package com.footbase.api.service;

import com.footbase.api.domain.*;
import com.footbase.api.dto.*;
import com.footbase.api.exception.ConflictException;
import com.footbase.api.exception.NotFoundException;
import com.footbase.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final CurrentUser currentUser;

    @Transactional
    public MatchDto createMatch(AdminMatchRequest request) {
        requireAdmin();
        
        Team homeTeam = teamRepository.findById(request.getHomeTeamId())
                .orElseThrow(() -> new NotFoundException("Ev sahibi takım bulunamadı"));
        Team awayTeam = teamRepository.findById(request.getAwayTeamId())
                .orElseThrow(() -> new NotFoundException("Deplasman takımı bulunamadı"));

        MatchFixture match = MatchFixture.builder()
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .kickoffAt(request.getKickoffAt())
                .venue(request.getVenue())
                .status(request.getStatus() != null ? request.getStatus() : "SCHEDULED")
                .build();

        MatchFixture saved = matchRepository.save(match);
        return toMatchDto(saved);
    }

    @Transactional
    public MatchDto updateMatch(Long id, AdminMatchRequest request) {
        requireAdmin();
        
        MatchFixture match = matchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Maç bulunamadı"));

        if (request.getHomeTeamId() != null) {
            Team homeTeam = teamRepository.findById(request.getHomeTeamId())
                    .orElseThrow(() -> new NotFoundException("Ev sahibi takım bulunamadı"));
            match.setHomeTeam(homeTeam);
        }

        if (request.getAwayTeamId() != null) {
            Team awayTeam = teamRepository.findById(request.getAwayTeamId())
                    .orElseThrow(() -> new NotFoundException("Deplasman takımı bulunamadı"));
            match.setAwayTeam(awayTeam);
        }

        if (request.getKickoffAt() != null) {
            match.setKickoffAt(request.getKickoffAt());
        }

        if (request.getVenue() != null) {
            match.setVenue(request.getVenue());
        }

        if (request.getStatus() != null) {
            match.setStatus(request.getStatus());
        }

        MatchFixture saved = matchRepository.save(match);
        return toMatchDto(saved);
    }

    @Transactional
    public Team createTeam(AdminTeamRequest request) {
        requireAdmin();
        
        if (teamRepository.existsByCode(request.getCode())) {
            throw new ConflictException("Bu kod zaten kullanılıyor");
        }

        Team team = Team.builder()
                .name(request.getName())
                .code(request.getCode())
                .build();

        return teamRepository.save(team);
    }

    @Transactional
    public Team updateTeam(Long id, AdminTeamRequest request) {
        requireAdmin();
        
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Takım bulunamadı"));

        if (request.getName() != null) {
            team.setName(request.getName());
        }

        if (request.getCode() != null && !team.getCode().equals(request.getCode())) {
            if (teamRepository.existsByCode(request.getCode())) {
                throw new ConflictException("Bu kod zaten kullanılıyor");
            }
            team.setCode(request.getCode());
        }

        return teamRepository.save(team);
    }

    @Transactional
    public Player createPlayer(AdminPlayerRequest request) {
        requireAdmin();
        
        Team team = null;
        if (request.getTeamId() != null) {
            team = teamRepository.findById(request.getTeamId())
                    .orElseThrow(() -> new NotFoundException("Takım bulunamadı"));
        }

        Player player = Player.builder()
                .team(team)
                .fullName(request.getFullName())
                .position(request.getPosition())
                .shirtNumber(request.getShirtNumber())
                .build();

        return playerRepository.save(player);
    }

    @Transactional
    public Player updatePlayer(Long id, AdminPlayerRequest request) {
        requireAdmin();
        
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Oyuncu bulunamadı"));

        if (request.getTeamId() != null) {
            Team team = teamRepository.findById(request.getTeamId())
                    .orElseThrow(() -> new NotFoundException("Takım bulunamadı"));
            player.setTeam(team);
        }

        if (request.getFullName() != null) {
            player.setFullName(request.getFullName());
        }

        if (request.getPosition() != null) {
            player.setPosition(request.getPosition());
        }

        if (request.getShirtNumber() != null) {
            player.setShirtNumber(request.getShirtNumber());
        }

        return playerRepository.save(player);
    }

    private MatchDto toMatchDto(MatchFixture match) {
        return MatchDto.builder()
                .id(match.getId())
                .homeTeam(match.getHomeTeam() != null ? match.getHomeTeam().getName() : null)
                .awayTeam(match.getAwayTeam() != null ? match.getAwayTeam().getName() : null)
                .kickoffAt(match.getKickoffAt())
                .venue(match.getVenue())
                .status(match.getStatus())
                .build();
    }

    private void requireAdmin() {
        UserAccount user = currentUser.get();
        if (user == null) {
            throw new IllegalStateException("Oturum bulunamadı");
        }
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new IllegalStateException("Bu işlem için admin yetkisi gereklidir");
        }
    }
}

