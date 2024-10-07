package com.wasd.user.controller;

import com.wasd.config.security.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    // 시큐리티 저장된 세션 사용하는 법
    @GetMapping("/test")
    public CustomOAuth2User test(@AuthenticationPrincipal CustomOAuth2User oAuth2User, HttpServletRequest request) {
        return oAuth2User;
    }
}
