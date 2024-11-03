package com.example.vitamate.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.vitamate.domain.mapping.ExerciseChallengeRecord;
import com.example.vitamate.domain.mapping.MemberChallenge;

public interface ExerciseChallengeRecordRepository extends JpaRepository<ExerciseChallengeRecord, Integer> {

	@Query("SELECT e "
		+ "FROM ExerciseChallengeRecord e "
		+ "WHERE e.memberChallenge = :memberChallenge "
		+ "AND DATE(e.createdAt) = :date")
	List<ExerciseChallengeRecord> findByMemberChallengeAndCreatedAtDate(
		@Param("memberChallenge") MemberChallenge memberChallenge,
		@Param("date") LocalDate date
	);
}
