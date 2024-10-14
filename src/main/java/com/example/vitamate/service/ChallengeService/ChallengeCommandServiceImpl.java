package com.example.vitamate.service.ChallengeService;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.vitamate.apiPayload.code.status.ErrorStatus;
import com.example.vitamate.apiPayload.exception.handler.ChallengeHandler;
import com.example.vitamate.apiPayload.exception.handler.MemberHandler;
import com.example.vitamate.converter.ChallengeConverter;
import com.example.vitamate.domain.Challenge;
import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.enums.ChallengeCategory;
import com.example.vitamate.domain.enums.ChallengeStatus;
import com.example.vitamate.domain.mapping.MemberChallenge;
import com.example.vitamate.repository.ChallengeRepository;
import com.example.vitamate.repository.MemberChallengeRepository;
import com.example.vitamate.repository.MemberRepository;
import com.example.vitamate.web.dto.ChallengeRequestDTO;
import com.example.vitamate.web.dto.ChallengeResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChallengeCommandServiceImpl implements ChallengeCommandService{

	private final MemberRepository memberRepository;
	private final MemberChallengeRepository memberChallengeRepository;
	private final ChallengeRepository challengeRepository;

	@Override
	@Transactional
	public ChallengeResponseDTO.CreateChallengeResponseDTO createChallenge(String email, ChallengeRequestDTO.CreateChallengeRequestDTO requestDTO){
		Member member = validMember(email);

		checkParticipationInChallengeType(member, requestDTO.getCategory());

		if(requestDTO.getMaxParticipants() < requestDTO.getMinParticipants())
			throw new ChallengeHandler(ErrorStatus.INVALID_NUMBERS_VALUE);

		Challenge challenge = ChallengeConverter.toChallenge(member, requestDTO);
		challengeRepository.save(challenge);

		boolean isLeader = true;
		MemberChallenge memberChallenge = ChallengeConverter.toMemberChallenge(member, challenge, isLeader);
		memberChallengeRepository.save(memberChallenge);

		return ChallengeConverter.toCreateChallengeResponseDTO(challenge);
	}

	private Member validMember(String email){
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

		return member;
	}

	private void checkParticipationInChallengeType(Member member, ChallengeCategory category){
		Boolean alreadyJoinChallenge = memberChallengeRepository.existsByMemberAndChallenge_ChallengeCategoryAndChallenge_StatusIn(member, category,
			List.of(ChallengeStatus.IN_PROGRESS, ChallengeStatus.WAITING));

		if(alreadyJoinChallenge){
			throw new ChallengeHandler(ErrorStatus.DUPLICATE_CATEGORY_PARTICIPATION);
		}
	}
}