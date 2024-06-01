package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.ReimbursementTypeDTO;
import com.example.services.ReimbursementTypeService;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller class for handling reimbursement types.
 * Manages endpoints related to reimbursements types.
 *
 * @RestController indicates that this class is a Spring REST controller.
 * @RequestMapping specifies the base URL path for all endpoints in this controller.
 *   In this case, all endpoints under this controller will start with "/api/reimbursements".
 */
@Tag(name = "Reimbursement Type", description = "Handling Reimbursement Type Requests" )
@RestController
@RequestMapping("api/reimbursements")
@CrossOrigin(origins = "http://localhost:4200")
public class ReimbursementTypeController {

	private ReimbursementTypeService reimbursementTypeService;

	
	/**
     * Constructor for the controller.
     *
     * @param reimbursementTypeService an instance of the ReimbursementTypeService.
     * Autowired by Spring to inject the service implementation.
     */
	@Autowired
	public ReimbursementTypeController(ReimbursementTypeService reimbursementTypeService) {
		super();
		this.reimbursementTypeService = reimbursementTypeService;
	}

	/**
	 * Retrieves all reimbursement types
	 *
	 * @return ResponseEntity containing a list of reimbursement types or a BAD_REQUEST status if not found.
	 */
	@GetMapping("types")
	public ResponseEntity<List<ReimbursementTypeDTO>> getReimbursementTypes() {

		List<ReimbursementTypeDTO> reimbursementTypes = reimbursementTypeService.getReimbursementTypes();

		if (reimbursementTypes.get(0).getId() <= 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<List<ReimbursementTypeDTO>>(reimbursementTypes, HttpStatus.OK);

	}
}
