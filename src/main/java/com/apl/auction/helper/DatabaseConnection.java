package com.apl.auction.helper;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DatabaseConnection {
	String dbName;
	MongoDatabase database;
	MongoClient client;

	public DatabaseConnection() {
		MongoClientURI uri = new MongoClientURI(
				"mongodb://" + Constant.USERNAME + ":" + Constant.PASSWORD + "@ds147789.mlab.com:47789/coen6313");
		this.client = new MongoClient(uri);
		this.database = this.client.getDatabase(uri.getDatabase());
	}

	public MongoCollection<Document> getCollection(String dbName) {
		return this.database.getCollection(dbName);
	}
	
	public void closeClient(){
		this.client.close();
	}
}