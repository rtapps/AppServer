package com.rtapps.db.mongo.repository;


import java.util.List;

import com.rtapps.db.mongo.data.Message;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface MessageRepository extends MongoRepository<Message, String> {

	public List<Message> findByApplicationIdAndLastUpdateDateGreaterThanOrderByLastUpdateDateDesc(String applicationId, long lastUpdateDate);

	public Message findAndRemoveByApplicationIdAndId(String applicationId, String id);

}
