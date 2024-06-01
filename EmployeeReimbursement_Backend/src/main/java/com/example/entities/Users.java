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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users")
public class Users {

	@Id
	@Column(name = "Id")
	private int id;

	@Column(name = "Username")
	private String username;
	
	@Column(name = "Password")
	private String password;
	
	@Column(name = "Role")
	private String role;

}
