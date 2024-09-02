package com.example.vitamate.service.SupplementService;

import com.example.vitamate.converter.MemberSupplementConverter;
import com.example.vitamate.domain.SupplementInfo;
import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.mapping.MemberSupplement;
import com.example.vitamate.repository.MemberRepository;
import com.example.vitamate.repository.MemberSupplementRepository;
import com.example.vitamate.repository.SupplementInfoRepository;
import com.example.vitamate.repository.SupplementRepository;
import com.example.vitamate.domain.SupplementDataSave;
import com.example.vitamate.web.dto.SupplementRequestDTO;
import com.example.vitamate.web.dto.SupplementResponseDTO;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupplementCommandServiceImpl implements SupplementCommandService{

    private final MemberRepository memberRepository;
    private final SupplementRepository supplementRepository;
    private final MemberSupplementRepository memberSupplementRepository;
    private final MemberSupplementConverter memberSupplementConverter;

    @Override
    @Transactional
    public SupplementResponseDTO.AddIntakeSupplementResultDTO addIntakeSupplement(String email, SupplementRequestDTO.AddIntakeSupplementDTO requestDTO) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        Supplement supplement = supplementRepository.findById(requestDTO.getSupplementId())
                .orElseThrow(() -> new IllegalArgumentException("영양제 정보를 찾을 수 없습니다."));

        // 기존에 스크랩 된 영양제 재활용
        MemberSupplement memberSupplement = memberSupplementRepository.findByMemberAndSupplement(member, supplement)
                .orElse(MemberSupplement.builder()
                        .member(member)
                        .supplement(supplement)
                        .isScrapped(false)
                        .build());

        // 기존 객체 상태 업데이트
        memberSupplement.setIsTaking(true);
        memberSupplement.setStartDate(LocalDate.now());

        memberSupplement = memberSupplementRepository.save(memberSupplement);
        System.out.println("Update at: " + memberSupplement.getUpdatedAt());
        return memberSupplementConverter.toAddIntakeSupplementResultDTO(memberSupplement);

    }

}
