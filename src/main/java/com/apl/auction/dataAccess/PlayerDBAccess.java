package com.apl.auction.dataAccess;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.client.MongoCollection;
import com.apl.auction.controllerImpl.TeamController;
import com.apl.auction.helper.Constant;
import com.apl.auction.helper.DatabaseConnectionAPL;
import com.apl.auction.model.DreamTeam3Player;
import com.apl.auction.model.Player;
import com.apl.auction.model.Team;

public class PlayerDBAccess {
	private static final int MIN_PLAYER_SELLING_COST = 100;
	DatabaseConnectionAPL dc;

	public boolean registerPlayer(Player newPlayer, String s3url) {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> players = dc.getCollection(Constant.PLAYERDATABASENAME);
		Document player = new Document();

		player.put("firstName", newPlayer.getFirstName());
		player.put("lastName", newPlayer.getLastName());
		player.put("address", newPlayer.getAddress());
		player.put("email", newPlayer.getEmail());
		player.put("mobileNumber", newPlayer.getMobileNumber());
		player.put("address", newPlayer.getAddress());
		player.put("streetAddress", newPlayer.getStreetAddress());
		player.put("city", newPlayer.getCity());

		player.put("state", newPlayer.getState());
		player.put("zipCode", newPlayer.getZipCode());
		player.put("country", newPlayer.getCountry());
		player.put("jerseyNumber", newPlayer.getJerseyNumber());
		player.put("sevaCollector", newPlayer.getSevaCollector());
		player.put("jerseySize", newPlayer.getJerseySize());
		player.put("isPaid", newPlayer.getIsPaid());
		player.put("isMine", false);
		player.put("isOpponent", false);
		player.put("photo", s3url);

		player.put("battingRating", newPlayer.getBattingRating());
		player.put("bowlingRating", newPlayer.getBowlingRating());
		player.put("fieldingRating", newPlayer.getFieldingRating());
		player.put("battingComment", newPlayer.getBattingComment());
		player.put("bowlingComment", newPlayer.getBowlingComment());
		player.put("fieldingComment", newPlayer.getFieldingComment());
		player.put("refName", newPlayer.getRefName());
		players.insertOne(player);
		dc.closeClient();
		return true;
	}

	public boolean updatePayment(List<Player> playerList) {

		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> students = dc.getCollection(Constant.PLAYERDATABASENAME);

		for (int i = 0; i < playerList.size(); i++) {
			BasicDBObject query = new BasicDBObject("_id", new ObjectId(playerList.get(i).get_id()));
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("isPaid", playerList.get(i).getIsPaid());
			BasicDBObject setQuery = new BasicDBObject();
			setQuery.append("$set", updateFields);
			students.updateOne(query, setQuery);
		}

		dc.closeClient();

		return true;
	}

	public List<Player> getPaymentDetails(String email) {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> players = dc.getCollection(Constant.PLAYERDATABASENAME);

		Document documentFind = new Document();
		documentFind.append("sevaCollector", email);

		List<Document> results = players.find(documentFind).into(new ArrayList<Document>());// FROM yourCollection
		List<Player> playerList = new ArrayList<Player>();
		for (Document result : results) {
			if (result != null) {
				Player p = new Player();
				p.set_id(result.getObjectId("_id").toString());
				p.setFirstName(result.get("firstName").toString());
				p.setLastName(result.get("lastName").toString());
				p.setEmail(result.get("email").toString());
				p.setMobileNumber(Long.parseLong(result.get("mobileNumber").toString()));
				p.setIsPaid(Boolean.parseBoolean(result.get("isPaid").toString()));
				playerList.add(p);
			}
		}
		dc.closeClient();
		return playerList;
	}

