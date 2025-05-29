package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OpenAIRequestDTO {

	private List<CombinedClient> clients;
	private LocalDateTime dateTime;
	private String question;

	public List<CombinedClient> getClients() {
		return clients;
	}

	public void setClients(List<CombinedClient> clients) {
		this.clients = clients;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	@Override
	public String toString() {
		return "OpenAIRequestDTO [clients=" + clients + ", dateTime=" + dateTime + ", question=" + question + "]";
	}

	public OpenAIRequestDTO(List<CombinedClient> clients, LocalDateTime dateTime, String question) {
		super();
		this.clients = clients;
		this.dateTime = dateTime;
		this.question = question;
	}

}
