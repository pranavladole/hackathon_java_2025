package com.example.demo.dto;

public class ResponseMessage {

	private String message;

	public ResponseMessage() {
	}

	public ResponseMessage(String message) {
		this.message = message;
	}

	// Getter
	public String getMessage() {
		return message;
	}

	// Setter
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ResponseMessage [message=" + message + "]";
	}
}
