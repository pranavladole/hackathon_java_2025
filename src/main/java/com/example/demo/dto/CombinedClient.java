package com.example.demo.dto;

public class CombinedClient {

	private String clientId;
	private long todayCount;
	private long yesterdayCount;

	public CombinedClient(String clientId, long todayCount, long yesterdayCount) {
		super();
		this.clientId = clientId;
		this.todayCount = todayCount;
		this.yesterdayCount = yesterdayCount;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public long getTodayCount() {
		return todayCount;
	}

	public void setTodayCount(long todayCount) {
		this.todayCount = todayCount;
	}

	public long getYesterdayCount() {
		return yesterdayCount;
	}

	public void setYesterdayCount(long yesterdayCount) {
		this.yesterdayCount = yesterdayCount;
	}

	@Override
	public String toString() {
		return "CombinedClient [clientId=" + clientId + ", todayCount=" + todayCount + ", yesterdayCount="
				+ yesterdayCount + "]";
	}

}
