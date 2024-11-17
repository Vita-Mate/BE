package com.example.vitamate.domain.mapping;

import com.example.vitamate.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SimpleVerificationChallengeRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean record;

    @ManyToOne
    @JoinColumn(name = "member_challenge_id")
    private MemberChallenge memberChallenge;
}
