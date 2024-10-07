package com.example.vitamate.repository;

import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.mapping.NutrientInfo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface NutrientInfoRepository extends JpaRepository<NutrientInfo, Integer> {

    List<NutrientInfo> findAllBySupplement(Supplement supplement);

    Page<NutrientInfo> findByNutrientIdIn(Set<Integer> nutrientIdSet, Pageable pageable);
}
