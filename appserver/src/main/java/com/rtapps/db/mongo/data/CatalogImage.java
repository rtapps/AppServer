package com.rtapps.db.mongo.data;

import org.springframework.data.annotation.Id;

/**
 * Created by rtichauer on 8/20/16.
 */
public class CatalogImage {

	@Id
	private String id;

	private String applicationId;

	private String fileServerHost;

	private String fullImageName;

	public CatalogImage(String id, String applicationId, String fileServerHost, String fullImageName){
		this.id = id;
		this.applicationId = applicationId;
		this.fileServerHost = fileServerHost;
		this.fullImageName = fullImageName;
	}

	public String getId(){
		return this.id;
	}

	public String getApplicationId(){
		return this.applicationId;
	}

	public String getFileServerHost(){
		return this.fileServerHost;
	}

	public String getFullImageName(){
		return this.fullImageName;
	}
}
