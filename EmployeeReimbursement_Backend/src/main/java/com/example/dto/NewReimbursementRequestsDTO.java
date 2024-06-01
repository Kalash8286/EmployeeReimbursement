package com.example.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * NewReimbursementRequestsDTO class helps to insert new request
 * This class represents DataTransferobject for ReimbursementRequests entity
 * @author Kalash Vishwakarma
 * 
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@JsonIgnoreProperties(ignoreUnknown = true)
public class NewReimbursementRequestsDTO {
	
	private int id;
	private int travelRequestId;
	private int requestRaisedByEmployeeId;
	private Date requestDate;
	private ReimbursementTypeDTO reimbursementTypeDTO;
	private String invoiceNo;
	@Past(message = "{com.cognizant.dto.NewReimbursementRequestsDTO.invoiceDate.error}")
	private Date invoiceDate;
	private int invoiceAmount;
	private String documentUrl;
	@FutureOrPresent(message = "{com.cognizant.dto.NewReimbursementRequestsDTO.requestProcessedOn.error}")
	private Date requestProcessedOn;	
	private int requestProcessedByEmployeeId;	
	private String status;
	private String remark;
	// The from and to date of the travel which can be obtained from travel planner
	private Date fromTravelDate;
	private Date toTravelDate;
	private MultipartFile multipartFile;
	

}
