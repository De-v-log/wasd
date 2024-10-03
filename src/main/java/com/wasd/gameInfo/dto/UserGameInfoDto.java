package com.wasd.gameInfo.dto;

import com.wasd.gameInfo.entity.UserGameInfo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGameInfoDto {
    private String userId;
    private List<GameInfoDto> gameInfoList;

    public UserGameInfo toEntity() {
        return UserGameInfo.builder()
                .userId(this.userId)
                .gameInfoList(this.gameInfoList.stream()
                        .map(GameInfoDto::toEntity).toList()) // dto <-> entity
                .build();
    }
}
