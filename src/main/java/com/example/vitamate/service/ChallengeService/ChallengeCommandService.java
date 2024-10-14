package com.example.vitamate.service.ChallengeService;

import com.example.vitamate.web.dto.ChallengeRequestDTO;
import com.example.vitamate.web.dto.ChallengeResponseDTO;

public interface ChallengeCommandService {

	ChallengeResponseDTO.CreateChallengeResponseDTO createChallenge(String email, ChallengeRequestDTO.CreateChallengeRequestDTO requestDTO);

}
