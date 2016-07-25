package appserver.db.mongo.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import appserver.db.mongo.data.Message;

public interface MessageRepository extends MongoRepository<Message, String> {

    public List<Message> findByApplicationIdAndLastUpdateDateGreaterThanOrderByLastUpdateDateDesc(String applicationId, long lastUpdateDate);
    
}
