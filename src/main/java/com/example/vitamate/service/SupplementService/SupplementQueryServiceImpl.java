package com.example.vitamate.service.SupplementService;

import com.example.vitamate.apiPayload.code.status.ErrorStatus;
import com.example.vitamate.apiPayload.exception.handler.MemberHandler;
import com.example.vitamate.converter.SupplementConverter;
import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.mapping.MemberSupplement;
import com.example.vitamate.domain.mapping.NutrientInfo;
import com.example.vitamate.repository.MemberRepository;
import com.example.vitamate.repository.MemberSupplementRepository;
import com.example.vitamate.repository.NutrientInfoRepository;
import com.example.vitamate.repository.SupplementRepository;
import com.example.vitamate.web.dto.SupplementResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SupplementQueryServiceImpl implements SupplementQueryService {

    private final MemberRepository memberRepository;
    private final MemberSupplementRepository memberSupplementRepository;
    private final SupplementRepository supplementRepository;
    private final NutrientInfoRepository nutrientInfoRepository;

    @Override
    @Transactional
    public Page<MemberSupplement> getTakingSupplementPage(String email, Integer page){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Page<MemberSupplement> memberSupplementPage = memberSupplementRepository.findAllByMemberAndIsTakingTrue(member, PageRequest.of(page, 10));

        return memberSupplementPage;
    }

    @Override
    @Transactional
    public Page<Supplement> getSupplementsByName(String keyword, Integer page, Integer pageSize){

        return supplementRepository.findByNameContaining(keyword, PageRequest.of(page, pageSize));

    }

    @Override
    @Transactional
    public Page<Supplement> getSupplementsByNutrient(String keyword, Integer page, Integer pageSize){
        // NutrientInfo를 검색하여 관련 Supplement ID 추출
        List<NutrientInfo> nutrientInfoList =nutrientInfoRepository.findByNutrientNameContaining(keyword);

        // 중복된 Supplement ID를 Set에 저장
        Set<Long> supplementIdSet = nutrientInfoList.stream()
                .map(nutrientInfo -> nutrientInfo.getSupplement().getId())
                .collect(Collectors.toSet());

        // 중복 제거된 Supplement ID를 기준으로 Supplement를 검색하고 페이지네이션 적용
        Page<Supplement> supplementPage = supplementRepository.findAllByIdIn(supplementIdSet, PageRequest.of(page, pageSize));

        return supplementPage;
    }

    @Override
    @Transactional
    public SupplementResponseDTO.SupplementDetailDTO getSupplementDetail(String email, Long supplementId){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        Supplement supplement = supplementRepository.findById(supplementId)
                .orElseThrow(() -> new IllegalArgumentException("영양제 정보를 찾을 수 없습니다."));

        // MemberSupplement에 있는 객체는 스크랩 or 복용중 or 둘 다
        // MemberSupplement에 있고, isScrapped가 true이면 스크랩된 영양제
        Optional<MemberSupplement> memberSupplement = memberSupplementRepository.findByMemberAndSupplement(member, supplement);

        Boolean isScrapped = false;

        if(memberSupplement.isPresent()){
            isScrapped = memberSupplement.get().getIsScrapped();
        }

        return SupplementConverter.toSupplementDetailDTO(supplement, isScrapped);
    }
}
