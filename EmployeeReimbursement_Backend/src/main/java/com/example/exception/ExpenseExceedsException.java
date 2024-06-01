package com.example.exception;

/**
 * ExpenseExceedsException class is an unchecked exception
 * When Expense exceeds for any ReimbursementType throw ExpenseExceedsException
 * 
 * @author Kalash Vishwakarma
 * 
 * */
@SuppressWarnings("serial")
public class ExpenseExceedsException extends Exception {

	public ExpenseExceedsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
