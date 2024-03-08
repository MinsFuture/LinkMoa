package com.knulinkmoa.auth.principal;

import com.knulinkmoa.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
public class PricipalDetails implements OAuth2User, UserDetails {

    private Member member;
    private Map<String, Object> attributes;

    /**
     * 자체 로그인
     */
    public PricipalDetails(Member member) {
        this.member = member;
    }

    /**
     * OAuth2 로그인
     */
    public PricipalDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    /**
     * 자체 로그인, OAuth2 로그인 공통
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return String.valueOf(member.getRole());
            }
        });

        return authorities;
    }

    // UserDetails //
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // OAuth2User //
    @Override
    public String getName() {
        return member.getName();
    }
    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public String getEmail() {
        return member.getEmail();
    }
}
