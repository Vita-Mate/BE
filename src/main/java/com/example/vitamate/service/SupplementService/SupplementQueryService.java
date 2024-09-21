package com.example.vitamate.service.SupplementService;

import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.mapping.MemberSupplement;
import com.example.vitamate.jwt.SecurityUtil;
import com.example.vitamate.web.dto.SupplementResponseDTO;
import org.springframework.data.domain.Page;

public interface SupplementQueryService {

    Page<MemberSupplement> getTakingSupplementPage(String email, Integer page);

    Page<Supplement> getSupplementsByName(String keyword, Integer page, Integer pageSize);
    Page<Supplement> getSupplementsByNutrient(String keyword, Integer page, Integer pageSize);
    SupplementResponseDTO.SupplementDetailDTO getSupplementDetail(String email, Long supplementId);
}
