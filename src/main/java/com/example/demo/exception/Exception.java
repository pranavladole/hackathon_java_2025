package com.example.demo.exception;

import javax.security.sasl.AuthenticationException;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.dto.ExceptionResponse;

@RestControllerAdvice
public class Exception {
	
	  @ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ExceptionResponse> AuthenticationException(AuthenticationException message){
		System.out.println("inside auth exception"+ message.getLocalizedMessage());
		ExceptionResponse res = new ExceptionResponse();
		res.setMessage(message.getLocalizedMessage());
		return new ResponseEntity<ExceptionResponse>(res,HttpStatusCode.valueOf(500));
	}
	

}
