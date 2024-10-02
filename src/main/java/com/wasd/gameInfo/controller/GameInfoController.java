package com.wasd.gameInfo.controller;

import com.wasd.gameInfo.dto.GameInfoDto;
import com.wasd.gameInfo.service.GameInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
     * @param userId 유저아이디
     * @return List<GameInfoDto> 게임목록
     */
    @GetMapping("/user")
    public ResponseEntity<List<GameInfoDto>> findUserGameInfo(@RequestParam String userId){
        return ResponseEntity.ok(gameInfoService.findUserGameInfo(userId));
    }

    /**
     * 유저아이디, 게임아이디에 해당하는 게임 정보들 리턴
     * @param userId 유저아이디
     * @param gameId 게임아이디
     * @return GameInfo 해당 게임정보
     */
    @GetMapping("/user/game")
    public ResponseEntity<GameInfoDto> findUserGameInfo(@RequestParam String userId, @RequestParam String gameId){
        return ResponseEntity.ok(gameInfoService.findUserGameInfo(userId, gameId));
    }
}
