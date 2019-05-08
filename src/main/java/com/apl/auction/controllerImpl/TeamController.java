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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.apl.auction.controller.Controller;
import com.apl.auction.dataAccess.PlayerDBAccess;
import com.apl.auction.dataAccess.TeamDBAccess;
import com.apl.auction.model.Team;
import com.mongodb.BasicDBList;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("team")
public class TeamController extends ControllerImpl implements Controller{

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
		for (Session session : SocketCaptainImpl.captainPeers) {
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

	/**
	 * saveDreamTeam endpoint for the captain with session email
	 * @param team
	 * @return
	 */
	@POST
	@Path("saveDreamTeam")
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean saveDreamTeam(Team team) {
		try {
			TeamDBAccess teamDB = new TeamDBAccess();
			System.out.println(team);
			return teamDB.saveDreamTeam(team);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * getDreamTeam endpoint for the captain with session email 
	 * @return response with dreamteam of form <<ID,Budget,Role>>
	 */
	@GET
	@Path("getDreamTeam")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDreamTeam() {

		TeamDBAccess teamDB = new TeamDBAccess();
		PlayerDBAccess playerDB = new PlayerDBAccess();

		session = request.getSession();
		String email = (String) session.getAttribute("email");
		String teamName = playerDB.getTeamName(email);
		
		Object dreamTeam = teamDB.getDreamTeamofCaptain(teamName);
		return Response.ok(dreamTeam==null?"[]":dreamTeam).build();

	}

	/**
	 * getAllTeams endpoint for auctioneer screen
	 * @return json all teams object
	 */
	@GET
	@Path("getAllTeams")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTeams() {
		TeamDBAccess teamDB = new TeamDBAccess();
		BasicDBList allTeams = teamDB.getAllTeams();
		return Response.ok().entity(allTeams).build();
	}
	
	/**
	 * getNextPlayer endpoint for the Auctioneer/ Projection/ Captain screen
	 * @return Player object
	 */
	@GET
	@Path("getHundredDollarTeamList")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get100$TeamList() {
		try {
			TeamDBAccess teamDB = new TeamDBAccess();
			List<Team> hundred$teams = teamDB.get100$TeamList();
			
			return Response.ok().entity(hundred$teams).build();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return Response.ok().entity(e).build();
		}
	}
	
	@GET
	@Path("preview")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPreview()
	{
		TeamDBAccess teamDB = new TeamDBAccess();
		BasicDBList allTeams = teamDB.getAllTeams();
		projectorBroadcast(allTeams);
		return Response.ok().entity(allTeams!=null?true:false).build();
	}
	
}