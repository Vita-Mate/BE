package com.example.vitamate.web.controller;

import com.example.vitamate.domain.Member;
import com.example.vitamate.service.MemberService.MemberCommandService;
import com.example.vitamate.service.MemberSupplementService.MemberSupplementServiceImpl;
import com.example.vitamate.web.dto.SupplementResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/supplements")
public class SupplementRestController {

    private final MemberSupplementServiceImpl memberSupplementService;
    private final MemberCommandService memberCommandService

    @GetMapping("/taking")
    @Operation(summary = "사용자가 복용 중인 영양제 목록 조회 API", description = "사용자가 복용 중인 영양제 목록을 조회하는 API이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호, 0번이 첫 페이지 입니다.")
    })
    public ApiResponse<SupplementResponseDTO.TakingSupplementListDTO> getTakingSupplementList(@RequestHeader(value = "Authorization") String token,
                                                                                              @RequestParam(name = "page") Integer page){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Member member = memberCommandService.findMemberByEmail(auth.getPrinciple().toString());

        memberSupplementService.getTakingSuppplementList(member.getEmail(), page);

        return null;
    }
}
