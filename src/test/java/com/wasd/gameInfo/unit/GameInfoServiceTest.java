package com.wasd.gameInfo.unit;

import com.wasd.gameInfo.dto.UserGameInfoDto;
import com.wasd.gameInfo.entity.GameInfo;
import com.wasd.gameInfo.entity.UserGameInfo;
import com.wasd.gameInfo.repository.UserGameInfoRepository;
import com.wasd.gameInfo.service.GameInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpSession;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameInfoServiceTest {

    @Mock
    UserGameInfoRepository userGameInfoRepository;

    @Mock
    HttpSession session;

    @InjectMocks
    GameInfoService gameInfoService;

    private UserGameInfo userGameInfo;

    @BeforeEach
    public void setUp() {
        // Mockito 초기화
        MockitoAnnotations.openMocks(this);

        // Dummy UserGameInfo 설정
        userGameInfo = UserGameInfo.builder()
                .id("1")
                .userId("test")
                .gameInfoList(Arrays.asList(
                        GameInfo.builder()
                                .gameId("lol")
                                .gameNm("롤(리그오브레전드)")
                                .info(Map.of("tier", "silver", "lane", "mid", "playStyle", "defensive"))
                                .build(),
                        GameInfo.builder()
                                .gameId("lostark")
                                .gameNm("로스트아크")
                                .info(Map.of("server", "mari", "class", "warrior", "playStyle", "balanced"))
                                .build()
                ))
                .build();
    }

    @Test
    @DisplayName("게임 정보 삭제 테스트")
    public void deleteUserGameInfoTest() {
        // given
        String userId = "test";
        String gameId = "lol";

        when(session.getAttribute("userId")).thenReturn(userId);
        when(userGameInfoRepository.findByUserId(userId)).thenReturn(Optional.of(userGameInfo));

        // when
        UserGameInfoDto result = gameInfoService.deleteUserGameInfo(gameId, session);

        assertEquals(1, result.getGameInfoList().size());
        assertEquals("lostark", result.getGameInfoList().get(0).getGameId());
    }

}

