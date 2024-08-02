package com.example.vitamate.service.MemberSupplementService;

import com.example.vitamate.domain.mapping.MemberSupplement;
import org.springframework.data.domain.Page;

public interface MemberSupplementService {

    Page<MemberSupplement> getTakingSuppplementList(String email, Integer page);
}
