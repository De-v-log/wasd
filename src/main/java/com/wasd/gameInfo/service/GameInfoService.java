package com.wasd.gameInfo.service;

import com.wasd.gameInfo.dto.GameInfoDto;
import com.wasd.gameInfo.dto.UserGameInfoDto;
import com.wasd.gameInfo.entity.GameInfo;
import com.wasd.gameInfo.entity.UserGameInfo;
import com.wasd.gameInfo.repository.GameInfoRepository;
import com.wasd.gameInfo.repository.UserGameInfoRepository;
import jakarta.servlet.http.HttpSession;
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

    public UserGameInfoDto insertUserGameInfo(List<GameInfoDto> gameInfoDtoList, HttpSession session) {
        try {
            String userId = (String) session.getAttribute("userId");
            if (userId == null) throw new RuntimeException("유저 아이디가 없음 (노세션)");

            UserGameInfoDto dto = UserGameInfoDto.builder()
                    .userId(userId)
                    .gameInfoList(gameInfoDtoList)
                    .build();
            return userGameInfoRepository.save(dto.toEntity()).toDto();
        } catch (Exception e) {
            throw new RuntimeException("게임 정보 저장 실패", e);
        }
    }

    public UserGameInfoDto updateUserGameInfo(GameInfoDto gameInfoDto, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        UserGameInfo byUserId = userGameInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
        List<GameInfo> gameInfoList = byUserId.getGameInfoList();

        // case 0: 유저 게임 정보가 없을 경우
        if (gameInfoList.isEmpty()) {
            gameInfoList.add(gameInfoDto.toEntity());
        } else {
            String gameId = gameInfoDto.getGameId();
            boolean gameExists = false;

            // case 1: 유저 게임 정보 중 현재 게임이 있을 때, 업데이트
            for (int i = 0; i < gameInfoList.size(); i++) {
                if (gameInfoList.get(i).getGameId().equals(gameId)) {
                    // 기존 게임 정보 업데이트
                    gameInfoList.set(i, gameInfoDto.toEntity());
                    gameExists = true;
                    break;
                }
            }

            // case 2: 유저 게임 정보 중 현재 게임이 없을 때, 추가
            if (!gameExists) {
                gameInfoList.add(gameInfoDto.toEntity()); // 새로운 게임 정보 추가
            }
        }

        UserGameInfo updatedUserGameInfo = UserGameInfo.builder()
                .id(byUserId.getId())
                .userId(userId)
                .gameInfoList(gameInfoList)
                .build();

        // 기존 _id save 시 업데이트
        return userGameInfoRepository.save(updatedUserGameInfo).toDto();
    }

}
