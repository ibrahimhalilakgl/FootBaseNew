package com.footbase.api.service;

import com.footbase.api.domain.CommentLike;
import com.footbase.api.domain.MatchComment;
import com.footbase.api.domain.MatchFixture;
import com.footbase.api.domain.MatchPrediction;
import com.footbase.api.domain.UserAccount;
import com.footbase.api.dto.*;
import com.footbase.api.exception.NotFoundException;
import com.footbase.api.repository.CommentLikeRepository;
import com.footbase.api.repository.MatchCommentRepository;
import com.footbase.api.repository.MatchPredictionRepository;
import com.footbase.api.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchPredictionRepository matchPredictionRepository;
    private final MatchCommentRepository matchCommentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CurrentUser currentUser;

    @Transactional(readOnly = true)
    public List<MatchDto> listMatches() {
        UserAccount user = currentUser.get();
        return matchRepository.findAll()
                .stream()
                .map(match -> toDto(match, user))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MatchDto> searchMatches(Long teamId, String status, Instant fromDate, Instant toDate) {
        UserAccount user = currentUser.get();
        return matchRepository.searchMatches(teamId, status, fromDate, toDate)
                .stream()
                .map(match -> toDto(match, user))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MatchDto getMatch(Long id) {
        MatchFixture match = matchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mac bulunamadi"));
        return toDto(match, currentUser.get());
    }

    @Transactional
    public PredictionDto upsertPrediction(Long matchId, PredictionRequest request) {
        MatchFixture match = matchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Mac bulunamadi"));
        UserAccount user = requireUser();
        MatchPrediction prediction = matchPredictionRepository.findByMatchAndUser(match, user)
                .orElseGet(() -> MatchPrediction.builder()
                        .match(match)
                        .user(user)
                        .build());
        prediction.setHomeScore(request.getHomeScore());
        prediction.setAwayScore(request.getAwayScore());
        MatchPrediction saved = matchPredictionRepository.save(prediction);
        return PredictionDto.builder()
                .id(saved.getId())
                .homeScore(saved.getHomeScore())
                .awayScore(saved.getAwayScore())
                .build();
    }

    @Transactional
    public CommentDto addComment(Long matchId, CommentRequest request) {
        MatchFixture match = matchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Mac bulunamadi"));
        UserAccount user = requireUser();
        MatchComment comment = MatchComment.builder()
                .match(match)
                .user(user)
                .message(request.getMessage())
                .build();
        MatchComment saved = matchCommentRepository.save(comment);
        return toCommentDto(saved, user);
    }

    @Transactional
    public CommentDto updateComment(Long commentId, CommentRequest request) {
        MatchComment comment = matchCommentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Yorum bulunamadi"));
        UserAccount user = requireUser();

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Bu yorumu duzenleme yetkiniz yok");
        }

        comment.setMessage(request.getMessage());
        MatchComment saved = matchCommentRepository.save(comment);
        return toCommentDto(saved, user);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        MatchComment comment = matchCommentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Yorum bulunamadi"));
        UserAccount user = requireUser();

        boolean isOwner = comment.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new IllegalStateException("Bu yorumu silme yetkiniz yok");
        }

        matchCommentRepository.delete(comment);
    }

    @Transactional
    public void toggleCommentLike(Long commentId) {
        MatchComment comment = matchCommentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Yorum bulunamadi"));
        UserAccount user = requireUser();

        commentLikeRepository.findByCommentAndUser(comment, user)
                .ifPresentOrElse(
                        commentLikeRepository::delete,
                        () -> {
                            CommentLike like = CommentLike.builder()
                                    .comment(comment)
                                    .user(user)
                                    .build();
                            commentLikeRepository.save(like);
                        }
                );
    }

    private MatchDto toDto(MatchFixture match, UserAccount user) {
        PredictionDto userPrediction = null;
        if (user != null) {
            userPrediction = matchPredictionRepository.findByMatchAndUser(match, user)
                    .map(pred -> PredictionDto.builder()
                            .id(pred.getId())
                            .homeScore(pred.getHomeScore())
                            .awayScore(pred.getAwayScore())
                            .build())
                    .orElse(null);
        }

        List<CommentDto> comments = matchCommentRepository.findByMatchOrderByCreatedAtDesc(match)
                .stream()
                .map(c -> toCommentDto(c, user))
                .collect(Collectors.toList());

        return MatchDto.builder()
                .id(match.getId())
                .homeTeam(match.getHomeTeam() != null ? match.getHomeTeam().getName() : null)
                .awayTeam(match.getAwayTeam() != null ? match.getAwayTeam().getName() : null)
                .kickoffAt(match.getKickoffAt())
                .venue(match.getVenue())
                .status(match.getStatus())
                .homeScore(match.getHomeScore())
                .awayScore(match.getAwayScore())
                .homeTeamLogo(match.getHomeTeam() != null ? match.getHomeTeam().getLogoUrl() : null)
                .awayTeamLogo(match.getAwayTeam() != null ? match.getAwayTeam().getLogoUrl() : null)
                .userPrediction(userPrediction)
                .comments(comments)
                .build();
    }

    private CommentDto toCommentDto(MatchComment comment, UserAccount currentUser) {
        boolean isLiked = currentUser != null && commentLikeRepository.existsByCommentAndUser(comment, currentUser);
        long likesCount = commentLikeRepository.countByComment(comment);
        boolean isOwner = currentUser != null && comment.getUser().getId().equals(currentUser.getId());

        return CommentDto.builder()
                .id(comment.getId())
                .author(comment.getUser().getDisplayName())
                .authorId(comment.getUser().getId())
                .message(comment.getMessage())
                .createdAt(comment.getCreatedAt())
                .likesCount(likesCount)
                .isLiked(isLiked)
                .canDelete(isOwner)
                .canEdit(isOwner)
                .build();
    }

    private UserAccount requireUser() {
        UserAccount user = currentUser.get();
        if (user == null) {
            throw new IllegalStateException("Oturum bulunamadi");
        }
        return user;
    }
}
