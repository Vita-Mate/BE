package com.example.vitamate.repository;

import com.example.vitamate.domain.mapping.NutrientInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NutrientInfoRepository extends JpaRepository<NutrientInfo, Integer> {
    List<NutrientInfo> findByNutrientNameContaining(String keyword);
}
