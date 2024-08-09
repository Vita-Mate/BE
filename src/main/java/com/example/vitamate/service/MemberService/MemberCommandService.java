package com.example.vitamate.service.MemberService;

import com.example.vitamate.jwt.JwtTokenDTO;
import com.example.vitamate.web.dto.MemberRequestDTO;
import com.example.vitamate.web.dto.MemberResponseDTO;

public interface MemberCommandService {

    JwtTokenDTO signIn(String email);

    MemberResponseDTO.SignUpResultDTO signUp(MemberRequestDTO.SignUpDTO request);

}
