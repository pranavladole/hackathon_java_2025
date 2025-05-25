package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CombinedAIClientResponse;
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

	@PostMapping("/angularstatuscodeanalyse")
	public List<CombinedStatusCode> angularhandleDateRange(@RequestBody DateTime dateTime) throws Exception {
		System.out.println("inside transaction controller");
		List<CombinedStatusCode> list = transactionService.angularprocessDateRange(dateTime);
		for (CombinedStatusCode item : list) {
		    System.out.println(item);
		}
		return list;  
		
	}
	
	// no traffic
		@PostMapping("/angularzerotraffic")
		public CombinedAIClientResponse zerotraffic(@RequestBody DateTime dateTime){
			System.out.println("inside transaction controller "+dateTime.getStartDate() +" "+ dateTime.getEndDate() );
			CombinedAIClientResponse list = transactionService.angularprocessZeroTraffic(dateTime);
			return list;  
		}
		
		// increase traffic 
		@PostMapping("/angularincreasepercent")
		public CombinedAIClientResponse increaseCount(@RequestBody DateTime dateTime) {
			CombinedAIClientResponse res = this.percentageService.increasePercent(dateTime);
			return res;
		}
		
	
}
