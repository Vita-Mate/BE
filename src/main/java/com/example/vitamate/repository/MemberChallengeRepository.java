package com.example.vitamate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.enums.ChallengeCategory;
import com.example.vitamate.domain.enums.ChallengeStatus;
import com.example.vitamate.domain.mapping.MemberChallenge;

public interface MemberChallengeRepository extends JpaRepository<MemberChallenge, Long> {

	Boolean existsByMemberAndChallenge_ChallengeCategoryAndChallenge_StatusIn(Member member, ChallengeCategory challengeCategory, List<ChallengeStatus> challengeStatuses);
}
