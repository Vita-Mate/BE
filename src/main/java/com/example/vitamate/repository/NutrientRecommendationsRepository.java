package com.example.vitamate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vitamate.domain.NutrientRecommendations;
import com.example.vitamate.domain.enums.Gender;

public interface NutrientRecommendationsRepository extends JpaRepository<NutrientRecommendations, Integer> {

	Optional<NutrientRecommendations> findByNutrientId(Integer nutrientId);

	NutrientRecommendations findByNutrientIdAndAgeGroupIdAndGender(Integer nutrientId, Integer ageGroupId, String gender);
}
