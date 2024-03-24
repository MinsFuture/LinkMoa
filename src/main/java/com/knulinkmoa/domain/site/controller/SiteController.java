package com.knulinkmoa.domain.site.controller;


import com.knulinkmoa.global.util.ApiUtil;
import com.knulinkmoa.domain.site.dto.request.SiteSaveRequest;
import com.knulinkmoa.domain.site.dto.request.SiteUpdateRequest;
import com.knulinkmoa.domain.site.dto.response.SiteReadResponse;
import com.knulinkmoa.domain.site.service.SiteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sites")
@RequiredArgsConstructor
@Tag(name = "사이트", description = "사이트 관련 Api")

public class SiteController {

    private final SiteService siteService;

    /**
     * 사이트 추가(CREATE)
     *
     * @param request     사이트 추가 DTO
     * @param directoryId 디렉토리 ID
     * @return 추가한 데이터 PK값
     */
    @PostMapping("/{directoryId}")
    @Operation(summary = "디렉토리에 사이트 하나 추가", description = "사이트 하나를 추가하는 로직")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> save(
            @RequestBody SiteSaveRequest request,
            @PathVariable("directoryId") Long directoryId
    ) {
        Long saveSiteId = siteService.saveSite(request, directoryId);

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.CREATED, saveSiteId));
    }

    /**
     * 사이트 정보 조회 (READ)
     *
     * @param siteId 사이트 id pk 값
     * @return 사이트 정보
     */
    @GetMapping()
    @Operation(summary = "사이트 하나 정보 조회", description = "사이트 하나의 정보를 조회")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<SiteReadResponse>> read(
            @RequestParam(name = "siteId") Long siteId
    ) {
        SiteReadResponse response = siteService.readSite(siteId);

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, response));
    }


    /**
     * 사이트 정보 수정 (UPDATE)
     *
     * @param request 사이트 수정 DTO
     * @return 수정한 데이터 PK값
     */
    @PutMapping("/{siteId}")
    @Operation(summary = "사이트 정보 수정", description = "사이트 정보를 수정")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> update(
            @RequestBody SiteUpdateRequest request,
            @PathVariable("siteId") Long siteId
    ) {
        Long newSiteid = siteService.updateSite(request, siteId);

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.CREATED, newSiteid));
    }

    /**
     * DELETE
     *
     * @param siteId 사이트 id pk 값
     * @return
     */
    @DeleteMapping("/{siteId}")
    @Operation(summary = "사이트 삭제", description = "사이트 삭제")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<?>> delete(
            @PathVariable("siteId") Long siteId
    ) {
        siteService.deleteSite(siteId);
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK));
    }
}
