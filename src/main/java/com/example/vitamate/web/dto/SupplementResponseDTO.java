package com.example.vitamate.web.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
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
        List<SearchSupplementDTO> supplementList;
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
    public static class SearchSupplementDTO{
        Long id;
        String brand;
        String name;
        // String imageURL;
    }
}
