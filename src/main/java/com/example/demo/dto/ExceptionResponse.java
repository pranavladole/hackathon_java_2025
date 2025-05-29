package com.example.demo.dto;

public class ExceptionResponse {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ExceptionResponse [message=" + message + "]";
	}

}
