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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.apl.auction.dataAccess.PlayerDBAccess;
import com.apl.auction.dataAccess.TeamDBAccess;
import com.apl.auction.model.Player;
import com.apl.auction.model.Team;
import sun.misc.BASE64Decoder;


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
	
	
	
	
	@POST
	@Path("saveDreamTeam")
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean saveDreamTeam(Team team) {
		try {
			TeamDBAccess teamDB = new TeamDBAccess();
				
			
			return teamDB.saveTeam(team);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@POST
	@Path("getDreamTeam")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getDreamTeam() {
		
			TeamDBAccess teamDB = new TeamDBAccess();
			PlayerDBAccess playerDB = new PlayerDBAccess();
			
			session = request.getSession();
			String email = (String) session.getAttribute("email");
			String captainId = playerDB.getCaptainId(email);
			Object dreamTeam = teamDB.getDreamTeamofCaptain(captainId);
			return Response.ok(dreamTeam).build();
			
			
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
	@Path("getAllTeams")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTeams() {
		TeamDBAccess teamDB = new TeamDBAccess();
		BasicDBList allTeams = teamDB.getAllTeams();
		
		return Response.ok().entity(allTeams).build();
		
	}

	
	
	

	

}