package com.wasd.config.security;

import com.wasd.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Builder
@Getter
@ToString
@AllArgsConstructor
public class OAuth2UserInfo implements Serializable {
    private String id;
    private String email;
    private String provider;
    private String nickname;
    private String profileImg;

    public static OAuth2UserInfo of(String provider, Map<String, Object> attributes) {
        return switch (provider) {
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            case "naver" -> ofNaver(attributes);
            default -> throw new RuntimeException("제공되지 않는 로그인 유형입니다.");
        };
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .provider("google")
                .id("google_" + String.valueOf(attributes.get("sub"))) // 안전한 처리
                .nickname(String.valueOf(attributes.get("name"))) // 안전한 처리
                .email(String.valueOf(attributes.get("email"))) // 안전한 처리
                .profileImg(String.valueOf(attributes.get("picture"))) // 안전한 처리
                .build();
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        return OAuth2UserInfo.builder()
                .provider("kakao")
                .id("kakao_" + String.valueOf(attributes.get("id"))) // 안전한 처리
                .nickname(String.valueOf(properties.get("nickname"))) // 안전한 처리
                .email(String.valueOf(kakaoAccount.get("email"))) // 안전한 처리
                .profileImg(String.valueOf(properties.get("profile_image"))) // 안전한 처리
                .build();
    }

    private static OAuth2UserInfo ofNaver(Map<String, Object> attributes) {
        Map<String, Object> res = (Map<String, Object>) attributes.get("response");
        return OAuth2UserInfo.builder()
                .provider("naver")
                .id("naver_" + String.valueOf(res.get("id"))) // 안전한 처리
                .nickname(String.valueOf(res.get("name"))) // 안전한 처리
                .email(String.valueOf(res.get("email"))) // 안전한 처리
                .profileImg(String.valueOf(res.get("profile_image"))) // 안전한 처리
                .build();
    }
    public User toEntity(){
        return User.builder()
                .userId(id)
                .nickname(nickname)
                .provider(provider)
                .profileImg(profileImg)
                .email(email)
                .build();
    }
}

