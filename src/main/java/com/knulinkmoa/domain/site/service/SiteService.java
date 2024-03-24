package com.knulinkmoa.domain.site.service;


import com.knulinkmoa.domain.directory.entity.Directory;
import com.knulinkmoa.domain.directory.exception.DirectoryErrorCode;
import com.knulinkmoa.domain.directory.repository.DirectoryRepository;
import com.knulinkmoa.global.exception.GlobalException;
import com.knulinkmoa.domain.site.dto.request.SiteSaveRequest;
import com.knulinkmoa.domain.site.dto.request.SiteUpdateRequest;
import com.knulinkmoa.domain.site.dto.response.SiteReadResponse;
import com.knulinkmoa.domain.site.entity.Site;
import com.knulinkmoa.domain.site.exception.SiteErrorCode;
import com.knulinkmoa.domain.site.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SiteService {

    private final SiteRepository siteRepository;
    private final DirectoryRepository directoryRepository;

    /**
     * CREATE
     */

    @Transactional
    public Long saveSite(SiteSaveRequest request, Long directoryId) {
        Directory findDirectory = directoryRepository.findById(directoryId)
                .orElseThrow(() -> new GlobalException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        Site site = Site.builder()
                .siteName(request.name())
                .url(request.url())
                .directory(findDirectory)
                .build();

        siteRepository.save(site);
        return site.getId();
    }

    /**
     * READ
     */
    public SiteReadResponse readSite(Long siteId) {
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new GlobalException(SiteErrorCode.SITE_NOT_FOUND));

        return SiteReadResponse.builder()
                .siteName(site.getSiteName())
                .siteUrl(site.getUrl())
                .build();
    }


    //read 함수를 실행하기 위해서는 siteId가 필요한데.
    //siteId를 어떻게 가져올것인가 ? =>read DTO를 따로 만들어야 되는건가?

    /**
     * UPDATE
     */

    @Transactional
    public Long updateSite(SiteUpdateRequest request, Long siteId) {
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new GlobalException(SiteErrorCode.SITE_NOT_FOUND));

        site.update(request);

        siteRepository.save(site);
        return site.getId();
    }

    /**
     * DELETE
     */
    @Transactional
    public void deleteSite(Long siteId) {
        Site deleteSite = siteRepository.findById(siteId)
                .orElseThrow(() -> new GlobalException(SiteErrorCode.SITE_NOT_FOUND));

        siteRepository.delete(deleteSite);
    }
}
