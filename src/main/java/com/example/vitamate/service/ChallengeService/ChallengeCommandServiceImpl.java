package com.example.vitamate.service.ChallengeService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.vitamate.apiPayload.code.status.ErrorStatus;
import com.example.vitamate.apiPayload.exception.handler.MemberHandler;
import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.enums.ChallengeCategory;
import com.example.vitamate.repository.MemberRepository;
import com.example.vitamate.web.dto.ChallengeRequestDTO;
import com.example.vitamate.web.dto.ChallengeResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChallengeCommandServiceImpl implements ChallengeCommandService{

	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public ChallengeResponseDTO createChallenge(String email, ChallengeRequestDTO requestDTO){
		Member member = validMember(email);

		return null;
	}

	private Member validMember(String email){
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

		return member;
	}
}
