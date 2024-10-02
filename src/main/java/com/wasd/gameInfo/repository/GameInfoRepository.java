package com.wasd.gameInfo.repository;

import com.wasd.gameInfo.entity.GameInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GameInfoRepository extends MongoRepository<GameInfo, String > {

    @Query(value = "{}", fields = "{'gameId': true, 'gameNm': true}")
    Optional<List<GameInfo>> findAllGameIdAndGameNm();

    Optional<GameInfo> findByGameId(String gameID);
}
