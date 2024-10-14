package com.example.vitamate.service.ChallengeService;

import com.example.vitamate.web.dto.ChallengeRequestDTO;
import com.example.vitamate.web.dto.ChallengeResponseDTO;

public interface ChallengeCommandService {

	ChallengeResponseDTO createChallenge(String email, ChallengeRequestDTO requestDTO);

}
