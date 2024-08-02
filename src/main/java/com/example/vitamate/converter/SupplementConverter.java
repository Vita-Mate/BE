package com.example.vitamate.converter;

import com.example.vitamate.domain.mapping.MemberSupplement;
import com.example.vitamate.web.dto.SupplementResponseDTO;
import org.springframework.data.domain.Page;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class SupplementConverter {
    public static SupplementResponseDTO.TakingSupplementDTO takingSupplementDTO(MemberSupplement memberSupplement) {
        return SupplementResponseDTO.TakingSupplementDTO.builder()
                .brand(memberSupplement.getSupplement().getBrand())
                .name(memberSupplement.getSupplement().getName())
                .duration((int)DAYS.between(memberSupplement.getStartDate(), LocalDate.now()))
                .build();
    }

    public static SupplementResponseDTO.TakingSupplementListDTO takingSupplementListDTO(Page<MemberSupplement> memberSupplementPage){
        List<SupplementResponseDTO.TakingSupplementDTO> takingSupplementDTOList = memberSupplementPage.stream()
                .map(SupplementConverter::takingSupplementDTO).collect(Collectors.toList());

        return SupplementResponseDTO.TakingSupplementListDTO.builder()
                .isFirst(memberSupplementPage.isFirst())
                .isLast(memberSupplementPage.isLast())
                .totalPage(memberSupplementPage.getTotalPages())
                .totalElements(memberSupplementPage.getTotalElements())
                .listSize(takingSupplementDTOList.size())
                .supplementList(takingSupplementDTOList)
                .build();
    }


}