package com.rtapps.db.mongo.repository;

import com.rtapps.db.mongo.data.CatalogImage;
import com.rtapps.db.mongo.data.PushToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface CatalogImageRepository extends MongoRepository <CatalogImage, String> {

	CatalogImage findByIdAndApplicationId (String id, String applicationId);

	List<CatalogImage> findByApplicationId (String applicationId);

}

