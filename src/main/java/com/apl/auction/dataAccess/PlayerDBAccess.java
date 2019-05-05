package com.apl.auction.dataAccess;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.client.MongoCollection;

import com.apl.auction.helper.Constant;
import com.apl.auction.helper.DatabaseConnectionAPL;
import com.apl.auction.model.Player;

public class PlayerDBAccess {
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
				p.setBattingComment(
						result.get("battingComment") == null ? "" : result.get("battingComment").toString());
				p.setBowlingComment(
						result.get("bowlingComment") == null ? "" : result.get("bowlingComment").toString());
				p.setFieldingComment(
						result.get("fieldingComment") == null ? "" : result.get("fieldingComment").toString());
				p.setPhoto(result.get("photo").toString());

				playerList.add(p);
			}
		}
		dc.closeClient();
		return playerList;
	}

	public Document getNextPlayer() {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> players = dc.getCollection(Constant.PLAYERDATABASENAME);
		Document documentFind = new Document();
		documentFind.append("teamName",null );
		Document player = players.find(documentFind).first();// FROM yourCollection
		
		dc.closeClient();
		return player;
	}

	
	
	public boolean getTeamNameOfCaptain(String email, String password) {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> players = dc.getCollection(Constant.PLAYERDATABASENAME);
		Document documentFind = new Document();
		documentFind.append("email", email);
		documentFind.append("password", password);
		Document playerDetails = players.find(documentFind).first();
		//String teamName = "";
		
		if(playerDetails==null)
			return false;
		
		else
			return true;
		/*if(playerDetails!=null)
			teamName = "wrong email";
		
		if (playerDetails.getString("password").equals(password))
			
			teamName = playerDetails.getString("teamName");
		else
			teamName = "wrong password";

		dc.closeClient();
		return teamName;*/

	}
	
	
	public String getCaptainId(String email) {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> players = dc.getCollection(Constant.PLAYERDATABASENAME);
		Document documentFind = new Document();
		documentFind.append("email", email);
		Document playerDetails = players.find(documentFind).first();
		String pl =  playerDetails.get("_id").toString();
		return playerDetails.get("_id").toString();
	}

	public Player getDreamTeam(String email) {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> players = dc.getCollection(Constant.PLAYERDATABASENAME);

		Document documentFind = new Document();
		documentFind.append("isCaptain", true);
		documentFind.append("email", email);

		List<Document> results = players.find(documentFind).into(new ArrayList<Document>());// FROM yourCollection
		Player player = new Player();
		for (Document result : results) {
			if (result != null) {
				player.set_id(result.getObjectId("_id").toString());
				player.setDreamTeam((List<List<String>>) result.get("dreamTeam"));
			}
		}
		dc.closeClient();
		return player;
	}
	
	
	public Boolean setDreamTeam(Player playerDreamTeam) {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> teams = dc.getCollection(Constant.TEAMDATABASE);

		BasicDBObject query = new BasicDBObject("_id", new ObjectId(playerDreamTeam.get_id()));
		BasicDBObject updateFields = new BasicDBObject();
		updateFields.append("dreamTeam", playerDreamTeam.getDreamTeam());
		BasicDBObject setQuery = new BasicDBObject();
		setQuery.append("$set", updateFields);
		teams.updateOne(query, setQuery);

		dc.closeClient();

		return true;
	}

	public boolean postTemp(List<Player> playerList) {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> students = dc.getCollection(Constant.PLAYERDATABASENAME);

		/*for (int i = 0; i < playerList.size(); i++) {
			BasicDBObject query = new BasicDBObject("_id", new ObjectId(playerList.get(i).get_id()));
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("isMine", false);
			updateFields.append("isOpponent", false);
			BasicDBObject setQuery = new BasicDBObject();
			setQuery.append("$set", updateFields);
			students.updateOne(query, setQuery);
		}*/
		
		for (int i = 0; i < playerList.size(); i++) 
		{
			BasicDBObject query = new BasicDBObject("_id", new ObjectId(playerList.get(i).get_id()));
			//String id = playerList.get(i).get_id();
			
			//BasicDBObject updateFields = new BasicDBObject();
			//db.example.update({}, {$unset: {words:1}}, false, true);
			//updateFields.append("isMine", false);
			//updateFields.append("isOpponent", false);
			BasicDBObject setQuery = new BasicDBObject();
			//setQuery.append("$unset", updateFields);
			//db.domain.update({},{$unset: {affLink:1}},{multi: true});
			setQuery.append("$unset",new BasicDBObject("isMine",""));
			//setQuery.put("$unset",new BasicDBObject("isOpponent",""));
			students.updateOne(query, setQuery);
		}

		
		dc.closeClient();

		return true;
	}

	public boolean soldPlayer(Player player) {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> players = dc.getCollection(Constant.PLAYERDATABASENAME);

		
		BasicDBObject query = new BasicDBObject("_id", new ObjectId(player.get_id()));
		BasicDBObject updateFields = new BasicDBObject();
		updateFields.append("teamName", player.getTeamName());
		updateFields.append("cost", player.getCost());
		BasicDBObject setQuery = new BasicDBObject();
		setQuery.append("$set", updateFields);
		players.updateOne(query, setQuery);
		
		Document documentFind = new Document();
		documentFind.append("_id", player.get_id());
		Document playerDetails = players.find(documentFind).first();// FROM yourCollection
		
		return true;
	}
}