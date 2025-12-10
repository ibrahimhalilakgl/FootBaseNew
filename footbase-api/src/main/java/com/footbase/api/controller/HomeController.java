package com.footbase.api.controller;

import com.footbase.api.dto.HomePageDto;
import com.footbase.api.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public ResponseEntity<HomePageDto> getHome() {
        return ResponseEntity.ok(homeService.getHome());
    }
}

