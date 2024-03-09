package com.knulinkmoa.domain.site.controller;


import com.knulinkmoa.global.util.ApiUtil;
import com.knulinkmoa.domain.site.dto.request.SiteIdGetRequest;
import com.knulinkmoa.domain.site.dto.request.SiteSaveRequest;
import com.knulinkmoa.domain.site.dto.request.SiteUpdateRequest;
import com.knulinkmoa.domain.site.dto.response.SiteReadResponse;
import com.knulinkmoa.domain.site.service.SiteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@RestController
@RequestMapping("/dir/{directoryId}")
@RequiredArgsConstructor
@Tag(name = "사이트", description = "사이트 관련 Api")

public class SiteController {

    private final SiteService siteService;

    /**
     * 사이트 추가(CREATE)
     * @param request 사이트 추가 DTO
     * @param directoyId 디렉토리 ID
     * @return 추가한 데이터 PK값
     */
    @PostMapping("/sites")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> save(
            @RequestBody SiteSaveRequest request,
            @PathVariable("directoryId") Long directoyId
            )
    {
        Long saveSiteId=siteService.saveSite(request, directoyId);

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.CREATED, saveSiteId));
    }

    /**
     * 사이트 정보 조회 (READ)
     * @param request 사이트ID 조회 DTO
     * @return 사이트 정보
     */
    @GetMapping("/sites/{siteId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<SiteReadResponse>>read
        (@RequestBody SiteIdGetRequest request)
    {
        SiteReadResponse response=siteService.readSite(request);

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, response));
    }


    /**
     * 사이트 정보 수정 (UPDATE)
     * @param request 사이트 수정 DTO
     * @return 수정한 데이터 PK값
     */
    @PutMapping("/sites/{siteId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> update(
            @RequestBody SiteUpdateRequest request,
            @PathVariable("directoryId") Long directoyId
            )
    {
        Long newSiteid = siteService.updateSite(request,directoyId);

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.CREATED, newSiteid));
    }

    /**
     * DELETE
     * @param request 사이트ID 조회 DTO
     * @return
     */

    @DeleteMapping("/sites/{siteId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<?>> delete(
            @RequestBody SiteIdGetRequest request
            )
    {
        siteService.deleteSite(request);
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK));
    }
}
