package com.example.vitamate.service.MemberService;

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
        Member newMember = MemberConverter.toMember(request);
        return memberRepository.save(newMember);
    }
}
