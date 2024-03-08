package com.knulinkmoa.auth.controller;

import com.knulinkmoa.auth.itself.service.LoginService;
import com.knulinkmoa.auth.principal.PricipalDetails;
import com.knulinkmoa.auth.itself.dto.request.MemberSignUpDTO;
import com.knulinkmoa.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "회원가입 및 로그인", description = "회원가입 및 로그인 관련 Api")

public class LoginController {

    private final LoginService loginService;
    @Operation(summary = "문자열 반복", description = "파라미터로 받은 문자열을 2번 반복합니다.")
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
