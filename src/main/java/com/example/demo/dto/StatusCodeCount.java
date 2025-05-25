package com.example.demo.dto;

public class StatusCodeCount {
    private String statuscode;
    private long count;

    public StatusCodeCount(String statuscode, long count) {
        this.statuscode = statuscode;
        this.count = count;
    }

    public String getStatuscode() {
        return statuscode;
    }

    public long getCount() {
        return count;
    }

	@Override
	public String toString() {
		return "StatusCodeCount [statuscode=" + statuscode + ", count=" + count + "]";
	}
    
    
}


