package com.rtapps.db.mongo.data;

import org.springframework.data.annotation.Id;

public class Message {

	@Id
	private String id;

	private String applicationId;
	private String header;
	private String body;
	private String fileServerHost;
	private String fileName;
	private long creationDate;
	private long lastUpdateDate;
	private boolean isExists;


	public Message (String id, String applicationId, String header, String body, String fileServerHost, String fileName, long creationDate, long lastUpdateDate, boolean isExists){
		this.id = id;
		this.applicationId = applicationId;
		this.header = header;
		this.body = body;
		this.fileServerHost = fileServerHost;
		this.fileName = fileName;
		this.creationDate = creationDate;
		this.lastUpdateDate = lastUpdateDate;
		this.isExists = isExists;
	}

	public String getApplicationId(){
		return this.applicationId;
	}

	public String getHeader(){
		return this.header;
	}

	public String getBody(){
		return this.body;
	}

	public String getFileServerHost(){
		return this.fileServerHost;
	}

	public String getFileName(){return this.fileName;}

	public long getLastUpdateDate(){
		return this.lastUpdateDate;
	}

	public boolean isExists(){
		return this.isExists;
	}

	public String getId(){
		return this.id;
	}

	public long getCreationDate(){return this.creationDate;}

}

/*
 * {
    "_id" : ObjectId("579654525e729733343c8007"),
    "applicationId" : "1234",
    "header" : "מבצעים והנחות ששנחה",
    "body" : "רק השבוע 20% הנחה על כל מוצרי הכלבים, החתולים והיונים",
    "fileUrl" : "http://animalia-life.com/image.php?pic=/data_images/dog/dog1.jpg",
    "lastUpdateDate" : "1469469170",
    "exists" : "false"
}
 *
 */
