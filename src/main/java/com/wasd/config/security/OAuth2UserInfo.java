package com.wasd.config.security;

import com.wasd.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Builder
@Getter
@ToString
public class OAuth2UserInfo {
    private String id;
    private String email;
    private String provider;
    private String nickname;
    private String profileImg;

    public static OAuth2UserInfo of(String provider, Map<String, Object> attributes) {
        switch (provider) {
            case "google":
                return ofGoogle(attributes);
            case "kakao":
                return ofKakao(attributes);
            case "naver":
                return ofNaver(attributes);
            default:
                throw new RuntimeException("제공되지 않는 로그인 유형입니다.");
        }
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .provider("google")
                .id("google_" + (String) attributes.get("sub"))
                .nickname((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profileImg((String) attributes.get("picture"))
                .build();
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .provider("kakao")
                .id("kakao_" + attributes.get("id").toString())
                .nickname((String) ((Map) attributes.get("properties")).get("nickname"))
                .email((String) ((Map) attributes.get("kakao_account")).get("email"))
                .profileImg((String) ((Map) attributes.get("properties")).get("profile_image"))
                // .profileImg((String) ((Map) attributes.get("properties")).get("thumbnail_image"))
                .build();
    }

    private static OAuth2UserInfo ofNaver(Map<String, Object> attributes) {
        Map<String, String> res = (Map) attributes.get("response");
        return OAuth2UserInfo.builder()
                .provider("naver")
                .id("naver_" + res.get("id"))
                .nickname(res.get("name"))
                .email(res.get("email"))
                .profileImg(res.get("profile_image"))
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

