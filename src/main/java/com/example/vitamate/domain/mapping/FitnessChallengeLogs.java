package com.example.vitamate.domain.mapping;

import com.example.vitamate.domain.common.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FitnessChallengeLogs extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate uploadTime;

    @Column(nullable = false)
    private String imageUrl;

    private String comment;

    @Column(nullable = false, length = 30)
    private String exerciseType;

    @ManyToOne
    @JoinColumn(name = "member_challenge_id")
    private MemberChallenge memberChallenge;


}
