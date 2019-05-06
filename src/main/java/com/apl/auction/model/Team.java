package com.apl.auction.model;

import java.util.List;

public class Team implements Comparable<Team>{
	String teamName;
	int balance;
	int hundred$flag;
	
	String logo;
	String captain;
	String viceCaptain;
	String associatedCaptain;     
	List<MyTeam> myTeam;  
	private List<List<DreamTeam3Player>> dreamTeam;
	int hundredDollarPlayerCount;
	public int getHundred$flag() {
		return hundred$flag;
	}
	public void setHundred$flag(int hundred$flag) {
		this.hundred$flag = hundred$flag;
	}
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
	public List<List<DreamTeam3Player>> getDreamTeam() {
		return dreamTeam;
	}
	public void setDreamTeam(List<List<DreamTeam3Player>> dreamTeam) {
		this.dreamTeam = dreamTeam;
	}
	@Override
	public int compareTo(Team o) {
		return this.hundred$flag > o.hundred$flag ? 1 : this.hundred$flag < o.hundred$flag ? -1 : 0;
	}
	
	

}
