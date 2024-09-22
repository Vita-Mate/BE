package com.example.vitamate.web.dto;

import lombok.*;

import java.time.LocalDate;
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

//    @Getter
//    @Setter
//    @XmlRootElement
//    public static class SupplementInfoResultDTO{
//        private Long id;
//        private String brand;  // 업소명 BSSH_NM
//        private String name; // 품목명 PRDLST_NM
//        private String intakeMethod; // 섭취방법 NTK_MTHD
//        private String mainFunction; // 주된기능성 PRIMARY_FNCLTY
//        private String standardSpecification; // 기준규격 STDR_STND
//    }

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
    }

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
}
