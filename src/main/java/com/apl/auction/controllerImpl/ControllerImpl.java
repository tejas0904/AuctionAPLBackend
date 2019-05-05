package com.apl.auction.controllerImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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

import sun.misc.BASE64Decoder;

public class ControllerImpl implements Controller{
	
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
