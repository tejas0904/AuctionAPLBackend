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
import javax.ws.rs.core.Response.Status;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import com.apl.auction.helper.Constant;
import com.apl.auction.dataAccess.PlayerDBAccess;
import com.apl.auction.dataAccess.TeamDBAccess;
import com.apl.auction.externalApi.S3ImageUpload;
import com.apl.auction.helper.ResponseLogin;

import com.apl.auction.model.Player;


import com.apl.auction.model.Team;
import com.google.gson.Gson;

import sun.misc.BASE64Decoder;


/**
 * Root resource (exposed at "myresource" path)
 */
@Path("player")
public class PlayerController {
	@Context
	HttpServletRequest request;
	HttpSession session;
	
	/*PlayerController() {
		TeamDBAccess teamDb = new TeamDBAccess();
		teamDb.getAllTeams();
	}*/

	// Test method
	
	TeamController teamController;
	TeamDBAccess teamDB;
	BasicDBList allTeams;
	public PlayerController() {
		teamController = new TeamController();
		teamDB = new TeamDBAccess();
		allTeams = teamDB.getAllTeams();
		for(int i=0;i<allTeams.size();i++)
		{
			teamController.hundredDollarPlayerBuyerTeamQueue.add((Team) allTeams.get(i));
		}
	}
	@GET
	@Path("getit")
	public String getit() {
		return "getit!!!";
	}

	// Test method -- web socket
	@GET
	@Path("newPlayer")
	public String newPlayer() {
		    for(Session session: SocketImpl.peers) {
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
				// String[] s = playerDetail.getPhoto().split(".");
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
			return playerList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GET
	@Path("getNextPlayer")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNextPlayer() {
		try {
			PlayerDBAccess playerDB = new PlayerDBAccess();
			Player player = playerDB.getNextPlayer();
			projectorBroadcast(player);
			
			return Response.ok().entity(player).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@POST
	@Path("playerSold")
	@Produces(MediaType.APPLICATION_JSON)
	public Response playerSold(Player player) {
		try {
			PlayerDBAccess playerDB = new PlayerDBAccess();
			Boolean flag=playerDB.soldOrUndoPlayer(player);
			projectorBroadcast(player);
			
			return Response.ok().entity(flag).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void projectorBroadcast(Player player) {
		if(SocketProjectorImpl.peers!=null)
		{
			Session session = SocketProjectorImpl.peers;
		    try {
		    	session.getBasicRemote().sendText(new Gson().toJson(player));
			} catch (IOException e) {
				e.printStackTrace();
			}
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

	@POST
	@Path("teamCostRemove")
	@Consumes(MediaType.APPLICATION_JSON)
	public String teamCostIdToRemove(List<Player> playerList) {
		try {
			PlayerDBAccess playerDB = new PlayerDBAccess();
			boolean isPlayerRegistered = false;
			if (playerList != null) {
				isPlayerRegistered = playerDB.teamCostIdToRemove(playerList);
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
	 * 
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
	
	/*@POST
	@Path("teamCostIdRemove")
	@Consumes(MediaType.APPLICATION_JSON)
	public String undoPlayerSold(Player player) {
		try {
			PlayerDBAccess playerDB = new PlayerDBAccess();
			boolean isPlayerRegistered = false;
			if (player != null) {
				isPlayerRegistered = playerDB.soldPlayer(player);
			}
			if (isPlayerRegistered)
				return "success";
			else
				throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}*/
	
	
	public String getImageUrl(String playerImage, String ext, long mobileNumber) {

		String base64Image = playerImage;

		byte[] jpgBytes = null;
		try {
			jpgBytes = (new BASE64Decoder()).decodeBuffer(base64Image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(jpgBytes);

		return uploadFile(bis, null, ext, String.valueOf(mobileNumber), jpgBytes.length + "");
	}

	
	@POST
	@Path("uploadimage")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail, String ext,
			@HeaderParam("studentId") String mobileNumber, @HeaderParam("fileSize") String fileSize) {

		// try {
		// byte[] b = new byte[Integer.parseInt(fileSize)];
		// uploadedInputStream.read(b);
		// FileOutputStream fos = new FileOutputStream(new
		// File("/Users/i862354/output.jpg"));
		// fos.write(b, 0, b.length);
		// fos.flush();
		// fos.close();
		// System.out.println("image created");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// check if all form parameters are provided

		S3ImageUpload imageUpload = new S3ImageUpload();
		String s3url = imageUpload.imageUpload(uploadedInputStream, mobileNumber + "." + ext, Long.parseLong(fileSize));
		try {
			uploadedInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (s3url == null) {
			return "No Photo Available";
		} else {

			return s3url;
		}

	}

	
}