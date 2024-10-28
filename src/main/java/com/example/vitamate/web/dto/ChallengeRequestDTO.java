package com.example.vitamate.web.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.vitamate.domain.enums.ChallengeCategory;
import com.example.vitamate.domain.enums.ChallengeDuration;
import com.example.vitamate.domain.enums.ExerciseIntensity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
public class ChallengeRequestDTO {

	@Getter
	@Schema(description = "챌린지 생성시 입력할 정보")
	public static class CreateChallengeRequestDTO {
		@Schema(description = "챌린지 종류")
		@NotNull(message = "챌린지 종류는 필수 항목입니다. EXERCISE, QUIT_SMOKE, QUIT_ALCOHOL 중에서 입력해주세요.")
		ChallengeCategory category;

		@Schema(description = "챌린지 제목")
		@NotNull(message = "챌린지 제목은 필수 항목입니다.")
		String title;

		@Schema(description = "챌린지 설명")
		@Size(max = 500, message = "챌린지 설명은 최대 500자까지 입력할 수 있습니다.")
		String description;

		@Schema(description = "챌린지 시작일")
		@NotNull(message = "챌린지 시작일은 필수 항목입니다.")
		@Future(message = "시작일은 미래 날짜여야 합니다.")
		LocalDate startDate;

		@Schema(description = "챌린지 기간")
		@NotNull(message = "챌린지 기간은 필수 항목입니다. ONE_WEEK, ONE_MONTH, THREE_MONTHS, SIX_MONTHS, ONE_YEAR 중에서 입력해주세요.")
		ChallengeDuration duration;

		@Schema(description = "최대 참여자 수 (2~10명 사이)")
		@Min(value = 2, message = "최대 참여자 수는 2명 이상이어야 합니다.")
		@Max(value = 10, message = "최대 참여자 수는 10명 이하여야 합니다.")
		Integer maxParticipants;

		@Schema(description = "최소 참여자 수")
		@Min(value = 1, message = "최소 참여자 수는 1명 이상이어야 합니다.")
		@Max(value = 10, message = "최소 참여자 수는 10명 이하여야 합니다.")
		Integer minParticipants;

		@Schema(description = "주간 빈도")
		@Min(value = 1, message = "주간 빈도는 최소 1회 이상이어야 합니다.")
		@Max(value = 7, message = "주간 빈도는 최대 7회 이하여야 합니다.")
		Integer weeklyFrequency;
	}

	@Getter
	@Setter
	@Schema(description = "운동 챌린지 기록 입력할 정보")
	public static class AddExerciseRecordDTO{

		@Schema(description = "운동 이름")
		@NotNull(message = "운동 이름은 필수 항목입니다.")
		String exerciseType;

		@Schema(description = "운동 강도")
		@NotNull(message = "운동 강도는 필수 항복입니다. VERY_EASY, EASY, MODERATE, HARD, VERY_HARD, EXTREME 중에서 입력해주세요.")
		ExerciseIntensity intensity;

		@Schema(description = "운동 시작 시간 (시)")
		@NotNull(message = "운동 시작 시각은 필수 항목입니다.")
		@Min(value = 0, message = "시(hour)는 0 이상이어야 합니다.")
		@Max(value = 23, message = "시(hour)는 23 이하이어야 합니다.")
		Integer startHour;

		@Schema(description = "운동 시작 시간 (분)")
		@NotNull(message = "운동 시작 분은 필수 항목입니다.")
		@Min(value = 0, message = "분(minute)은 0 이상이어야 합니다.")
		@Max(value = 59, message = "분(minute)은 59 이하이어야 합니다.")
		Integer startMinute;

		@Schema(description = "운동 종료 시간 (시)")
		@NotNull(message = "운동 종료 시각은 필수 항목입니다.")
		@Min(value = 0, message = "시(hour)는 0 이상이어야 합니다.")
		@Max(value = 23, message = "시(hour)는 23 이하이어야 합니다.")
		Integer endHour;

		@Schema(description = "운동 종료 시간 (분)")
		@NotNull(message = "운동 종료 분은 필수 항목입니다.")
		@Min(value = 0, message = "분(minute)은 0 이상이어야 합니다.")
		@Max(value = 59, message = "분(minute)은 59 이하이어야 합니다.")
		Integer endMinute;

		@Schema(description = "메모 (선택사항)")
		String comment;
	}
}