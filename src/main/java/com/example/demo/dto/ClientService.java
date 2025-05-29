package com.example.demo.dto;

public class ClientService {
	
	private String clientname;
	private String servicename;
	private String channel;
	private long count;
	public ClientService(String clientname, String servicename, String channel, long count) {
		super();
		this.clientname = clientname;
		this.servicename = servicename;
		this.channel = channel;
		this.count = count;
	}
	@Override
	public String toString() {
		return "ClientService [clientname=" + clientname + ", servicename=" + servicename + ", channel=" + channel
				+ ", count=" + count + "]";
	}
	public String getClientname() {
		return clientname;
	}
	public void setClientname(String clientname) {
		this.clientname = clientname;
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
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	
	

}
