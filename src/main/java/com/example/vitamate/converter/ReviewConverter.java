package com.example.vitamate.converter;

import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.mapping.Review;
import com.example.vitamate.web.dto.SupplementRequestDTO;
import com.example.vitamate.web.dto.SupplementResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverter {
    public static Review toReview(Member member, Supplement supplement, SupplementRequestDTO.AddReviewDTO requestDTO){
        return Review.builder()
                .grade(requestDTO.getGrade())
                .content(requestDTO.getContent())
                .member(member)
                .supplement(supplement)
                .build();
    }

    public static SupplementResponseDTO.ReviewResultDTO toReviewResultDTO(Review review){
        return SupplementResponseDTO.ReviewResultDTO.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .nickname(review.getMember().getNickname())
                .grade(review.getGrade())
                .createdDate(review.getCreatedAt())
                .build();
    }
}
