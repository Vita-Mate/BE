package com.example.vitamate.web.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.vitamate.domain.enums.ChallengeCategory;
import com.example.vitamate.domain.enums.ChallengeStatus;
import com.example.vitamate.domain.enums.ExerciseIntensity;

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
		LocalDate startDate;
		LocalDate endDate;
		Integer maxParticipants;
		Integer currentParticipants;
		Integer weeklyFrequency;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class JoinChallengeResultDTO {
		Long ChallengeId;
		LocalDateTime joinedAt;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AddExerciseRecordResultDTO{
		Long recordId;
		String imageURL;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetExerciseRecordResultDTO{
		Long ExerciseRecordId;
		String nickname;
		String imageURL;
		String exerciseType;
		LocalDateTime startTime;
		LocalDateTime endTime;
		String comment;
	}

}
