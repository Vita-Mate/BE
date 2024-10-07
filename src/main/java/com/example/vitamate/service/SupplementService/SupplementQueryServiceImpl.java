package com.example.vitamate.service.SupplementService;

import com.example.vitamate.apiPayload.code.status.ErrorStatus;
import com.example.vitamate.apiPayload.exception.handler.MemberHandler;
import com.example.vitamate.apiPayload.exception.handler.SupplementHandler;
import com.example.vitamate.converter.ReviewConverter;
import com.example.vitamate.converter.SupplementConverter;
import com.example.vitamate.domain.AgeGroups;
import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.NutrientAlias;
import com.example.vitamate.domain.NutrientRecommendations;
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

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final NutrientRecommendationsRepository nutrientRecommendationsRepository;
    private final SupplementConverter supplementConverter;
    private final AgeGroupsRepository ageGroupsRepository;

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

        // 중복되는 nutrientId에 대해 섭취량을 합산할 Map 준비
        Map<Integer, Double> nutrientAmountMap = new HashMap<>();

        // nutrientId와 해당 영양소 정보를 저장할 Map
        Map<Integer, NutrientInfo> nutrientInfoMap = new HashMap<>();

        // MemberSupplement 리스트에 있는 각 객체에 포함된 NutrientInfo를 가져와 합산
        for(MemberSupplement memberSupplement : memberSupplementPage){
            Supplement supplement = memberSupplement.getSupplement();
            List<NutrientInfo> nutrientInfoList = nutrientInfoRepository.findAllBySupplement(supplement);

            for (NutrientInfo nutrientInfo : nutrientInfoList){
                Integer nutrientId = nutrientInfo.getNutrientId();
                nutrientInfoMap.put(nutrientId, nutrientInfo); // 해당 영양소 정보를 저장

                Double nutrientAmount = Double.parseDouble(nutrientInfo.getAmount());

                // 섭취량을 nutrientAmountMap에 저장하고 중복일 경우 합산
                nutrientAmountMap.merge(nutrientId, nutrientAmount, Double::sum);
            }
        }

        // IntakeNutrientResultDTO 리스트 생성
        List<SupplementResponseDTO.IntakeNutrientResultDTO> intakeNutrientDTOList = new ArrayList<>();

        for(Map.Entry<Integer, Double> entry: nutrientAmountMap.entrySet()) {
            Integer nutrientId = entry.getKey();
            Double totalAmount = entry.getValue();
            NutrientInfo nutrientInfo = nutrientInfoMap.get(nutrientId);

            // NutrientRecommended에서 권장량과 단위를 가져와 DTO에 추가
            AgeGroups ageGroups = ageGroupsRepository.findById(calculateAgeGroup(member.getBirthDay()))
                .orElseThrow(() -> new IllegalArgumentException("해당 연령 그룹이 존재하지 않습니다."));

            NutrientRecommendations recommendation = nutrientRecommendationsRepository.findByNutrientIdAndAgeGroupIdAndGender
                (nutrientId, ageGroups.getId(), member.getGender().toString());

            // DTO 생성
            SupplementResponseDTO.IntakeNutrientResultDTO dto = supplementConverter.toIntakeNutrientResultDTO(
                recommendation, totalAmount);
            intakeNutrientDTOList.add(dto);
        }

        // 전체 사이즈와 페이지 정보는 중복된 영양소 리스트를 기준으로 설정
        int totalElements = intakeNutrientDTOList.size();
        int totalPage = (int) Math.ceil((double) totalElements / pageSize);

        // 필요한 페이지의 영양소 리스트를 잘라서 반환
        List<SupplementResponseDTO.IntakeNutrientResultDTO> pagedNutrientList = intakeNutrientDTOList.stream()
            .skip((long) page * pageSize)
            .limit(pageSize)
            .collect(Collectors.toList());

        return supplementConverter.toIntakeNutrientListDTO(pagedNutrientList, totalPage, totalElements, page);
    }

    private int calculateAgeGroup(LocalDate birthDate){
        LocalDate today = LocalDate.now();

        int age = Period.between(birthDate, today).getYears();

        // 나이에 따라 AgeGroup ID 반환
        if (age >= 6 && age <= 8) {
            return 1;
        } else if (age >= 9 && age <= 11) {
            return 2;
        } else if (age >= 12 && age <= 14) {
            return 3;
        } else if (age >= 15 && age <= 18) {
            return 4;
        } else if (age >= 19 && age <= 29) { // 19세부터 29세까지
            return 5;
        } else if (age >= 30 && age <= 49) {
            return 6;
        } else if (age >= 50 && age <= 64) {
            return 7;
        } else if (age >= 65 && age <= 74) {
            return 8;
        } else {
            return 9; // 75세 이상
        }
    }
}
