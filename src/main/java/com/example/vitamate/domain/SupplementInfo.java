package com.example.vitamate.domain;

import com.example.vitamate.domain.mapping.NutrientInfo;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;


@Entity
@Getter
@lombok.Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SupplementInfo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private String LCNS_NO; // 인허가번호
    private String BSSH_NM; // 업소명
//    private String PRDLST_REPORT_NO; // 품목제조번호
    private String PRDLST_NM; // 품목명
//    private String PRMS_DT; // 보고일자
//    private String POG_DAYCNT; // 소비기한
//    private String DISPOS; // 성상
    private String NTK_MTHD; // 섭취방법
    private String PRIMARY_FNCLTY; // 주된기능성
//    private String IFTKN_ATNT_MATR_CN; // 섭취시주의사항
//    private String CSTDY_MTHD; // 보관방법
//    private String SHAP; // 형태
    private String STDR_STND; // 기준규격
//    private String RAWMTRL_NM; // 원재료
//    private String CRET_DTM; // 최초생성일시
//    private String LAST_UPDT_DTM; // 최종수정일시
//    private String PRDT_SHAP_CD_NM; // 제품형태

    @OneToMany(mappedBy = "supplementInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NutrientInfo> nutrients;

}