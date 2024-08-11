package com.example.vitamate.domain.mapping;

import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberSupplement extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean isScrapped;

    @Column(nullable = false)
    private Boolean isTaking;

    private LocalDate startDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "supplement_id")
    private Supplement supplement;
}
