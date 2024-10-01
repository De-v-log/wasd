package com.wasd.gameInfo.service;

import com.wasd.gameInfo.dto.GameInfoDto;
import com.wasd.gameInfo.entity.GameInfo;
import com.wasd.gameInfo.entity.UserGameInfo;
import com.wasd.gameInfo.repository.GameInfoRepository;
import com.wasd.gameInfo.repository.UserGameInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GameInfoService {
    private final GameInfoRepository gameInfoRepository;
    private final UserGameInfoRepository userGameInfoRepository;

    public List<GameInfoDto> findGameList(){
        return gameInfoRepository.findAllGameIdAndGameNm().stream()
                .map(GameInfo::toDto)
                .collect(Collectors.toList());
    }

    public GameInfo findGameInfoByGameId(String gameId){
        return gameInfoRepository.findByGameId(gameId)
                .orElseThrow(() -> new RuntimeException("게임이 없습니다"));
    }

    public GameInfo findUserGameInfo(String userId, String gameId) {
        return userGameInfoRepository.findByUserId(userId)
                .map(userGameInfo -> userGameInfo.getGameInfoList().stream()
                        .filter(gameInfo -> gameId.equals(gameInfo.getGameId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("게임 아이디에 해당하는 정보가 없습니다.")))
                .orElseThrow(() -> new RuntimeException("유저에 해당하는 정보가 없습니다."));
    }


}
