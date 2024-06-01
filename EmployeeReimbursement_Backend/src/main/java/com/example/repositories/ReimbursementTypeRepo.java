package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.ReimbursementType;


/**
 * Repository for managing reimbursement types
 * 
 * @author Kalash Vishwakarma
 * 
 * */
@Repository
public interface ReimbursementTypeRepo extends JpaRepository<ReimbursementType, Integer> {

}
