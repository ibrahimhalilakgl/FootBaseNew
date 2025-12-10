package com.footbase.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private Long id;
    private String email;
    private String displayName;
    private Instant createdAt;
    private Long followersCount;
    private Long followingCount;
    private Boolean isFollowing;
    private Boolean isOwnProfile;
    private java.util.List<UserRecentCommentDto> recentComments;
}

