package com.wasd.user.dto;

import com.wasd.config.security.CustomOAuth2User;
import com.wasd.gameInfo.dto.GameInfoDto;
import com.wasd.user.entity.User;
import com.wasd.user.enums.MbtiEnum;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL) // NULL 이면 no return
public class UserDto {

    private String userId;
    private String email;
    private String nickname;
    private String provider;
    private String profileImg;
    private int yearOfBirth;
    private MbtiEnum mbti;
    private LocalTime startTime;
    private LocalTime endTime;

    private List<GameInfoDto> gameInfoList;

    // 공통으로 처리할 필드 빌더 부분
    private User.UserBuilder commonBuilder() {
        return User.builder()
                .userId(this.userId)
                .email(this.email)
                .nickname(this.nickname)
                .provider(this.provider)
                .profileImg(this.profileImg)
                .yearOfBirth(this.yearOfBirth)
                .mbti(this.mbti != null ? this.mbti.name() : null)
                .startTime(this.startTime)
                .endTime(this.endTime);
    }

    // 기본 toEntity 메서드
    public User toEntity() {
        return commonBuilder().build();
    }

    // OAuth2User를 이용한 toEntity 메서드
    public User toEntity(CustomOAuth2User oAuth2User) {
        return commonBuilder()
                .userId(oAuth2User.getUserInfo().getId())
                .email(oAuth2User.getUserInfo().getEmail())
                .provider(oAuth2User.getUserInfo().getProvider())
                .build();
    }

}
