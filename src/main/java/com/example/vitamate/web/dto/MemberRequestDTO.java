package com.example.vitamate.web.dto;

import com.example.vitamate.domain.enums.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Optional;

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
        LocalDate birthDay;
        @NotNull
        Double height;
        @NotNull
        Double weight;
        @NotNull
        Integer gender;


        Optional<Integer> bmr = Optional.empty();
    }
}
