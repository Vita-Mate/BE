package com.example.vitamate.web.dto;

import java.time.LocalDate;

import org.springframework.stereotype.Component;
import com.example.vitamate.domain.enums.ChallengeCategory;
import com.example.vitamate.domain.enums.ChallengeDuration;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Component
public class ChallengeRequestDTO {

	@Getter
	@Schema(description = "챌린지 생성시 입력할 정보")
	public static class CreateChallengeRequestDTO {
		@Schema(description = "챌린지 종류")
		@NotNull(message = "챌린지 종류는 필수 항목입니다. EXERCISE, QUIT_SMOKE, QUIT_ALCOHOL 중에서 입력해주세요.")
		private ChallengeCategory category;

		@Schema(description = "챌린지 제목")
		@NotNull(message = "챌린지 제목은 필수 항목입니다.")
		private String title;

		@Schema(description = "챌린지 설명")
		@Size(max = 500, message = "챌린지 설명은 최대 500자까지 입력할 수 있습니다.")
		private String description;

		@Schema(description = "챌린지 시작일")
		@NotNull(message = "챌린지 시작일은 필수 항목입니다.")
		@Future(message = "시작일은 미래 날짜여야 합니다.")
		private LocalDate startDate;

		@Schema(description = "챌린지 기간")
		@NotNull(message = "챌린지 기간은 필수 항목입니다. ONE_WEEK, ONE_MONTH, THREE_MONTHS, SIX_MONTHS, ONE_YEAR 중에서 입력해주세요.")
		private ChallengeDuration duration;

		@Schema(description = "최대 참여자 수 (2~10명 사이)")
		@Min(value = 2, message = "최대 참여자 수는 2명 이상이어야 합니다.")
		@Max(value = 10, message = "최대 참여자 수는 10명 이하여야 합니다.")
		private Integer maxParticipants;

		@Schema(description = "최소 참여자 수")
		@Min(value = 1, message = "최소 참여자 수는 1명 이상이어야 합니다.")
		@Max(value = 10, message = "최소 참여자 수는 10명 이하여야 합니다.")
		private Integer minParticipants;

		@Schema(description = "주간 빈도")
		@Min(value = 1, message = "주간 빈도는 최소 1회 이상이어야 합니다.")
		@Max(value = 7, message = "주간 빈도는 최대 7회 이하여야 합니다.")
		private Integer weeklyFrequency;
	}
}