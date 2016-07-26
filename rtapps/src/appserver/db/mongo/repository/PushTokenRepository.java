package appserver.db.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import appserver.db.mongo.data.PushToken;

public interface PushTokenRepository extends MongoRepository <PushToken, String> {

	public PushToken findByIdAndApplicationId(String id, String applicationId);
}

