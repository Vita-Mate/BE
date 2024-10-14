package com.example.vitamate.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vitamate.apiPayload.ApiResponse;
import com.example.vitamate.jwt.SecurityUtil;
import com.example.vitamate.service.ChallengeService.ChallengeCommandService;
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

	@PostMapping("/")
	@Operation(summary = "챌린지 방 생성 API", description = "방장이 챌린지 방을 생성하는 API 입니다. CreateChallengeRequestDTO 설명 참고해주세요.")
	public ApiResponse<ChallengeResponseDTO.CreateChallengeResponseDTO> createChallenge(@Valid @RequestBody ChallengeRequestDTO.CreateChallengeRequestDTO challengeRequestDTO) {

		return ApiResponse.onSuccess(challengeCommandService.createChallenge(SecurityUtil.getCurrentUsername(), challengeRequestDTO));
	}
}
