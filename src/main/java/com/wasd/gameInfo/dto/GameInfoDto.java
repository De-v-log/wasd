package com.wasd.gameInfo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wasd.gameInfo.entity.GameInfo;
import lombok.*;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // NULL 이면 no return
public class GameInfoDto {
    private String gameId;
    private String gameNm;
    private Map<String, Object> info;

    public GameInfo toEntity() {
        return GameInfo.builder()
                .gameId(this.gameId)
                .gameNm(this.gameNm)
                .info(this.info)
                .build();
    }

}
