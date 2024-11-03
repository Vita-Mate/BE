package com.example.vitamate.service.ChallengeService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.vitamate.converter.ChallengeConverter;
import com.example.vitamate.domain.Challenge;
import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.RecordImage;
import com.example.vitamate.domain.enums.ChallengeCategory;
import com.example.vitamate.domain.enums.ChallengeDuration;
import com.example.vitamate.domain.enums.ChallengeStatus;
import com.example.vitamate.domain.mapping.ExerciseChallengeRecord;
import com.example.vitamate.domain.mapping.MemberChallenge;
import com.example.vitamate.repository.ChallengeRepository;
import com.example.vitamate.repository.ExerciseChallengeRecordRepository;
import com.example.vitamate.repository.MemberChallengeRepository;
import com.example.vitamate.repository.RecordImageRepository;
import com.example.vitamate.service.MemberService.MemberCommandService;
import com.example.vitamate.web.dto.ChallengeResponseDTO;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChallengeQueryServiceImpl implements ChallengeQueryService{

	private final ChallengeRepository challengeRepository;
	private final ChallengeConverter challengeConverter;
	private final MemberCommandService memberCommandService;
	private final MemberChallengeRepository memberChallengeRepository;
	private final ChallengeCommandService challengeCommandService;
	private final ExerciseChallengeRecordRepository exerciseChallengeRecordRepository;
	private final RecordImageRepository recordImageRepository;

	@Override
	@Transactional
	public ChallengeResponseDTO.ChallengeListDTO getChallengeList(
		ChallengeCategory category,
		List<Integer> weeklyFrequency,
		LocalDate startDate,
		ChallengeDuration duration,
		Integer minParticipants,
		Integer maxParticipants,
		Integer page,
		Integer pageSize){

		PageRequest pageRequest = PageRequest.of(page, pageSize);

		Specification<Challenge> spec = (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();

			// ChallengeStatus가 WAITING인 조건 추가
			predicates.add(builder.equal(root.get("status"), ChallengeStatus.WAITING));

			if (category != null) {
				predicates.add(builder.equal(root.get("challengeCategory"), category));
			}
			if(weeklyFrequency != null && !weeklyFrequency.isEmpty()){
				predicates.add(root.get("weeklyFrequency").in(weeklyFrequency));
			}
			if(startDate != null) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("startDate"), startDate));
			}
			if(duration != null){
				predicates.add(builder.equal(root.get("duration"), duration));
			}
			if(minParticipants != null){
				predicates.add(builder.greaterThanOrEqualTo(root.get("minParticipants"), minParticipants));
			}
			if(maxParticipants != null){
				predicates.add(builder.lessThanOrEqualTo(root.get("maxParticipants"), maxParticipants));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};

		Page<Challenge> challengePage = challengeRepository.findAll(spec, pageRequest);

		return challengeConverter.toChallengeListDTO(challengePage);
	}

	@Override
	@Transactional
	public ChallengeResponseDTO.ParticipatingChallengeListDTO getParticipatingChallengeList(String email){
		Member member = memberCommandService.validMember(email);

		List<MemberChallenge> memberChallenges = memberChallengeRepository.findByMemberIdAndChallenge_StatusIn(member.getId(), Arrays.asList(ChallengeStatus.WAITING, ChallengeStatus.IN_PROGRESS));

		ChallengeResponseDTO.ParticipatingChallengeDTO exerciseChallenge = null;
		ChallengeResponseDTO.ParticipatingChallengeDTO quitAlcoholChallenge = null;
		ChallengeResponseDTO.ParticipatingChallengeDTO quitSmokeChallenge = null;

		for(MemberChallenge memberChallenge : memberChallenges){
			ChallengeResponseDTO.ParticipatingChallengeDTO challengeDTO = challengeConverter.toParticipatingChallengeDTO(memberChallenge.getChallenge());
			switch (memberChallenge.getChallenge().getChallengeCategory()){
				case EXERCISE :
					exerciseChallenge = challengeDTO;
					break;
				case QUIT_ALCOHOL:
					quitAlcoholChallenge = challengeDTO;
					break;
				case QUIT_SMOKE:
					quitSmokeChallenge = challengeDTO;
					break;
			}
		}

		return challengeConverter.toParticipatingChallengeListDTO(exerciseChallenge, quitAlcoholChallenge, quitSmokeChallenge);
	}

	@Override
	@Transactional
	public List<ChallengeResponseDTO.GetExerciseRecordResultDTO> getMyExerciseRecord(String email, Long challengeId, LocalDate date){
		Member member = memberCommandService.validMember(email);
		Challenge challenge = challengeCommandService.validChallenge(challengeId);
		MemberChallenge memberChallenge = challengeCommandService.validMemberChallenge(member, challenge);

		List<ExerciseChallengeRecord> exerciseChallengeRecordList = exerciseChallengeRecordRepository.findByMemberChallengeAndCreatedAtDate(memberChallenge, date);

		List<ChallengeResponseDTO.GetExerciseRecordResultDTO> DTOList = exerciseChallengeRecordList.stream()
			.map(record -> {
				RecordImage recordImage = recordImageRepository.findByExerciseChallengeRecord(record);
				String imageURL = recordImage.getImageUrl();
				return challengeConverter.toGetExerciseRecordResultDTO(record, imageURL);
			}).collect(Collectors.toList());

		return DTOList;
	}
}
