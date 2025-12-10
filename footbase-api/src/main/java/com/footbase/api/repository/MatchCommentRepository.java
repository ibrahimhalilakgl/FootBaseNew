package com.footbase.api.repository;

import com.footbase.api.domain.MatchComment;
import com.footbase.api.domain.MatchFixture;
import com.footbase.api.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchCommentRepository extends JpaRepository<MatchComment, Long> {
    List<MatchComment> findByMatchOrderByCreatedAtDesc(MatchFixture match);
    List<MatchComment> findByUserInOrderByCreatedAtDesc(List<UserAccount> users);
    List<MatchComment> findByUser(UserAccount user);
    List<MatchComment> findTop3ByOrderByIdDesc();
    List<MatchComment> findTop5ByUserOrderByCreatedAtDesc(UserAccount user);
}
