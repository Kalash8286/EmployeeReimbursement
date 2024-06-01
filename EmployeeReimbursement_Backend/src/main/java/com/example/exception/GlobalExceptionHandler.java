package com.example.exception;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * GlobalExceptionHandler class is handling exceptions occurred in all RestController  
 * 
 * @author Kalash Vishwakarma
 * 
 * */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ExpenseExceedsException.class)
	public ResponseEntity<String> handleExpenseExceedsException(ExpenseExceedsException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}

	@ExceptionHandler(InvalidInvoiceDateException.class)
	public ResponseEntity<String> handleInvalidInvoiceDateException(InvalidInvoiceDateException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}

	@ExceptionHandler(InvalidStatusException.class)
	public ResponseEntity<String> handleInvalidStatusException(InvalidStatusException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}

	@ExceptionHandler(ReimbursementRequestsNotFoundException.class)
	public ResponseEntity<String> handleReimbursementRequestsNotFoundException(
			ReimbursementRequestsNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}

	@ExceptionHandler(EmptyRemarkFoStatusException.class)
	public ResponseEntity<String> handleEmptyRemarkFoStatusException(EmptyRemarkFoStatusException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}

	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<String> handleFileNotFoundException(FileNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}

	@ExceptionHandler(FileUploadException.class)
	public ResponseEntity<String> handleFileUploadException(FileUploadException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}

	@ExceptionHandler(FileSizeLimitExceededException.class)
	public ResponseEntity<String> handleFileSizeLimitExceededException(FileSizeLimitExceededException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}

	@ExceptionHandler(ParseException.class)
	public ResponseEntity<String> handleParseException(ParseException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntimeException(RuntimeException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}

}
