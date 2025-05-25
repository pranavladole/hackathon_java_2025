package com.example.demo.dto;

public class CombinedStatusCode {
	
	private String servicename;
	private String StatusCode;
	private long TodayCount;
	private long YesterdayCount;
	public String getServicename() {
		return servicename;
	}
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	public String getStatusCode() {
		return StatusCode;
	}
	public void setStatusCode(String statusCode) {
		StatusCode = statusCode;
	}
	public long getTodayCount() {
		return TodayCount;
	}
	public void setTodayCount(long todayCount) {
		TodayCount = todayCount;
	}
	public long getYesterdayCount() {
		return YesterdayCount;
	}
	public void setYesterdayCount(long yesterdayCount) {
		YesterdayCount = yesterdayCount;
	}
	@Override
	public String toString() {
		return "CombinedStatusCode [servicename=" + servicename + ", StatusCode=" + StatusCode + ", TodayCount="
				+ TodayCount + ", YesterdayCount=" + YesterdayCount + "]";
	}
	public CombinedStatusCode(String servicename, String statusCode, long todayCount, long yesterdayCount) {
		super();
		this.servicename = servicename;
		StatusCode = statusCode;
		TodayCount = todayCount;
		YesterdayCount = yesterdayCount;
	}
	
		

}
