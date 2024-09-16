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
        List<SupplementResponseDTO.SearchSupplementDTO> searchSupplementDTOList = supplementPage.stream()
                .map(SupplementConverter::toSearchSupplementDTO).collect(Collectors.toList());

        return SupplementResponseDTO.SearchSupplementListDTO.builder()
                .isFirst(supplementPage.isFirst())
                .isLast(supplementPage.isLast())
                .totalPage(supplementPage.getTotalPages())
                .listSize(searchSupplementDTOList.size())
                .supplementList(searchSupplementDTOList)
                .build();
    }


    public static SupplementResponseDTO.SearchSupplementDTO toSearchSupplementDTO(Supplement supplement) {
        return SupplementResponseDTO.SearchSupplementDTO.builder()
                .id(supplement.getId())
                .brand(supplement.getBrand())
                .name(supplement.getName())
                .build();
    }
}
