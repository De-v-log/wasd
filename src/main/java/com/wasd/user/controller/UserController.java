package com.wasd.user.controller;

import com.wasd.config.security.CustomOAuth2User;
import com.wasd.user.dto.UserDto;
import com.wasd.user.enums.MbtiEnum;
import com.wasd.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 시큐리티 저장된 세션 사용하는 법
    @GetMapping("/test")
    public CustomOAuth2User test(@AuthenticationPrincipal CustomOAuth2User oAuth2User, HttpServletRequest request) {
        return oAuth2User;
    }

    @GetMapping("/mbti")
    public ResponseEntity<String[]> mbti(){
        return ResponseEntity.ok(
                Arrays.stream(MbtiEnum.values())
                .map(Enum::name)
                .toArray(String[]::new));
    }

    @PostMapping("/")
    public ResponseEntity<UserDto> insertUserInfo(@RequestBody UserDto userDto, @AuthenticationPrincipal CustomOAuth2User oAuth2User){
        return ResponseEntity.ok(userService.JoinUser(userDto, oAuth2User));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> selectUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.selectUser(userId));
    }

}
