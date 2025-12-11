package com.footbase.api.service;

import com.footbase.api.domain.Player;
import com.footbase.api.domain.PlayerRating;
import com.footbase.api.domain.UserAccount;
import com.footbase.api.dto.PlayerDto;
import com.footbase.api.dto.PlayerRatingDto;
import com.footbase.api.dto.PlayerRatingRequest;
import com.footbase.api.exception.NotFoundException;
import com.footbase.api.repository.PlayerRatingRepository;
import com.footbase.api.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerRatingRepository playerRatingRepository;
    private final CurrentUser currentUser;

    @Transactional(readOnly = true)
    public List<PlayerDto> listPlayers() {
        return playerRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlayerDto getPlayer(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Oyuncu bulunamadı"));
        return toDto(player);
    }

    private PlayerDto toDto(Player player) {
        Double avgRating = playerRatingRepository.findAverageScoreByPlayer(player.getId()).orElse(0.0);
        Long ratingCount = playerRatingRepository.countByPlayer(player.getId());

        return PlayerDto.builder()
                .id(player.getId())
                .fullName(player.getFullName())
                .position(player.getPosition())
                .shirtNumber(player.getShirtNumber())
                .team(player.getTeam() != null ? player.getTeam().getName() : null)
                .teamId(player.getTeam() != null ? player.getTeam().getId() : null)
                .imageUrl(player.getImageUrl())
                .externalId(player.getExternalId())
                .averageRating(avgRating)
                .ratingCount(ratingCount)
                .build();
    }

    @Transactional
    public PlayerRatingDto ratePlayer(Long playerId, PlayerRatingRequest request) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException("Oyuncu bulunamadı"));
        UserAccount user = requireUser();
        PlayerRating rating = playerRatingRepository.findByPlayerAndUser(player, user)
                .orElseGet(() -> PlayerRating.builder()
                        .player(player)
                        .user(user)
                        .build());
        rating.setScore(request.getScore());
        rating.setComment(request.getComment());
        PlayerRating saved = playerRatingRepository.save(rating);
        return PlayerRatingDto.builder()
                .id(saved.getId())
                .score(saved.getScore())
                .comment(saved.getComment())
                .author(user.getDisplayName())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<PlayerRatingDto> listRatings(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException("Oyuncu bulunamadı"));
        return playerRatingRepository.findByPlayer(player)
                .stream()
                .map(r -> PlayerRatingDto.builder()
                        .id(r.getId())
                        .score(r.getScore())
                        .comment(r.getComment())
                        .author(r.getUser().getDisplayName())
                        .createdAt(r.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    private UserAccount requireUser() {
        UserAccount user = currentUser.get();
        if (user == null) {
            throw new IllegalStateException("Oturum bulunamadı");
        }
        return user;
    }
}
