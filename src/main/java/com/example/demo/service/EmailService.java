package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.CombinedAIClientResponse;
import com.example.demo.dto.CombinedClientService;
import com.example.demo.dto.DateTime;
import com.example.demo.dto.StatusCodeCount;

public interface EmailService {

	public void processDateRange(List<StatusCodeCount> ydata, List<StatusCodeCount> data, DateTime dateTime)
			throws Exception;

	public void getClientsWithNoTrafficToday(CombinedAIClientResponse combinedAIClientResponse, DateTime datetime)
			throws Exception;
	

	public void sendNewZeroTrafficEmail(List<CombinedClientService> noTrafficClients, DateTime datetime)
			throws Exception;
	
	
}
