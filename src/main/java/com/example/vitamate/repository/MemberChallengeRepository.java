package com.example.vitamate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.vitamate.domain.Challenge;
import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.enums.ChallengeCategory;
import com.example.vitamate.domain.enums.ChallengeStatus;
import com.example.vitamate.domain.mapping.MemberChallenge;

public interface MemberChallengeRepository extends JpaRepository<MemberChallenge, Long> {

	Boolean existsByMemberAndChallenge_ChallengeCategoryAndChallenge_StatusIn(Member member, ChallengeCategory challengeCategory, List<ChallengeStatus> challengeStatuses);

	@Query("SELECT mc FROM MemberChallenge mc "
		+ "JOIN mc.challenge c "
		+ "where mc.member.id = :memberId "
		+ "AND c.status IN (:statuses)")
	List<MemberChallenge> findByMemberIdAndChallenge_StatusIn(
		@Param("memberId") Long memberId,
		@Param("statuses") List<ChallengeStatus> statuses
	);

	Optional<MemberChallenge> findByMemberAndChallenge(Member member, Challenge challenge);
}
