package com.example.vitamate.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SupplementResponseDTO {

    // 복용중인 영양제 목록 조회 DTO
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IntakeSupplementListDTO {
        List<IntakeSupplementDTO> supplementList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IntakeSupplementDTO {
        Long supplementId;
        String name;
        String brand;
        Integer duration;
    }

    // 복용중인 영양제 추가 DTO
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddIntakeSupplementResultDTO{
        Long supplementID;
        String supplementName;
        String supplementBrand;
        Integer duration;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteIntakeSupplementResultDTO{
        Long supplementID;
        String supplementName;
        String supplementBrand;
    }

    // 영양제 검색 DTO
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchSupplementListDTO{
        List<PreviewSupplementDTO> supplementList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    // 영양제 간단 정보 (이름, 브랜드, 사진)
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreviewSupplementDTO {
        Long supplementId;
        String brand;
        String name;
        String imageURL;
        Boolean isScrapped;
    }

    // 영양제 상세 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SupplementDetailDTO{
        Long supplementId;
        String brand;
        String name;
        String nutrientInfoImageUrl;
        Boolean isScrapped;
        List<ReviewResponseDTO.ReviewResultDTO> reviewList;
        List<PreviewSupplementDTO> recommendList;
    }

    // 스크랩 추가
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddScrapResultDTO{
        Long supplementId;
        String supplementBrand;
        String supplementName;
        LocalDate scrappedDate;
    }

    // 스크랩 삭제
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteScrapResultDTO{
        Long supplementId;
        String supplementBrand;
        String supplementName;
        LocalDate deletedDate;
    }


    // 섭취중인 영양소
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IntakeNutrientResultDTO {
        String nutrientName;
        BigDecimal recommendedAmount;
        Double nutrientAmount;
        String unit;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IntakeNutrientListDTO{
        List<IntakeNutrientResultDTO> intakeNutrientList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

}
