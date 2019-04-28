package com.apl.auction.dataAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.Response;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.apl.auction.helper.Constant;
import com.apl.auction.helper.DatabaseConnectionAPL;
import com.apl.auction.model.DreamTeam3Player;
import com.apl.auction.model.MyTeam;
import com.apl.auction.model.Player;
import com.apl.auction.model.Team;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class TeamDBAccess {
	DatabaseConnectionAPL dc;

	public boolean registerTeam(Team team) {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> teams = dc.getCollection(Constant.TEAMDATABASE);
		MongoCollection<Document> players = dc.getCollection(Constant.PLAYERDATABASENAME);
		Document newTeam = new Document();
		
		newTeam.put("teamName", team.getTeamName());
		newTeam.put("balance", team.getBalance());
		newTeam.put("logo", team.getLogo());
		Document documentFind = new Document();
		documentFind.append("firstName", team.getCaptain());
		documentFind.append("isCaptain", true);
		Document playerDetails = players.find(documentFind).first();
		newTeam.put("captain", playerDetails.getObjectId("_id").toString());
		
		documentFind.clear();
		String viceCaptain[] = team.getViceCaptain().split(" ");
		documentFind.append("firstName", viceCaptain[0]);
		documentFind.append("lastName", viceCaptain[1]);
		playerDetails = null;
		playerDetails = players.find(documentFind).first();
		newTeam.put("viceCaptain", playerDetails.getObjectId("_id").toString());
		
		
		
		documentFind.clear();
		String associatedCaptain[] = team.getAssociatedCaptain().split(" ");
		documentFind.append("firstName", associatedCaptain[0]);
		documentFind.append("lastName", associatedCaptain[1]);
		playerDetails = null;
		playerDetails = players.find(documentFind).first();
		newTeam.put("associatedCaptain", playerDetails.getObjectId("_id").toString());
		
		teams.insertOne(newTeam);
		dc.closeClient();
		return true;
	}
	
	
	
	public boolean saveTeam(Team team) {
		try {
			dc = new DatabaseConnectionAPL();
			MongoCollection<Document> teams = dc.getCollection(Constant.TEAMDATABASE);			
			BasicDBObject query = new BasicDBObject("teamName", team.getTeamName());
			BasicDBObject updateFields = new BasicDBObject();
			
			BasicDBList set = new BasicDBList();
			for(DreamTeam3Player dreamTeam3Player: team.getDreamTeam3Player()) {
				BasicDBObject player3Json = new BasicDBObject();
				
				BasicDBList playerIds = new BasicDBList();
				for (String playerId : dreamTeam3Player.getPlayerId()) {
					playerIds.add(playerId);
				}
				
				player3Json.put("playerId", playerIds);
				player3Json.put("budget", dreamTeam3Player.getBudget());
				
				set.add(player3Json);
			}
			updateFields.append("dreamTeam", set);
			BasicDBObject setQuery = new BasicDBObject();
			setQuery.append("$set", updateFields);
			teams.updateOne(query, setQuery);
			dc.closeClient();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}



	public Object getDreamTeamofCaptain(String captainId) {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> teams = dc.getCollection(Constant.TEAMDATABASE);
		Document team = new Document();
		Document documentFind = new Document();
		documentFind.append("captain", captainId);
		team = teams.find(documentFind).first();

		return team.get("dreamTeam");
	}



	public BasicDBList getAllTeams() {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> teams = dc.getCollection(Constant.TEAMDATABASE);
		MongoCursor<Document> teamCursor = teams.find().iterator();
		BasicDBList allTeams = new BasicDBList();
		
		PlayerDBAccess playerDB = new PlayerDBAccess();
		List<Player> allPlayers = playerDB.getAllPlayers();
		HashMap<String,List<MyTeam>> allPlayersHashMap = new HashMap<>();
		
		for(int i =0;i<allPlayers.size();i++) {
			if(allPlayers.get(i).getTeamName()==null|| allPlayers.get(i).getTeamName().isEmpty()) {
				continue;
			}
			List<MyTeam> listMyTeam = allPlayersHashMap.get(allPlayers.get(i).getTeamName());
			if (listMyTeam == null) {
				listMyTeam = new ArrayList<>();
				allPlayersHashMap.put(allPlayers.get(i).getTeamName(), listMyTeam);
			}
			MyTeam myTeam = new MyTeam();
			myTeam.setId(allPlayers.get(i).get_id());
			myTeam.setFirstName(allPlayers.get(i).getFirstName());
			myTeam.setLastName(allPlayers.get(i).getLastName());
			listMyTeam.add(myTeam);
		}
		while (teamCursor.hasNext()) {
			
			Document teamDocument = teamCursor.next();
			Team team = new Team();
			team.setTeamName(teamDocument.getString("teamName"));
			team.setLogo(teamDocument.getString("logo"));
			team.setCaptain(teamDocument.getString("captain"));
			team.setViceCaptain(teamDocument.getString("viceCaptain"));
			team.setAssociatedCaptain(teamDocument.getString("associatedCaptain"));
			List<MyTeam> myTeam = allPlayersHashMap.get(teamDocument.getString("teamName"));
			team.setMyTeam(myTeam);
	        allTeams.add(team);
	    }
		
		return allTeams;
	}
	
	

	
}