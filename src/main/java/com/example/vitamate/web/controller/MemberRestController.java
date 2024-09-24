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
    @Operation(summary = "회원가입 API", description = "사용자의 카카오 계정 이메일과 기타 정보를 입력해주세요. " +
            "bmr(기초대사량) 정보가 없는 경우엔 ,(콤마부터) \"bmr\": 0 (까지) 을 제거해주세요! (gender 필드가 마지막 필드인 것 처럼! 마지막에 콤마로 끝나지 않도록 꼭 지우기!) " +
            "성별은 남성은 0, 여성은 1 입니다. ")
    public ApiResponse<MemberResponseDTO.SignUpResultDTO> signUp(@RequestBody MemberRequestDTO.SignUpDTO signUpDTO){
        MemberResponseDTO.SignUpResultDTO signUpResultDTO = memberCommandService.signUp(signUpDTO);
        return ApiResponse.onSuccess(signUpResultDTO);
    }

    @PostMapping("/test")
    public String test(){
        return SecurityUtil.getCurrentUsername();
    }
}
