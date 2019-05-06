package com.apl.auction.controllerImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import com.apl.auction.helper.Constant;
import com.apl.auction.controller.Controller;
import com.apl.auction.dataAccess.PlayerDBAccess;
import com.apl.auction.dataAccess.TeamDBAccess;
import com.apl.auction.externalApi.S3ImageUpload;
import com.apl.auction.helper.ResponseLogin;

import com.apl.auction.model.Player;


import com.apl.auction.model.Team;
import com.google.gson.Gson;

import sun.misc.BASE64Decoder;


/**
 * Player details fetch resource
 * @author i862354
 *
 */
@Path("player")
public class PlayerController extends ControllerImpl implements Controller{
	@Context
	HttpServletRequest request;
	HttpSession session;
	
	TeamController teamController;
	TeamDBAccess teamDB;
	BasicDBList allTeams;

	// -------------------------------------------------------- TEST API's -------------------------------------------------------------
	
	@GET
	@Path("getit")
	public String getit() {
		return "getit!!!";
	}

	// Test method -- web socket
	@GET
	@Path("newPlayer")
	public String newPlayer() {
		    for(Session session: SocketCaptainImpl.captainPeers) {
		        try {
					session.getBasicRemote().sendText("Update with new players");
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		return "getit!!!";
	}


	@GET
	@Path("getAllPlayers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Player> getAllPlayers() {
		try {
			PlayerDBAccess playerDB = new PlayerDBAccess();
			List<Player> playerList = playerDB.getAllPlayers();
			return playerList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// -------------------------------------------------------- MAIN API's -------------------------------------------------------------
	
	/**
	 * getNextPlayer endpoint for the Auctioneer/ Projection/ Captain screen
	 * @return Player object
	 */
	@GET
	@Path("getNextPlayer")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNextPlayer() {
		try {
			PlayerDBAccess playerDB = new PlayerDBAccess();
			Player player = playerDB.getNextPlayer();
			
			projectorBroadcast(player);
			
			captainBroadcast(player);
			
			return Response.ok().entity(player).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * playerSold endpoint to sell a "nextPlayer" player record from playerDB 
	 * _id
	 * teamName
	 * cost
	 * @param player
	 * @return Boolean 
	 */
	@POST
	@Path("playerSold")
	@Produces(MediaType.APPLICATION_JSON)
	public Response playerSold(Player player) {
		try {
			PlayerDBAccess playerDB = new PlayerDBAccess();
			TeamDBAccess teamDB = new TeamDBAccess();
			player = playerDB.soldPlayer(player);
			
			if(player.getCost() == 100)
				{
					List<Team> hundred$teams = teamDB.set100$TeamList(player.getTeamName());
					captainBroadcast(hundred$teams);
				}
			
			if(!player.getIsEdit())
			projectorBroadcast(player);
			
			captainBroadcast(player);
			
			return Response.ok().entity(player!=null?true:false).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// -------------------------------------------------------- CLEAN UP API's -------------------------------------------------------------
	
	
	/**
	 * Temporary method for posting into DB from postman
	 * @param playerList
	 * @return
	 */
	@POST
	@Path("postTemp")
	@Consumes(MediaType.APPLICATION_JSON)
	public String postTemp(List<Player> playerList) {
		try {
			PlayerDBAccess playerDB = new PlayerDBAccess();
			boolean isPlayerRegistered = false;
			if (playerList != null) {
				isPlayerRegistered = playerDB.removeFields(playerList);
			}
			if (isPlayerRegistered)
				return "success";
			else
				throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	

	// -- TODO -- Remove <Consumes PlayerList> and remove from whole database itself @Rajan
	/**
	 * Data Clean up for removing cost and teamName from all players in Player DB
	 * @param playerList
	 * @return Boolean
	 */
	@POST
	@Path("teamReset")
	public String removeFields() {
		try {
			PlayerDBAccess playerDB = new PlayerDBAccess();
			if (playerDB.teamCostRemove())
				return "success";
			else
				throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	
	// -- TODO -- Edit player that has been sold for Auctioneer screen @Rajan
	/**
	 * Remove a players team from Player DB
	 * @param player
	 * @return
	 */
	@POST
	@Path("teamCostIdRemove")
	@Consumes(MediaType.APPLICATION_JSON)
	public String teamCostIdToRemove(Player player) {
		try {
			PlayerDBAccess playerDB = new PlayerDBAccess();
			boolean isPlayerRegistered = false;
			if (player != null) {
				isPlayerRegistered = playerDB.teamCostIdToRemove(player);
			}
			if (isPlayerRegistered)
				return "success";
			else
				throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	
}