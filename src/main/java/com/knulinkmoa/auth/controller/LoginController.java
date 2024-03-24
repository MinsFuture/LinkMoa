package com.knulinkmoa.auth.controller;

import com.knulinkmoa.auth.itself.dto.request.MemberLoginDTO;
import com.knulinkmoa.auth.itself.service.LoginService;
import com.knulinkmoa.auth.principal.PricipalDetails;
import com.knulinkmoa.auth.itself.dto.request.MemberSignUpDTO;
import com.knulinkmoa.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class LoginController {

    private final LoginService loginService;
    @Operation(summary = "회원가입", description = "자체 사이트의 회원가입 로직")
    @PostMapping("/sign-up")
    public ResponseEntity<String> signup(
            @RequestBody MemberSignUpDTO memberSignUpDTO) {

        Member member = loginService.signUpMember(memberSignUpDTO);

        return ResponseEntity.ok("회원가입 성공");
    }

    @Operation(summary = "로그인, 실제 Url은 /auth/login", description = "자체 사이트의 로그인 로직, swagger ui를 위한 테스트 컨트롤러. 요구 데이터만 참고하고 실제로는 사용XXX")
    @PostMapping("login-for-swaager")
    public ResponseEntity<String> login(@RequestBody MemberLoginDTO memberLoginDTO){
        return ResponseEntity.ok("로그인 성공");
    }

}
