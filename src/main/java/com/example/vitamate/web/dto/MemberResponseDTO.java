package com.example.vitamate.web.dto;

import com.example.vitamate.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MemberResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpResultDTO {
        Long id;
        String email;
        String nickname;
        Gender gender;
        Integer age;
        LocalDateTime createdAt;
    }
}
