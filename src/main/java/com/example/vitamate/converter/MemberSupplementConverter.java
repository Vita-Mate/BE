package com.example.vitamate.converter;

import com.example.vitamate.domain.mapping.MemberSupplement;
import com.example.vitamate.web.dto.SupplementResponseDTO;
import org.springframework.data.domain.Page;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MemberSupplementConverter {

    // MemberSupplement를 TakingSupplementDTO 로 변환
    public static SupplementResponseDTO.TakingSupplementDTO toTakingSupplementDTO(MemberSupplement memberSupplement) {
        return SupplementResponseDTO.TakingSupplementDTO.builder()
                .brand(memberSupplement.getSupplement().getBrand())
                .name(memberSupplement.getSupplement().getName())
                .duration((int)DAYS.between(memberSupplement.getStartDate(), LocalDate.now()))
                .build();
    }

    // Page<MemberSupplement>를 TakingSupplementListDTO 로 변환
    public static SupplementResponseDTO.TakingSupplementListDTO toTakingSupplementListDTO(Page<MemberSupplement> memberSupplementPage){
        List<SupplementResponseDTO.TakingSupplementDTO> takingSupplementDTOList = memberSupplementPage.stream()
                .map(MemberSupplementConverter::toTakingSupplementDTO).collect(Collectors.toList());

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