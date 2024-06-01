package com.example.entities;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ReimbursementRequests class to create entity in database
 * Two constraints - 
 * 	1. requestDate should be current date
 * 	2. valid values for status (new, approved, rejected)
 * 
 * @author Kalash Vishwakarma
 * 
 * */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ReimbursementRequests")
public class ReimbursementRequests {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private int id;

	@Column(name = "Travel_Request_Id")
	private int travelRequestId;

	@Column(name = "Request_Raised_By_Employee_Id")
	private int requestRaisedByEmployeeId;

	@Column(name = "Request_Date")
	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	private Date requestDate;

	@ManyToOne
	@JoinColumn(name = "Reimbursement_Type_Id")
	private ReimbursementType reimbursementType;

	@Column(name = "Invoice_No")
	private String invoiceNo;

	
	@Column(name = "Invoice_Date")
	@Temporal(TemporalType.DATE)
	private Date invoiceDate;

	@Column(name = "Invoice_Amount")
	private int invoiceAmount;

	@Column(name = "Document_Url")
	private String documentUrl;


	@Column(name = "Request_Processed_On")
	@Temporal(TemporalType.DATE)
	private Date requestProcessedOn;

	@Column(name = "Request_Processed_By_Employee_Id")
	private int requestProcessedByEmployeeId;

	@Column(name = "Status", columnDefinition = "VARCHAR(25) CHECK (status IN ('new', 'approved', 'rejected'))")
	private String status;

	@Column(name = "Remark")
	private String remark;

	

	

}
