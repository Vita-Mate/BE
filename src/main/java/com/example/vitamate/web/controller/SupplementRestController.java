package com.example.vitamate.web.controller;

import com.example.vitamate.apiPayload.ApiResponse;
import com.example.vitamate.converter.MemberSupplementConverter;
import com.example.vitamate.converter.SupplementConverter;
import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.mapping.MemberSupplement;
import com.example.vitamate.jwt.SecurityUtil;
import com.example.vitamate.service.SupplementService.SupplementCommandService;
import com.example.vitamate.service.SupplementService.SupplementQueryServiceImpl;
import com.example.vitamate.web.dto.ReviewRequestDTO;
import com.example.vitamate.web.dto.ReviewResponseDTO;
import com.example.vitamate.web.dto.SupplementRequestDTO;
import com.example.vitamate.web.dto.SupplementResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/supplements")
public class SupplementRestController {

    private final SupplementQueryServiceImpl supplementQueryService;
    private final SupplementCommandService supplementCommandService;

    @GetMapping("/taking")
    @Operation(summary = "사용자가 복용 중인 영양제 목록 조회 API", description = "사용자가 복용 중인 영양제 목록을 조회하는 API이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호, 0번이 첫 페이지 입니다.")
    })
    public ApiResponse<SupplementResponseDTO.IntakeSupplementListDTO> getTakingSupplementList(@RequestParam(name = "page") Integer page){

        Page<MemberSupplement> memberSupplementPage = supplementQueryService.getTakingSupplementPage(SecurityUtil.getCurrentUsername(), page);

        return ApiResponse.onSuccess(MemberSupplementConverter.toTakingSupplementListDTO(memberSupplementPage));
    }

    @PostMapping("/{supplementId}/taking")
    @Operation(summary = "사용자가 복용할 영양제 추가 API", description = "사용자가 복용할 영양제를 추가하는 API입니다.")
    public ApiResponse<SupplementResponseDTO.AddIntakeSupplementResultDTO> addIntakeSupplement(@PathVariable(name="supplementId") Long supplementId,
                                                                                               @RequestBody SupplementRequestDTO.AddIntakeSupplementDTO requestDTO){
        SupplementResponseDTO.AddIntakeSupplementResultDTO resultDTO = supplementCommandService.addIntakeSupplement(SecurityUtil.getCurrentUsername(), supplementId, requestDTO);
        return ApiResponse.onSuccess(resultDTO);
    }

    @GetMapping("/search")
    @Operation(summary = "영양제 검색 API", description = "영양제 이름을 검색하는 API 이며, 페이징을 포함합니다. query string으로 page 번호, 검색 방식, 검색어를 주세요")
    @Parameters({
            @Parameter(name = "searchType", description = "검색 타입 (name: 이름 검색, nutrient: 성분 검색)"),
            @Parameter(name = "keyword", description = "검색할 이름 또는 성분 (일부 검색도 가능합니다.)"),
            @Parameter(name = "page", description = "페이지 번호, 0번이 첫 페이지 입니다."),
            @Parameter(name = "pageSize", description = "한 페이지당 항목 개수입니다.")
    })
    public ApiResponse<SupplementResponseDTO.SearchSupplementListDTO> getSupplementList(@RequestParam(name = "searchType") String searchType,
                                                                                        @RequestParam(name = "keyword") String keyword,
                                                                                        @RequestParam(name = "page") Integer page,
                                                                                        @RequestParam(name = "pageSize") Integer pageSize){
        Page<Supplement> supplementPage;

        if("name".equals(searchType)){
            //키워드, 페이지 번호로 조회한 페이지
            supplementPage = supplementQueryService.getSupplementsByName(keyword, page, pageSize);
        }else if("nutrient".equals(searchType)){
            //키워드, 페이지 번호로 조회한 페이지
            supplementPage = supplementQueryService.getSupplementsByNutrient(keyword, page, pageSize);
        } else{
            throw new IllegalArgumentException("Invalid search Type");
        }

        return ApiResponse.onSuccess(SupplementConverter.toSearchSupplementListDTO(supplementPage));
    }

    @GetMapping("/{supplementId}")
    @Operation(summary = "영양제 상세 정보 조회 API", description = "영양제 id로 상세 정보를 조회하는 API 입니다. 스크랩 여부도 반환합니다.")
    public ApiResponse<SupplementResponseDTO.SupplementDetailDTO> getSupplementDetail(@PathVariable(name="supplementId") Long supplementId){

        return ApiResponse.onSuccess(supplementQueryService.getSupplementDetail(SecurityUtil.getCurrentUsername(), supplementId));
    }

    @PostMapping("/{supplementId}/scrap")
    @Operation(summary = "스크랩 추가 API", description = "영양제 id로 특정 영양제를 스크랩 추가하는 API 입니다.")
    public ApiResponse<SupplementResponseDTO.AddScrapResultDTO> addScrap(@PathVariable(name = "supplementId") Long supplementId){
        return ApiResponse.onSuccess(supplementCommandService.addScrap(SecurityUtil.getCurrentUsername(), supplementId));
    }

    @PatchMapping("/{supplementId}/scrap")
    @Operation(summary = "스크랩 취소 API", description = "영양제 스크랩 해제 API 입니다.")
    public ApiResponse<SupplementResponseDTO.DeleteScrapResultDTO> deleteScrap(@PathVariable(name = "supplementId") Long supplementId){
        return ApiResponse.onSuccess(supplementCommandService.deleteScrap(SecurityUtil.getCurrentUsername(), supplementId));
    }

    @PostMapping("/{supplementId}/review")
    @Operation(summary = "리뷰 작성 API", description = "특정 영양제 리뷰 작성 API 입니다. (현재까지는 복용 여부 상관 없이 모두 작성 가능). 별점은 1~5 사이 값 입력 가능합니다.")
    public ApiResponse<ReviewResponseDTO.ReviewResultDTO> addReview(@PathVariable(name = "supplementId") Long supplementId,
                                                                    @RequestBody ReviewRequestDTO.AddReviewDTO requestDTO){
        return ApiResponse.onSuccess(supplementCommandService.addReview(SecurityUtil.getCurrentUsername(), supplementId, requestDTO));
    }

    @GetMapping("/{supplementId}/review")
    @Operation(summary = "리뷰 조회 API", description = "특정 영양제 리뷰 조회 API 입니다. 페이징을 포함합니다. query string으로 페이지 번호, 페이지 크기를 주세요.")
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호, 0번이 첫 페이지 입니다."),
            @Parameter(name = "pageSize", description = "한 페이지당 항목 개수입니다.")})
    public ApiResponse<ReviewResponseDTO.ReviewListDTO> getReviewList(@PathVariable(name = "supplementId") Long supplementId,
                                                                      @RequestParam(name = "page") Integer page,
                                                                      @RequestParam(name = "pageSize") Integer pageSize){

        return ApiResponse.onSuccess(supplementQueryService.getReviewList(supplementId, page, pageSize));
    }
}
