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
@Table(name = "kullanici_takipleri")
@IdClass(UserFollowId.class)
public class UserFollow {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "takip_eden_id", nullable = false)
    private UserAccount follower;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "takip_edilen_id", nullable = false)
    private UserAccount following;

    @Column(name = "olusturma_tarihi")
    private Instant createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
    }
}

