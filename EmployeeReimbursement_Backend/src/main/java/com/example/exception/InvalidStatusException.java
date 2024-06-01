package com.example.exception;

/**
 * InvalidStatusException class is an unchecked exception
 * When Status is not valid i.e. status is other than (approved, rejected) throw InvalidStatusException
 * 
 * @author Kalash Vishwakarma
 * 
 * */
@SuppressWarnings("serial")
public class InvalidStatusException extends Exception{

	public InvalidStatusException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	
	
	
}
