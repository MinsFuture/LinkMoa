package com.knulinkmoa.auth.controller;

import com.knulinkmoa.auth.itself.service.LoginService;
import com.knulinkmoa.auth.principal.PricipalDetails;
import com.knulinkmoa.auth.itself.dto.request.MemberSignUpDTO;
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
public class LoginController {

    private final LoginService loginService;
    @PostMapping("/sign-up")
    public ResponseEntity<String> signup(
            @RequestBody MemberSignUpDTO memberSignUpDTO) {

        Member member = loginService.signUpMember(memberSignUpDTO);

        return ResponseEntity.ok("회원가입 성공");
    }
    @GetMapping("/main")
    public ResponseEntity<String> mainPage(
            ) {

        return ResponseEntity.ok().body("main page");
    }
}
