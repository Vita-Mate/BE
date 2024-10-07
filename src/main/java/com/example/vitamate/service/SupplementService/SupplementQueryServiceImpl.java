package com.example.vitamate.service.SupplementService;

import com.example.vitamate.apiPayload.code.status.ErrorStatus;
import com.example.vitamate.apiPayload.exception.handler.MemberHandler;
import com.example.vitamate.apiPayload.exception.handler.SupplementHandler;
import com.example.vitamate.converter.ReviewConverter;
import com.example.vitamate.converter.SupplementConverter;
import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.NutrientAlias;
import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.mapping.MemberSupplement;
import com.example.vitamate.domain.mapping.NutrientInfo;
import com.example.vitamate.domain.mapping.Review;
import com.example.vitamate.repository.*;
import com.example.vitamate.web.dto.ReviewResponseDTO;
import com.example.vitamate.web.dto.SupplementResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;
    private final NutrientAliasRepository nutrientAliasRepository;

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

        // 검색 키워드를 포함하는 영양소 이름 모두 조회
        List<NutrientAlias> nutrientAliasList = nutrientAliasRepository.findByAliasNameContaining(keyword);

        // 각각의 영양소 id 추출
        Set<Integer> nutrientIdSet = nutrientAliasList.stream().map(nutrientAlias-> nutrientAlias.getNutrient().getId()).collect(Collectors.toSet());

        Page<NutrientInfo> nutrientInfoPage = nutrientInfoRepository.findByNutrientIdIn(nutrientIdSet, PageRequest.of(page, pageSize));

        // 추출한 supplement_id 목록
        List<Supplement> supplementList = nutrientInfoPage.stream()
            .map(NutrientInfo::getSupplement)
            .collect(Collectors.toList());

        Page<Supplement> supplementPage = new PageImpl<>(supplementList, nutrientInfoPage.getPageable(), nutrientInfoPage.getTotalElements());

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

    @Override
    @Transactional
    public ReviewResponseDTO.ReviewListDTO getReviewList(Long supplementId, Integer page, Integer pageSize){
        Supplement supplement = supplementRepository.findById(supplementId)
                .orElseThrow(() -> new SupplementHandler(ErrorStatus.SUPPLEMENT_NOT_FOUND));

        Page<Review> reviewPage = reviewRepository.findAllBySupplement(supplement, PageRequest.of(page, pageSize));

        return reviewConverter.toReviewListDTO(reviewPage);
    }

    @Override
    @Transactional
    public SupplementResponseDTO.IntakeNutrientListDTO getIntakeNutrientList(String email, Integer page, Integer pageSize) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        // 유저가 복용중인 영양제에 대한 MemberSupplement 객체 리스트
        Page<MemberSupplement> memberSupplementPage = memberSupplementRepository.findAllByMemberAndIsTakingTrue(member, PageRequest.of(page, pageSize));

        // 복용중인 영양소를 담을 리스트
        List<NutrientInfo> nutrientInfoList = new ArrayList<>();

        // MemberSupplement 리스트에 있는 각 객체에 포함된 NutrientInfo를 리스트에 추가
        for(MemberSupplement memberSupplement : memberSupplementPage){
              Supplement supplement = memberSupplement.getSupplement();
              nutrientInfoList.addAll(nutrientInfoRepository.findAllBySupplement(supplement));
        }
        return null;

    }
}
