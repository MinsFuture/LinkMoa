package com.knulinkmoa.auth.jwt.filter;

import com.knulinkmoa.auth.principal.PricipalDetails;
import com.knulinkmoa.domain.member.entity.Member;
import com.knulinkmoa.domain.member.service.MemberService;
import com.knulinkmoa.auth.jwt.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.io.PrintWriter;
import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final JwtService jwtService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePathLists = {"/login", "/favicon.ico",
                "/oauth2/authorization/google", "/login/oauth2/code/google"};
        String path = request.getRequestURI();

        if (path.startsWith("/auth") || path.startsWith("/v3")) {
            return true;
        }

        return Arrays.stream(excludePathLists).
                anyMatch((excludePath) -> excludePath.equals(path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = request.getHeader("Accesstoken");

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwtService.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        if (accessToken != null && jwtService.validateToken(accessToken)) {

            String email = jwtService.getEmail(accessToken);
            Member member = memberService.findMemberByEmail(email);
            authentication(member, request, response, filterChain);

            return;
        }

        filterChain.doFilter(request, response);
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
}

