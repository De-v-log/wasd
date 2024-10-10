package com.wasd.user.service;

import com.wasd.config.security.CustomOAuth2User;
import com.wasd.gameInfo.service.GameInfoService;
import com.wasd.user.dto.UserDto;
import com.wasd.user.entity.User;
import com.wasd.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GameInfoService gameInfoService;

    /**
     * 사용자 정보 INSERT
     * @param userDto
     * @param oAuth2User
     * @return
     */
    public UserDto insertUser(UserDto userDto, CustomOAuth2User oAuth2User){
        return userRepository.save(userDto.toEntity(oAuth2User)).toDto();
    }

    /**
     * 회원가입
     * @param userDto
     * @param oAuth2User
     */
    @Transactional
    public UserDto JoinUser(UserDto userDto, CustomOAuth2User oAuth2User){

        if (userRepository.findById(oAuth2User.getUserInfo().getId()).isPresent()) {
            throw new RuntimeException("이미 등록된 아이디입니다.");
        }

        // 사용자 정보 INSERT
        UserDto resUserInfo = insertUser(userDto, oAuth2User);
        // 사용자 게임 정보 INSERT
        gameInfoService.insertUserGameInfo(userDto.getGameInfoList(), resUserInfo.getUserId());
        return resUserInfo;
    }

    public UserDto selectUser(String userId){
        return userRepository.findById(userId)
                .map(User::toDto)
                .orElse(new UserDto());
    }
}
