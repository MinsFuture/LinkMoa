package com.knulinkmoa.domain.directory.dto.response;

import com.knulinkmoa.domain.directory.entity.Directory;
import lombok.Builder;

@Builder
public record DirectoryReadResponse(Long id, String name) {
    public static DirectoryReadResponse from(Directory directory) {
        return DirectoryReadResponse.builder()
                .id(directory.getId())
                .name(directory.getDirectoryName())
                .build();
    }
}
