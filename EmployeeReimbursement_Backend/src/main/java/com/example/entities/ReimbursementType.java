package com.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ReimbursementType class to create entity in database
 * 
 * @author Kalash Vishwakarma
 * 
 * */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ReimbursementTypes")
public class ReimbursementType {

	@Id
	@Column(name = "Id")
	private int id;

	@Column(name = "Type")
	private String type;

}
