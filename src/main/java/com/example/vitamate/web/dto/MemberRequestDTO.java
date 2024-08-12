package com.example.vitamate.web.dto;

import com.example.vitamate.domain.enums.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class MemberRequestDTO {

    @Getter
    public static class SignInDTO{
        String email;
    }

    @Getter
    public static class SignUpDTO{

        @NotNull
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
    }
}
