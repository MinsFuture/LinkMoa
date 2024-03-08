package com.knulinkmoa.domain.directory.controller;

import com.knulinkmoa.auth.principal.PricipalDetails;
import com.knulinkmoa.domain.directory.dto.response.DirectoryReadResponse;
import com.knulinkmoa.domain.directory.service.DirectoryService;
import com.knulinkmoa.global.util.ApiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final DirectoryService directoryService;

    @GetMapping
    public ResponseEntity<ApiUtil.ApiSuccessResult<List<DirectoryReadResponse>>> main(
            @AuthenticationPrincipal PricipalDetails pricipalDetails
            ) {

        List<DirectoryReadResponse> readResponseList = directoryService.readAllDirectory(pricipalDetails.getMember());

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, readResponseList));
    }
}
