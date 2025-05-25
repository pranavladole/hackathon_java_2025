package com.example.demo.dto;

public class Client {
	
	private String clientname;
	private long count;
	
	public String getClientname() {
		return clientname;
	}
	public void setClientname(String clientname) {
		this.clientname = clientname;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return "Client [clientname=" + clientname + ", count=" + count + "]";
	}
	
	public Client(String clientname, long count) {
		super();
		this.clientname = clientname;
		this.count = count;
	}

	

}
