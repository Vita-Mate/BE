package com.example.vitamate.domain.mapping;

import com.example.vitamate.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class IntakeLogs extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date = LocalDate.now();

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isTaken = false;

    @ManyToOne
    @JoinColumn(name = "member_supplement_id")
    private MemberSupplement memberSupplement;
}
