package com.example.vitamate.web.dto;

import com.example.vitamate.domain.enums.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

public class MemberRequestDTO {

    @Getter
    public static class SignInDTO{
        private String email;
    }

    @Getter
    public static class JoinDTO{

        //소셜로그인
        String email;

        @NotNull
        String nickname;
        @NotNull
        Integer age;
        @NotNull
        Double height;
        @NotNull
        Double weight;
        @NotNull
        Integer gender;

        Integer bmr;
//        List<Long> preferCategory;
    }
}
