package com.example.vitamate.service.SupplementService;

import com.example.vitamate.web.dto.ReviewRequestDTO;
import com.example.vitamate.web.dto.ReviewResponseDTO;
import com.example.vitamate.web.dto.SupplementRequestDTO;
import com.example.vitamate.web.dto.SupplementResponseDTO;

public interface SupplementCommandService {
    SupplementResponseDTO.AddIntakeSupplementResultDTO addIntakeSupplement(String email, Long supplementId, SupplementRequestDTO.AddIntakeSupplementDTO requestDTO);
    SupplementResponseDTO.DeleteIntakeSupplementResultDTO deleteIntakeSupplement(String email, Long supplementId);
    SupplementResponseDTO.AddScrapResultDTO addScrap(String email, Long supplementId);
    SupplementResponseDTO.DeleteScrapResultDTO deleteScrap(String email, Long supplementId);

    ReviewResponseDTO.ReviewResultDTO addReview(String email, Long supplementId, ReviewRequestDTO.AddReviewDTO requestDTO);
}
