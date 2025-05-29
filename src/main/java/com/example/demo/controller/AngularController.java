package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CombinedAIClientResponse;
import com.example.demo.dto.CombinedClient;
import com.example.demo.dto.CombinedClientService;
import com.example.demo.dto.CombinedStatusCode;
import com.example.demo.dto.DateTime;
import com.example.demo.service.PercentageService;
import com.example.demo.service.TransactionService;

@RestController
public class AngularController {

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private PercentageService percentageService;

		// Status code analyzer
		@PostMapping("/angularstatuscodeanalyse")
		public List<CombinedStatusCode> angularhandleDateRange(@RequestBody DateTime dateTime) throws Exception {
		System.out.println("inside transaction controller");
		List<CombinedStatusCode> combinedStatusCode = transactionService.angularprocessDateRange(dateTime);
		for (CombinedStatusCode item : combinedStatusCode) {
		    System.out.println(item);
		}
			return combinedStatusCode;  
		}
	
		// no traffic
		@PostMapping("/angularzerotraffic")
		public CombinedAIClientResponse angularzerotraffic(@RequestBody DateTime dateTime){
			System.out.println("inside transaction controller "+dateTime.getStartDate() +" "+ dateTime.getEndDate() );
			CombinedAIClientResponse combinedAIClientResponse = transactionService.angularprocessZeroTraffic(dateTime);
			return combinedAIClientResponse;  
		}
		
		// no traffic
				@PostMapping("/zerotraffic")
				public List<CombinedClientService> zerotraffic(@RequestBody DateTime dateTime){
					System.out.println("inside transaction controller "+dateTime.getStartDate() +" "+ dateTime.getEndDate() );
					List<CombinedClientService> combinedAIClientResponse = transactionService.processZeroTraffic(dateTime);
					return combinedAIClientResponse;  
				}
		
		// 30% increase traffic 
		@PostMapping("/angularincreasepercent")
		public CombinedAIClientResponse increaseCount(@RequestBody DateTime dateTime) {
			CombinedAIClientResponse combinedAIClientResponse = this.percentageService.increasePercent(dateTime);
			return combinedAIClientResponse;
		}
		
		// 30% decrease traffic
		@PostMapping("/decreasepercent")
		public List<CombinedClient> decreaseCount(@RequestBody DateTime dateTime) {
			List<CombinedClient> combinedClient = this.percentageService.decreasePercent(dateTime);		
			return combinedClient;
		}
		
	
}
