package com.example.vitamate.domain.mapping;

import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SupplementNutrients extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 50)
    private String nutrientName;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double recommendedAmount;

    @ManyToOne
    @JoinColumn(name = "supplement_id")
    private Supplement supplement;

}
