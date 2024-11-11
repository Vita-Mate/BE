package com.example.vitamate.web.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.vitamate.apiPayload.ApiResponse;
import com.example.vitamate.domain.enums.ChallengeCategory;
import com.example.vitamate.domain.enums.ChallengeDuration;
import com.example.vitamate.jwt.SecurityUtil;
import com.example.vitamate.service.ChallengeService.ChallengeCommandService;
import com.example.vitamate.service.ChallengeService.ChallengeQueryService;
import com.example.vitamate.web.dto.ChallengeRequestDTO;
import com.example.vitamate.web.dto.ChallengeResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/challenges")
public class ChallengeRestController {

	private final ChallengeCommandService challengeCommandService;
	private final ChallengeQueryService challengeQueryService;

	@PostMapping("/")
	@Operation(summary = "챌린지 방 생성 API", description = "방장이 챌린지 방을 생성하는 API 입니다. CreateChallengeRequestDTO 설명 참고해주세요.")
	public ApiResponse<ChallengeResponseDTO.CreateChallengeResultDTO> createChallenge(@Valid @RequestBody ChallengeRequestDTO.CreateChallengeRequestDTO challengeRequestDTO) {

		return ApiResponse.onSuccess(challengeCommandService.createChallenge(SecurityUtil.getCurrentUsername(), challengeRequestDTO));
	}

	@GetMapping("/{category}")
	@Operation(summary = "챌린지 방 목록 조회 API", description = "생성된 챌린지 방 목록(대기중인 방만)을 조회합니다.")
	public ApiResponse<ChallengeResponseDTO.ChallengeListDTO> getChallengeList(
		@PathVariable(name="category") ChallengeCategory category,
		@RequestParam(value="weeklyFrequency", required = false) List<Integer> weeklyFrequency,
		@RequestParam(value="startDate", required = false) LocalDate startDate,
		@RequestParam(value="duration", required = false) ChallengeDuration duration,
		@RequestParam(value = "minParticipants", required = false) Integer minParticipants,
		@RequestParam(value = "maxParticipants", required = false) Integer maxParticipants,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

		return ApiResponse.onSuccess(challengeQueryService.getChallengeList(category, weeklyFrequency, startDate, duration, minParticipants, maxParticipants, page, pageSize));
	}

	@PostMapping("/{challengeId}")
	@Operation(summary = "챌린지 참가 API")
	public ApiResponse<ChallengeResponseDTO.JoinChallengeResultDTO> joinChallenge(
		@PathVariable(name = "challengeId") Long challengeId){
		return ApiResponse.onSuccess(challengeCommandService.joinChallenge(SecurityUtil.getCurrentUsername(), challengeId));
	}

	@GetMapping("/{category}/my")
	@Operation(summary = "나의 챌린지 그룹 API", description = "챌린지 카테고리 EXERCISE, QUIT_SMOKE, QUIT_ALCOHOL")
	public ApiResponse<ChallengeResponseDTO.ChallengePreviewDTO> getParticipatingChallengeList(
		@PathVariable(name = "category") ChallengeCategory category
	){
		return ApiResponse.onSuccess(challengeQueryService.getParticipatingChallengeList(SecurityUtil.getCurrentUsername(), category));
	}

	@PostMapping(value = "/{challengeId}/records", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "운동 습관 챌린지 - 기록 업로드 API")
	public ApiResponse<ChallengeResponseDTO.AddExerciseRecordResultDTO> addExerciseRecord(
		@PathVariable(name = "challengeId") Long challengeId,
		@RequestPart(name = "record") ChallengeRequestDTO.AddExerciseRecordDTO addExerciseRecordDTO,
		@RequestPart(name = "photo") MultipartFile photo
		){
		return ApiResponse.onSuccess(challengeCommandService.addExerciseRecord(SecurityUtil.getCurrentUsername(), challengeId, addExerciseRecordDTO, photo));
	}

	@GetMapping(value = "/{challengeId}/myRecord")
	@Operation(summary = "단체 챌린지 - 나의 기록 조회 API", description = "참여중인 챌린지의 id와 조회할 날짜를 넣어주세요. 날짜는 YYYY-MM-DD 형식으로 넣어주세요.")
	public ApiResponse<List<ChallengeResponseDTO.GetExerciseRecordResultDTO>> getMyRecord(
		@PathVariable(name = "challengeId") Long challengeId,
		@RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	){
		return ApiResponse.onSuccess(challengeQueryService.getMyExerciseRecord(SecurityUtil.getCurrentUsername(), challengeId, date));
	}

	@GetMapping(value = "/{challengeId}/teamRecord")
	@Operation(summary = "단체 챌린지 - 팀원기록 조회 API", description = "참여중인 챌린지의 id와 조회할 날짜를 넣어주세요. 날짜는 YYYY-MM-DD 형식으로 넣어주세요.")
	public ApiResponse<List<ChallengeResponseDTO.GetExerciseRecordResultDTO>> getTeamRecord(
		@PathVariable(name = "challengeId") Long challengeId,
		@RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	){
		return ApiResponse.onSuccess(challengeQueryService.getTeamExerciseRecord(SecurityUtil.getCurrentUsername(), challengeId, date));
	}

	@DeleteMapping(value = "/{challengeId}")
	@Operation(summary = "챌린지 참가 취소 API", description = "참여중인 챌린지의 id를 넣어주세요.")
	public ApiResponse<String> calcelChallengeParticipation(
		@PathVariable(name = "challengeId") Long challengeId
	){
		return ApiResponse.onSuccess(challengeCommandService.cancelChallengeParticipation(SecurityUtil.getCurrentUsername(), challengeId));
	}
}
