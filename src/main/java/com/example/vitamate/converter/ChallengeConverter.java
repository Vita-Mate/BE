package com.example.vitamate.converter;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.example.vitamate.domain.Challenge;
import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.enums.ChallengeStatus;
import com.example.vitamate.domain.mapping.MemberChallenge;
import com.example.vitamate.web.dto.ChallengeRequestDTO;
import com.example.vitamate.web.dto.ChallengeResponseDTO;

@Component
public class ChallengeConverter {
	public static Challenge toChallenge(ChallengeRequestDTO.CreateChallengeRequestDTO requestDTO){
		return Challenge.builder()
			.title(requestDTO.getTitle())
			.description(requestDTO.getDescription())
			.minUsers(requestDTO.getMinParticipants())
			.maxUsers(requestDTO.getMaxParticipants())
			.currentUsers(1)
			.expiryDate(LocalDate.now().plusWeeks(1))
			.startDate(requestDTO.getStartDate())
			.challengeCategory(requestDTO.getCategory())
			.duration(requestDTO.getDuration())
			.status(ChallengeStatus.WAITING)
			.weeklyFrequency(requestDTO.getWeeklyFrequency())
			.build();
	}

	public static MemberChallenge toMemberChallenge(Member member, Challenge challenge, boolean isLeader){
		return MemberChallenge.builder()
			.member(member)
			.challenge(challenge)
			.isLeader(isLeader)
			.build();
	}

	public static ChallengeResponseDTO.CreateChallengeResultDTO toCreateChallengeResponseDTO(Challenge challenge){
		return ChallengeResponseDTO.CreateChallengeResultDTO.builder()
			.challengeId(challenge.getId())
			.title(challenge.getTitle())
			.status(challenge.getStatus())
			.createdAt(challenge.getCreatedAt())
			.build();
	}
}
