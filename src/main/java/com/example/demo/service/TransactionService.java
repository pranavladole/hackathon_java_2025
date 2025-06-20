package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.CombinedAIClientResponse;
import com.example.demo.dto.CombinedClientService;
import com.example.demo.dto.CombinedStatusCode;
import com.example.demo.dto.DateTime;

public interface TransactionService {
	public List<CombinedStatusCode> angularprocessDateRange(DateTime dateTime) throws Exception;

	public CombinedAIClientResponse angularprocessZeroTraffic(DateTime dateTime);
	
	//public CombinedAIClientResponse processZeroTraffic(DateTime dateTime);
	public List<CombinedClientService> processZeroTraffic(DateTime dateTime);
	
	
}
