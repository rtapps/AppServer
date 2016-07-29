package com.rtapps.db.mongo.data;

import org.springframework.data.annotation.Id;

public class PushToken {

	@Id
	private String id;
	private String applicationId;
	private String pushToken;

	private String os;

	private String deviceModelType;

	public PushToken(String applicationId, String pushToken, String os, String deviceModelType){
		this.applicationId = applicationId;
		this.pushToken = pushToken;
		this.os = os;
		this.deviceModelType = deviceModelType;
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

	public String getDeviceModelType(){
		return this.deviceModelType;
	}
}
