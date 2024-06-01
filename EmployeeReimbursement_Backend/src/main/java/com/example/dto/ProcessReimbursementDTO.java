package com.example.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ProcessReimbursementDTO class helps to update request
 * This class represents DataTransferobject for ReimbursementRequests entity
 * @author Kalash Vishwakarma
 * 
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessReimbursementDTO {

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
