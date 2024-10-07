package com.example.vitamate.converter;

import com.example.vitamate.domain.NutrientRecommendations;
import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.mapping.MemberSupplement;
import com.example.vitamate.web.dto.SupplementResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
                .imageURL(supplement.getNutrientsImageUrl())
                .build();
    }

    public static SupplementResponseDTO.SupplementDetailDTO toSupplementDetailDTO(Supplement supplement, Boolean isScrapped) {
        return SupplementResponseDTO.SupplementDetailDTO.builder()
                .supplementId(supplement.getId())
                .brand(supplement.getBrand())
                .name(supplement.getName())
                .isScrapped(isScrapped)
                .nutrientInfoImageUrl(supplement.getNutrientsImageUrl())
                .build();
    }

    public static SupplementResponseDTO.AddScrapResultDTO toAddScrapResultDTO(MemberSupplement memberSupplement){
        return SupplementResponseDTO.AddScrapResultDTO.builder()
                .supplementId(memberSupplement.getSupplement().getId())
                .supplementName(memberSupplement.getSupplement().getName())
                .supplementBrand(memberSupplement.getSupplement().getBrand())
                .scrappedDate(LocalDate.from(memberSupplement.getCreatedAt()))
                .build();
    }

    public static SupplementResponseDTO.DeleteIntakeSupplementResultDTO toDeleteIntakeSupplement(Supplement supplement){
        return SupplementResponseDTO.DeleteIntakeSupplementResultDTO.builder()
                .supplementID(supplement.getId())
                .supplementBrand(supplement.getBrand())
                .supplementName(supplement.getName())
                .build();
    }

    public static SupplementResponseDTO.DeleteScrapResultDTO toDeleteScrapResultDTO(MemberSupplement memberSupplement){
        return SupplementResponseDTO.DeleteScrapResultDTO.builder()
                .supplementId(memberSupplement.getSupplement().getId())
                .supplementName(memberSupplement.getSupplement().getName())
                .supplementBrand(memberSupplement.getSupplement().getBrand())
                .deletedDate(LocalDate.now())
                .build();
    }

    public static SupplementResponseDTO.IntakeNutrientResultDTO toIntakeNutrientResultDTO(NutrientRecommendations recommended, Double totalAmount){
        return SupplementResponseDTO.IntakeNutrientResultDTO.builder()
            .nutrientAmount(totalAmount)
            .nutrientName(recommended.getNutrient().getName())
            .recommendedAmount(recommended.getRecommendedIntake())
            .unit(recommended.getUnit().getUnitName())
            .build();
    }

    public static SupplementResponseDTO.IntakeNutrientListDTO toIntakeNutrientListDTO(List<SupplementResponseDTO.IntakeNutrientResultDTO> intakeNutrientResultDTOList, int totalElements, int totalPage, Integer page){
        return SupplementResponseDTO.IntakeNutrientListDTO.builder()
            .intakeNutrientList(intakeNutrientResultDTOList)
            .listSize(intakeNutrientResultDTOList.size())
            .totalPage(totalPage)
            .totalElements((long)totalElements)
            .isFirst(page == 0)
            .isLast(page + 1 == totalPage)
            .build();
    }
}
