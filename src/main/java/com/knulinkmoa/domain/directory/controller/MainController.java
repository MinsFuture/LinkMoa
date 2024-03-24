package com.knulinkmoa.domain.directory.controller;

import com.knulinkmoa.auth.principal.PricipalDetails;
import com.knulinkmoa.domain.directory.dto.response.DirectoryReadResponse;
import com.knulinkmoa.domain.directory.service.DirectoryService;
import com.knulinkmoa.global.util.ApiUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "메인화면", description = "메인화면 관련 Api")
public class MainController {

    private final DirectoryService directoryService;

    @GetMapping
    @Operation(summary = "메인화면", description = "모든 root 디렉토리의 id 값을 리턴 해줌")
    public ResponseEntity<ApiUtil.ApiSuccessResult<List<DirectoryReadResponse>>> main(
            @AuthenticationPrincipal PricipalDetails pricipalDetails
            ) {

        List<DirectoryReadResponse> readResponseList = directoryService.readAllDirectory(pricipalDetails.getMember());

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, readResponseList));
    }
}
