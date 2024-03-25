package com.ssafy.pickitup.domain.user.command;

import com.ssafy.pickitup.domain.user.entity.UserMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserCommandMongoRepository extends
    MongoRepository<UserMongo, Integer> {

}