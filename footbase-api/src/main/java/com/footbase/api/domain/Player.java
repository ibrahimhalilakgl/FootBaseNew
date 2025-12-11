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
@Table(name = "oyuncular")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "takim_id")
    private Team team;

    @Column(name = "tam_ad", nullable = false, length = 150)
    private String fullName;

    @Column(name = "pozisyon", length = 50)
    private String position;

    @Column(name = "forma_numarasi")
    private Integer shirtNumber;

    @Column(name = "external_id")
    private Long externalId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "olusturma_tarihi")
    private Instant createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
    }
}
