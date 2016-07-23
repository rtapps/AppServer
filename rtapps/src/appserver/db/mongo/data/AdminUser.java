package appserver.db.mongo.data;

import org.springframework.data.annotation.Id;


public class AdminUser {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String buisnessName;
    private String applicationId;
    private String username;
    private String password;

    public AdminUser() {}

    public AdminUser(String firstName, String lastName, String buisnessName, String applicationId, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.buisnessName = buisnessName;
        this.applicationId = applicationId;
        this.username = username;
        this.password = password;
    }
    
    public String getFirstName(){
    	return this.firstName;
    }
    public String getLastName(){
    	return this.lastName;
    }
    public String getBuisnessName(){
    	return this.buisnessName;
    }
    
    public String getApplicationId(){
    	return this.applicationId;
    }
    public String getUsername(){
    	return this.username;
    }
    public String getPassword(){
    	return this.password;
    }
    

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%s, firstName='%s', lastName='%s',  buisnessName='%s',  applicationId='%s', username='%s', password='%s']",
                id, firstName, lastName, buisnessName, applicationId, username, password);
    }

}
