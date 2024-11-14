package com.example.vitamate.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.vitamate.domain.Challenge;
import com.example.vitamate.domain.mapping.ExerciseChallengeRecord;
import com.example.vitamate.domain.mapping.MemberChallenge;
import com.example.vitamate.domain.mapping.SimpleVerificationChallengeRecord;

public interface SimpleVerificationChallengeRecordRepository extends JpaRepository<SimpleVerificationChallengeRecord, Long> {

	@Query("SELECT s "
		+ "FROM SimpleVerificationChallengeRecord s "
		+ "WHERE s.memberChallenge = :memberChallenge "
		+ "AND DATE(s.createdAt) = :date")
	Optional<SimpleVerificationChallengeRecord> findByMemberChallengeAndCreatedAtDate(
		@Param("memberChallenge") MemberChallenge memberChallenge,
		@Param("date") LocalDate date
	);




	@Query("SELECT s "
		+ "FROM SimpleVerificationChallengeRecord s "
		+ "WHERE s.memberChallenge.challenge = :challenge "
		+ "AND DATE(s.createdAt) = :date")
	List<SimpleVerificationChallengeRecord> findByChallengeAndCreatedAtDate(
		@Param("challenge") Challenge challenge,
		@Param("date") LocalDate date
	);
}
