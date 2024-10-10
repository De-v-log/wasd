package com.wasd.config.security;

import com.wasd.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class OAuthTempController {
    @Value("${kakao.client-id}")
    private String SECRET_KEY;

    private final UserRepository userRepository;

    @GetMapping("/auth/temp")
    public ResponseEntity<String> forceAuthentication(@RequestParam String userId, @RequestParam String secretKey, HttpSession session) {
        if (!secretKey.equals(SECRET_KEY)) throw new RuntimeException("인증키가 다릅니다.");

        // 사용자 정보가 있으면 DB에서, 없으면 하드코딩된 값 사용
        OAuth2UserInfo userInfo = userRepository.findById(userId)
                .map(user -> new OAuth2UserInfo(user.getUserId(), user.getEmail(), user.getProvider(), user.getNickname(), user.getProfileImg()))
                .orElseGet(() -> new OAuth2UserInfo(userId, "wasd@wasd.com", "kakao", "wasd", "http://localhost:8088/images/wasd_logo.png"));

        // DB 정보에 따라 권한 부여
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_GUEST");
        if (userRepository.existsById(userId)) authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // 임의로 인증할 인증 객체를 설정합니다.
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userInfo, authorities);
        Authentication authentication = new OAuth2AuthenticationToken(customOAuth2User, customOAuth2User.getAuthorities(), userId);

        // SecurityContext에 인증 정보를 설정합니다.
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        // SecurityContext를 세션에 저장합니다.
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        log.info("TEMP API USER JOIN::" + userInfo);

        // 응답 반환
        return ResponseEntity.ok(userInfo.toString());
    }
}
