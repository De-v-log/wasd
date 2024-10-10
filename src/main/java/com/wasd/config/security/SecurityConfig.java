package com.wasd.config.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Slf4j
public class SecurityConfig {
    private final OAuth2UserService oAuth2UserService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화 (필요에 따라 설정)
                .authorizeHttpRequests(auth -> auth

                        // 로그인페이지 말고는 인증 안되게 ( + 정적파일 접근권한도 추가해줘야함)
                        .requestMatchers("/login", "/css/**", "/images/**", "/js/**", "/error").permitAll()
                        .requestMatchers("/auth/temp").permitAll()

                        /*// 특정 HTTP 메서드에 대한 제한 설정
                        .requestMatchers(HttpMethod.GET, "/public/**").permitAll() // GET 요청은 인증 없이 허용
                        .requestMatchers(HttpMethod.POST, "/private/**").authenticated() // POST 요청은 인증 필요
                        // 권한에 따른 접근 제한
                        .requestMatchers("/admin/**").hasAuthority("ADMIN_PRIVILEGE")*/

                        // 다른 모든 페이지는 인증 후에 접속가능
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Configurer -> oauth2Configurer
                        .loginPage("/login")
                        .successHandler(successHandler())
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))// 사용자 정보를 처리하는 서비스 설정
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 URL 설정 (기본값: /logout)
                        .logoutSuccessUrl("/login") // 로그아웃 성공 시 리다이렉트할 URL
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 특정 쿠키 삭제
                );
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        // 로그인 인증 후 들어옴
        return (request, response, authentication) -> {
            CustomOAuth2User userInfo = (CustomOAuth2User) authentication.getPrincipal();
            Collection<? extends GrantedAuthority> authorities = userInfo.getAuthorities();

            log.info("OAuth2 Success::" + userInfo
                    +", Roles::" + authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", ")));

            // 가입여부에 따라 redirect
            boolean isSigned = authorities.stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"));

            response.sendRedirect(isSigned ? "/board" : "/join");
        };
    }

    // Security에서 사용자의 인증 정보를 HttpSession에 저장하고 관리하여 여러 요청 간에 인증 상태를 유지하게 함
    // This is for OAuthTempController
    @Bean
    public HttpSessionSecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
}
