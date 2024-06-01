package com.example.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ReimbursementDTO class helps to fetch requests from database
 * This class represents DataTransferobject for ReimbursementRequests entity
 * @author Kalash Vishwakarma
 * 
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReimbursementDTO {

	private int id;
	private int travelRequestId;
	private int requestRaisedByEmployeeId;
	private Date requestDate;
	private ReimbursementTypeDTO reimbursementType;
	private String invoiceNo;
	private Date invoiceDate;
	private int invoiceAmount;
	private String documentUrl;
	private Date requestProcessedOn;
	private int requestProcessedByEmployeeId;
	private String status;
	private String remark;

}
