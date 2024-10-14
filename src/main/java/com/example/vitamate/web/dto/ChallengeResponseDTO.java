package com.example.vitamate.web.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
	public static class CreateChallengeResponseDTO {
		Long challengeId;
		String title;
		ChallengeStatus status;
		LocalDateTime createdAt;
	}
}
