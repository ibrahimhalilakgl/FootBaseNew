package com.footbase.api.repository;

import com.footbase.api.domain.CommentLike;
import com.footbase.api.domain.CommentLikeId;
import com.footbase.api.domain.MatchComment;
import com.footbase.api.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeId> {
    Optional<CommentLike> findByCommentAndUser(MatchComment comment, UserAccount user);
    boolean existsByCommentAndUser(MatchComment comment, UserAccount user);
    long countByComment(MatchComment comment);
}

