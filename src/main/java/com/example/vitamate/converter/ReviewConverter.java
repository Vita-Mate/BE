package com.example.vitamate.converter;

import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.mapping.Review;
import com.example.vitamate.web.dto.ReviewRequestDTO;
import com.example.vitamate.web.dto.ReviewResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public static ReviewResponseDTO.ReviewListDTO toReviewListDTO(Page<Review> reviewPage){
        List<ReviewResponseDTO.ReviewResultDTO> reviewDTOList = reviewPage.stream()
                .map(ReviewConverter::toReviewResultDTO).collect(Collectors.toList());

        return ReviewResponseDTO.ReviewListDTO.builder()
                .isLast(reviewPage.isLast())
                .isFirst(reviewPage.isFirst())
                .totalPage(reviewPage.getTotalPages())
                .totalElements(reviewPage.getTotalElements())
                .reviewList(reviewDTOList)
                .build();
    }
}
