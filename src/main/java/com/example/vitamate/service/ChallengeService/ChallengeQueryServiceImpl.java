package com.example.vitamate.service.ChallengeService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.vitamate.apiPayload.code.status.ErrorStatus;
import com.example.vitamate.apiPayload.exception.handler.ChallengeHandler;
import com.example.vitamate.converter.ChallengeConverter;
import com.example.vitamate.domain.Challenge;
import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.RecordImage;
import com.example.vitamate.domain.enums.ChallengeCategory;
import com.example.vitamate.domain.enums.ChallengeDuration;
import com.example.vitamate.domain.enums.ChallengeStatus;
import com.example.vitamate.domain.mapping.ExerciseChallengeRecord;
import com.example.vitamate.domain.mapping.MemberChallenge;
import com.example.vitamate.domain.mapping.SimpleVerificationChallengeRecord;
import com.example.vitamate.repository.ChallengeRepository;
import com.example.vitamate.repository.ExerciseChallengeRecordRepository;
import com.example.vitamate.repository.MemberChallengeRepository;
import com.example.vitamate.repository.RecordImageRepository;
import com.example.vitamate.repository.SimpleVerificationChallengeRecordRepository;
import com.example.vitamate.service.MemberService.MemberCommandService;
import com.example.vitamate.web.dto.ChallengeResponseDTO;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChallengeQueryServiceImpl implements ChallengeQueryService {

	private final ChallengeRepository challengeRepository;
	private final ChallengeConverter challengeConverter;
	private final MemberCommandService memberCommandService;
	private final MemberChallengeRepository memberChallengeRepository;
	private final ChallengeCommandService challengeCommandService;
	private final ExerciseChallengeRecordRepository exerciseChallengeRecordRepository;
	private final RecordImageRepository recordImageRepository;
	private final SimpleVerificationChallengeRecordRepository simpleVerificationChallengeRecordRepository;

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
		Integer pageSize) {

		PageRequest pageRequest = PageRequest.of(page, pageSize);

		Specification<Challenge> spec = (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();

			// ChallengeStatus가 WAITING인 조건 추가
			predicates.add(builder.equal(root.get("status"), ChallengeStatus.WAITING));

			// 필터링
			if (category != null) {
				predicates.add(builder.equal(root.get("challengeCategory"), category));
			}
			if (weeklyFrequency != null && !weeklyFrequency.isEmpty()) {
				predicates.add(root.get("weeklyFrequency").in(weeklyFrequency));
			}
			if (startDate != null) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("startDate"), startDate));
			}
			if (duration != null) {
				predicates.add(builder.equal(root.get("duration"), duration));
			}
			if (minParticipants != null) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("minParticipants"), minParticipants));
			}
			if (maxParticipants != null) {
				predicates.add(builder.lessThanOrEqualTo(root.get("maxParticipants"), maxParticipants));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};

		Page<Challenge> challengePage = challengeRepository.findAll(spec, pageRequest);

		return challengeConverter.toChallengeListDTO(challengePage);
	}

	@Override
	@Transactional
	public ChallengeResponseDTO.ChallengePreviewDTO getParticipatingChallengeList(String email, ChallengeCategory category) {
		Member member = memberCommandService.validMember(email);

		MemberChallenge memberChallenge = memberChallengeRepository.findByMemberIdAndChallenge_StatusInChallengeCategory(
			member.getId(), Arrays.asList(ChallengeStatus.WAITING, ChallengeStatus.IN_PROGRESS), category)
			.orElseThrow(() -> new ChallengeHandler(ErrorStatus.NOT_PARTICIPATING_IN_CHALLENGE));

		return challengeConverter.toChallengePreviewDTO(memberChallenge.getChallenge());
	}

	@Override
	@Transactional
	public List<ChallengeResponseDTO.GetExerciseRecordResultDTO> getMyExerciseRecord(String email, Long challengeId,
		LocalDate date) {
		Member member = memberCommandService.validMember(email);
		Challenge challenge = challengeCommandService.validChallenge(challengeId);
		MemberChallenge memberChallenge = challengeCommandService.validMemberChallenge(member, challenge);

		List<ExerciseChallengeRecord> exerciseChallengeRecordList = exerciseChallengeRecordRepository.findByMemberChallengeAndCreatedAtDate(
			memberChallenge, date);

		List<ChallengeResponseDTO.GetExerciseRecordResultDTO> DTOList = exerciseChallengeRecordList.stream()
			.map(record -> {
				RecordImage recordImage = recordImageRepository.findByExerciseChallengeRecord(record);
				String imageURL = recordImage.getImageUrl();
				return challengeConverter.toGetExerciseRecordResultDTO(record, imageURL);
			}).collect(Collectors.toList());

		return DTOList;
	}

	@Override
	@Transactional
	public List<ChallengeResponseDTO.GetExerciseRecordResultDTO> getTeamExerciseRecord(String email, Long challengeId,
		LocalDate date) {
		Member member = memberCommandService.validMember(email);
		challengeCommandService.validChallenge(challengeId);

		// 특정 날짜와 challengeId에 해당하는 모든 기록 가져오기
		List<ExerciseChallengeRecord> exerciseChallengeRecordList = exerciseChallengeRecordRepository.findByChallengeIdAndCreatedAtDate(challengeId, date);

		// 자신의 기록을 제외하고 다른 팀원들의 기록만 남기기
		List<ChallengeResponseDTO.GetExerciseRecordResultDTO> DTOList = exerciseChallengeRecordList.stream()
			.filter(record -> !record.getMemberChallenge().getMember().getId().equals(member.getId()))
			.map(record -> {
				RecordImage recordImage = recordImageRepository.findByExerciseChallengeRecord(record);
				String imageURL = recordImage.getImageUrl();
				return challengeConverter.toGetExerciseRecordResultDTO(record, imageURL);
			}).collect(Collectors.toList());

		return DTOList;
	}

	@Override
	@Transactional
	public String getMyOXRecord(String email, Long challengeId, LocalDate date){
		Member member = memberCommandService.validMember(email);
		Challenge challenge = challengeCommandService.validChallenge(challengeId);
		MemberChallenge memberChallenge = challengeCommandService.validMemberChallenge(member, challenge);

		if(date.isBefore(challenge.getStartDate()))
			throw new ChallengeHandler(ErrorStatus.NOT_PARTICIPATING_IN_CHALLENGE);

		Boolean record = simpleVerificationChallengeRecordRepository.findByMemberChallengeAndCreatedAtDate(memberChallenge, date)
			.map(SimpleVerificationChallengeRecord::getRecord)
			.orElse(false);

		if (record) {
			return "O";
		} else {
			return "X";
		}
	}


	@Override
	@Transactional
	public List<ChallengeResponseDTO.GetExerciseRankingDTO> getChallengeRanking(String email, Long challengeId) {
		Challenge challenge = challengeCommandService.validChallenge(challengeId);
		challengeCommandService.validMemberChallenge(memberCommandService.validMember(email), challenge);

		// challenge로 MemberChallenge 가져오기
		List<MemberChallenge> memberChallengeList = memberChallengeRepository.findAllByChallenge(challenge);

		// 각 멤버의 운동 기록 합산해서 Map에 저장
		Map<MemberChallenge, Duration> memberTotalExerciseTimeMap = memberChallengeList.stream()
			.collect(Collectors.toMap(
				memberChallenge -> memberChallenge,
				memberChallenge -> calculateTotalExerciseTime(
					exerciseChallengeRecordRepository.findAllByMemberChallenge(memberChallenge)
				)
			));

		// 누적 운동 시간으로 순위 계산
		List<ChallengeResponseDTO.GetExerciseRankingDTO> rankingDTOList = calculateRank(memberTotalExerciseTimeMap);

		// 순위 할당
		return assignRanks(rankingDTOList);
	}

	// 누적 운동 시간 계산
	public Duration calculateTotalExerciseTime(List<ExerciseChallengeRecord> records) {
		return records.stream()
			.map(record -> Duration.between(record.getStartTime(), record.getEndTime()))
			.reduce(Duration.ZERO, Duration::plus); // 모든 운동시간 합산 계산
	}

	// 랭킹 계산
	public List<ChallengeResponseDTO.GetExerciseRankingDTO> calculateRank(Map<MemberChallenge, Duration> memberChallengeDurationMap){
		return memberChallengeDurationMap.entrySet().stream()
			.sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())) // 운동시간 기준 내림차순
			.map(entry -> {
				Duration duration = entry.getValue();
				int hours = (int) duration.toHours();
				int minutes = duration.toMinutesPart();

				// DTO 변환 (순위는 나중에 할당)
				return challengeConverter.toGetExerciseRankingDTO(null, entry.getKey().getMember().getNickname(), hours, minutes);
			}).collect(Collectors.toList());
	}

	// 순위 할당
	public List<ChallengeResponseDTO.GetExerciseRankingDTO> assignRanks(List<ChallengeResponseDTO.GetExerciseRankingDTO> rankingList){
		for (int i = 0; i < rankingList.size(); i++)
			rankingList.get(i).setRank(i + 1);
		return rankingList;
	}


}
