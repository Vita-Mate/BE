package com.example.vitamate.apiPayload.code.status;

import com.example.vitamate.apiPayload.code.BaseErrorCode;
import com.example.vitamate.apiPayload.code.ErrorReasonDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),


    // 멤버 관련 응답
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자 정보를 찾을 수 없습니다."),
    NICKNAME_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수입니다."),
    INVALID_GENDER_VALUE(HttpStatus.BAD_REQUEST, "MEMBER4003", "성별 값이 유효하지 않습니다."),
    INVALID_AGE_VALUE(HttpStatus.BAD_REQUEST, "MEMBER4004", "나이 값이 유효하지 않습니다."),
    INVALID_HEIGHT_VALUE(HttpStatus.BAD_REQUEST, "MEMBER4005", "키 값이 유효하지 않습니다."),
    INVALID_WEIGHT_VALUE(HttpStatus.BAD_REQUEST, "MEMBER4006", "체중 값이 유효하지 않습니다."),
    ALREADY_JOINED_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER4007", "이미 가입된 이메일입니다."),


    // 토큰 관련 응답
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "AUTH003", "access 토큰을 주세요!"),
    TOKEN_EXPIRED(HttpStatus.FORBIDDEN, "AUTH004", "access 토큰 만료"),
    TOKEN_INVALID(HttpStatus.BAD_REQUEST, "AUTH006", "access 토큰 모양이 이상함"),

    // 영양제 관련 응답
    ALREADY_SCRAPPED_ERROR(HttpStatus.BAD_REQUEST, "SUPPLEMENT001", "이미 스크랩한 영양제입니다."),
    SUPPLEMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "SUPPLEMENT002", "영양제 젖보를 찾을 수 없습니다."),
    NOT_SCRAPPED(HttpStatus.BAD_REQUEST, "SUPPLEMENT003", "해당 영양제는 스크랩 상태가 아닙니다"),
    ALREADY_TAKEN_ERROR(HttpStatus.BAD_REQUEST, "SUPPLEMENT004", "이미 복용중인 영양제입니다."),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "SUPPLEMENT005", "복용 날짜가 미래일 수 없습니다."),
    NOT_TAKEN_ERROR(HttpStatus.BAD_REQUEST, "SUPPLEMENT006", "해당 영양제는 복용중 상태가 아닙니다."),

    // 영양소 관련 응답
    RECOMMENDATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NUTRIENT5001", "권장량 정보를 찾을 수 없습니다."),

    // 리뷰 관련 응답
    INVALID_GRADE_VALUE(HttpStatus.BAD_REQUEST, "REVIEW001", "별점 값이 유효하지 않습니다."),

    // 챌린지 관련 응답
    DUPLICATE_CATEGORY_PARTICIPATION(HttpStatus.BAD_REQUEST, "CHALLENGE4001", "이미 같은 카테고리의 챌린지에 참여중입니다."),
    INVALID_NUMBERS_VALUE(HttpStatus.BAD_REQUEST, "CHALLENGE4002", "최소 인원수는 최대 인원수 이하여야 합니다."),
    CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGE4003", "챌린지 정보를 찾을 수 없습니다."),
    CHALLENGE_FULL(HttpStatus.BAD_REQUEST, "CHALLENGE4004", "챌린지 방이 꽉 찼습니다."),
    MEMBER_CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGE4005", "참여 중인 챌린지가 아닙니다."),
    CHALLENGE_NOT_IN_PROGRESS(HttpStatus.BAD_REQUEST, "CHALLENGE4006", "진행 중인 챌린지가 아닙니다."),
    INVALID_START_DATE(HttpStatus.BAD_REQUEST, "CHALLENGE4008", "시작일은 현재 날짜로부터 최대 일주일(7일) 미래여야 합니다."),
    NOT_PARTICIPATING_IN_CHALLENGE(HttpStatus.NOT_FOUND, "CHALLENGE4009", "참여중인 챌린지가 없습니다."),
    INVALID_CHALLENGE_STATUS_FOR_CANCELLATION(HttpStatus.CONFLICT, "CHALLENGE4010", "챌린지가 대기상태일 경우에만 참가 취소가 가능합니다."),
    DATE_BEFORE_CHALLENGE_START(HttpStatus.BAD_REQUEST, "CHALLENGE4011", "챌린지 시작일 이후의 날짜만 조회 가능합니다."),

    // ~~ 관련 응답

    // For test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus(){
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }


}
