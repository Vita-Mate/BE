package com.example.vitamate.service.ChallengeService;

import org.springframework.web.multipart.MultipartFile;

import com.example.vitamate.domain.Challenge;
import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.enums.ChallengeCategory;
import com.example.vitamate.domain.mapping.MemberChallenge;
import com.example.vitamate.web.dto.ChallengeRequestDTO;
import com.example.vitamate.web.dto.ChallengeResponseDTO;

public interface ChallengeCommandService {

	ChallengeResponseDTO.CreateChallengeResultDTO createChallenge(String email,
		ChallengeRequestDTO.CreateChallengeRequestDTO requestDTO);

	void checkParticipationInChallengeType(Member member, ChallengeCategory category);

	Challenge validChallenge(Long challengeId);

	ChallengeResponseDTO.JoinChallengeResultDTO joinChallenge(String email, Long challengeId);

	ChallengeResponseDTO.AddExerciseRecordResultDTO addExerciseRecord(String email, Long challengeId,
		ChallengeRequestDTO.AddExerciseRecordDTO addExerciseRecordDTO, MultipartFile photo);

	MemberChallenge validMemberChallenge(Member member, Challenge challenge);
	String cancelChallengeParticipation(String email, Long challengeId);
}
