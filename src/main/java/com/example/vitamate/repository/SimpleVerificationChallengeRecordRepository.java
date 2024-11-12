package com.example.vitamate.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.vitamate.domain.mapping.ExerciseChallengeRecord;
import com.example.vitamate.domain.mapping.MemberChallenge;
import com.example.vitamate.domain.mapping.SimpleVerificationChallengeRecord;

public interface SimpleVerificationChallengeRecordRepository extends JpaRepository<SimpleVerificationChallengeRecord, Long> {
	Optional<SimpleVerificationChallengeRecord> findByMemberChallengeAndCreatedAtBetween(
		MemberChallenge memberChallenge,
		LocalDateTime startOfDay,
		LocalDateTime endOfDay
	);

	@Query("SELECT s "
		+ "FROM SimpleVerificationChallengeRecord s "
		+ "WHERE s.memberChallenge = :memberChallenge "
		+ "AND DATE(s.createdAt) = :date")
	Optional<SimpleVerificationChallengeRecord> findByMemberChallengeAndCreatedAtDate(
		@Param("memberChallenge") MemberChallenge memberChallenge,
		@Param("date") LocalDate date);
}
