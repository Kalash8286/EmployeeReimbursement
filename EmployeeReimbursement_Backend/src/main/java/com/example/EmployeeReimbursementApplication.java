package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * This is a Main Class of EmployeeReimbursementApplication
 * Bootstraps and Initializes the application
 * 
 * @author Kalash Vishwakarma
 *   
 */


@SpringBootApplication
@EnableDiscoveryClient(autoRegister = true)
public class EmployeeReimbursementApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeReimbursementApplication.class, args);
	}

}