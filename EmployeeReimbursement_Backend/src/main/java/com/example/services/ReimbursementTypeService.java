package com.example.services;

import java.util.List;

import com.example.dto.ReimbursementTypeDTO;


/**
 * Interface to implement business logic related to Reimbursement Types
 * Provides methods for retrieving reimbursement types 
 * 
 * @author Kalash Vishwakarma
 * 
 * */
public interface ReimbursementTypeService {

	List<ReimbursementTypeDTO> getReimbursementTypes();
	
}
