package com.rtapps.db.mongo.data;

import org.springframework.data.annotation.Id;

public class PushToken {

	@Id
	private String id;
	private String applicationId;
	private String pushToken;

	private String os;

	public PushToken(String applicationId, String pushToken, String os){
		this.applicationId = applicationId;
		this.pushToken = pushToken;
		this.os = os;
	}

	public String getApplicationId(){
		return this.applicationId;
	}

	public String getId(){
		return this.id;
	}

	public String getPushToken(){
		return this.pushToken;
	}

	public void setPushToken(String pushToken){
		this.pushToken = pushToken;
	}

	public String getOs(){
		return this.os;
	}
}
