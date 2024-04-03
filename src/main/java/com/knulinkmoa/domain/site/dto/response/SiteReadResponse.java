package com.knulinkmoa.domain.site.dto.response;

import com.knulinkmoa.domain.site.entity.Site;
import lombok.Builder;

import java.util.List;

@Builder
public record SiteReadResponse(String siteName, String siteUrl) {
    public static List<SiteReadResponse> makeFrom(List<Site> siteList){
        return siteList.stream()
                .map(site -> SiteReadResponse
                        .builder()
                        .siteName(site.getSiteName())
                        .siteUrl(site.getUrl())
                        .build())
                .toList();
    }
}
