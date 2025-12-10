package com.footbase.api.repository;

import com.footbase.api.domain.UserAccount;
import com.footbase.api.domain.UserFollow;
import com.footbase.api.domain.UserFollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, UserFollowId> {
    Optional<UserFollow> findByFollowerAndFollowing(UserAccount follower, UserAccount following);
    boolean existsByFollowerAndFollowing(UserAccount follower, UserAccount following);
    long countByFollower(UserAccount follower);
    long countByFollowing(UserAccount following);
    List<UserFollow> findByFollower(UserAccount follower);
}

