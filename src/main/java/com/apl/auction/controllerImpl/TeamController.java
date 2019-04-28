package com.apl.auction.controllerImpl;

import java.io.IOException;

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

import com.apl.auction.dataAccess.PlayerDBAccess;
import com.apl.auction.dataAccess.TeamDBAccess;
import com.apl.auction.model.Player;
import com.apl.auction.model.Team;


/**
 * Root resource (exposed at "myresource" path)
 */
@Path("team")
public class TeamController {
	
	@Context
	HttpServletRequest request;
	HttpSession session;

	// Test method
	@GET
	@Path("getit")
	public String getit() {
		return "getit!!!";
	}

	// Test method -- web socket
	@GET
	@Path("newTeam")
	public String newTeam() {
		    for(Session session: SocketImpl.peers){
		        try {
					session.getBasicRemote().sendText("Update with new teams");
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		return "getit!!!";
	}


	@POST
	@Path("teamDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	public String test(Team teamDetail) {
		try {
			TeamDBAccess teamDB = new TeamDBAccess();
			boolean isTeamRegistered = false;
			if (teamDetail != null) {
				
				isTeamRegistered = teamDB.registerTeam(teamDetail);
			}

			if (isTeamRegistered)
				return "success";
			else
				throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
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

}