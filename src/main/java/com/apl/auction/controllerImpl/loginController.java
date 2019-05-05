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

	
	
	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@QueryParam("email") String email, @QueryParam("password") String password) {
		session = request.getSession();
		PlayerDBAccess playerDB = new PlayerDBAccess();
		
		if(playerDB.verifyEmailPassword(email, password))
		{
			System.out.println("HERE In LOGIN");
			session.setAttribute("email", email);
			session.setAttribute("password", password);
			return Response.status(Status.ACCEPTED).build();
		}
		else
		{
			return Response.status(Status.UNAUTHORIZED).build();
		}
			
		
		/*if (!playerDB.getTeamNameOfCaptain(email, password))//.equals("wrong password")) {
			String json = "{\"teamName\": \"" + playerDB.getTeamNameOfCaptain(email, password)
					+ "\",\"teamBudget\":16000}";
			return Response.ok(json).build();
		} else {
			S error = playerDB.getTeamNameOfCaptain(email, password);
			return Response.status(Status.UNAUTHORIZED).entity(error).build();
		}*/

	}

	@GET
	@Path("logout")
	public Response logout() {
		System.out.println("here");
		session = request.getSession();
		session.invalidate();
		return Response.status(Status.UNAUTHORIZED).build();
	}

}
