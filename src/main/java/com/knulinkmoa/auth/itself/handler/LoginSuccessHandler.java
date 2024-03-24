package com.knulinkmoa.auth.itself.handler;

import com.knulinkmoa.auth.jwt.service.JwtService;
import com.knulinkmoa.auth.principal.PricipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PricipalDetails itselfMember = (PricipalDetails) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = itselfMember.getAuthorities();

        String email = itselfMember.getEmail();
        String role = authorities.iterator().next().getAuthority();

        String accessToken = jwtService.createAccessToken(email, role);
        String refreshToken =  jwtService.createRefreshToken();
        jwtService.saveRefreshToken(email, refreshToken);

        response.setHeader("Accesstoken", accessToken);
        response.addCookie(jwtService.createCookie("Authorization-refresh", refreshToken));
        response.sendRedirect("http://localhost:8080");

        log.info("자체 로그인에 성공하였습니다. 이메일 : {}",  email);
        log.info("자체 로그인에 성공하였습니다. Access Token : {}",  accessToken);
        log.info("자체 로그인에 성공하였습니다. Refresh Token : {}",  refreshToken);
    }
}
