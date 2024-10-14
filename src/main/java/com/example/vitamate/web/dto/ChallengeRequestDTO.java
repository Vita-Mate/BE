package com.example.vitamate.web.dto;

import java.time.LocalDate;

import com.example.vitamate.domain.enums.ChallengeDuration;

public class ChallengeRequestDTO {

	public static class createChallengeRequestDTO {
		String title;
		String description;
		LocalDate startDate;
		ChallengeDuration duration;
		Integer maxParticipants;
		Integer minParticipants;
		Integer weeklyFrequency;
	}
}
