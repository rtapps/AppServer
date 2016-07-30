package com.rtapps.db.mongo.repository;

import com.rtapps.db.mongo.data.PushToken;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;


public interface PushTokenRepository extends MongoRepository <PushToken, String> {

	public PushToken findByIdAndApplicationId(String id, String applicationId);

	public PushToken findByPushToken(String pushToken);

	public List<PushToken> findByApplicationId(String applicationId);

	public Long deleteByPushToken(String pushToken);

}

