package com.knulinkmoa.auth.controller;

import com.knulinkmoa.auth.jwt.service.JwtService;
import com.knulinkmoa.domain.member.entity.Member;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Controller
@ResponseBody
@RequiredArgsConstructor
@Slf4j
public class ReissueController {

    private final JwtService jwtService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //get refresh token
        String refreshToken = getRefreshTokenFromCookie(request.getCookies());

        if (!jwtService.validateToken(refreshToken) || refreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Location", "http://localhost:8080/auth/login")
                    .build();
        }

        Optional<Member> optionalMember = jwtService.findMemberByRefreshToken(refreshToken);

        return optionalMember.map(member -> {
            String email = member.getEmail();
            String role = String.valueOf(member.getRole());

            String newAccessToken = jwtService.createAccessToken(email, role);
            log.info("newAccessToken : {}", newAccessToken);


            return ResponseEntity.status(HttpStatus.OK)
                    .header("Accesstoken", newAccessToken)
                    .build();
        }).orElseGet(() ->
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .header("Location", "http://localhost:8080/auth/login")
                        .build()
        );

    }

    private static String getRefreshTokenFromCookie(Cookie[] cookies) {
        String token = null;

        if (cookies == null) {
            return token;
        } else {
            token = Arrays.stream(cookies)
                    .filter(header -> header.getName().equals("Authorization-refresh"))
                    .findAny()
                    .map(cookie -> cookie.getValue())
                    .orElse(null);

            return token;
        }
    }


    /*
        Optional<Member> optionalMember = jwtService.findMemberByRefreshToken(refreshToken);
        if (optionalMember != null) {
            Member member = optionalMember.get();
            String email = member.getEmail();
            String role = String.valueOf(member.getRole());

            String newAccessToken = jwtService.createAccessToken(email, role);
            log.info("newAccessToken : {}", newAccessToken);

            return ResponseEntity.status(HttpStatus.OK)
                    .header("Accesstoken", newAccessToken)
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Location", "http://localhost:8080/auth/login")
                    .build();
        }
*/

}

