package com.wasd.gameInfo.controller;

import com.wasd.gameInfo.dto.GameInfoDto;
import com.wasd.gameInfo.entity.GameInfo;
import com.wasd.gameInfo.service.GameInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class GameInfoController {
    private final GameInfoService gameInfoService;

    /**
     * 전체 게임 목록 반환
     * @return 게임아이디(영문) 게임명(한글) 목록 반환
     */
    @GetMapping("/gameInfo")
    public ResponseEntity<List<GameInfoDto>> findGameInfo(){
        return ResponseEntity.ok(gameInfoService.findGameList());

    }

    /**
     * 사용자가 선택한 게임의 속성값들 반환
     * @param gameId 게임이름
     */
    @GetMapping("/gameInfo/{gameId}")
    public ResponseEntity<GameInfo> findGameInfo(@PathVariable String gameId){
        return ResponseEntity.ok(gameInfoService.findGameInfoByGameId(gameId));
    }
}
