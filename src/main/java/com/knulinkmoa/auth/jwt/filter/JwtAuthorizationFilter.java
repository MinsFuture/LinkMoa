package com.knulinkmoa.auth.jwt.filter;

import com.knulinkmoa.auth.principal.PricipalDetails;
import com.knulinkmoa.domain.member.entity.Member;
import com.knulinkmoa.domain.member.service.MemberService;
import com.knulinkmoa.auth.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final JwtService jwtService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePathLists = {"/login", "/favicon.ico", "/auth/sign-up", "/auth/login", "/auth/main"
        ,"/oauth2/authorization/google", "/login/oauth2/code/google"};
        String path = request.getRequestURI();

        return Arrays.stream(excludePathLists).
                anyMatch((excludePath) -> excludePath.equals(path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessTokenFromCookie(request.getCookies());
        String refreshToken = getRefreshTokenFromCookie(request.getCookies());

        log.info("url = {}", request.getRequestURL());
        log.info("accesstoken = {}", accessToken);
        log.info("refreshToken = {}", refreshToken);

        // 먼저 access 토큰을 검증 후 인증/인가 처리
        if (accessToken != null && jwtService.validateToken(accessToken)) {

            log.info("인증 성공!!");
            String email = jwtService.getEmail(accessToken);

            log.info("email = {}", email);

            Member member = memberService.findMemberByEmail(email);
            authentication(member, request, response, filterChain);

            return;
        }

        // 유효한 Refresh Token 이면
        if (jwtService.isVaildRefreshToken(refreshToken)) {
            // access token과 refresh token을 반환
            String email = jwtService.findEmailByRefreshToken(refreshToken);

            String newAccessToken = jwtService.createAccessToken(email, "ROLE_USER");
            String newRefreshToken = jwtService.createRefreshToken();
            jwtService.updateRefreshToken(email, newRefreshToken);

            response.addCookie(jwtService.createCookie("Authorization", newAccessToken));
            response.addCookie(jwtService.createCookie("Authorization-refresh", newRefreshToken));
            Member member = memberService.findMemberByEmail(email);
            authentication(member, request, response, filterChain);

            log.info("재발급에 성공하였습니다. 이메일 : {}",  email);
            log.info("재발급에 성공하였습니다. Access Token : {}",  newAccessToken);
            log.info("재발급에 성공하였습니다. Refresh Token : {}",  newRefreshToken);
        } else {
            // 재로그인
            response.sendRedirect("http://localhost:8080/auth/main");
        }
    }

    private void authentication(Member member, HttpServletRequest request, HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {
        //PrinciDetails에 회원 정보 객체 담기
        PricipalDetails pricipalDetails = new PricipalDetails(member);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(pricipalDetails, null, pricipalDetails.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private static String getAccessTokenFromCookie(Cookie[] cookies) {
        String token = null;

        if (cookies == null) {
            return token;
        } else {
            token = Arrays.stream(cookies)
                    .filter(header -> header.getName().equals("Authorization"))
                    .findAny()
                    .map(cookie -> cookie.getValue())
                    .orElse(null);

            return token;
        }
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

}

