package com.knulinkmoa.auth.handler;

import com.knulinkmoa.auth.service.CustomOAuth2User;
import com.knulinkmoa.global.jwt.provider.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = oAuth2User.getAuthorities();
        String role = authorities.iterator().next().getAuthority();

        String token = jwtService.createJwt(oAuth2User.getEmail(), role, 60 * 60 * 1000L);

        response.addCookie(jwtService.createCookie("Authorization", token));
        response.sendRedirect("http://localhost:8080/main");

        log.info("OAuth2 로그인에 성공하였습니다. 이메일 : {}",  oAuth2User.getEmail());
        log.info("OAuth2 로그인에 성공하였습니다. Access Token : {}",  token);
    }
}
