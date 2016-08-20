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

	private int index;

	public CatalogImage(String id, String applicationId, String fileServerHost, String fullImageName, int index){
		this.id = id;
		this.applicationId = applicationId;
		this.fileServerHost = fileServerHost;
		this.fullImageName = fullImageName;
		this.index = index;
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

	public void setIndex(int index){
		this.index = index;
	}
}
