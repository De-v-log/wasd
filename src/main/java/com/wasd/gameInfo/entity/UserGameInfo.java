package com.wasd.gameInfo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wasd.gameInfo.dto.UserGameInfoDto;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("userGameInfo")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserGameInfo {
    @Id
    private String id;
    @Id
    private String userId;

    private List<GameInfo> gameInfoList;

    public UserGameInfoDto toDto(){
        return UserGameInfoDto.builder()
                .userId(this.userId)
                .gameInfoList(this.gameInfoList.stream()
                        .map(GameInfo::toDto).toList()) // dto <-> entity
                .build();
    }
}
