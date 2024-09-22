package com.example.vitamate.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class ReviewRequestDTO {

    @Getter
    public static class AddReviewDTO{
        @NotNull
        Integer grade;
        String content;
    }
}
