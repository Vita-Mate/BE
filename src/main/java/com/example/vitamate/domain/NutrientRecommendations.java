package com.example.vitamate.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NutrientRecommendations {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "nutrient_id", nullable = false)
	private Nutrient nutrient;

	@ManyToOne
	@JoinColumn(name = "age_group_id", nullable = false)
	private AgeGroups ageGroup;

	@Column(nullable = false, length = 10)
	private String gender;

	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal recommendedIntake;

	@ManyToOne
	@JoinColumn(name = "unit_id", nullable = false)
	private NutrientUnit unit;
}
