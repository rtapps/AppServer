package appserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import appserver.db.mongo.data.AdminUser;
import appserver.db.mongo.repository.AdminUserRepository;

@SpringBootApplication
public class Application implements CommandLineRunner {

	 @Autowired AdminUserRepository adminUserReposiroty;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		System.out.println("aaa");
	}
}