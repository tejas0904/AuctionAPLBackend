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

@ServerEndpoint("/captainSocket/{mail}")
public class SocketCaptainImpl {

	public static Set<Session> captainPeers = Collections.synchronizedSet(new HashSet<Session>());

	@OnMessage
	public String onMessage(String message, Session session, @PathParam("mail") String mail) {
		try {
			System.out.println("received message from client " + mail);
			for (Session s : captainPeers) {
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
		return "message was received by socket mediator and processed: " + message;
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("mail") String mail) {
		System.out.println("Mediator: opened websocket channel for captain :: " + mail);
		captainPeers.add(session);

		try {
			session.getBasicRemote().sendText("good to be in touch");
		} catch (IOException e) {
		}
	}

	@OnClose
	public void onClose(Session session, @PathParam("mail") String mail) {
		System.out.println("Mediator: closed websocket channel for captain " + mail);
		captainPeers.remove(session);
	}
}