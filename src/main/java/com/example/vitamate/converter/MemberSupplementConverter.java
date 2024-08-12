package com.example.vitamate.converter;

import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.mapping.MemberSupplement;
import com.example.vitamate.web.dto.SupplementResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemberSupplementConverter {

    // MemberSupplement를 TakingSupplementDTO 로 변환
    public static SupplementResponseDTO.IntakeSupplementDTO toTakingSupplementDTO(MemberSupplement memberSupplement) {
        return SupplementResponseDTO.IntakeSupplementDTO.builder()
                .brand(memberSupplement.getSupplement().getBrand())
                .name(memberSupplement.getSupplement().getName())
                .duration((int)DAYS.between(memberSupplement.getStartDate(), LocalDate.now()))
                .build();
    }

    // Page<MemberSupplement>를 TakingSupplementListDTO 로 변환
    public static SupplementResponseDTO.IntakeSupplementListDTO toTakingSupplementListDTO(Page<MemberSupplement> memberSupplementPage){
        List<SupplementResponseDTO.IntakeSupplementDTO> intakeSupplementDTOList = memberSupplementPage.stream()
                .map(MemberSupplementConverter::toTakingSupplementDTO).collect(Collectors.toList());

        return SupplementResponseDTO.IntakeSupplementListDTO.builder()
                .isFirst(memberSupplementPage.isFirst())
                .isLast(memberSupplementPage.isLast())
                .totalPage(memberSupplementPage.getTotalPages())
                .totalElements(memberSupplementPage.getTotalElements())
                .listSize(intakeSupplementDTOList.size())
                .supplementList(intakeSupplementDTOList)
                .build();
    }

    public static SupplementResponseDTO.AddIntakeSupplementResultDTO toAddIntakeSupplementResultDTO(MemberSupplement memberSupplement){

        Integer duration = (int) DAYS.between(memberSupplement.getStartDate(), LocalDate.now()) + 1;

        return SupplementResponseDTO.AddIntakeSupplementResultDTO.builder()
                .supplementID(memberSupplement.getSupplement().getId())
                .supplementName(memberSupplement.getSupplement().getName())
                .supplementBrand(memberSupplement.getSupplement().getBrand())
                .duration(duration).build();
    }


}