package db.mongo;

import java.net.UnknownHostException;

import com.mongodb.MongoClient;

public class MongoConnection {
	
	MongoClient mongoClient;
	
	private MongoConnection(){
		try {
			mongoClient = new MongoClient( "ec2-54-87-196-110.compute-1.amazonaws.com" , 27017 );
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MongoClient getMongoClient(){
		return this.mongoClient;
	}
	
	private static MongoConnection mongoConnection;
	
	public static MongoConnection get(){
		if (mongoConnection == null){
			synchronized(MongoConnection.class){
				if (mongoConnection == null){
					mongoConnection = new MongoConnection();
				}
			}
		}
		return mongoConnection;
	}

}
