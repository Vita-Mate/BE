package com.example.vitamate.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

public class SupplementRequestDTO {

    @Getter
    public static class AddIntakeSupplementDTO {
        @NotNull
        LocalDate startDate;
    }
}
