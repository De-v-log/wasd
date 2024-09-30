package com.wasd.gameInfo.service;

import com.wasd.gameInfo.dto.GameInfoDto;
import com.wasd.gameInfo.entity.GameInfo;
import com.wasd.gameInfo.repository.GameInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GameInfoService {
    private final GameInfoRepository gameInfoRepository;

    public List<GameInfoDto> findGameList(){
        return gameInfoRepository.findAllGameIdAndGameNm().stream()
                .map(GameInfo::toDto)
                .collect(Collectors.toList());
    }

    public GameInfo findGameInfoByGameId(String gameId){
        return gameInfoRepository.findByGameId(gameId)
                .orElseThrow(() -> new RuntimeException("게임이 없습니다"));
    }
}
