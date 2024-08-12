package com.example.vitamate.web.controller;

import com.example.vitamate.apiPayload.ApiResponse;
import com.example.vitamate.jwt.JwtTokenDTO;
import com.example.vitamate.jwt.SecurityUtil;
import com.example.vitamate.service.MemberService.MemberCommandService;
import com.example.vitamate.web.dto.MemberRequestDTO;
import com.example.vitamate.web.dto.MemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
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


    @PostMapping("/sign-in")
    @Operation(summary = "로그인 API", description = "사용자의 카카오 계정 이메일을 입력해주세요. access 토큰과 refresh 토큰이 반환됩니다. access 토큰으로 로그인 진행해주세요")
    public ApiResponse<JwtTokenDTO> signIn(@RequestBody MemberRequestDTO.SignInDTO signInDTO) {
        String email = signInDTO.getEmail();
        JwtTokenDTO jwtTokenDTO = memberCommandService.signIn(email);
        log.info("request email = {}", email);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtTokenDTO.getAccessToken(), jwtTokenDTO.getRefreshToken());
        return ApiResponse.onSuccess(jwtTokenDTO);
    }

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입 API", description = "사용자의 카카오 계정 이메일과 기타 정보를 입력해주세요. bmr(기초대사량)필드는 입력하지 않을 경우 자동 계산됩니다.")
    public ApiResponse<MemberResponseDTO.SignUpResultDTO> signUp(@RequestBody MemberRequestDTO.SignUpDTO signUpDTO){
        MemberResponseDTO.SignUpResultDTO signUpResultDTO = memberCommandService.signUp(signUpDTO);
        return ApiResponse.onSuccess(signUpResultDTO);
    }

    @PostMapping("/test")
    public String test(){
        return SecurityUtil.getCurrentUsername();
    }
}
