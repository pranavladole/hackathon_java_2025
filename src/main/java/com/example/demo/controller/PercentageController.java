package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CombinedClient;
import com.example.demo.dto.DateTime;
import com.example.demo.service.PercentageService;

@RestController
public class PercentageController {
	
	@Autowired
	private PercentageService percentageService;
	
	@PostMapping("/decreasepercent")
	public List<CombinedClient> decreaseCount(@RequestBody DateTime dateTime) {
		List<CombinedClient> res = this.percentageService.decreasePercent(dateTime);		
		return res;
	}
	

}
