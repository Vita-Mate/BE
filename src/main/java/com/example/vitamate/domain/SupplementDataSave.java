package com.example.vitamate.domain;

import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@XmlRootElement(name = "C003")
@XmlType(propOrder = {"totalCount", "result", "row"})

public class SupplementDataSave {
    @XmlElement(name = "total_count")
    private String totalCount;

    @XmlElement(name="RESULT")
    public Result result;

    @XmlElement(name="row")
    private List<Row> row;

    @Getter
    @Setter
    @XmlType(name = "", propOrder = {
            "LCNS_NO", "BSSH_NM", "PRDLST_REPORT_NO", "PRDLST_NM",
            "PRMS_DT", "POG_DAYCNT", "DISPOS", "NTK_MTHD", "PRIMARY_FNCLTY",
            "IFTKN_ATNT_MATR_CN", "CSTDY_MTHD", "SHAP", "STDR_STND",
            "RAWMTRL_NM", "CRET_DTM", "LAST_UPDT_DTM", "PRDT_SHAP_CD_NM"
    })
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Row{
        private String LCNS_NO; // 인허가번호
        private String BSSH_NM; // 업소명
        private String PRDLST_REPORT_NO; // 품목제조번호
        private String PRDLST_NM; // 품목명
        private String PRMS_DT; // 보고일자
        private String POG_DAYCNT; // 소비기한
        private String DISPOS; // 성상
        private String NTK_MTHD; // 섭취방법
        private String PRIMARY_FNCLTY; // 주된기능성
        private String IFTKN_ATNT_MATR_CN; // 섭취시주의사항
        private String CSTDY_MTHD; // 보관방법
        private String SHAP; // 형태
        private String STDR_STND; // 기준규격
        private String RAWMTRL_NM; // 원재료
        private String CRET_DTM; // 최초생성일시
        private String LAST_UPDT_DTM; // 최종수정일시
        private String PRDT_SHAP_CD_NM; // 제품형태
    }

    @Getter
    @Setter
    @XmlType(name = "", propOrder = {"CODE", "MSG"})
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class Result{
        private String CODE;
        private String MSG;
    }
}
