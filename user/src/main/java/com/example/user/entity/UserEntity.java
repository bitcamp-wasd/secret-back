package com.example.user.entity;

import com.example.user.dto.request.auth.SignUpRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
@Table(name = "user")
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String socialType;
    // 소셜 로그인 타입 kakao naver app

    private int point;
    // 처음 회원가입 후 부여 되는 포인트 300 포인트 붜여

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rank_id")
    private UserRankEntity rankId;

    private String role;
    // 기본적으로 회윈가입 할 시 디폴트 값 ROLE_USER

    public UserEntity(SignUpRequestDto dto){
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.nickname = dto.getNickName();
        this.socialType = "app";
        this.point = 300;
        this.role = "ROLE_USER";
    }

    public UserEntity(String email, String nickname, String socialType){
        this.email = email;
        this.password = "passw0rd";
        this.nickname = nickname;
        this.socialType = socialType;
        this.point = 300;
        this.role = "ROLE_USER";
    }

    // 등급 업데이트 메소드
}
