package com.example.vitamate.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.vitamate.domain.Challenge;
import com.example.vitamate.domain.enums.ChallengeStatus;

public interface ChallengeRepository extends JpaRepository<Challenge, Long>, JpaSpecificationExecutor<Challenge> {

	List<Challenge> findByStartDateAndStatus(LocalDate startDate, ChallengeStatus status);

	List<Challenge> findByEndDateAndStatus(LocalDate endDate, ChallengeStatus status);

}
