package com.example.exception;


/**
 * InvalidInvoiceDateException class is an unchecked exception
 * When Invoice date is out of range of Travel From and To date throw EmptyRemarkFoStatusException
 * 
 * @author Kalash Vishwakarma
 * 
 * */
@SuppressWarnings("serial")
public class InvalidInvoiceDateException extends Exception {

	public InvalidInvoiceDateException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
