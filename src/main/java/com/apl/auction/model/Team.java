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
	List<List<DreamTeam>> dreamTeam;
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
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
	public List<List<DreamTeam>> getDreamTeam() {
		return dreamTeam;
	}
	public void setDreamTeam(List<List<DreamTeam>> dreamTeam) {
		this.dreamTeam = dreamTeam;
	}
	

}
