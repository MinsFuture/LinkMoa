package com.knulinkmoa.auth.oauth2.dto.request;

import com.knulinkmoa.domain.member.entity.Member;
import com.knulinkmoa.domain.member.entity.Role;
import lombok.Builder;

import java.util.Map;

@Builder
public record OAuth2DTO(Map<String, Object> attributes, String name, String email, String role) {

    public Member oAuth2DtoToMember(OAuth2DTO oAuth2DTO) {
        return Member.builder()
                .name(oAuth2DTO.name())
                .email(oAuth2DTO.email())
                .role(Role.valueOf(oAuth2DTO.role()))
                .build();
    }
}