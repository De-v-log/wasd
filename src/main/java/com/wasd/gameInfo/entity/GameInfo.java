package com.wasd.gameInfo.entity;

import com.wasd.gameInfo.dto.GameInfoDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "gameInfo")
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GameInfo {
    @Id
    private String id;

    private String gameId;
    private String gameNm;
    private Map<String, Object> info; // 모든 데이터를 유동적으로 처리하는 Map

    public GameInfoDto toDto() {
        return GameInfoDto.builder()
                .gameId(this.gameId)
                .gameNm(this.gameNm)
                .info(this.info)
                .build();
    }
}

