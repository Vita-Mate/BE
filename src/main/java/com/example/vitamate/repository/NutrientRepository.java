package com.example.vitamate.repository;

import com.example.vitamate.domain.Nutrient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NutrientRepository extends JpaRepository<Nutrient, Integer> {
    List<Nutrient> findAll();
}
