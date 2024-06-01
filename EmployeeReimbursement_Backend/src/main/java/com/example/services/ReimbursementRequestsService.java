package com.example.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.dto.NewReimbursementRequestsDTO;
import com.example.dto.ProcessReimbursementDTO;
import com.example.dto.ReimbursementDTO;
import com.example.exception.EmptyRemarkFoStatusException;
import com.example.exception.ExpenseExceedsException;
import com.example.exception.InvalidInvoiceDateException;
import com.example.exception.InvalidStatusException;
import com.example.exception.ReimbursementRequestsNotFoundException;

/**
 * Interface to implement business logic related to Reimbursement Requests
 * Provides methods for - 
 * 1. Retrieving reimbursement requests based on travel request ID
 * 2. Retrieving reimbursement requests based on reimbursement request ID
 * 3. Adding reimbursement requests
 * 4. Updating reimbursement requests
 * 
 * 
 * @author Kalash Vishwakarma
 * 
 * */
public interface ReimbursementRequestsService {

	List<ReimbursementDTO> getAllReimbursementRequestsForTravelRequestId(int travelRequestId) throws ReimbursementRequestsNotFoundException;
	
	ReimbursementDTO getReimbursementRequestById(int requestId) throws ReimbursementRequestsNotFoundException;
	
	NewReimbursementRequestsDTO addReimbursementRequest(NewReimbursementRequestsDTO newReimbursementRequestsDTO) throws IOException, ExpenseExceedsException, InvalidInvoiceDateException;
	
	ProcessReimbursementDTO processReimbursementRequests(int requestId, ProcessReimbursementDTO processReimbursementDTO) throws ReimbursementRequestsNotFoundException, InvalidStatusException, EmptyRemarkFoStatusException;
}
