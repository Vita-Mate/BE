package com.example.vitamate.service.SupplementService;

import com.example.vitamate.apiPayload.code.status.ErrorStatus;
import com.example.vitamate.apiPayload.exception.handler.MemberHandler;
import com.example.vitamate.apiPayload.exception.handler.ReviewHandler;
import com.example.vitamate.apiPayload.exception.handler.SupplementHandler;
import com.example.vitamate.converter.MemberSupplementConverter;
import com.example.vitamate.converter.ReviewConverter;
import com.example.vitamate.converter.SupplementConverter;
import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.mapping.MemberSupplement;
import com.example.vitamate.domain.mapping.Review;
import com.example.vitamate.repository.MemberRepository;
import com.example.vitamate.repository.MemberSupplementRepository;
import com.example.vitamate.repository.ReviewRepository;
import com.example.vitamate.repository.SupplementRepository;
import com.example.vitamate.web.dto.SupplementRequestDTO;
import com.example.vitamate.web.dto.SupplementResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupplementCommandServiceImpl implements SupplementCommandService{

    private final MemberRepository memberRepository;
    private final SupplementRepository supplementRepository;
    private final MemberSupplementRepository memberSupplementRepository;
    private final MemberSupplementConverter memberSupplementConverter;
    private final SupplementConverter supplementConverter;
    private final ReviewConverter reviewConverter;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public SupplementResponseDTO.AddIntakeSupplementResultDTO addIntakeSupplement(String email, Long supplementId, SupplementRequestDTO.AddIntakeSupplementDTO requestDTO) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Supplement supplement = supplementRepository.findById(supplementId)
                .orElseThrow(() -> new SupplementHandler(ErrorStatus.SUPPLEMENT_NOT_FOUND));

        if(requestDTO.getStartDate().isAfter(LocalDate.now())){
            throw new SupplementHandler(ErrorStatus.INVALID_DATE);
        }

        // 스크랩 되어 있으면 -> 복용중 true로 변경
        // 스크랩 안되어있으면 -> 객체 생성해서 save
        // 이미 복용중이면 -> 에러
        MemberSupplement memberSupplement = memberSupplementRepository.findByMemberAndSupplement(member, supplement)
                .orElse(MemberSupplement.builder()
                        .member(member)
                        .supplement(supplement)
                        .isScrapped(false)
                        .isTaking(false)
                        .build()
                );

        if(memberSupplement.getIsTaking()){
            throw new SupplementHandler(ErrorStatus.ALREADY_TAKEN_ERROR);
        }

        memberSupplement.setIsTaking(true);
        memberSupplement.setStartDate(LocalDate.now());
        memberSupplement = memberSupplementRepository.save(memberSupplement);

        LocalDate startDate = memberSupplement.getStartDate();
        LocalDate now = LocalDate.now();

        Integer duration = (int) DAYS.between(startDate, now);
        duration += 1;

        return memberSupplementConverter.toAddIntakeSupplementResultDTO(memberSupplement, duration);

    }

    @Override
    @Transactional
    public SupplementResponseDTO.AddScrapResultDTO addScrap(String email, Long supplementId){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Supplement supplement = supplementRepository.findById(supplementId)
                .orElseThrow(() -> new SupplementHandler(ErrorStatus.SUPPLEMENT_NOT_FOUND));

        // 복용 중 or 스크랩 중인 영양제 재활용
        MemberSupplement memberSupplement = memberSupplementRepository.findByMemberAndSupplement(member, supplement)
                .orElse(MemberSupplement.builder()
                        .member(member)
                        .supplement(supplement)
                        .build());

        // 이미 스크랩 된 경우 에러 핸들링
        if(memberSupplement.getIsScrapped()){
            throw new SupplementHandler(ErrorStatus.ALREADY_SCRAPPED_ERROR);
        }

        memberSupplement.setIsScrapped(true);

        return SupplementConverter.toAddScrapResultDTO(memberSupplementRepository.save(memberSupplement));

    }

    @Override
    @Transactional
    public SupplementResponseDTO.DeleteScrapResultDTO deleteScrap(String email, Long supplementId){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Supplement supplement = supplementRepository.findById(supplementId)
                .orElseThrow(() -> new SupplementHandler(ErrorStatus.SUPPLEMENT_NOT_FOUND));

        MemberSupplement memberSupplement = memberSupplementRepository.findByMemberAndSupplement(member, supplement)
                .orElseThrow(() -> new SupplementHandler(ErrorStatus.NOT_SCRAPPED));

        // 스크랩 되어있지 않은 경우
        if(memberSupplement.getIsScrapped() != true) {
            throw new SupplementHandler(ErrorStatus.NOT_SCRAPPED);
        } else if (memberSupplement.getIsTaking() != true) {
            // 복용 중은 아니고 스크랩만 되어있는 경우
            memberSupplementRepository.delete(memberSupplement);
        } else {
            // 복용중이고 스크랩중인 경우
            memberSupplement.setIsScrapped(false);
            memberSupplement = memberSupplementRepository.save(memberSupplement);
        }

        return SupplementConverter.toDeleteScrapResultDTO(memberSupplement);
    }

    @Override
    @Transactional
    public SupplementResponseDTO.ReviewResultDTO addReview(String email, Long supplementId, SupplementRequestDTO.AddReviewDTO requestDTO){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Supplement supplement = supplementRepository.findById(supplementId)
                .orElseThrow(() -> new SupplementHandler(ErrorStatus.SUPPLEMENT_NOT_FOUND));

        if(requestDTO.getGrade()<0 || requestDTO.getGrade()>5){
            throw new ReviewHandler(ErrorStatus.INVALID_GRADE_VALUE);
        }

        Review review = reviewConverter.toReview(member, supplement, requestDTO);
        reviewRepository.save(review);
        SupplementResponseDTO.ReviewResultDTO resultDTO = reviewConverter.toReviewResultDTO(reviewRepository.save(review));

        return resultDTO;

    }
}
