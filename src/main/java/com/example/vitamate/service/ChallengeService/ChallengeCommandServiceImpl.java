package com.example.vitamate.service.ChallengeService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.vitamate.web.dto.ChallengeRequestDTO;
import com.example.vitamate.web.dto.ChallengeResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChallengeCommandServiceImpl implements ChallengeCommandService{

	@Override
	@Transactional
	public ChallengeResponseDTO createChallenge(String email, ChallengeRequestDTO requestDTO){

		return null;
	}
}
