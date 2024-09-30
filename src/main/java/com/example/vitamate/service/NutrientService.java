package com.example.vitamate.service;

import com.example.vitamate.domain.Nutrient;
import com.example.vitamate.repository.NutrientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NutrientService {

    @Autowired
    private NutrientRepository nutrientRepository;

    public List<String> getValidNutrientsFromDB(){
        return nutrientRepository.findAll()
                .stream()
                .map(Nutrient::getName)
                .collect(Collectors.toList());
    }
}
