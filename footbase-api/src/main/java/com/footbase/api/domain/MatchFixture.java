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
@Table(name = "maclar")
public class MatchFixture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ev_sahibi_takim_id")
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deplasman_takim_id")
    private Team awayTeam;

    @Column(name = "baslama_tarihi", nullable = false)
    private Instant kickoffAt;

    @Column(name = "saha", length = 150)
    private String venue;

    @Column(name = "durum", length = 40)
    private String status;

    @Column(name = "ev_sahibi_skor")
    private Integer homeScore;

    @Column(name = "deplasman_skor")
    private Integer awayScore;

    @PrePersist
    public void onCreate() {
        if (status == null) {
            status = "PLANLI";
        }
    }
}
