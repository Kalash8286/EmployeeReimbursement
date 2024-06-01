package com.example.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.NewReimbursementRequestsDTO;
import com.example.dto.ProcessReimbursementDTO;
import com.example.dto.ReimbursementDTO;
import com.example.exception.EmptyRemarkFoStatusException;
import com.example.exception.ExpenseExceedsException;
import com.example.exception.InvalidInvoiceDateException;
import com.example.exception.InvalidStatusException;
import com.example.exception.ReimbursementRequestsNotFoundException;
import com.example.services.ReimbursementRequestsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller class for handling reimbursement requests. Manages endpoints
 * related to reimbursements requests.
 *
 * @RestController indicates that this class is a Spring REST controller.
 * @RequestMapping specifies the base URL path for all endpoints in this
 *                 controller. In this case, all endpoints under this controller
 *                 will start with "/api/reimbursements".
 */
@Tag(name = "Reimbursement Requests", description = "Handling Reimbursement Requests" )
@RestController
@RequestMapping("api/reimbursements")
@CrossOrigin(origins = "http://localhost:4200")
public class ReimbursementRequestsController {

	private ReimbursementRequestsService reimbursementRequestsService;
	private ObjectMapper objectMapper;

	/**
	 * Constructor for the controller.
	 *
	 * @param reimbursementRequestsService an instance of the
	 *                                     ReimbursementRequestsService. Autowired
	 *                                     by Spring to inject the service
	 *                                     implementation.
	 */
	@Autowired
	public ReimbursementRequestsController(ReimbursementRequestsService reimbursementRequestsService,
			ObjectMapper objectMapper) {
		super();
		this.reimbursementRequestsService = reimbursementRequestsService;
		this.objectMapper = objectMapper;
	}

	/**
	 * Adds a new reimbursement request.
	 *
	 * @param newReimbursementRequestsDTO The DTO containing details of the new
	 *                                    reimbursement request.
	 * @return ResponseEntity with the created reimbursement request or a
	 *         BAD_REQUEST status if unsuccessful.
	 * @throws ParseException              If there is an issue parsing data (e.g.,
	 *                                     date format).
	 * @throws ExpenseExceedsException     If the expense exceeds a certain limit.
	 * @throws InvalidInvoiceDateException If the invoice date is invalid.
	 */
	@PostMapping(path = "add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<NewReimbursementRequestsDTO> addNewReimbursementRequest(
			@RequestParam("requestDTO") String newReimbursementRequestsDTO, @RequestParam("file") MultipartFile file)
			throws IOException, ExpenseExceedsException, InvalidInvoiceDateException {

		NewReimbursementRequestsDTO userRequest = objectMapper.readValue(newReimbursementRequestsDTO,
				NewReimbursementRequestsDTO.class);
		userRequest.setMultipartFile(file);

		NewReimbursementRequestsDTO addReimbursementRequestResponse = reimbursementRequestsService
				.addReimbursementRequest(userRequest);

		if (addReimbursementRequestResponse.getId() > 0) {
			return new ResponseEntity<NewReimbursementRequestsDTO>(addReimbursementRequestResponse, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Retrieves all reimbursement requests associated with a specific travel
	 * request ID.
	 *
	 * @param travelRequestId The ID of the travel request.
	 * @return ResponseEntity containing a list of reimbursement requests or a
	 *         BAD_REQUEST status if not found.
	 * @throws ReimbursementRequestsNotFoundException If no reimbursement requests
	 *                                                are found for the given ID.
	 */
	@GetMapping("{travelrequestid}/requests")
	public ResponseEntity<List<ReimbursementDTO>> getAllReimbursementsForTravelRequestId(
			@PathVariable("travelrequestid") int travelRequestId) throws ReimbursementRequestsNotFoundException {
		List<ReimbursementDTO> allReimbursementRequests = reimbursementRequestsService
				.getAllReimbursementRequestsForTravelRequestId(travelRequestId);
		if (allReimbursementRequests.get(0).getTravelRequestId() != travelRequestId) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<List<ReimbursementDTO>>(allReimbursementRequests, HttpStatus.OK);
	}

	/**
	 * Retrieves all reimbursement requests associated with a specific reimbursement
	 * request ID.
	 *
	 * @param reimbursementId The ID of the reimbursement request.
	 * @return ResponseEntity with reimbursement request or a BAD_REQUEST status if
	 *         not found.
	 * @throws ReimbursementRequestsNotFoundException If no reimbursement requests
	 *                                                are found for the given ID.
	 */
	@GetMapping("{reimbursementid}")
	public ResponseEntity<ReimbursementDTO> getReimbursementById(@PathVariable("reimbursementid") int reimbursementId)
			throws ReimbursementRequestsNotFoundException {

		ReimbursementDTO reimbursementRequest = reimbursementRequestsService
				.getReimbursementRequestById(reimbursementId);

		if (reimbursementRequest.getId() != reimbursementId) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<ReimbursementDTO>(reimbursementRequest, HttpStatus.OK);
	}

	/**
	 * Processes a reimbursement request by its ID.
	 *
	 * @param reimbursementId         The ID of the reimbursement request.
	 * @param processReimbursementDTO The DTO containing processing details.
	 * @return ResponseEntity with the processed reimbursement request or a
	 *         BAD_REQUEST status if unsuccessful.
	 * @throws ReimbursementRequestsNotFoundException If the reimbursement request
	 *                                                is not found.
	 * @throws InvalidStatusException                 If an invalid status is
	 *                                                encountered during processing.
	 * @throws EmptyRemarkFoStatusException           If the remark for the status
	 *                                                is empty.
	 */
	@PutMapping("{reimbursementid}/process")
	public ResponseEntity<ProcessReimbursementDTO> processReimbursementById(
			@PathVariable("reimbursementid") int reimbursementId,
			@RequestBody ProcessReimbursementDTO processReimbursementDTO)
			throws ReimbursementRequestsNotFoundException, InvalidStatusException, EmptyRemarkFoStatusException {

		ProcessReimbursementDTO processedReimbursementRequest = reimbursementRequestsService
				.processReimbursementRequests(reimbursementId, processReimbursementDTO);

		if (processedReimbursementRequest.getId() != reimbursementId) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<ProcessReimbursementDTO>(processedReimbursementRequest, HttpStatus.OK);

	}

}
