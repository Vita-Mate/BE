package com.example.vitamate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vitamate.domain.AgeGroups;

public interface AgeGroupsRepository extends JpaRepository<AgeGroups, Integer> {
}
