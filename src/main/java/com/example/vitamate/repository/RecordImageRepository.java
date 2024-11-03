package com.example.vitamate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vitamate.domain.RecordImage;
import com.example.vitamate.domain.mapping.ExerciseChallengeRecord;

public interface RecordImageRepository extends JpaRepository<RecordImage, Long> {

	RecordImage findByExerciseChallengeRecord(ExerciseChallengeRecord exerciseChallengeRecord);
}
