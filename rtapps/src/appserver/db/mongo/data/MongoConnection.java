package appserver.db.mongo.data;


import com.mongodb.MongoClient;

public class MongoConnection {
	
	MongoClient mongoClient;
	
	private MongoConnection(){
		mongoClient = new MongoClient( "ec2-54-87-196-110.compute-1.amazonaws.com" , 27017 );
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
