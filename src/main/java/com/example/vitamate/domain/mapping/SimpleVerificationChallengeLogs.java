package com.example.vitamate.domain.mapping;

import com.example.vitamate.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SimpleVerificationChallengeLogs extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    private Boolean isSuccess;

    private Integer reward;

    @ManyToOne
    @JoinColumn(name = "member_challenge_id")
    private MemberChallenge memberChallenge;
}
