package com.example.vitamate.web.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class SupplementResponseDTO {

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

    @Getter
    @Setter
    @XmlRootElement
    public static class SupplementInfoResultDTO{
        private Long id;
//        private String LCNS_NO; // 인허가번호
        private String brand;  // 업소명 BSSH_NM
//        private String PRDLST_REPORT_NO; // 품목제조번호
        private String name; // 품목명 PRDLST_NM
//        private String PRMS_DT; // 보고일자
//        private String POG_DAYCNT; // 소비기한
//        private String DISPOS; // 성상
        private String intakeMethod; // 섭취방법 NTK_MTHD
        private String mainFunction; // 주된기능성 PRIMARY_FNCLTY
//        private String IFTKN_ATNT_MATR_CN; // 섭취시주의사항
//        private String CSTDY_MTHD; // 보관방법
//        private String SHAP; // 형태
        private String standardSpecification; // 기준규격 STDR_STND
//        private String RAWMTRL_NM; // 원재료
//        private String CRET_DTM; // 최초생성일시
//        private String LAST_UPDT_DTM; // 최종수정일시
//        private String PRDT_SHAP_CD_NM; // 제품형태

    }
}
