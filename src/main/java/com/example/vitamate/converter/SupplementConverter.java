package com.example.vitamate.converter;

import com.example.vitamate.domain.Supplement;
import com.example.vitamate.web.dto.SupplementResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SupplementConverter {

    public static SupplementResponseDTO.SearchSupplementListDTO toSearchSupplementListDTO(Page<Supplement> supplementPage) {
        List<SupplementResponseDTO.PreviewSupplementDTO> previewSupplementDTOList = supplementPage.stream()
                .map(SupplementConverter::toSearchSupplementDTO).collect(Collectors.toList());

        return SupplementResponseDTO.SearchSupplementListDTO.builder()
                .isFirst(supplementPage.isFirst())
                .isLast(supplementPage.isLast())
                .totalPage(supplementPage.getTotalPages())
                .listSize(previewSupplementDTOList.size())
                .supplementList(previewSupplementDTOList)
                .build();
    }


    public static SupplementResponseDTO.PreviewSupplementDTO toSearchSupplementDTO(Supplement supplement) {
        return SupplementResponseDTO.PreviewSupplementDTO.builder()
                .supplementId(supplement.getId())
                .brand(supplement.getBrand())
                .name(supplement.getName())
                .build();
    }

    public static SupplementResponseDTO.SupplementDetailDTO toSupplementDetailDTO(Supplement supplement, Boolean isScrapped) {
        return SupplementResponseDTO.SupplementDetailDTO.builder()
                .supplementId(supplement.getId())
                .brand(supplement.getBrand())
                .name(supplement.getName())
                .isScrapped(isScrapped)
                .nutrientInfoImageUrl(supplement.getNutrientsImageUrl())
                //리뷰 기능 구현 후 마저 구현
//                .reviewList()
//                .recommendList()
                .build();
    }
}
