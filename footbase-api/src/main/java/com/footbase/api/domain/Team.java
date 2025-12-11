package com.footbase.api.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "takimlar")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ad", nullable = false, length = 150)
    private String name;

    @Column(name = "kod", nullable = false, unique = true, length = 10)
    private String code;

    @Column(name = "short_name", length = 50)
    private String shortName;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "external_id")
    private Long externalId;

    @Column(name = "sehir", length = 120)
    private String city;

    @Column(name = "stadyum", length = 150)
    private String stadium;

    @Column(name = "lig", length = 120)
    private String league;
}
