package com.rtapps.controllers.webdata;

import com.rtapps.db.mongo.data.AdminUser;

/**
 * Created by rtichauer on 8/13/16.
 */
public class AdminUserData {


    private String firstName;
    private String lastName;
    private String buisnessName;
    private int applicationId;
    private String username;

    public AdminUserData(AdminUser adminUser){
        this.firstName = adminUser.getFirstName();
        this.lastName = adminUser.getLastName();
        this.buisnessName = adminUser.getBuisnessName();
        this.applicationId = adminUser.getApplicationId();
        this.username = adminUser.getUsername();
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

    public int getApplicationId(){
        return this.applicationId;
    }
    public String getUsername(){
        return this.username;
    }
}
