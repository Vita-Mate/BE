package com.example.vitamate.service.ChallengeService;

import java.time.LocalDate;
import java.util.List;

import com.example.vitamate.domain.enums.ChallengeCategory;
import com.example.vitamate.domain.enums.ChallengeDuration;
import com.example.vitamate.web.dto.ChallengeResponseDTO;

public interface ChallengeQueryService {

	ChallengeResponseDTO.ChallengeListDTO getChallengeList(ChallengeCategory category, List<Integer> weeklyFrequency, LocalDate startDate, ChallengeDuration duration, Integer page, Integer pageSize);
}
