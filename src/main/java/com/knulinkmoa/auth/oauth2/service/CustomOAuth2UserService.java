package com.knulinkmoa.auth.oauth2.service;

import com.knulinkmoa.auth.oauth2.dto.request.OAuth2DTO;
import com.knulinkmoa.auth.oauth2.dto.response.GoogleResponse;
import com.knulinkmoa.auth.oauth2.dto.response.NaverResponse;
import com.knulinkmoa.auth.oauth2.dto.response.OAuth2Response;
import com.knulinkmoa.auth.principal.PricipalDetails;
import com.knulinkmoa.domain.member.entity.Member;
import com.knulinkmoa.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // user 정보를 가져옴

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("oAuth2User = " + oAuth2User);

        // 어떤 소셜인지 .. google, naver, kakao
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // DTO에 맞는 response를 가져오고
        OAuth2Response oAuth2Response = getOAuth2Response(registrationId, oAuth2User);

        // oAuth2DTO
        OAuth2DTO oAuth2DTO = createOAuth2DTO(oAuth2User, oAuth2Response);

        // oAuth2DTO를 통해서, 우리 서비스에서 관리할 member를 saveOrUpdate
        Member member = memberService.saveOrUpdate(oAuth2DTO);

        // PricipalDetails을 리턴
        PricipalDetails pricipalDetails = new PricipalDetails(member, oAuth2User.getAttributes());

        return pricipalDetails;
    }

    /**
     *
     * @param registrationId google, naver, kakao 등등
     * @param oAuth2User resource server로 부터 받아온 user 정보
     * @return google, naver, kakao 등에 맞는 oAuth2Response
     */
    private static OAuth2Response getOAuth2Response(String registrationId, OAuth2User oAuth2User) {
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        return oAuth2Response;
    }

    /**
     * OAuth2DTo 생성
     *
     * @param oAuth2User
     * @param oAuth2Response
     * @return
     */
    private static OAuth2DTO createOAuth2DTO(OAuth2User oAuth2User, OAuth2Response oAuth2Response) {
        return OAuth2DTO.builder()
                .attributes(oAuth2User.getAttributes())
                .name(oAuth2Response.getName())
                .email(oAuth2Response.getEmail())
                .role("ROLE_USER")
                .build();
    }

}
