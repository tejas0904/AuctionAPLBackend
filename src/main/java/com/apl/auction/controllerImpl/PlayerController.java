package com.apl.auction.controllerImpl;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.apl.auction.controller.Controller;
import com.apl.auction.dataAccess.PlayerDBAccess;
import com.apl.auction.model.Player;

@Path("player")
public class PlayerController extends ControllerImpl implements Controller {
	PlayerDBAccess dbAccessObj;

	@Context
	HttpServletRequest request;
	HttpSession session;

	// Test method -- web socket
	@GET
	@Path("newPlayer")
	public String newPlayer() {
		for (Session session : SocketImpl.peers) {
			try {
				session.getBasicRemote().sendText("Update with new players");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "getit!!!";
	}

	@POST
	@Path("playerDetails")
	@Consumes(MediaType.APPLICATION_JSON)

	public String test(Player playerDetail) {
		try {
			PlayerDBAccess playerDB = new PlayerDBAccess();
			boolean isPlayerRegistered = false;
			if (playerDetail != null) {
				String s3url = getImageUrl(playerDetail.getPhoto(), playerDetail.getImageFormat(),
						playerDetail.getMobileNumber());
				isPlayerRegistered = playerDB.registerPlayer(playerDetail, s3url);
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

	@GET
	@Path("getAllPlayers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Player> getAllPlayers() {
		try {
			PlayerDBAccess playerDB = new PlayerDBAccess();
			List<Player> playerList = playerDB.getAllPlayers();
			if (playerList != null) {
				return playerList;
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("getDreamTeam")
	@Produces(MediaType.APPLICATION_JSON)
	public Player getDreamTeam(@QueryParam("email") String email) {
		try {
			PlayerDBAccess playerDB = new PlayerDBAccess();
			Player player = playerDB.getDreamTeam(email);
			if (player != null) {
				return player;
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Path("setDreamTeam")
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean setDreamTeam(Player playerDreamTeam) {
		try {
			PlayerDBAccess playerDB = new PlayerDBAccess();
			Boolean isSetTeam = playerDB.setDreamTeam(playerDreamTeam);
			if (isSetTeam != null) {
				return isSetTeam;
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@GET
	@Path("getPlayerPayments")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Player> playerPayments(@QueryParam("email") String email) {
		try {
			PlayerDBAccess playerDB = new PlayerDBAccess();
			List<Player> playerList = playerDB.getPaymentDetails(email);
			if (playerList != null) {
				return playerList;
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Path("postPlayerPayments")
	@Consumes(MediaType.APPLICATION_JSON)
	public String playerPayments(List<Player> playerList) {
		try {
			PlayerDBAccess playerDB = new PlayerDBAccess();
			boolean isPlayerRegistered = false;
			if (playerList != null) {
				isPlayerRegistered = playerDB.updatePayment(playerList);
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

	/**
	 * Temporary post api for posting database manually
	 * 
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
				isPlayerRegistered = playerDB.postTemp(playerList);
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
