package com.rtapps.db.mongo.repository;

import com.rtapps.db.mongo.data.PushToken;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PushTokenRepository extends MongoRepository <PushToken, String> {

	public PushToken findByIdAndApplicationId(String id, String applicationId);

	public PushToken findByPushToken(String pushToken);

}

