package com.wasd.config.security;

import com.wasd.user.repository.UserRepository;
import com.wasd.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    // 로그인 시 호출
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // provider 추가 (kakao || naver || google)
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // 인증 객체를 가지고 제공자로 구분해서 필요한 정보 담은 객체
        OAuth2UserInfo userInfo = OAuth2UserInfo.of(provider, oAuth2User.getAttributes());

        // 해당 아이디로 가입되어 있는지 여부
        User byId = userRepository.findById(userInfo.getId()).orElse(null);
        boolean isSigned = byId != null;

        /*
        // accssToken
        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        // Role generate
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN");
        // nameAttributeKey
        String userNameAttr = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
                */

        return new CustomOAuth2User(userInfo, isSigned);
    }
}
