package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents DataTransferobject for ReimbursementType entity
 * @author Kalash Vishwakarma
 * 
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReimbursementTypeDTO {

	private int id;
	private String type;

	

}