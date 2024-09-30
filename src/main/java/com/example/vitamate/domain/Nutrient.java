package com.example.vitamate.domain;

import com.example.vitamate.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nutrients")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Nutrient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;
}
