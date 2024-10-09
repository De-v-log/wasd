package com.wasd.gameInfo.controller;

import com.wasd.config.security.CustomOAuth2User;
import com.wasd.gameInfo.dto.GameInfoDto;
import com.wasd.gameInfo.dto.UserGameInfoDto;
import com.wasd.gameInfo.service.GameInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gameInfo")
@RequiredArgsConstructor
public class GameInfoController {
    private final GameInfoService gameInfoService;

    /**
     * 전체 게임 목록 반환
     * @return 게임아이디(영문) 게임명(한글) 목록 반환
     */
    @GetMapping
    public ResponseEntity<List<GameInfoDto>> findGameInfo(){
        return ResponseEntity.ok(gameInfoService.findGameInfo());

    }

    /**
     * 사용자가 선택한 게임의 속성값들 반환
     * @param gameId 게임이름
     */
    @GetMapping("/{gameId}")
    public ResponseEntity<GameInfoDto> findGameInfo(@PathVariable String gameId){
        return ResponseEntity.ok(gameInfoService.findGameInfo(gameId));
    }

    /**
     * 유저의 게임 목록들 반환
     * @param oAuth2User oauth 인증객체 - 유저아이디 포함
     * @return List<GameInfoDto> 게임목록
     */
    @GetMapping("/user")
    public ResponseEntity<List<GameInfoDto>> findUserGameInfo(@AuthenticationPrincipal CustomOAuth2User oAuth2User){
        return ResponseEntity.ok(gameInfoService.findUserGameInfo(oAuth2User.getUserInfo().getId()));
    }

    /**
     * 유저아이디, 게임아이디에 해당하는 게임 정보들 리턴
     * @param gameId 게임아이디
     * @param oAuth2User oauth 인증객체 - 유저아이디 포함
     * @return GameInfo 해당 게임정보
     */
    @GetMapping("/user/game")
    public ResponseEntity<GameInfoDto> findUserGameInfo(@RequestParam String gameId, @AuthenticationPrincipal CustomOAuth2User oAuth2User){
        return ResponseEntity.ok(gameInfoService.findUserGameInfo(gameId, oAuth2User.getUserInfo().getId()));
    }

    /**
     * 유저 게임 정보 insert
     * @param gameInfoDtoList 게임정보 목록
     * @param oAuth2User oauth 인증객체 - 유저아이디 포함
     * @return UserGameInfoDto
     */
    @PostMapping("/user/game")
    public ResponseEntity<UserGameInfoDto> insertUserGameInfo(@RequestBody List<GameInfoDto> gameInfoDtoList, @AuthenticationPrincipal CustomOAuth2User oAuth2User){
        // 이 메소드는 가입시에만 호출되므로 drop 후 insert 로 구현함
        return ResponseEntity.ok(gameInfoService.insertUserGameInfo(gameInfoDtoList, oAuth2User.getUserInfo().getId()));
    }

    // 유저가 선택한 게임 정보 update

    /**
     * 유저 선택 게임 정보 업데이트
     * @param gameInfoDto 수정된 게임 정보
     * @param oAuth2User oauth 인증객체 - 유저아이디 포함
     * @return UserGameInfoDto
     */
    @PutMapping("/user/game")
    public ResponseEntity<UserGameInfoDto> updateUserGameInfo(@RequestBody GameInfoDto gameInfoDto, @AuthenticationPrincipal CustomOAuth2User oAuth2User){
        return ResponseEntity.ok(gameInfoService.updateUserGameInfo(gameInfoDto, oAuth2User.getUserInfo().getId()));
    }

    /**
     *  선택 게임 삭제
     * @param gameId 게임아이디
     * @param oAuth2User oauth 인증객체 - 유저아이디 포함
     * @return UserGameInfoDto 삭제 후 남은 현재 정보
     */
    @DeleteMapping("/user/game")
    public ResponseEntity<UserGameInfoDto> deleteUserGameInfo(@RequestParam String gameId, @AuthenticationPrincipal CustomOAuth2User oAuth2User){
        return ResponseEntity.ok(gameInfoService.deleteUserGameInfo(gameId, oAuth2User.getUserInfo().getId()));
    }
}
