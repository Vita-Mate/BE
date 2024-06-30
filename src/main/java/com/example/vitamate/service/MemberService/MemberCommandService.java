package com.example.vitamate.service.MemberService;

import com.example.vitamate.domain.Member;
import com.example.vitamate.web.dto.MemberRequestDTO;

public interface MemberCommandService {

    Member joinMember(MemberRequestDTO.JoinDTO request);

}
