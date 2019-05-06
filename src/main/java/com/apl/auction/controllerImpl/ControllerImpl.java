package com.apl.auction.controllerImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.websocket.Session;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.apl.auction.controller.Controller;
import com.apl.auction.dataAccess.PlayerDBAccess;
import com.apl.auction.externalApi.S3ImageUpload;
import com.apl.auction.model.Player;
import com.apl.auction.model.Team;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;

import sun.misc.BASE64Decoder;

public abstract class ControllerImpl implements Controller{
	
	@POST
	@Path("getit")
	public String getit(Player playerDetail) {
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
	
	// -------------------------------------------------------- BROADCAST API's -------------------------------------------------------------
	
	protected void captainBroadcast(Player player) {
		for (Session session : SocketCaptainImpl.captainPeers) {
			try {
				BasicDBObject jsonPayload = new BasicDBObject();
				jsonPayload.put("type", "player");
				jsonPayload.put("json", new Gson().toJson(player));
				
				session.getBasicRemote().sendText(jsonPayload.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void projectorBroadcast(Player player) {
		if (SocketProjectorImpl.peers != null) {
			Session session = SocketProjectorImpl.peers;
			try {
				session.getBasicRemote().sendText(new Gson().toJson(player));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void captainBroadcast(List<Team> hundred$teams) {
		for (Session session : SocketCaptainImpl.captainPeers) {
			try {
				BasicDBObject jsonPayload = new BasicDBObject();
				jsonPayload.put("type", "team");
				jsonPayload.put("json", new Gson().toJson(hundred$teams));
				session.getBasicRemote().sendText(new Gson().toJson(jsonPayload));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@POST
	@Path("uploadimage")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail, String ext,
			@HeaderParam("studentId") String mobileNumber, @HeaderParam("fileSize") String fileSize) {
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
}
