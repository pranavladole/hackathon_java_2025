package com.example.demo.dto;

public class CombinedClientService {

	private String clientId;
	private String servicename;
	private String channel;
	private long todayCount;
	private long yesterdayCount;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getServicename() {
		return servicename;
	}
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
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
		return "CombinedClientService [clientId=" + clientId + ", servicename=" + servicename + ", channel=" + channel
				+ ", todayCount=" + todayCount + ", yesterdayCount=" + yesterdayCount + "]";
	}
	public CombinedClientService(String clientId, String servicename, String channel, long todayCount,
			long yesterdayCount) {
		super();
		this.clientId = clientId;
		this.servicename = servicename;
		this.channel = channel;
		this.todayCount = todayCount;
		this.yesterdayCount = yesterdayCount;
	}
	
	
	
}
