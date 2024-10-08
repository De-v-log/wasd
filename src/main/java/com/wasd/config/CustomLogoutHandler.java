package com.wasd.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class CustomLogoutHandler {
    public LogoutHandler kakaoLogoutHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            if (authentication != null) {
                DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
                String accessToken = defaultOAuth2User.getAttributes().get("access_token").toString();

                // 카카오 로그아웃 API 호출
                RestTemplate restTemplate = new RestTemplate();
                String logoutUrl = "https://kapi.kakao.com/v1/user/logout";

                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(accessToken);
                headers.setContentType(MediaType.APPLICATION_JSON);

                try {
                    restTemplate.postForEntity(logoutUrl, null, String.class);
                    log.info("카카오 로그아웃 성공");
                } catch (Exception e) {
                    log.error("카카오 로그아웃 실패: {}", e.getMessage());
                }
            }
        };
    }
}
