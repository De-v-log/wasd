package com.wasd.gameInfo.service;

import com.wasd.gameInfo.dto.GameInfoDto;
import com.wasd.gameInfo.dto.UserGameInfoDto;
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

    public GameInfoDto findUserGameInfo(String gameId, String userId) {
        return userGameInfoRepository.findByUserId(userId)
                .map(userGameInfo -> userGameInfo.getGameInfoList().stream()
                        .filter(gameInfo -> gameId.equals(gameInfo.getGameId()))
                        .findFirst()
                        .map(GameInfo::toDto)
                        .orElseThrow(() -> new RuntimeException("게임 아이디에 해당하는 정보가 없습니다.")))
                .orElseThrow(() -> new RuntimeException("유저에 해당하는 정보가 없습니다."));
    }

    public UserGameInfoDto insertUserGameInfo(List<GameInfoDto> gameInfoDtoList, String userId) {
        // 혹시 만약 있다면 삭제 먼저
        userGameInfoRepository.findByUserId(userId).ifPresent(userGameInfoRepository::delete);
        UserGameInfo save = UserGameInfo.builder()
                .userId(userId)
                .gameInfoList(gameInfoDtoList.stream()
                        .map(GameInfoDto::toEntity) // 각 DTO를 엔티티로 변환
                        .collect(Collectors.toList())
                )
                .build();
        return userGameInfoRepository.save(save).toDto();
    }

    public UserGameInfoDto updateUserGameInfo(GameInfoDto gameInfoDto, String userId) {
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

    public UserGameInfoDto deleteUserGameInfo(String gameId, String userId){
        UserGameInfo beforeDelete = userGameInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));

        // 삭제를 위해선 복사본 생성해야됨
        List<GameInfo> gameInfoList = new ArrayList<>(beforeDelete.getGameInfoList());
        // 게임아이디에 해당하는 gameInfo 목록에서 지우기
        if (!gameInfoList.removeIf(gameInfo -> gameInfo.getGameId().equals(gameId))) {
            // 만약 지워진게 없으면 그냥 현재 정보 바로 반환
            return beforeDelete.toDto();
        }

        // update by deleted
        UserGameInfo deleted = UserGameInfo.builder()
                .id(beforeDelete.getId())
                .userId(beforeDelete.getUserId())
                .gameInfoList(gameInfoList)
                .build();

        // 저장 후 삭제된 게임 정보 확인
        UserGameInfo save = userGameInfoRepository.save(deleted);
        return save.toDto();
    }

}
