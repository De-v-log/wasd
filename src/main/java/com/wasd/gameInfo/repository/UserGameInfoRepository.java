package com.wasd.gameInfo.repository;

import com.wasd.gameInfo.entity.UserGameInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserGameInfoRepository extends MongoRepository<UserGameInfo, String> {
    @Query("{ 'userId': ?0, 'gameInfoList.gameId': ?1 }")
    UserGameInfo findByUserIdAndGameId(String userId, String gameId);

    Optional<UserGameInfo> findByUserId(String userId);
}
