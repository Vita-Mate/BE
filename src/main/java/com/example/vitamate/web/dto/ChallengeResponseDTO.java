package com.example.vitamate.web.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.vitamate.domain.enums.ChallengeStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChallengeResponseDTO {

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CreateChallengeResultDTO {
		Long challengeId;
		String title;
		ChallengeStatus status;
		LocalDateTime createdAt;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ChallengeListDTO{
		List<ChallengePreviewDTO> challengeList;
		Integer listSize;
		Integer totalPage;
		Long totalElements;
		Boolean isFirst;
		Boolean isLast;

	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ChallengePreviewDTO{
		Long ChallengeId;
		String title;
		Integer dDay;
		Integer maxParticipants;
		Integer currentParticipants;
		Integer weeklyFrequency;
	}
}
