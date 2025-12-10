package com.footbase.api.controller;

import com.footbase.api.dto.PlayerDto;
import com.footbase.api.dto.PlayerRatingDto;
import com.footbase.api.dto.PlayerRatingRequest;
import com.footbase.api.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    public ResponseEntity<List<PlayerDto>> listPlayers() {
        return ResponseEntity.ok(playerService.listPlayers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> getPlayer(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.getPlayer(id));
    }

    @GetMapping("/{id}/ratings")
    public ResponseEntity<List<PlayerRatingDto>> listRatings(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.listRatings(id));
    }

    @PostMapping("/{id}/ratings")
    public ResponseEntity<PlayerRatingDto> rate(@PathVariable Long id,
                                                @Valid @RequestBody PlayerRatingRequest request) {
        return ResponseEntity.ok(playerService.ratePlayer(id, request));
    }
}

