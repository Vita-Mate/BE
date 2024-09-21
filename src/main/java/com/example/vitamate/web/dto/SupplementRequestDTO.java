package com.example.vitamate.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class SupplementRequestDTO {

    @Getter
    public static class AddIntakeSupplementDTO {
        @NotNull
        LocalDate startDate;
    }

    @Getter
    public static class AddReviewDTO{
        @NotNull
        Integer grade;

        String content;
    }
}
