package com.apl.auction.controllerImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.apl.auction.controller.Controller;
import com.apl.auction.dataAccess.PlayerDBAccess;


/**
 * Root resource (exposed at "myresource" path)
 */
@Path("logger")
public class loginController extends ControllerImpl implements Controller {

	@Context
	HttpServletRequest request;
	HttpSession session;

	
	/**
	 * Login endpoint for the captains
	 * @param email
	 * @param password
	 * @return teamname and total budget(160000) static for now*
	 */
	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@QueryParam("email") String email, @QueryParam("password") String password) {
		session = request.getSession();
		PlayerDBAccess playerDB = new PlayerDBAccess();
		String teamName = playerDB.getTeamNameOfCaptain(email, password) ;
		System.out.println("LOGGER :: LOGIN --> "+email);
		if(teamName!= null){
			String json = "{\"teamName\": \"" + teamName
					+ "\",\"teamBudget\":16000}";
			session.setAttribute("email", email);
			
			return Response.ok(json).build();
		} else {
			String error = "{\"error\":\"Invalid Username or Password please check\"}";
			return Response.status(Status.UNAUTHORIZED).entity(error).build();
		}

	}

	/**
	 * Logout for a captain screen
	 * @return
	 */
	@GET
	@Path("logout")
	public Response logout() {
		System.out.println("LOGGER :: LOGOUT");
		session = request.getSession();
		session.invalidate();
		return Response.status(Status.UNAUTHORIZED).build();
	}

}
