package com.example.vitamate.apiPayload.code.status;

import com.example.vitamate.apiPayload.code.BaseErrorCode;
import com.example.vitamate.apiPayload.code.ErrorReasonDTO;
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
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    NICKNAME_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수입니다."),
    INVALID_GENDER_VALUE(HttpStatus.BAD_REQUEST, "MEMBER4003", "성별 값이 유효하지 않습니다."),
    INVALID_AGE_VALUE(HttpStatus.BAD_REQUEST, "MEMBER4004", "나이 값이 유효하지 않습니다."),
    INVALID_HEIGHT_VALUE(HttpStatus.BAD_REQUEST, "MEMBER4005", "키 값이 유효하지 않습니다."),
    INVALID_WEIGHT_VALUE(HttpStatus.BAD_REQUEST, "MEMBER4006", "체중 값이 유효하지 않습니다."),

    // ~~ 관련 응답


    // For test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트");
    ;

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
