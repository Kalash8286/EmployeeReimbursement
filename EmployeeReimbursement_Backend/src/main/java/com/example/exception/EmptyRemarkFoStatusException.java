package com.example.exception;


/**
 * EmptyRemarkFoStatusException class is an unchecked exception
 * When Remark is Empty for rejected status throw EmptyRemarkFoStatusException
 * 
 * @author Kalash Vishwakarma
 * 
 * */
@SuppressWarnings("serial")
public class EmptyRemarkFoStatusException extends Exception {

	public EmptyRemarkFoStatusException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
