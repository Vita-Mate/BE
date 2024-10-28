package com.example.vitamate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vitamate.domain.mapping.ExerciseChallengeRecord;

public interface ExerciseChallengeRecordRepository extends JpaRepository<ExerciseChallengeRecord, Integer> {

}
