package com.masai.exception;

import java.time.LocalDateTime;

public class MyErrorDetails {
	private LocalDateTime timestamp;
	private String message;
	private String description;
	
	public MyErrorDetails() {
		// TODO Auto-generated constructor stub
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "MyErrorDetails [timestamp=" + timestamp + ", message=" + message + ", description=" + description + "]";
	}
	
	
	

}
