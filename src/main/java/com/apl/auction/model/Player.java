package com.apl.auction.model;

import java.util.List;

public class Player {

	private String _id;
	private String firstName;
	private String lastName;
	private String teamName;
	
	private String email;
	private Long mobileNumber;
	private String address;
	private String streetAddress;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	private Integer jerseyNumber;
	private String sevaCollector;
	private String jerseySize;
	private Boolean isPaid;
	private Boolean isMine;
	private Boolean isOpponent;
	private String photo;
	private Integer battingRating;
	private Integer bowlingRating;
	private Integer fieldingRating;
	private String battingComment;
	private String bowlingComment;
	private String fieldingComment;
	private String imageFormat;
	private String refName;
	private List<List<String>> dreamTeam;
	
	public Player() {}
	
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


	public String getStreetAddress() {
		return streetAddress;
	}


	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getZipCode() {
		return zipCode;
	}


	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}

	public String getSevaCollector() {
		return sevaCollector;
	}


	public void setSevaCollector(String sevaCollector) {
		this.sevaCollector = sevaCollector;
	}

	public String getPhoto() {
		return photo;
	}


	public void setPhoto(String photo) {
		this.photo = photo;
	}



	public String getBattingComment() {
		return battingComment;
	}


	public void setBattingComment(String battingComment) {
		this.battingComment = battingComment;
	}


	public String getBowlingComment() {
		return bowlingComment;
	}


	public void setBowlingComment(String bowlingComment) {
		this.bowlingComment = bowlingComment;
	}


	public String getFieldingComment() {
		return fieldingComment;
	}


	public void setFieldingComment(String fieldingComment) {
		this.fieldingComment = fieldingComment;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}

	public String getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getJerseySize() {
		return jerseySize;
	}

	public void setJerseySize(String jerseySize) {
		this.jerseySize = jerseySize;
	}

	public Integer getBattingRating() {
		return battingRating;
	}

	public void setBattingRating(Integer battingRating) {
		this.battingRating = battingRating;
	}

	public Integer getBowlingRating() {
		return bowlingRating;
	}

	public void setBowlingRating(Integer bowlingRating) {
		this.bowlingRating = bowlingRating;
	}

	public Integer getFieldingRating() {
		return fieldingRating;
	}

	public void setFieldingRating(Integer fieldingRating) {
		this.fieldingRating = fieldingRating;
	}

	public void setJerseyNumber(Integer jerseyNumber) {
		this.jerseyNumber = jerseyNumber;
	}

	public Integer getJerseyNumber() {
		return jerseyNumber;
	}

	public List<List<String>> getDreamTeam() {
		return dreamTeam;
	}

	public void setDreamTeam(List<List<String>> dreamTeam) {
		this.dreamTeam = dreamTeam;
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	public Boolean getIsMine() {
		return isMine;
	}

	public void setIsMine(Boolean isMine) {
		this.isMine = isMine;
	}

	public Boolean getIsOpponent() {
		return isOpponent;
	}

	public void setIsOpponent(Boolean isOpponent) {
		this.isOpponent = isOpponent;
	}
	
	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}


	
	
}
