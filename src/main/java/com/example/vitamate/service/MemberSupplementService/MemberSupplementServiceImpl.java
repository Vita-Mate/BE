package com.example.vitamate.service.MemberSupplementService;

import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.mapping.MemberSupplement;
import com.example.vitamate.repository.MemberRepository;
import com.example.vitamate.repository.MemberSupplementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberSupplementServiceImpl implements MemberSupplementService {

    private final MemberRepository memberRepository;
    private final MemberSupplementRepository memberSupplementRepository;

    @Override
    public Page<MemberSupplement> getTakingSuppplementList(String email, Integer page){
        Member member = memberRepository.findByEmail(email);

        Page<MemberSupplement> memberSupplementPage = memberSupplementRepository.findAllByMember(member, PageRequest.of(page, 10));
        return memberSupplementPage;
    }

}
