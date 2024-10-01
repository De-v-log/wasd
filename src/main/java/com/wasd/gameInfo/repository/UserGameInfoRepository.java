package com.wasd.gameInfo.repository;

import com.wasd.gameInfo.entity.UserGameInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserGameInfoRepository extends MongoRepository<UserGameInfo, String> {
    @Query(value = "{ 'userId': ?0, 'gameInfoList': { $elemMatch: { 'gameId': ?1 } } }",
            fields = "{ 'gameInfoList.$': 1 }")
    Optional<UserGameInfo> findByUserIdAndGameId(String userId, String gameId);

    Optional<UserGameInfo> findByUserId(String userId);

    @Query(value = "{ 'userId': ?0 }",
            fields = "{ 'gameInfoList.gameId': true, 'gameInfoList.gameNm' :true}")
    Optional<UserGameInfo> findUserGameInfoListByUserId(String userId);


}
