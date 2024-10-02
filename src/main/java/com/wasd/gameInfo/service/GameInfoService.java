package com.wasd.gameInfo.service;

import com.wasd.gameInfo.dto.GameInfoDto;
import com.wasd.gameInfo.entity.GameInfo;
import com.wasd.gameInfo.entity.UserGameInfo;
import com.wasd.gameInfo.repository.GameInfoRepository;
import com.wasd.gameInfo.repository.UserGameInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GameInfoService {
    private final GameInfoRepository gameInfoRepository;
    private final UserGameInfoRepository userGameInfoRepository;

    public List<GameInfoDto> findGameInfo() {
        return gameInfoRepository.findAllGameIdAndGameNm()
                .map(gameInfoList -> gameInfoList.stream()
                        .map(GameInfo::toDto)
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }

    public GameInfoDto findGameInfo(String gameId) {
        return gameInfoRepository.findByGameId(gameId)
                .map(GameInfo::toDto)
                .orElseThrow(() -> new RuntimeException("해당 게임이 없습니다"));
    }

    public List<GameInfoDto> findUserGameInfo(String userId) {
        return userGameInfoRepository.findUserGameInfoListByUserId(userId)
                .map(UserGameInfo::getGameInfoList)
                .orElse(new ArrayList<>())// 유저 게임 정보가 없음
                .stream()
                .map(GameInfo::toDto) // return to Dto
                .collect(Collectors.toList());
    }


    public GameInfoDto findUserGameInfo(String userId, String gameId) {
        return userGameInfoRepository.findByUserId(userId)
                .map(userGameInfo -> userGameInfo.getGameInfoList().stream()
                        .filter(gameInfo -> gameId.equals(gameInfo.getGameId()))
                        .findFirst()
                        .map(GameInfo::toDto)
                        .orElseThrow(() -> new RuntimeException("게임 아이디에 해당하는 정보가 없습니다.")))
                .orElseThrow(() -> new RuntimeException("유저에 해당하는 정보가 없습니다."));
    }

}
