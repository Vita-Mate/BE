package com.example.vitamate.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vitamate.domain.mapping.MemberChallenge;
import com.example.vitamate.domain.mapping.SimpleVerificationChallengeRecord;

public interface SimpleVerificationChallengeRecordRepository extends JpaRepository<SimpleVerificationChallengeRecord, Long> {
	Optional<SimpleVerificationChallengeRecord> findByMemberChallengeAndCreatedAtBetween(
		MemberChallenge memberChallenge,
		LocalDateTime startOfDay,
		LocalDateTime endOfDay
	);
}
