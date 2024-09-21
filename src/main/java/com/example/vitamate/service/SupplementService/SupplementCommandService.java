package com.example.vitamate.service.SupplementService;

import com.example.vitamate.web.dto.SupplementRequestDTO;
import com.example.vitamate.web.dto.SupplementResponseDTO;

import java.time.LocalDate;

public interface SupplementCommandService {
    SupplementResponseDTO.AddIntakeSupplementResultDTO addIntakeSupplement(String email, Long supplementId, SupplementRequestDTO.AddIntakeSupplementDTO requestDTO);

}
