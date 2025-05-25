package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.EmailService;

@RestController
public class EmailController {

	@Autowired
	private EmailService emailservice;
	
	
	@GetMapping("/api")
	public String email() {
		System.out.println("inside get api call..");
		return "from email controller";
	}
	
}
