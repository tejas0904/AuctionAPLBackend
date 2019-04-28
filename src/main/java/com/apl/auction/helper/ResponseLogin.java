package com.apl.auction.helper;

public class ResponseLogin {
	private String imageMatch;
private String studentName;
private int quizTaken;


public ResponseLogin(String studentName, int quizCount) {
	this.studentName = studentName;
	this.quizTaken = quizCount;
}
public ResponseLogin() {
	
}
public String getStudentName() {
	return studentName;
}
public void setStudentName(String studentName) {
	this.studentName = studentName;
}
public int getQuizTaken() {
	return quizTaken;
}
public void setQuizTaken(int quizTaken) {
	this.quizTaken = quizTaken;
}
public String getImageMatch() {
	return imageMatch;
}
public void setImageMatch(String imageMatch) {
	this.imageMatch = imageMatch;
}

}
