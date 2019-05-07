package com.apl.auction.dataAccess;

import java.util.ArrayList;
import java.util.Collections;
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

	/**
	 * DB call for saving dream team for each team
	 * 
	 * @param team
	 * @return
	 */
	public boolean saveDreamTeam(Team team) {
		try {
			dc = new DatabaseConnectionAPL();
			MongoCollection<Document> teams = dc.getCollection(Constant.TEAMDATABASE);
			BasicDBObject query = new BasicDBObject("teamName", team.getTeamName());
			BasicDBObject updateFields = new BasicDBObject();

			BasicDBList set = new BasicDBList();
			for (List<DreamTeam3Player> dreamTeam3Player : team.getDreamTeam()) {
				BasicDBList positionPlayers = new BasicDBList();

				for (DreamTeam3Player dreamTeam : dreamTeam3Player) {
					BasicDBObject player3Json = new BasicDBObject();
					player3Json.put("playerId", dreamTeam.getPlayerId());
					player3Json.put("budget", dreamTeam.getBudget());
					positionPlayers.add(player3Json);

				}
				set.add(positionPlayers);
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

	/**
	 * DB call to get dream team
	 * 
	 * @param teamName
	 * @return dreamTeam for a captain in Team DB
	 */
	public Object getDreamTeamofCaptain(String teamName) {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> teams = dc.getCollection(Constant.TEAMDATABASE);
		Document team = new Document();
		Document documentFind = new Document();
		documentFind.append("teamName", teamName);
		team = teams.find(documentFind).first();
		return team.get("dreamTeam");
	}

	/**
	 * DB call to get all teams for auctioneer screen
	 * 
	 * @return list of all teams from Team DB
	 */
	public BasicDBList getAllTeams() {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> teams = dc.getCollection(Constant.TEAMDATABASE);
		MongoCursor<Document> teamCursor = teams.find().iterator();
		BasicDBList allTeams = new BasicDBList();

		PlayerDBAccess playerDB = new PlayerDBAccess();
		List<Player> allPlayers = playerDB.getAllPlayers();
		HashMap<String, List<MyTeam>> allPlayersHashMap = new HashMap<>();

		for (int i = 0; i < allPlayers.size(); i++) {
			if (allPlayers.get(i).getTeamName() == null || allPlayers.get(i).getTeamName().isEmpty()) {
				continue;
			}
			List<MyTeam> listMyTeam = allPlayersHashMap.get(allPlayers.get(i).getTeamName());
			if (listMyTeam == null) {
				listMyTeam = new ArrayList<>();
				allPlayersHashMap.put(allPlayers.get(i).getTeamName(), listMyTeam);
			}
			MyTeam myTeam = new MyTeam();
			myTeam.set_id(allPlayers.get(i).get_id());
			myTeam.setFirstName(allPlayers.get(i).getFirstName());
			myTeam.setLastName(allPlayers.get(i).getLastName());
			myTeam.setCost(allPlayers.get(i).getCost());
			myTeam.setRole(allPlayers.get(i).getRole());

			if (allPlayers.get(i).getRole().equals("captain")) {
				myTeam.setTimeStamp(1);

			} else if (allPlayers.get(i).getRole().equals("vice captain")) {
				myTeam.setTimeStamp(2);

			} else if (allPlayers.get(i).getRole().equals("associate captain")) {
				myTeam.setTimeStamp(3);

			} else {
				myTeam.setTimeStamp(allPlayers.get(i).getTimeStamp());
			}
			listMyTeam.add(myTeam);
		}
		while (teamCursor.hasNext()) {

			Document teamDocument = teamCursor.next();
			Team team = new Team();
			team.setTeamName(teamDocument.getString("teamName"));
			team.setLogo(teamDocument.getString("logo"));
//			team.setCaptain(teamDocument.getString("captain"));
//			team.setViceCaptain(teamDocument.getString("viceCaptain"));
//			team.setAssociatedCaptain(teamDocument.getString("associatedCaptain"));
			List<MyTeam> myTeam = allPlayersHashMap.get(teamDocument.getString("teamName"));
			team.setMyTeam(myTeam);
			// team.setHundredDollarPlayerCount(team.getHundredDollarPlayerCount()+1);
			allTeams.add(team);
		}

		return allTeams;
	}

	public List<Team> set100$TeamList(String teamName) {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> teams = dc.getCollection(Constant.TEAMDATABASE);
		Document team = new Document();
		Document documentFind = new Document();
		documentFind.append("teamName", teamName);
		team = teams.find(documentFind).first();
		Document test = teams.find().sort(new BasicDBObject("hundred$flag", -1)).limit(1).first();
		int hundred$flagMaxIndex = test.getInteger("hundred$flag");
		BasicDBObject updateFields = new BasicDBObject();

		updateFields.append("hundred$flag", hundred$flagMaxIndex + 1);
		BasicDBObject setQuery = new BasicDBObject();
		setQuery.append("$set", updateFields);
		teams.updateOne(team, setQuery);

		return get100$TeamList();
	}

	public List<Team> get100$TeamList() {
		dc = new DatabaseConnectionAPL();
		MongoCollection<Document> teams = dc.getCollection(Constant.TEAMDATABASE);
		List<Document> results = teams.find().into(new ArrayList<Document>());
		List<Team> final100$Teams = new ArrayList<Team>();
		for (Document result : results) {
			Team hundred$team = new Team();
			hundred$team.setTeamName(result.getString("teamName"));
			hundred$team.setLogo(result.getString("logo"));
			int hundred$flag = result.getInteger("hundred$flag") != null ? result.getInteger("hundred$flag") : 0;
			hundred$team.setHundred$flag(hundred$flag);
			final100$Teams.add(hundred$team);

		}
		Collections.sort(final100$Teams);
		return final100$Teams;
	}

}