package com.rtapps.db.mongo.data;

import org.springframework.data.annotation.Id;


public class AdminUser {

	@Id
	private String id;

	private String firstName;
	private String lastName;
	private String buisnessName;
	private int applicationId;
	private String username;
	private String password;
	private String googleApiKey;

	public AdminUser() {}

	public AdminUser(String firstName, String lastName, String buisnessName, int applicationId, String username, String password, String googleApiKey) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.buisnessName = buisnessName;
		this.applicationId = applicationId;
		this.username = username;
		this.password = password;
		this.googleApiKey = googleApiKey;
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
	public String getPassword(){
		return this.password;
	}


	@Override
	public String toString() {
		return String.format(
				"Customer[id=%s, firstName='%s', lastName='%s',  buisnessName='%s',  applicationId='%s', username='%s', password='%s']",
				id, firstName, lastName, buisnessName, applicationId, username, password);
	}

	public String getGoogleApiKey() {
		return this.googleApiKey;
	}

	public void setGoogleApiKey(String googleApiKey) {
		this.googleApiKey = googleApiKey;
	}
}
