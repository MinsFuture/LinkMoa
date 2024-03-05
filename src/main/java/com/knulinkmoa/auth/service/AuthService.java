package com.knulinkmoa.auth.service;

import com.knulinkmoa.domain.member.dto.request.MemberSignUpDTO;
import com.knulinkmoa.domain.member.entity.Member;
import com.knulinkmoa.domain.member.reposotiry.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService{

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public Member signUpMember(MemberSignUpDTO memberSignUpDTO) {

        Member member = memberSignUpDTO.toMember(passwordEncoder);
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new RuntimeException("member email이 이미 존재합니다");
        }

        return memberRepository.save(member);
    }
}
