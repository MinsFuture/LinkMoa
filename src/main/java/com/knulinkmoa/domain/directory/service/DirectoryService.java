package com.knulinkmoa.domain.directory.service;

import com.knulinkmoa.domain.directory.dto.request.DirectorySaveRequest;
import com.knulinkmoa.domain.directory.dto.response.DirectoryReadResponse;
import com.knulinkmoa.domain.directory.entity.Directory;
import com.knulinkmoa.domain.directory.exception.DirectoryErrorCode;
import com.knulinkmoa.domain.directory.repository.DirectoryRepository;
import com.knulinkmoa.domain.member.entity.Member;
import com.knulinkmoa.domain.site.dto.response.SiteReadResponse;
import com.knulinkmoa.domain.site.entity.Site;
import com.knulinkmoa.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DirectoryService {

    private final DirectoryRepository directoryRepository;

    /**
     * CREATE
     */
    @Transactional
    public Long saveDirectory(DirectorySaveRequest request, Member member, Long parentId) {
        Directory directory = Directory.builder()
                .directoryName(request.directoryName())
                .member(member)
                .build();

        if (parentId != 0L) {
            Directory parentDirectory = directoryRepository.findById(parentId)
                    .orElseThrow(() -> new GlobalException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

            parentDirectory.addChildDirectory(directory);
        }
        directoryRepository.save(directory);

        return directory.getId();
    }

    /**
     * READ
     */
    public DirectoryReadResponse readDirectory(Long id) {
        Directory directory = directoryRepository.findById(id)
                .orElseThrow(() -> new GlobalException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        return DirectoryReadResponse.from(directory);
    }


    public List<DirectoryReadResponse> readAllDirectory(Member member) {
        List<Directory> allRootDirectories =
                directoryRepository.findDirectoriesWithNullParentByMemberId(member.getId());

        return allRootDirectories.stream()
                .map((directory) -> DirectoryReadResponse.from(directory))
                .toList();
    }

    public List<SiteReadResponse> readAllSites(Long id){
        Directory directory = directoryRepository.findById(id)
                .orElseThrow(() -> new GlobalException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        List<Site> siteList = directory.getSiteList();
        return SiteReadResponse.makeFrom(siteList);
    }

    /**
     * UPDATE
     */
    @Transactional
    public Long updateDirectory(DirectorySaveRequest request, Long id) {
        Directory directory = directoryRepository.findById(id)
                .orElseThrow(() -> new GlobalException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        directory.update(request);
        directoryRepository.save(directory);

        return directory.getId();
    }

    /**
     * DELETE
     */
    @Transactional
    public void deleteDirectory(Long id) {
        Directory directory = directoryRepository.findById(id)
                .orElseThrow(() -> new GlobalException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        directoryRepository.delete(directory);
    }
}
