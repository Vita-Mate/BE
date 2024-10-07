package com.example.vitamate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vitamate.domain.NutrientAlias;
import com.example.vitamate.domain.mapping.NutrientInfo;

public interface NutrientAliasRepository extends JpaRepository<NutrientAlias, Integer> {

	Optional<NutrientAlias> findByAliasName(String nutrientName);

	List<NutrientAlias> findByAliasNameContaining(String keyword);
}
