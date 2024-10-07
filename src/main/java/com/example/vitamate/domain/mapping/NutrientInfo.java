package com.example.vitamate.domain.mapping;
import com.example.vitamate.domain.Supplement;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutrientInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer nutrientId;
    private String amount;
    private String unit;
    private String totalAmount;
    private String totalUnit;
//    private String range;

    @ManyToOne
    @JoinColumn(name = "supplement_id")
    private Supplement supplement;
}