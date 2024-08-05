package com.example.vitamate.service.MemberService;

import com.example.vitamate.domain.Member;
import com.example.vitamate.jwt.JwtTokenDTO;
import com.example.vitamate.web.dto.MemberRequestDTO;

public interface MemberCommandService {

    JwtTokenDTO signIn(String email);

    Member joinMember(MemberRequestDTO.JoinDTO request);
//    Member findMemberByEmail(String email);

    Member saveOrUpdateMember(Member member);
    void deleteMember(String email);
}
