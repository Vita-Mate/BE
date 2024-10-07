package com.example.vitamate.domain;

import java.util.List;

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

    @OneToMany(mappedBy = "nutrient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NutrientAlias> aliases;
}