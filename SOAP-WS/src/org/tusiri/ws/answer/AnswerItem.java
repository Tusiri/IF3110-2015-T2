package org.tusiri.ws.answer;

import javax.xml.bind.annotation.XmlElement;

public class AnswerItem {
	
	private int num_answer;
	private int id_question;
	private int id_user;
	private String content;
	private String answer_date;
	private int num_votes;
	
	@XmlElement(name = "num_answer")
	public int getNumAnswer() {
		return num_answer;
	}

	public void setNumAnswer(int num_answer) {
		this.num_answer = num_answer;
	}
	
	@XmlElement(name = "id_question")
	public int getIDQuestion() {
		return id_question;
	}

	public void setIDQuestion(int id_question) {
		this.id_question = id_question;
	}
	
	@XmlElement(name = "id_user")
	public int getIDUser() {
		return id_user;
	}

	public void setIDUser(int id_user) {
		this.id_user = id_user;
	}
	
	@XmlElement(name = "content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@XmlElement(name = "question_date")
	public String getAnswerDate() {
		return answer_date.toString();
	}

	public void setAnswerDate(String answer_date) {
		this.answer_date = answer_date;
	}
	
	@XmlElement(name = "num_votes")
	public int getNumVotes() {
		return num_votes;
	}

	public void setNumVotes(int num_votes) {
		this.num_votes = num_votes;
	}
	
}
