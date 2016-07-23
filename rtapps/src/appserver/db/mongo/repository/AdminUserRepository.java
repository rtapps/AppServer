package appserver.db.mongo.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import appserver.db.mongo.data.AdminUser;

public interface AdminUserRepository extends MongoRepository<AdminUser, String> {

    public List<AdminUser> findByFirstName(String firstName);
    public List<AdminUser> findByLastName(String lastName);
	

}
