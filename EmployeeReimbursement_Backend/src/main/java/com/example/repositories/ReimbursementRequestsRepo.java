package com.example.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.ReimbursementRequests;

/**
 * Repository for managing reimbursement requests related to travel.
 * Provides methods for retrieving reimbursement requests based on travel request ID
 * 
 * @author Kalash Vishwakarma
 * 
 * */
@Repository
public interface ReimbursementRequestsRepo extends JpaRepository<ReimbursementRequests, Integer> {

	/**
     * Retrieves a list of reimbursement requests associated with a specific travel request ID.
     *
     * @param travelRequestId The ID of the travel request.
     * @return A list of reimbursement requests.
     */
	List<ReimbursementRequests> findByTravelRequestId(int travelRequestId);

	
}
