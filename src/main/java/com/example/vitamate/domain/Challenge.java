package com.example.vitamate.domain;

import com.example.vitamate.domain.common.BaseEntity;
import com.example.vitamate.domain.enums.ChallengeDuration;
import jakarta.persistence.*;
import com.example.vitamate.domain.enums.Category;
import com.example.vitamate.domain.enums.ChallengeStatus;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Challenge extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    @ColumnDefault("10")
    private Integer maxUsers;

    @Column(nullable = false)
    @ColumnDefault("1")
    private Integer currentUsers;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChallengeDuration duration;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChallengeStatus status;

    @Column(nullable = false)
    private Integer weeklyFrequency;
}
