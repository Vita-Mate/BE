package com.example.vitamate.service.ChallengeService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.vitamate.converter.ChallengeConverter;
import com.example.vitamate.domain.Challenge;
import com.example.vitamate.domain.enums.ChallengeCategory;
import com.example.vitamate.domain.enums.ChallengeDuration;
import com.example.vitamate.domain.enums.ChallengeStatus;
import com.example.vitamate.repository.ChallengeRepository;
import com.example.vitamate.web.dto.ChallengeResponseDTO;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChallengeQueryServiceImpl implements ChallengeQueryService{

	private final ChallengeRepository challengeRepository;
	private final ChallengeConverter challengeConverter;

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

}
