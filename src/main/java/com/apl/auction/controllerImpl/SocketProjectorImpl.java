package com.apl.auction.controllerImpl;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/projector/{mail}")
public class SocketProjectorImpl {

	public static Session peers;

	@OnMessage
	public String onMessage(String message, Session session, @PathParam("mail") String mail) {
		/*try {
			//JSONObject jObj = new JSONObject(message);
			System.out.println("received message from client " + mail);
			for (Session s : peers) {
				try {
					s.getBasicRemote().sendText(message);
					System.out.println("send message to peer ");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "message was received by socket mediator and processed: " + message;*/
		return " ";
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("mail") String mail) {
		System.out.println("mediator: opened websocket channel for projector " + mail);
		peers=session;

		try {
			session.getBasicRemote().sendText("good to be in touch");
		} catch (IOException e) {
		}
	}

	@OnClose
	public void onClose(Session session, @PathParam("mail") String mail) {
		System.out.println("mediator: closed websocket channel for projector " + mail);
		peers=null;
	}
}