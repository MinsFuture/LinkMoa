package com.knulinkmoa.domain.member.service;

import com.knulinkmoa.auth.oauth2.dto.request.OAuth2DTO;
import com.knulinkmoa.domain.member.entity.Member;
import com.knulinkmoa.domain.member.exception.MemberErrorCode;
import com.knulinkmoa.domain.member.reposotiry.MemberRepository;
import com.knulinkmoa.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * oAuth2Dto를 바탕으로 member를 saveOrUpdate
     * @param oAuth2DTO
     * @return
     */
    @Transactional
    public Member saveOrUpdate(OAuth2DTO oAuth2DTO) {
        Member member = memberRepository.findByEmail(oAuth2DTO.email())
                .map(existingMember -> { // 만약 member가 있으면
                    existingMember.update(oAuth2DTO); // update
                    return existingMember;
                })
                .orElseGet(() -> { // 만약 member가 없으면
                    return oAuth2DTO.oAuth2DtoToMember(oAuth2DTO);
                });

        return memberRepository.save(member);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findMemberByRefresh(String refresh) {
        return memberRepository.findByRefresh(refresh)
                .orElseThrow(() -> new GlobalException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

}
