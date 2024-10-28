package com.example.vitamate.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.example.vitamate.domain.Challenge;
import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.RecordImage;
import com.example.vitamate.domain.enums.ChallengeDuration;
import com.example.vitamate.domain.enums.ChallengeStatus;
import com.example.vitamate.domain.mapping.ExerciseChallengeRecord;
import com.example.vitamate.domain.mapping.MemberChallenge;
import com.example.vitamate.web.dto.ChallengeRequestDTO;
import com.example.vitamate.web.dto.ChallengeResponseDTO;

@Component
public class ChallengeConverter {
	public static Challenge toChallenge(ChallengeRequestDTO.CreateChallengeRequestDTO requestDTO){
		return Challenge.builder()
			.title(requestDTO.getTitle())
			.description(requestDTO.getDescription())
			.minUsers(requestDTO.getMinParticipants())
			.maxUsers(requestDTO.getMaxParticipants())
			.currentUsers(1)
			.expiryDate(LocalDate.now().plusWeeks(1))
			.startDate(requestDTO.getStartDate())
			.challengeCategory(requestDTO.getCategory())
			.duration(requestDTO.getDuration())
			.status(ChallengeStatus.WAITING)
			.weeklyFrequency(requestDTO.getWeeklyFrequency())
			.build();
	}

	public static MemberChallenge toMemberChallenge(Member member, Challenge challenge, boolean isLeader){
		return MemberChallenge.builder()
			.member(member)
			.challenge(challenge)
			.isLeader(isLeader)
			.build();
	}

	public static ChallengeResponseDTO.CreateChallengeResultDTO toCreateChallengeResponseDTO(Challenge challenge){
		return ChallengeResponseDTO.CreateChallengeResultDTO.builder()
			.challengeId(challenge.getId())
			.title(challenge.getTitle())
			.status(challenge.getStatus())
			.createdAt(challenge.getCreatedAt())
			.build();
	}


	public static ChallengeResponseDTO.ChallengePreviewDTO toChallengePreviewDTO(Challenge challenge){
		return ChallengeResponseDTO.ChallengePreviewDTO.builder()
			.ChallengeId(challenge.getId())
			.title(challenge.getTitle())
			.dDay((int)ChronoUnit.DAYS.between(LocalDate.now(), challenge.getStartDate()))
			.weeklyFrequency(challenge.getWeeklyFrequency())
			.currentParticipants(challenge.getCurrentUsers())
			.maxParticipants(challenge.getMaxUsers())
			.build();
	}

	public static ChallengeResponseDTO.ChallengeListDTO toChallengeListDTO(
		Page<Challenge> challnegePage) {
		List<ChallengeResponseDTO.ChallengePreviewDTO> challengePreviewDTOList = challnegePage.stream()
			.map(ChallengeConverter::toChallengePreviewDTO).collect(Collectors.toList());

		return ChallengeResponseDTO.ChallengeListDTO.builder()
			.challengeList(challengePreviewDTOList)
			.listSize(challengePreviewDTOList.size())
			.totalPage(challnegePage.getTotalPages())
			.totalElements(challnegePage.getTotalElements())
			.isFirst(challnegePage.isFirst())
			.isLast(challnegePage.isLast())
			.build();
	}

	public static ChallengeResponseDTO.JoinChallengeResultDTO toJoinChallengeResultDTO(MemberChallenge memberChallenge){
		return ChallengeResponseDTO.JoinChallengeResultDTO.builder()
			.joinedAt(memberChallenge.getCreatedAt())
			.ChallengeId(memberChallenge.getChallenge().getId())
			.build();
	}

	public static ChallengeResponseDTO.ParticipatingChallengeDTO toParticipatingChallengeDTO(Challenge challenge){
		return ChallengeResponseDTO.ParticipatingChallengeDTO.builder()
			.category(challenge.getChallengeCategory())
			.title(challenge.getTitle())
			.challengeId(challenge.getId())
			.startDate(challenge.getStartDate())
			.endDate(getEndDate(challenge.getStartDate(), challenge.getDuration()))
			.build();
	}

	public static ChallengeResponseDTO.ParticipatingChallengeListDTO toParticipatingChallengeListDTO(
		ChallengeResponseDTO.ParticipatingChallengeDTO exerciseChallenge,
		ChallengeResponseDTO.ParticipatingChallengeDTO quitAlcoholChallenge,
		ChallengeResponseDTO.ParticipatingChallengeDTO quitSmokeChallenge){
		return ChallengeResponseDTO.ParticipatingChallengeListDTO.builder()
			.exerciseChallenge(exerciseChallenge)
			.quitSmokeChallenge(quitSmokeChallenge)
			.quitAlcoholChallenge(quitAlcoholChallenge)
			.build();
	}

	public static ExerciseChallengeRecord toExerciseChallengeRecord(
		ChallengeRequestDTO.AddExerciseRecordDTO requestDTO,
		MemberChallenge memberChallenge){
		return ExerciseChallengeRecord.builder()
			.exerciseType(requestDTO.getExerciseType())
			.comment(requestDTO.getComment())
			.memberChallenge(memberChallenge)
			.startTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(requestDTO.getStartHour(), requestDTO.getStartMinute())))
			.endTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(requestDTO.getEndHour(), requestDTO.getEndMinute())))
			.build();
	}

	public static RecordImage toRecordImage(String imageUrl, ExerciseChallengeRecord record){
		return RecordImage.builder()
			.exerciseChallengeRecord(record)
			.imageUrl(imageUrl)
			.build();
	}

	public static ChallengeResponseDTO.AddExerciseRecordResultDTO toAddExerciseRecordResultDTO(ExerciseChallengeRecord record, String imageURL){
		return ChallengeResponseDTO.AddExerciseRecordResultDTO.builder()
			.recordId(record.getId())
			.imageURL(imageURL)
			.build();
	}

	public static LocalDate getEndDate(LocalDate startDate, ChallengeDuration duration){
		switch(duration){
			case ONE_WEEK :
				return startDate.plusWeeks(1);
			case ONE_MONTH :
				return startDate.plusMonths(1);
			case THREE_MONTHS:
				return startDate.plusMonths(3);
			case SIX_MONTHS:
				return startDate.plusMonths(6);
			case ONE_YEAR :
				return startDate.plusYears(1);
			default:
				throw new IllegalArgumentException("Invalid duration");
		}
	}
}
