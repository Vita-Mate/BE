package com.example.vitamate.converter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
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


	public static ChallengeResponseDTO.ChallengePreviewDTO toChallengePreviewDTO(Challenge challenge){
		return ChallengeResponseDTO.ChallengePreviewDTO.builder()
			.ChallengeId(challenge.getId())
			.title(challenge.getTitle())
			.dDay((int)ChronoUnit.DAYS.between(LocalDate.now(), challenge.getStartDate()))
			.weeklyFrequency(challenge.getWeeklyFrequency())
			.currentParticipants(challenge.getCurrentUsers())
			.maxParticipants(challenge.getMaxUsers())
			.build();
	}

	public static ChallengeResponseDTO.ChallengeListDTO toChallengeListDTO(
		Page<Challenge> challnegePage) {
		List<ChallengeResponseDTO.ChallengePreviewDTO> challengePreviewDTOList = challnegePage.stream()
			.map(ChallengeConverter::toChallengePreviewDTO).collect(Collectors.toList());

		return ChallengeResponseDTO.ChallengeListDTO.builder()
			.challengeList(challengePreviewDTOList)
			.listSize(challengePreviewDTOList.size())
			.totalPage(challnegePage.getTotalPages())
			.totalElements(challnegePage.getTotalElements())
			.isFirst(challnegePage.isFirst())
			.isLast(challnegePage.isLast())
			.build();
	}

	public static ChallengeResponseDTO.JoinChallengeResultDTO toJoinChallengeResultDTO(MemberChallenge memberChallenge){
		return ChallengeResponseDTO.JoinChallengeResultDTO.builder()
			.joinedAt(memberChallenge.getCreatedAt())
			.ChallengeId(memberChallenge.getChallenge().getId())
			.build();
	}
}
