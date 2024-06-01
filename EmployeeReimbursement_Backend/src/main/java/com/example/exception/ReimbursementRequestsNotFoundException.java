package com.example.exception;

/**
 * ReimbursementRequestsNotFoundException class is an unchecked exception
 * When ReimbursementRequest is not found in database throw ReimbursementRequestsNotFoundException
 * 
 * @author Kalash Vishwakarma
 * 
 * */
@SuppressWarnings("serial")
public class ReimbursementRequestsNotFoundException extends Exception {

	public ReimbursementRequestsNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