	public List<Player> getAllPlayers() {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> players = dc.getCollection(Constant.PLAYERDATABASENAME);

		List<Document> results = players.find().into(new ArrayList<Document>());// FROM yourCollection
		List<Player> playerList = new ArrayList<Player>();
		for (Document result : results) {
			if (result != null) {
				Player p = new Player();
				p.set_id(result.getObjectId("_id").toString());
				p.setFirstName(result.get("firstName").toString());
				p.setLastName(result.get("lastName") == null ? "" : result.get("lastName").toString());
				p.setTeamName(result.getString("teamName"));
				p.setRole(result.getString("role"));
				p.setBattingRating(Integer.parseInt(result.get("battingRating").toString()));
				p.setBowlingRating(Integer.parseInt(result.get("bowlingRating").toString()));
				p.setFieldingRating(Integer.parseInt(result.get("fieldingRating").toString()));
				if (result.getString("role")!=null && result.getString("role").equals("captain")) {
					p.setTimeStamp(1);

				} else if (result.getString("role")!=null && result.getString("role").equals("vice captain")) {
					p.setTimeStamp(2);

				}  else if (result.getString("role")!=null && result.getString("role").equals("associate captain")) {
					p.setTimeStamp(3);

				}  else if (result.getLong("timeStamp")!=null){
					p.setTimeStamp(result.getLong("timeStamp"));
				}
				p.setBattingComment(
						result.get("battingComment") == null ? "" : result.get("battingComment").toString());
				p.setBowlingComment(
						result.get("bowlingComment") == null ? "" : result.get("bowlingComment").toString());
				p.setFieldingComment(
						result.get("fieldingComment") == null ? "" : result.get("fieldingComment").toString());
				p.setPhoto(result.get("photo").toString());
				p.setCost(Integer.parseInt(result.get("cost") == null ? "0": result.get("cost").toString()));
				//p.setTimeStamp((result.getLong("timeStamp")==null?0:result.getLong("timeStamp")));
				playerList.add(p);
			}
		}
		dc.closeClient();
		return playerList;
	}

	public static int getRandom(int max){ 
		 return (int) (Math.random()*max); //incorrect always return zero return (int) (Math.random()*max); }
	}
	
	public Player getNextPlayer() {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> players = dc.getCollection(Constant.PLAYERDATABASENAME);
		Document documentFind = new Document();
		documentFind.append("teamName", null);
		List<Document> results = players.find(documentFind).into(new ArrayList<Document>());// FROM yourCollection
		if (results != null) {
			Document result = results.get(getRandom(results.size()));
			dc.closeClient();
			Player p = new Player();
			p.set_id(result.getObjectId("_id").toString());
			p.setFirstName(result.get("firstName").toString());
			p.setLastName(result.get("lastName") == null ? "" : result.get("lastName").toString());
			p.setTeamName(result.getString("teamName"));
			p.setRole(result.getString("role"));
			p.setBattingRating(Integer.parseInt(result.get("battingRating").toString()));
			p.setBowlingRating(Integer.parseInt(result.get("bowlingRating").toString()));
			p.setFieldingRating(Integer.parseInt(result.get("fieldingRating").toString()));
			p.setBattingComment(result.get("battingComment") == null ? "" : result.get("battingComment").toString());
			p.setBowlingComment(result.get("bowlingComment") == null ? "" : result.get("bowlingComment").toString());
			p.setFieldingComment(result.get("fieldingComment") == null ? "" : result.get("fieldingComment").toString());
			p.setPhoto(result.get("photo").toString());
			
			if(results.size()%11 == 0) {
				p.setBlind(true);
			}
			return p;
		} else {
			return null;
		}
	}
	/**
	 * playerSold endpoint to sell a player after nextPlayer 
	 * _id
	 * teamName
	 * cost
	 * @param player{_id}
	 * @return
	 */
	public Player soldPlayer(Player player) {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> players = dc.getCollection(Constant.PLAYERDATABASENAME);

		try {
		BasicDBObject query = new BasicDBObject("_id", new ObjectId(player.get_id()));
		BasicDBObject updateFields = new BasicDBObject();
		updateFields.append("teamName", player.getTeamName());
		updateFields.append("cost", player.getCost());
		Long timeStamp = System.currentTimeMillis();
		updateFields.append("timeStamp", timeStamp);
		BasicDBObject setQuery = new BasicDBObject();
		setQuery.append("$set", updateFields);
		players.updateOne(query, setQuery);
		player.setTimeStamp(timeStamp);
		return player;
		}catch(Exception ex) {
			System.out.println(ex);
			return null;
		}
	}
	
	/**
	 * Logger/login database access call
	 * @param email
	 * @param password
	 * @return teamName for the email
	 */
	public String getTeamNameOfCaptain(String email, String password) {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> players = dc.getCollection(Constant.PLAYERDATABASENAME);
		Document documentFind = new Document();
		documentFind.append("email", email);
		documentFind.append("password", password);
		Document playerDetails = players.find(documentFind).first();
		
		if(playerDetails==null)
			return null;
		else
			return playerDetails.getString("teamName");
	}
	
