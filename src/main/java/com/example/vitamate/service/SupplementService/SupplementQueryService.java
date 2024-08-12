package com.example.vitamate.service.SupplementService;

import com.example.vitamate.domain.mapping.MemberSupplement;
import org.springframework.data.domain.Page;

public interface SupplementQueryService {

    Page<MemberSupplement> getTakingSupplementPage(String email, Integer page);
}
