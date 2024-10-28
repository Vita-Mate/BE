package com.example.vitamate.service.ChallengeService;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.vitamate.apiPayload.code.status.ErrorStatus;
import com.example.vitamate.apiPayload.exception.handler.ChallengeHandler;
import com.example.vitamate.aws.s3.AmazonS3Manager;
import com.example.vitamate.converter.ChallengeConverter;
import com.example.vitamate.domain.Challenge;
import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.Uuid;
import com.example.vitamate.domain.enums.ChallengeCategory;
import com.example.vitamate.domain.enums.ChallengeStatus;
import com.example.vitamate.domain.mapping.ExerciseChallengeRecord;
import com.example.vitamate.domain.mapping.MemberChallenge;
import com.example.vitamate.repository.ChallengeRepository;
import com.example.vitamate.repository.ExerciseChallengeRecordRepository;
import com.example.vitamate.repository.MemberChallengeRepository;
import com.example.vitamate.repository.RecordImageRepository;
import com.example.vitamate.repository.UuidRepository;
import com.example.vitamate.service.MemberService.MemberCommandService;
import com.example.vitamate.web.dto.ChallengeRequestDTO;
import com.example.vitamate.web.dto.ChallengeResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChallengeCommandServiceImpl implements ChallengeCommandService{

	private final MemberCommandService memberCommandService;
	private final MemberChallengeRepository memberChallengeRepository;
	private final ChallengeRepository challengeRepository;
	private final ChallengeConverter challengeConverter;
	private final ExerciseChallengeRecordRepository exerciseChallengeRecordRepository;

	private final AmazonS3Manager s3Manager;
	private final UuidRepository uuidRepository;
	private final RecordImageRepository recordImageRepository;

	@Override
	@Transactional
	public ChallengeResponseDTO.CreateChallengeResultDTO createChallenge(String email, ChallengeRequestDTO.CreateChallengeRequestDTO requestDTO){
		Member member = memberCommandService.validMember(email);

		checkParticipationInChallengeType(member, requestDTO.getCategory());

		if(requestDTO.getMaxParticipants() < requestDTO.getMinParticipants())
			throw new ChallengeHandler(ErrorStatus.INVALID_NUMBERS_VALUE);

		Challenge challenge = ChallengeConverter.toChallenge(requestDTO);
		challengeRepository.save(challenge);

		boolean isLeader = true;
		MemberChallenge memberChallenge = ChallengeConverter.toMemberChallenge(member, challenge, isLeader);
		memberChallengeRepository.save(memberChallenge);

		return ChallengeConverter.toCreateChallengeResponseDTO(challenge);
	}

	@Override
	@Transactional
	public ChallengeResponseDTO.JoinChallengeResultDTO joinChallenge(String email, Long challengeId){
		Member member = memberCommandService.validMember(email);
		Challenge challenge = validChallenge(challengeId);

		// 인원 제한 확인
		if (challenge.getCurrentUsers() == challenge.getMaxUsers()){
			throw new ChallengeHandler(ErrorStatus.CHALLENGE_FULL);
		}

		checkParticipationInChallengeType(member, challenge.getChallengeCategory());

		challenge.setCurrentUsers(challenge.getCurrentUsers() + 1);
		challengeRepository.save(challenge);

		MemberChallenge memberChallenge = challengeConverter.toMemberChallenge(member, challenge, false);
		memberChallengeRepository.save(memberChallenge);

		return challengeConverter.toJoinChallengeResultDTO(memberChallenge);
	}

	@Override
	@Transactional
	public Challenge validChallenge(Long challengeId){
		Challenge challenge = challengeRepository.findById(challengeId)
			.orElseThrow(() -> new ChallengeHandler(ErrorStatus.CHALLENGE_NOT_FOUND));

		return challenge;
	}

	@Override
	@Transactional
	public ChallengeResponseDTO.AddExerciseRecordResultDTO addExerciseRecord(String email, Long challengeId, ChallengeRequestDTO.AddExerciseRecordDTO requestDTO, MultipartFile photo){
		Member member = memberCommandService.validMember(email);
		Challenge challenge = validChallenge(challengeId);

		MemberChallenge memberChallenge = memberChallengeRepository.findByMemberAndChallenge(member, challenge);

		String uuid = UUID.randomUUID().toString();
		Uuid saveUuid = uuidRepository.save(Uuid.builder()
			.uuid(uuid).build());

		String imageUrl = s3Manager.uploadFile(s3Manager.generateChallengeKeyName(saveUuid), photo);

		ExerciseChallengeRecord exerciseChallengeRecord = challengeConverter.toExerciseChallengeRecord(requestDTO, memberChallenge);
		exerciseChallengeRecordRepository.save(exerciseChallengeRecord);

		recordImageRepository.save(challengeConverter.toRecordImage(imageUrl, exerciseChallengeRecord));

		return challengeConverter.toAddExerciseRecordResultDTO(exerciseChallengeRecord, imageUrl);

	}

	// 검증 메소드
	// 동일 카테고리 중복 참여 검증 메소드
	@Override
	@Transactional
	public void checkParticipationInChallengeType(Member member, ChallengeCategory category){
		Boolean alreadyJoinChallenge = memberChallengeRepository.existsByMemberAndChallenge_ChallengeCategoryAndChallenge_StatusIn(member, category,
			List.of(ChallengeStatus.IN_PROGRESS, ChallengeStatus.WAITING));

		if(alreadyJoinChallenge){
			throw new ChallengeHandler(ErrorStatus.DUPLICATE_CATEGORY_PARTICIPATION);
		}
	}



}
