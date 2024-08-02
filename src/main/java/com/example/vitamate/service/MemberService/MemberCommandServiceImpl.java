package com.example.vitamate.service.MemberService;

import com.example.vitamate.apiPayload.code.status.ErrorStatus;
import com.example.vitamate.apiPayload.exception.handler.MemberHandler;
import com.example.vitamate.converter.MemberConverter;
import com.example.vitamate.domain.Member;
import com.example.vitamate.repository.MemberRepository;
import com.example.vitamate.web.dto.MemberRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService{

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Member joinMember(MemberRequestDTO.JoinDTO request){
        // 잘못된 값 입력 에러
        // 닉네임 중복 체크 할건지?
        if(request.getAge()<0)
            throw new MemberHandler(ErrorStatus.NICKNAME_NOT_FOUND);
        if(request.getHeight()<0)
            throw new MemberHandler(ErrorStatus.INVALID_HEIGHT_VALUE);
        if(request.getWeight()<0)
            throw new MemberHandler(ErrorStatus.INVALID_WEIGHT_VALUE);
        if(request.getGender()!=1 && request.getGender()!=2)
            throw new MemberHandler(ErrorStatus.INVALID_GENDER_VALUE);

        Member newMember = MemberConverter.toMember(request);
        return memberRepository.save(newMember);
    }

    @Transactional
    public Member findMemberByEmail(String email){
        return memberRepository.findByEmail(email);
    }
}
