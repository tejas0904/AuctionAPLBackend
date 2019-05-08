package com.apl.auction.model;

public class MyTeam implements Comparable<MyTeam>{
	String _id;
	String role;
	long timeStamp;
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	String firstName;
	String lastName;
	private Integer cost;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getCost() {
		return cost;
	}
	public void setCost(Integer cost) {
		this.cost = cost;
	}
	@Override
	public int compareTo(MyTeam o) {
		return this.timeStamp > o.timeStamp ? 1 : this.timeStamp < o.timeStamp ? -1 : 0;	
	}
	
}