	// temp -- to get captain id from session email
	public String getTeamName(String email) {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> players = dc.getCollection(Constant.PLAYERDATABASENAME);
		Document documentFind = new Document();
		documentFind.append("email", email);
		Document playerDetails = players.find(documentFind).first();
		return playerDetails.get("teamName").toString();
	}	
	

	public boolean teamCostRemove() {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> playerDb = dc.getCollection(Constant.PLAYERDATABASENAME);

		BasicDBObject documentFind = new BasicDBObject("role", new BasicDBObject("$exists", false));
		

		//String md5Hex = DigestUtils
		//	      .md5Hex("1234").toUpperCase();
		
		//playerDb.updateMany(documentFind, new BasicDBObject().append("$set",new BasicDBObject("password",md5Hex)));
		playerDb.updateMany(documentFind, new BasicDBObject().append("$unset",new BasicDBObject("teamName","")));
		playerDb.updateMany(documentFind, new BasicDBObject().append("$unset",new BasicDBObject("cost","")));
//		playerDb.updateMany(documentFind, new BasicDBObject().append("$unset",new BasicDBObject("timeStamp","")));
//		
		dc.closeClient();
		return true;
	}
	
	public boolean addFields(List<Player> playerList)
	{
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> players = dc.getCollection(Constant.PLAYERDATABASENAME);
		for(int i=0;i<playerList.size();++i)
		{
			BasicDBObject query = new BasicDBObject("_id", new ObjectId(playerList.get(i).get_id()));
			Document documentFind = new Document();
			documentFind.append("timeStamp", false);
			BasicDBObject setQuery = new BasicDBObject();
			setQuery.append("$set", documentFind);
			players.updateOne(query, setQuery);
		}
		return true;
	}
	
	public boolean removeFields(List<Player> playerList)
	{
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> players = dc.getCollection(Constant.PLAYERDATABASENAME);
		for(int i=0;i<playerList.size();++i)
		{
			BasicDBObject query = new BasicDBObject("_id", new ObjectId(playerList.get(i).get_id()));
			Document documentFind = new Document();
			documentFind.append("isSold", false);
			BasicDBObject setQuery = new BasicDBObject();
			setQuery.append("$unset", documentFind);
			players.updateOne(query, setQuery);
		}
		return true;
	}
	
	public boolean teamCostIdToRemove(Player player) 
	{
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> students = dc.getCollection(Constant.PLAYERDATABASENAME);
		BasicDBObject query = new BasicDBObject("_id", new ObjectId(player.get_id()));
		BasicDBObject setQuery = new BasicDBObject();
		BasicDBObject setQuery1 = new BasicDBObject();
		
		setQuery.append("$unset",new BasicDBObject("cost",""));
		setQuery1.append("$unset",new BasicDBObject("teamName",""));
		students.updateOne(query, setQuery);
		students.updateOne(query, setQuery1);
		dc.closeClient();
		return true;
	}
	
	public boolean copyData() {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> playerCopyDB = dc.getCollection("playersTestData");
		MongoCollection<Document> playerDB = dc.getCollection(Constant.PLAYERDATABASENAME);
		List<Document> results = playerCopyDB.find().into(new ArrayList<Document>());
		for(Document result : results) {
			BasicDBObject query = new BasicDBObject("mobileNumber",result.get("mobileNumber"));
			BasicDBObject setQuery = new BasicDBObject();
			setQuery.append("$set", new BasicDBObject("photo",result.get("photo").toString()));
			playerDB.updateOne(query,setQuery);
		}
		return true;
	}
	
	public String getEmail()
	{
		String email="";
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> playerCopyDB = dc.getCollection("players");
		List<Document> results = playerCopyDB.find().into(new ArrayList<Document>());
		for(Document result : results) {
			email+=result.getString("email")+",";
		}
		return email.substring(0,email.length()-1);
	}
	
	public boolean resetPassword(String email, String oldPassword, String newPassword)
	{
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> playerDB = dc.getCollection(Constant.PLAYERDATABASENAME);
		
		BasicDBObject documentFind = new BasicDBObject("role", new BasicDBObject("$exists", true));
		documentFind.append("email", email);
		documentFind.append("password",oldPassword);
		Document playerDetails = playerDB.find(documentFind).first();
		
		if(playerDetails==null)
			return false;
		else {
			BasicDBObject where = new BasicDBObject("email",playerDetails.get("email").toString());
			BasicDBObject setQuery = new BasicDBObject();
			setQuery.append("$set", new BasicDBObject("password",newPassword));
			playerDB.updateOne(where, setQuery);
		}
		return true;
	}

}