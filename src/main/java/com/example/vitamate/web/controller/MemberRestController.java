package com.example.vitamate.web.controller;

import com.example.vitamate.apiPayload.ApiResponse;
import com.example.vitamate.converter.MemberConverter;
import com.example.vitamate.domain.Member;
import com.example.vitamate.jwt.JwtTokenDTO;
import com.example.vitamate.jwt.SecurityUtil;
import com.example.vitamate.service.MemberService.MemberCommandService;
import com.example.vitamate.web.dto.MemberRequestDTO;
import com.example.vitamate.web.dto.MemberResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberRestController {

    private final MemberCommandService memberCommandService;

    @PostMapping("/")
    public ApiResponse<MemberResponseDTO.JoinResultDTO> join(@RequestBody @Valid MemberRequestDTO.JoinDTO request){

        Member member = memberCommandService.joinMember(request);

        return ApiResponse.onSuccess(MemberConverter.toJoinResultDTO(member));
    }

    @PostMapping("/sign-in")
    public JwtTokenDTO signIn(@RequestBody MemberRequestDTO.SignInDTO signInDTO) {
        String email = signInDTO.getEmail();
        JwtTokenDTO jwtTokenDTO = memberCommandService.signIn(email);
        log.info("request email = {}", email);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtTokenDTO.getAccessToken(), jwtTokenDTO.getRefreshToken());
        return jwtTokenDTO;
    }

    @PostMapping("/test")
    public String test(){
        return SecurityUtil.getCurrentUsername();
    }
}
