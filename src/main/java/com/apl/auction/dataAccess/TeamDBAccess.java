package com.apl.auction.dataAccess;

import org.bson.Document;

import com.apl.auction.helper.Constant;
import com.apl.auction.helper.DatabaseConnectionAPL;
import com.apl.auction.model.Team;
import com.mongodb.client.MongoCollection;


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

	
}