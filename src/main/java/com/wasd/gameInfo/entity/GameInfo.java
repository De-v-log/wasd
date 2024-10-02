package com.wasd.gameInfo.entity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wasd.gameInfo.dto.GameInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Map;

@Document(collection = "gameInfo")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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

