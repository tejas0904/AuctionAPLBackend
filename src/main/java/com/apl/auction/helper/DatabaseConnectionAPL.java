package com.apl.auction.helper;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;



public class DatabaseConnectionAPL {
	String dbName;
	MongoDatabase database;
	MongoClient client;

	public DatabaseConnectionAPL() {
		MongoClientURI uri = new MongoClientURI(
				"mongodb://" + Constant.USERNAME + ":" + Constant.PASSWORD + "@ds221416.mlab.com:21416/apl2019");
		this.client = new MongoClient(uri);
		this.database = this.client.getDatabase(uri.getDatabase());
	}
	
	public MongoCollection<Document> getCollection(String dbName) {
		MongoCollection<Document> collection =  this.database.getCollection(dbName);
		return collection;
	}
	
	public void closeClient() {
		this.client.close();
	}
}