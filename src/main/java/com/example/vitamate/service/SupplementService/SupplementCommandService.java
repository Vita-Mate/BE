package com.example.vitamate.service.SupplementService;

import com.example.vitamate.web.dto.SupplementRequestDTO;
import com.example.vitamate.web.dto.SupplementResponseDTO;

import java.time.LocalDate;

public interface SupplementCommandService {
    SupplementResponseDTO.AddIntakeSupplementResultDTO addIntakeSupplement(String email, Long supplementId, SupplementRequestDTO.AddIntakeSupplementDTO requestDTO);

    SupplementResponseDTO.AddScrapResultDTO addScrap(String email, Long supplementId);
    SupplementResponseDTO.DeleteScrapResultDTO deleteScrap(String email, Long supplementId);
    SupplementResponseDTO.ReviewResultDTO addReview(String email, Long supplementId, SupplementRequestDTO.AddReviewDTO requestDTO);
}
