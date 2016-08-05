package com.rtapps.db.mongo.repository;


import java.util.List;

import com.rtapps.db.mongo.data.AdminUser;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface AdminUserRepository extends MongoRepository<AdminUser, String> {

	public List<AdminUser> findByFirstName(String firstName);
	public List<AdminUser> findByLastName(String lastName);

	public AdminUser findByUsername(String username);
	AdminUser findByApplicationId(String applicationId);
}
