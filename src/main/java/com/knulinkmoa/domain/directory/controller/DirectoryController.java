package com.knulinkmoa.domain.directory.controller;


import com.knulinkmoa.auth.principal.PricipalDetails;
import com.knulinkmoa.domain.directory.dto.request.DirectorySaveRequest;
import com.knulinkmoa.domain.directory.dto.response.DirectoryReadResponse;
import com.knulinkmoa.domain.directory.service.DirectoryService;
import com.knulinkmoa.domain.member.reposotiry.MemberRepository;
import com.knulinkmoa.domain.site.dto.response.SiteReadResponse;
import com.knulinkmoa.domain.site.entity.Site;
import com.knulinkmoa.global.util.ApiUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dir")
@RequiredArgsConstructor
@Tag(name = "디렉토리", description = "디렉토리 관련 Api")
@Slf4j
public class DirectoryController {

    private final DirectoryService directoryService;

    /**
     * DIRECTORY 추가
     *
     * @param request DIRECTORY 정보
     * @param pricipalDetails 어떤 member 인지
     * @param parentId 추가할 디렉토리의 부모 디렉토리 정보
     *
     * @return 저장한 DIRECTORY의 PK 값
     */
    @PostMapping("/{directoryId}")
    @Operation(summary = "디렉토리 추가", description = "디렉토리를 추가하는 로직, 단 Root 디렉토리의 경우에는 parentId 값이 null")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> saveSubDirectory(
            @RequestBody DirectorySaveRequest request,
            @AuthenticationPrincipal PricipalDetails pricipalDetails,
            @PathVariable("directoryId") Long parentId)
    {
        Long saveId = directoryService.saveDirectory(request, pricipalDetails.getMember(), parentId);
        log.info("디렉토리 추가");
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.CREATED, saveId));
    }

    /**
     * DIRECTORY 하나 조회
     *
     * @param id 조회할 DIRECTORY의 PK 값
     * @return 한 디렉토리의 디렉토리 정보
     */
    @GetMapping()
    @Operation(summary = "디렉토리 하나 조회", description = "디렉토리 하나를 조회하는 로직")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<DirectoryReadResponse>> readDirectory(
            @RequestParam(name = "directoryId") Long id
    ) {
        DirectoryReadResponse response = directoryService.readDirectory(id);

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, response));
    }

    /**
     * 모든 루트 디렉토리 조회, 메인 페이지 용도
     *
     * @param pricipalDetails 어떤 member 인지
     *
     * @return 모든 루트 디렉토리의 list
     */
    @GetMapping("/me")
    @Operation(summary = "모든 디렉토리 조회", description = "사용자의 모든 디렉토리를 조회, 메인 페이지 용도")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<List<DirectoryReadResponse>>> readAllRootDirectory(
            @AuthenticationPrincipal PricipalDetails pricipalDetails
    ) {
        List<DirectoryReadResponse> readResponseList = directoryService.readAllDirectory(pricipalDetails.getMember());

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, readResponseList));
    }

    /**
     * 한 디렉토리의 모든 사이트 조회
     *
     * @param directoryId 조회 할 DIRECTORY의 PK 값
     * @return 모든 site들의 lists
     */
    @GetMapping("/{directoryId}/sites")
    @Operation(summary = "한 디렉토리 모든 사이트 조회", description = "한 디렉토리에 저장 된 모든 사이트를 조회한다")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<List<SiteReadResponse>>> getAllSites(
            @PathVariable("directoryId") Long directoryId
    ){
        List<SiteReadResponse> sites = directoryService.readAllSites(directoryId);

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, sites));
    }

    /**
     * DIRECTORY 수정
     *
     * @param request 수정할 DIRECTORY 정보
     * @param id 수정할 DIRECTORY의 PK 값
     *
     * @return 수정한 DIRECTORY의 PK 값
     */
    @PutMapping("/{directoryId}")
    @Operation(summary = "디렉토리 수정", description = "디렉토리의 이름 수정")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> updateDirectory(
            @RequestBody DirectorySaveRequest request,
            @PathVariable("directoryId") Long id) {

        Long updateId = directoryService.updateDirectory(request, id);

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, updateId));
    }

    /**
     * DIRECTORY 삭제
     *
     * @param id 삭제할 DIRECTORY의 PK 값
     * @return
     */
    @DeleteMapping("/{directoryId}")
    @Operation(summary = "디렉토리 삭제", description = "하위의 디렉토리도 모두 삭제 됨")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<?>> deleteDirectory(
            @PathVariable("directoryId") Long id)
    {
        directoryService.deleteDirectory(id);

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK));
    }
}
