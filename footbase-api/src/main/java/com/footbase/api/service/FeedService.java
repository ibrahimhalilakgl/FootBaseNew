package com.footbase.api.service;

import com.footbase.api.domain.*;
import com.footbase.api.dto.FeedItemDto;
import com.footbase.api.repository.CommentLikeRepository;
import com.footbase.api.repository.MatchCommentRepository;
import com.footbase.api.repository.PlayerRatingRepository;
import com.footbase.api.repository.UserFollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final UserFollowRepository userFollowRepository;
    private final MatchCommentRepository matchCommentRepository;
    private final PlayerRatingRepository playerRatingRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CurrentUser currentUser;

    @Transactional(readOnly = true)
    public List<FeedItemDto> getFeed() {
        UserAccount user = requireUser();
        
        // Takip edilen kullanıcıları al
        List<UserAccount> following = userFollowRepository.findByFollower(user)
                .stream()
                .map(UserFollow::getFollowing)
                .collect(Collectors.toList());

        if (following.isEmpty()) {
            return List.of();
        }

        // Takip edilenlerin yorumlarını al
        List<MatchComment> comments = matchCommentRepository.findByUserInOrderByCreatedAtDesc(following);
        
        // Takip edilenlerin puanlamalarını al
        List<PlayerRating> ratings = playerRatingRepository.findByUserInOrderByCreatedAtDesc(following);

        // Her ikisini birleştir ve sırala
        Stream<FeedItemDto> commentStream = comments.stream()
                .map(this::commentToFeedItem);
        
        Stream<FeedItemDto> ratingStream = ratings.stream()
                .map(this::ratingToFeedItem);

        return Stream.concat(commentStream, ratingStream)
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(50)
                .collect(Collectors.toList());
    }

    private FeedItemDto commentToFeedItem(MatchComment comment) {
        UserAccount current = currentUser.get();
        boolean isLiked = current != null && commentLikeRepository.existsByCommentAndUser(comment, current);
        long likesCount = commentLikeRepository.countByComment(comment);

        return FeedItemDto.builder()
                .id(comment.getId())
                .type("COMMENT")
                .author(comment.getUser().getDisplayName())
                .content(comment.getMessage())
                .createdAt(comment.getCreatedAt())
                .matchId(comment.getMatch().getId())
                .matchInfo(String.format("%s vs %s", 
                    comment.getMatch().getHomeTeam() != null ? comment.getMatch().getHomeTeam().getName() : "TBD",
                    comment.getMatch().getAwayTeam() != null ? comment.getMatch().getAwayTeam().getName() : "TBD"))
                .likesCount(likesCount)
                .isLiked(isLiked)
                .build();
    }

    private FeedItemDto ratingToFeedItem(PlayerRating rating) {
        return FeedItemDto.builder()
                .id(rating.getId())
                .type("RATING")
                .author(rating.getUser().getDisplayName())
                .content(rating.getComment())
                .createdAt(rating.getCreatedAt())
                .playerId(rating.getPlayer().getId())
                .playerName(rating.getPlayer().getFullName())
                .rating(rating.getScore())
                .build();
    }

    private UserAccount requireUser() {
        UserAccount user = currentUser.get();
        if (user == null) {
            throw new IllegalStateException("Oturum bulunamadı");
        }
        return user;
    }
}

