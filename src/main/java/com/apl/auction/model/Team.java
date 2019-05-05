package com.apl.auction.model;

import java.util.List;

public class Team {
	String teamName;
	int balance;
	
	String logo;
	String captain;
	String viceCaptain;
	String associatedCaptain;     
	List<MyTeam> myTeam;  
	List<DreamTeam3Player> dreamTeam3Player;
	int hundredDollarPlayerCount;
	
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	public int getHundredDollarPlayerCount() {
		return hundredDollarPlayerCount;
	}
	public void setHundredDollarPlayerCount(int hundredDollarPlayerCount) {
		this.hundredDollarPlayerCount = hundredDollarPlayerCount;
	}
	
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getCaptain() {
		return captain;
	}
	public void setCaptain(String captain) {
		this.captain = captain;
	}
	public String getViceCaptain() {
		return viceCaptain;
	}
	public void setViceCaptain(String viceCaptain) {
		this.viceCaptain = viceCaptain;
	}
	public String getAssociatedCaptain() {
		return associatedCaptain;
	}
	public void setAssociatedCaptain(String associatedCaptain) {
		this.associatedCaptain = associatedCaptain;
	}
	public List<MyTeam> getMyTeam() {
		return myTeam;
	}
	public void setMyTeam(List<MyTeam> myTeam) {
		this.myTeam = myTeam;
	}
	public List<DreamTeam3Player> getDreamTeam3Player() {
		return dreamTeam3Player;
	}
	public void setDreamTeam3Player(List<DreamTeam3Player> dreamTeam) {
		this.dreamTeam3Player = dreamTeam;
	}
	

}
