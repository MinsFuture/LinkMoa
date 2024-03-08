package com.knulinkmoa.domain.directory.controller;


import com.knulinkmoa.auth.principal.PricipalDetails;
import com.knulinkmoa.domain.directory.dto.request.DirectorySaveRequest;
import com.knulinkmoa.domain.directory.dto.response.DirectoryReadResponse;
import com.knulinkmoa.domain.directory.service.DirectoryService;
import com.knulinkmoa.domain.member.reposotiry.MemberRepository;
import com.knulinkmoa.global.util.ApiUtil;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dir")
@RequiredArgsConstructor
public class DirectoryController {

    private final DirectoryService directoryService;
    private final MemberRepository memberRepository;


    /**
     * ROOT 디렉토리 추가
     *
     * @param request Directory 이름
     * @param pricipalDetails 어떤 member 인지
     *
     * @return 저장한 directory의 pk 값
     */
    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> saveRootDirectory(
            @RequestBody DirectorySaveRequest request,
            @AuthenticationPrincipal PricipalDetails pricipalDetails
    ) {
        Long saveId = directoryService.saveDirectory(request, pricipalDetails.getMember(), null);

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.CREATED, saveId));
    }


    /**
     * SUB DIRECTORY 추가
     *
     * @param request DIRECTORY 정보
     * @param pricipalDetails 어떤 member 인지
     * @param parentId 추가할 디렉토리의 부모 디렉토리 정보
     *
     * @return 저장한 DIRECTORY의 PK 값
     */
    @PostMapping("/{directoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> saveSubDirectory(
            @RequestBody DirectorySaveRequest request,
            @AuthenticationPrincipal PricipalDetails pricipalDetails,
            @PathVariable(name = "directoryId") Long parentId)
    {

        Long saveId = directoryService.saveDirectory(request, pricipalDetails.getMember(), parentId);
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.CREATED, saveId));
    }

    /**
     * DIRECTORY 하나 조회
     *
     * @param id 조회할 DIRECTORY의 PK 값
     * @return 한 디렉토리의 디렉토리 정보
     */
    @GetMapping("/{directoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<DirectoryReadResponse>> readDirectory(
            @PathVariable("directoryId") Long id
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
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<List<DirectoryReadResponse>>> readAllRootDirectory(
            @AuthenticationPrincipal PricipalDetails pricipalDetails
    ) {
        List<DirectoryReadResponse> readResponseList = directoryService.readAllDirectory(pricipalDetails.getMember());

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, readResponseList));
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiUtil.ApiSuccessResult<?>> deleteDirectory(
            @PathVariable("directoryId") Long id
    ) {
        directoryService.deleteDirectory(id);

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK));
    }
}
