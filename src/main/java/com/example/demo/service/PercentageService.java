package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.CombinedAIClientResponse;
import com.example.demo.dto.CombinedClient;
import com.example.demo.dto.DateTime;

public interface PercentageService {
	
	public CombinedAIClientResponse increasePercent(DateTime dateTime);
	public List<CombinedClient> decreasePercent(DateTime dateTime);

}
