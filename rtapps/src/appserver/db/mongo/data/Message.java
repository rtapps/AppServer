package appserver.db.mongo.data;

import org.springframework.data.annotation.Id;

public class Message {
	
    @Id
    private String id;
    
    private String applicationId;
    private String header;
    private String body;
    private String fileUrl;
    private long lastUpdateDate;
    private boolean isExists;
    
    public Message (String applicationId, String header, String body, String fileUrl, long lastUpdateDate, boolean isExists){
    	this.applicationId = applicationId;
    	this.header = header;
    	this.body = body;
    	this.fileUrl = fileUrl;
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
    
    public String getFileUrl(){
    	return this.fileUrl;
    }
    
    public long getLastUpdateDate(){
    	return this.lastUpdateDate;
    }
    
    public boolean isExists(){
    	return this.isExists;
    }
    
    public String getId(){
    	return this.id;
    }

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
