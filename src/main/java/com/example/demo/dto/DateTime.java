package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DateTime {


    private LocalDateTime startDate;
    private LocalDateTime endDate;
        // Getters and Setters
	public LocalDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}
	public LocalDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
	@Override
	public String toString() {
		return "DateTime [startDate=" + startDate + ", endDate=" + endDate + "]";
	}
    
    
}
