package com.wasd.config.security;

import com.wasd.config.CustomLogoutHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Slf4j
public class SecurityConfig {
    private final OAuth2UserService oAuth2UserService;
    private final CustomLogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화 (필요에 따라 설정)
                // .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // 모든 경로 인증 없이 허용
                .authorizeHttpRequests(auth -> auth
                        // 로그인페이지 말고는 인증 안되게
                        .requestMatchers("/login").permitAll()
                        // css 와 image 도 url 등록해줘야지 안깨짐
                        .requestMatchers("/css/**", "/images/**", "/js/**").permitAll()
                        // 다른 모든 페이지는 인증 후에 접속가능
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Configurer -> oauth2Configurer
                        .loginPage("/login")
                        .successHandler(successHandler())
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))// 사용자 정보를 처리하는 서비스 설정
                )
                // TODO 로그아웃 기능 구현해야댐
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 URL 설정 (기본값: /logout)
                        .logoutSuccessUrl("/login") // 로그아웃 성공 시 리다이렉트할 URL
                        .invalidateHttpSession(true) // 세션 무효화
                        .addLogoutHandler(logoutHandler.kakaoLogoutHandler())
                        .deleteCookies("JSESSIONID") // 특정 쿠키 삭제 (예: JSESSIONID)
                );
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        // 로그인 인증 후 들어옴
        return (request, response, authentication) -> {
            CustomOAuth2User userInfo = (CustomOAuth2User) authentication.getPrincipal();
            log.info("OAuth2 Success ::" + userInfo.getUserInfo().toString());
            // 가입여부에 따라 redirect
            response.sendRedirect(userInfo.isSigned() ? "/board" : "/join");
        };
    }
}
