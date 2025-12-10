package com.footbase.api.controller;

import com.footbase.api.domain.MatchFixture;
import com.footbase.api.domain.Player;
import com.footbase.api.domain.Team;
import com.footbase.api.dto.*;
import com.footbase.api.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // Match endpoints
    @PostMapping("/matches")
    public ResponseEntity<MatchDto> createMatch(@Valid @RequestBody AdminMatchRequest request) {
        return ResponseEntity.ok(adminService.createMatch(request));
    }

    @PutMapping("/matches/{id}")
    public ResponseEntity<MatchDto> updateMatch(@PathVariable Long id,
                                                @Valid @RequestBody AdminMatchRequest request) {
        return ResponseEntity.ok(adminService.updateMatch(id, request));
    }

    // Team endpoints
    @PostMapping("/teams")
    public ResponseEntity<Team> createTeam(@Valid @RequestBody AdminTeamRequest request) {
        return ResponseEntity.ok(adminService.createTeam(request));
    }

    @PutMapping("/teams/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id,
                                            @Valid @RequestBody AdminTeamRequest request) {
        return ResponseEntity.ok(adminService.updateTeam(id, request));
    }

    // Player endpoints
    @PostMapping("/players")
    public ResponseEntity<Player> createPlayer(@Valid @RequestBody AdminPlayerRequest request) {
        return ResponseEntity.ok(adminService.createPlayer(request));
    }

    @PutMapping("/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id,
                                                @Valid @RequestBody AdminPlayerRequest request) {
        return ResponseEntity.ok(adminService.updatePlayer(id, request));
    }
}

