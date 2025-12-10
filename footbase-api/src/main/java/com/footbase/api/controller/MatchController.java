package com.footbase.api.controller;

import com.footbase.api.dto.CommentDto;
import com.footbase.api.dto.CommentRequest;
import com.footbase.api.dto.MatchDto;
import com.footbase.api.dto.PredictionDto;
import com.footbase.api.dto.PredictionRequest;
import com.footbase.api.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    public ResponseEntity<List<MatchDto>> list(
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) java.time.Instant fromDate,
            @RequestParam(required = false) java.time.Instant toDate) {
        if (teamId != null || status != null || fromDate != null || toDate != null) {
            return ResponseEntity.ok(matchService.searchMatches(teamId, status, fromDate, toDate));
        }
        return ResponseEntity.ok(matchService.listMatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.getMatch(id));
    }

    @PostMapping("/{id}/predictions")
    public ResponseEntity<PredictionDto> predict(@PathVariable Long id,
                                                 @Valid @RequestBody PredictionRequest request) {
        return ResponseEntity.ok(matchService.upsertPrediction(id, request));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> comment(@PathVariable Long id,
                                              @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(matchService.addComment(id, request));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long commentId,
                                                    @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(matchService.updateComment(commentId, request));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        matchService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<Void> toggleCommentLike(@PathVariable Long commentId) {
        matchService.toggleCommentLike(commentId);
        return ResponseEntity.ok().build();
    }
}

