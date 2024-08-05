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
        private String email;
    }

    @Getter
    public static class SignUpDTO{
        @NotNull
        private String email;
        @NotNull
        private String nickname;
        @NotNull
        private Integer age;
        @NotNull
        private Double height;
        @NotNull
        private Double weight;
        @NotNull
        private Integer gender;

        private Integer bmr;
    }
}
