package com.example.vitamate.service.MemberService;

import com.example.vitamate.apiPayload.code.status.ErrorStatus;
import com.example.vitamate.apiPayload.exception.handler.MemberHandler;
import com.example.vitamate.converter.MemberConverter;
import com.example.vitamate.domain.enums.Gender;
import com.example.vitamate.jwt.JwtTokenDTO;
import com.example.vitamate.jwt.JwtTokenProvider;
import com.example.vitamate.repository.MemberRepository;
import com.example.vitamate.web.dto.MemberRequestDTO;
import com.example.vitamate.web.dto.MemberResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.vitamate.domain.enums.Gender.FEMALE;
import static com.example.vitamate.domain.enums.Gender.MALE;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService{

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberConverter memberConverter;

    @Override
    @Transactional
    public JwtTokenDTO signIn(String email){

        // 1. email을 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, "");

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtTokenDTO jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

    @Override
    @Transactional
    public MemberResponseDTO.SignUpResultDTO signUp(MemberRequestDTO.SignUpDTO request){
        if(memberRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 잘못된 값 입력 에러핸들링
        if(request.getBirthDay().isAfter(LocalDate.now()) || request.getBirthDay().isBefore(LocalDate.now().minusYears(120)))
            throw new MemberHandler(ErrorStatus.INVALID_AGE_VALUE);
        if(request.getHeight()<0)
            throw new MemberHandler(ErrorStatus.INVALID_HEIGHT_VALUE);
        if(request.getWeight()<0)
            throw new MemberHandler(ErrorStatus.INVALID_WEIGHT_VALUE);
        if(request.getGender()!= MALE && request.getGender()!=FEMALE)
            throw new MemberHandler(ErrorStatus.INVALID_GENDER_VALUE);
        // 닉네임 중복 체크 할건지?

        List<String> roles = new ArrayList<>();
        roles.add("USER"); // USER 권한 부여

        return memberConverter.toSignUpResultDTO(memberRepository.save(memberConverter.toMember(request, roles)));

    }


}
