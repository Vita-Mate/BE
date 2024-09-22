package com.example.vitamate.converter;

import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.mapping.Review;
import com.example.vitamate.web.dto.ReviewRequestDTO;
import com.example.vitamate.web.dto.ReviewResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverter {
    public static Review toReview(Member member, Supplement supplement, ReviewRequestDTO.AddReviewDTO requestDTO){
        return Review.builder()
                .grade(requestDTO.getGrade())
                .content(requestDTO.getContent())
                .member(member)
                .supplement(supplement)
                .build();
    }

    public static ReviewResponseDTO.ReviewResultDTO toReviewResultDTO(Review review){
        return ReviewResponseDTO.ReviewResultDTO.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .nickname(review.getMember().getNickname())
                .grade(review.getGrade())
                .createdDate(review.getCreatedAt())
                .build();
    }
}
