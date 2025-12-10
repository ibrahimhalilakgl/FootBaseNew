package com.footbase.api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mac_tahminleri", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"mac_id", "kullanici_id"})
})
public class MatchPrediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mac_id", nullable = false)
    private MatchFixture match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_id", nullable = false)
    private UserAccount user;

    @Column(name = "ev_sahibi_skor", nullable = false)
    private Integer homeScore;

    @Column(name = "deplasman_skor", nullable = false)
    private Integer awayScore;

    @Column(name = "olusturma_tarihi")
    private Instant createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
    }
}

