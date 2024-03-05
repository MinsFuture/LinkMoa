package com.knulinkmoa.auth.handler;

import com.knulinkmoa.global.jwt.provider.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        String email = userDetails.getUsername();
        String role = authorities.iterator().next().getAuthority();

        String token = jwtService.createJwt(email, role, 60 * 60 * 1000L);

        response.addCookie(jwtService.createCookie("Authorization", token));
        response.sendRedirect("http://localhost:8080/main");

        log.info("자체 로그인에 성공하였습니다. 이메일 : {}",  email);
        log.info("자체 로그인에 성공하였습니다. Access Token : {}",  token);
    }
}
