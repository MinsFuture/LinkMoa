package com.knulinkmoa.domain.member.entity;

import com.knulinkmoa.auth.oauth2.dto.request.OAuth2DTO;
import com.knulinkmoa.domain.directory.entity.Directory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "name")
    private String name; // 사용자 이름

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role; // 사용자의 역할

    @Column(name = "refresh_token")
    private String refresh;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Directory> directories = new ArrayList<>();
    @Builder
    public Member(Long id, String name, String email, String password, String phoneNumber, Role role, List<Directory> directories) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.directories = directories;
    }

    public void update(OAuth2DTO oAuth2DTO) {
        if (oAuth2DTO.name() != null) {
            this.name = oAuth2DTO.name();
        }

        if (oAuth2DTO.email() != null) {
            this.email = oAuth2DTO.email();
        }

        if (oAuth2DTO.role() != null) {
            this.role = Role.valueOf(oAuth2DTO.role());
        }
    }

    public void updateRefreshToken(String refresh) {
        this.refresh = refresh;
    }
 }
