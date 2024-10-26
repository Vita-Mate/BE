package com.example.vitamate.web.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.vitamate.apiPayload.ApiResponse;
import com.example.vitamate.domain.enums.ChallengeCategory;
import com.example.vitamate.domain.enums.ChallengeDuration;
import com.example.vitamate.jwt.SecurityUtil;
import com.example.vitamate.service.ChallengeService.ChallengeCommandService;
import com.example.vitamate.service.ChallengeService.ChallengeQueryServiceImpl;
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
	private final ChallengeQueryServiceImpl challengeQueryServiceImpl;

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
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

		return ApiResponse.onSuccess(challengeQueryServiceImpl.getChallengeList(category, weeklyFrequency, startDate, duration, page, pageSize));
	}
}
