package com.knulinkmoa.auth.controller;

import com.knulinkmoa.auth.service.CustomOAuth2User;
import com.knulinkmoa.auth.service.AuthService;
import com.knulinkmoa.auth.service.LoginService;
import com.knulinkmoa.domain.member.dto.request.MemberLoginDTO;
import com.knulinkmoa.domain.member.dto.request.MemberSignUpDTO;
import com.knulinkmoa.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuth2LoginController {

    private final AuthService authService;
    @PostMapping("/sign-up")
    public ResponseEntity<String> signup(
            @RequestBody MemberSignUpDTO memberSignUpDTO) {

        Member member = authService.signUpMember(memberSignUpDTO);

        return ResponseEntity.ok("회원가입 성공");
    }
    @GetMapping("/main")
    public ResponseEntity<String> mainPage(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
            ) {

        return ResponseEntity.ok().body("main page");
    }

}
