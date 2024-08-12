package com.example.vitamate.web.controller;

import com.example.vitamate.apiPayload.ApiResponse;
import com.example.vitamate.converter.MemberSupplementConverter;
import com.example.vitamate.domain.mapping.MemberSupplement;
import com.example.vitamate.jwt.SecurityUtil;
import com.example.vitamate.service.SupplementService.SupplementCommandService;
import com.example.vitamate.service.SupplementService.SupplementQueryServiceImpl;
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

        Page<MemberSupplement> supplementPageDTO = supplementQueryService.getTakingSupplementPage(SecurityUtil.getCurrentUsername(), page);

        return ApiResponse.onSuccess(MemberSupplementConverter.toTakingSupplementListDTO(supplementPageDTO));
    }

    @PostMapping("/taking")
    @Operation(summary = "사용자가 복용할 영양제 추가 API", description = "사용자가 복용할 영양제를 추가하는 API입니다.")
    public ApiResponse<SupplementResponseDTO.AddIntakeSupplementResultDTO> addIntakeSupplement(@RequestBody SupplementRequestDTO.AddIntakeSupplementDTO requestDTO){
        SupplementResponseDTO.AddIntakeSupplementResultDTO resultDTO = supplementCommandService.addIntakeSupplement(SecurityUtil.getCurrentUsername(), requestDTO);
        return ApiResponse.onSuccess(resultDTO);
    }
}
