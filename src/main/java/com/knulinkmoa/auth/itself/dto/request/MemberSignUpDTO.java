package com.knulinkmoa.auth.itself.dto.request;

import com.knulinkmoa.domain.member.entity.Member;
import com.knulinkmoa.domain.member.entity.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

public record MemberSignUpDTO(String name, String email, String password, String phoneNumber) {

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .name(name)
                .email(email)
                .role(Role.ROLE_USER)
                .password(passwordEncoder.encode(password))
                .phoneNumber(phoneNumber)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
