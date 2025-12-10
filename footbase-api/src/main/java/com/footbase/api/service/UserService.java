package com.footbase.api.service;

import com.footbase.api.domain.MatchComment;
import com.footbase.api.domain.UserAccount;
import com.footbase.api.domain.UserFollow;
import com.footbase.api.dto.UserProfileDto;
import com.footbase.api.dto.UserRecentCommentDto;
import com.footbase.api.exception.ConflictException;
import com.footbase.api.exception.NotFoundException;
import com.footbase.api.repository.MatchCommentRepository;
import com.footbase.api.repository.UserFollowRepository;
import com.footbase.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserFollowRepository userFollowRepository;
    private final MatchCommentRepository matchCommentRepository;
    private final CurrentUser currentUser;

    @Transactional(readOnly = true)
    public UserProfileDto getProfile(Long userId) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı"));

        UserAccount current = currentUser.get();
        boolean isOwnProfile = current != null && current.getId().equals(userId);
        boolean isFollowing = false;

        if (current != null && !isOwnProfile) {
            isFollowing = userFollowRepository.existsByFollowerAndFollowing(current, user);
        }

        long followersCount = userFollowRepository.countByFollowing(user);
        long followingCount = userFollowRepository.countByFollower(user);

        return UserProfileDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .createdAt(user.getCreatedAt())
                .followersCount(followersCount)
                .followingCount(followingCount)
                .isFollowing(isFollowing)
                .isOwnProfile(isOwnProfile)
                .recentComments(getRecentComments(user))
                .build();
    }

    @Transactional(readOnly = true)
    public UserProfileDto getCurrentProfile() {
        UserAccount current = requireUser();
        return getProfile(current.getId());
    }

    @Transactional
    public void followUser(Long userId) {
        UserAccount current = requireUser();
        UserAccount target = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı"));

        if (current.getId().equals(userId)) {
            throw new ConflictException("Kendinizi takip edemezsiniz");
        }

        if (userFollowRepository.existsByFollowerAndFollowing(current, target)) {
            throw new ConflictException("Bu kullanıcıyı zaten takip ediyorsunuz");
        }

        UserFollow follow = UserFollow.builder()
                .follower(current)
                .following(target)
                .build();
        userFollowRepository.save(follow);
    }

    @Transactional
    public void unfollowUser(Long userId) {
        UserAccount current = requireUser();
        UserAccount target = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı"));

        UserFollow follow = userFollowRepository.findByFollowerAndFollowing(current, target)
                .orElseThrow(() -> new NotFoundException("Bu kullanıcıyı takip etmiyorsunuz"));

        userFollowRepository.delete(follow);
    }

    private UserAccount requireUser() {
        UserAccount user = currentUser.get();
        if (user == null) {
            throw new IllegalStateException("Oturum bulunamadı");
        }
        return user;
    }

    private List<UserRecentCommentDto> getRecentComments(UserAccount user) {
        List<MatchComment> comments = matchCommentRepository.findTop5ByUserOrderByCreatedAtDesc(user);
        return comments.stream()
                .map(c -> UserRecentCommentDto.builder()
                        .commentId(c.getId())
                        .matchId(c.getMatch() != null ? c.getMatch().getId() : null)
                        .matchTitle(c.getMatch() != null
                                ? String.format("%s vs %s",
                                c.getMatch().getHomeTeam() != null ? c.getMatch().getHomeTeam().getName() : "-",
                                c.getMatch().getAwayTeam() != null ? c.getMatch().getAwayTeam().getName() : "-")
                                : "Maç bilgisi yok")
                        .message(c.getMessage())
                        .createdAt(c.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
