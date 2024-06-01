package com.example.services.Impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.NewReimbursementRequestsDTO;
import com.example.dto.ProcessReimbursementDTO;
import com.example.dto.ReimbursementDTO;
import com.example.dto.ReimbursementTypeDTO;
import com.example.entities.ReimbursementRequests;
import com.example.exception.EmptyRemarkFoStatusException;
import com.example.exception.ExpenseExceedsException;
import com.example.exception.InvalidInvoiceDateException;
import com.example.exception.InvalidStatusException;
import com.example.exception.ReimbursementRequestsNotFoundException;
import com.example.repositories.ReimbursementRequestsRepo;
import com.example.services.ReimbursementRequestsService;

/**
 * Service class for handling reimbursement requests.
 * 
 * @auther Kalash Vishwakarma
 */
@Service
public class ReimbursementRequestsServiceImpl implements ReimbursementRequestsService {

	/**
	 * Repository for Reimbursement Requests and Reimbursement Types ModelMapper for
	 * Mapping Objects
	 */
	ReimbursementRequestsRepo reimbursementRequestsRepo;
	ModelMapper modelMapper;
	MessageSource messageSource;

	/**
	 * Constructor for ReimbursementRequestsServiceImpl.
	 *
	 * @param ReimbursementRequestsRepo the repository for Reimbursement Requests
	 * @param ModelMapper               the ModelMapper for mapping models
	 */
	@Autowired
	public ReimbursementRequestsServiceImpl(ReimbursementRequestsRepo reimbursementRequestsRepo,
			ModelMapper modelMapper, MessageSource messageSource) {
		super();
		this.reimbursementRequestsRepo = reimbursementRequestsRepo;
		this.modelMapper = modelMapper;
		this.messageSource = messageSource;
	}

	/**
	 * This method Retrieves a list of reimbursement requests associated with a
	 * specific travel request ID.
	 *
	 * @param travelRequestId the request id of Travel
	 * @return list of Reimbursement request in form of ReimbursementDTO
	 * @throws ReimbursementRequestsNotFoundException if no Reimbursement request
	 *                                                found
	 */
	// Get All ReimbursementRequests By Travel Request Id
	@Override
	public List<ReimbursementDTO> getAllReimbursementRequestsForTravelRequestId(int travelRequestId)
			throws ReimbursementRequestsNotFoundException {

		List<ReimbursementDTO> reimbursementDTOList = new ArrayList<>();

		List<ReimbursementRequests> allReimbursementRequests = reimbursementRequestsRepo
				.findByTravelRequestId(travelRequestId);

		// If No ReimbursementRequests Found
		if (allReimbursementRequests.isEmpty()) {
			throw new ReimbursementRequestsNotFoundException(messageSource.getMessage("com.cognizant.services.Impl.ReimbursementRequestsServiceImpl.ReimbursementRequestsNotFoundException.exception", null, Locale.getDefault()));
		}

		// Converting Entity to DTO
		for (ReimbursementRequests request : allReimbursementRequests) {
			ReimbursementDTO reimbursementDTO = new ReimbursementDTO();
			modelMapper.map(request, reimbursementDTO);
			reimbursementDTOList.add(reimbursementDTO);
		}
		return reimbursementDTOList;
	}

	/**
	 * This method updates the reimbursement requests
	 *
	 * @param reimbursement request id
	 * @param updated       values for reimbursement request in form of
	 *                      ProcessReimbursementDTO
	 * @return Updated Reimbursement request in form of ProcessReimbursementDTO
	 * @throws ReimbursementRequestsNotFoundException if no Reimbursement request
	 *                                                found
	 * @throws InvalidStatusException                 Status is not valid other than
	 *                                                (approved, rejected)
	 * @throws EmptyRemarkFoStatusException           Remark is not given for
	 *                                                Rejected Status
	 */
	// Process ReimbursementRequests
	@Override
	public ProcessReimbursementDTO processReimbursementRequests(int requestId,
			ProcessReimbursementDTO processReimbursementDTOInput)
			throws ReimbursementRequestsNotFoundException, InvalidStatusException, EmptyRemarkFoStatusException {

		Optional<ReimbursementRequests> reimbursementRequest = reimbursementRequestsRepo.findById(requestId);

		// ReimbursementRequest is found
		if (!reimbursementRequest.isEmpty()) {

			// If Status == Rejected AND remark != Empty
			if (Arrays.asList("approved", "rejected")
					.contains(processReimbursementDTOInput.getStatus().toLowerCase())) {
				if (processReimbursementDTOInput.getStatus().equalsIgnoreCase("Rejected")
						&& processReimbursementDTOInput.getRemark().isBlank()) {
					throw new EmptyRemarkFoStatusException(messageSource.getMessage("com.cognizant.services.Impl.ReimbursementRequestsServiceImpl.EmptyRemarkFoStatusException.exception", null, Locale.getDefault()));
				}
			} else {
				throw new InvalidStatusException(messageSource.getMessage("com.cognizant.services.Impl.ReimbursementRequestsServiceImpl.InvalidStatusException.exception", null, Locale.getDefault()));
			}
		} else {
			throw new ReimbursementRequestsNotFoundException(messageSource.getMessage("com.cognizant.services.Impl.ReimbursementRequestsServiceImpl.ReimbursementRequestsNotFoundException.exception", null, Locale.getDefault()));
		}

		ProcessReimbursementDTO processReimbursement = modelMapper.map(reimbursementRequest.get(),
				ProcessReimbursementDTO.class);

		// update values
		processReimbursement.setStatus(processReimbursementDTOInput.getStatus().toLowerCase());
		if (processReimbursementDTOInput.getStatus().equalsIgnoreCase("Rejected")) {
			processReimbursement.setRemark(processReimbursementDTOInput.getRemark());
		}
		// Map DTO to Entity
		ReimbursementRequests reimbursementEntity = modelMapper.map(processReimbursement, ReimbursementRequests.class);

		// Update Entity
		reimbursementEntity = reimbursementRequestsRepo.save(reimbursementEntity);
		processReimbursementDTOInput = modelMapper.map(reimbursementEntity, ProcessReimbursementDTO.class);

		return processReimbursementDTOInput;
	}

	/**
	 * This method Retrieves reimbursement requests based on reimbursement request
	 * ID
	 *
	 * @param reimbursement request id
	 * @return reimbursement request in form of ReimbursementDTO
	 * @throws ReimbursementRequestsNotFoundException if no Reimbursement request
	 *                                                found
	 */
	// Get a ReimbursementRequests By Id
	@Override
	public ReimbursementDTO getReimbursementRequestById(int requestId) throws ReimbursementRequestsNotFoundException {

		Optional<ReimbursementRequests> reimbursementRequest = reimbursementRequestsRepo.findById(requestId);

		if (reimbursementRequest.isEmpty()) {
			throw new ReimbursementRequestsNotFoundException(messageSource.getMessage("com.cognizant.services.Impl.ReimbursementRequestsServiceImpl.ReimbursementRequestsNotFoundException.exception", null, Locale.getDefault()));
		}
		ReimbursementDTO reimbursementDTO = modelMapper.map(reimbursementRequest.get(), ReimbursementDTO.class);

		return reimbursementDTO;
	}

	/**
	 * This method adds reimbursement requests
	 *
	 * @param reimbursement request in form of NewReimbursementRequestsDTO
	 * @return added reimbursement request in form of NewReimbursementRequestsDTO
	 * @throws IOException                 if issue occurred related to file
	 *                                     handling
	 * @throws ExpenseExceedsException     if Invoice Amount exceeded the allowed
	 *                                     amount for reimbursement type
	 * @throws InvalidInvoiceDateException if Invoice date is out of range
	 */
	// Add new ReimbursementRequests
	@Override
	public NewReimbursementRequestsDTO addReimbursementRequest(NewReimbursementRequestsDTO newReimbursementRequestsDTO)
			throws ExpenseExceedsException, InvalidInvoiceDateException, IOException {

		// Verify document
		String documentURL = verifyDocument(newReimbursementRequestsDTO.getMultipartFile());

		if (documentURL.length() > 0) {
			// Invoice date must be within From and To Date of Travel
			if (newReimbursementRequestsDTO.getInvoiceDate().after(newReimbursementRequestsDTO.getFromTravelDate())
					&& newReimbursementRequestsDTO.getInvoiceDate()
							.before(newReimbursementRequestsDTO.getToTravelDate())) {
				// Invoice Amount must be within the range of allowed expenses
				boolean allowedExpenseExceeds = verifyTotalAllowedExpense(
						newReimbursementRequestsDTO.getReimbursementTypeDTO().getType().toUpperCase(),
						newReimbursementRequestsDTO.getInvoiceAmount());
				if (allowedExpenseExceeds) {
					throw new ExpenseExceedsException(messageSource.getMessage("com.cognizant.services.Impl.ReimbursementRequestsServiceImpl.ExpenseExceedsException.exception", null, null));
				}
			} else {
				throw new InvalidInvoiceDateException(messageSource.getMessage("com.cognizant.services.Impl.ReimbursementRequestsServiceImpl.InvalidInvoiceDateException.exception", null, Locale.getDefault()));
			}

		} else {
			throw new FileNotFoundException(messageSource.getMessage("com.cognizant.services.Impl.ReimbursementRequestsServiceImpl.FileNotFoundException.exception", null, Locale.getDefault()));
		}

		newReimbursementRequestsDTO.setDocumentUrl(documentURL);
		newReimbursementRequestsDTO.setStatus("new");

		// Converting DTO to Entity
		ReimbursementRequests requestEntity = modelMapper.map(newReimbursementRequestsDTO, ReimbursementRequests.class);

		// Save ReimbursementRequest
		requestEntity = reimbursementRequestsRepo.save(requestEntity);

		// Converting Entity to DTO
		newReimbursementRequestsDTO = modelMapper.map(requestEntity, NewReimbursementRequestsDTO.class);
		newReimbursementRequestsDTO.setReimbursementTypeDTO(
				modelMapper.map(requestEntity.getReimbursementType(), ReimbursementTypeDTO.class));

		return newReimbursementRequestsDTO;
	}

	// **************************************************
	// Calculate total amount for Type
	boolean verifyTotalAllowedExpense(String type, int totalAmount) {
		boolean totalAmountExceeds = true;
		switch (type) {
		case "FOOD":
		case "WATER":
			if (totalAmount >= 1000 && totalAmount <= 1500)
				totalAmountExceeds = false;
			break;
		case "LAUNDRY":
			if (totalAmount >= 250 && totalAmount <= 500)
				totalAmountExceeds = false;
			break;
		case "LOCAL TRAVEL":
			if (totalAmount <= 1000)
				totalAmountExceeds = false;
			break;
		}
		return totalAmountExceeds;
	}

	// Validating Document Requirements
	public String verifyDocument(MultipartFile file) throws IOException {
		String storeDocumentURL = null;
		// Check file is available
		if (file.isEmpty()) {
			throw new FileNotFoundException(messageSource.getMessage("com.cognizant.services.Impl.ReimbursementRequestsServiceImpl.FileNotFoundException.exception", null, Locale.getDefault()));
		}
		// Verify file type is PDF

		if (!file.getContentType().equals("application/pdf")) {
			
			throw new FileUploadException(messageSource.getMessage("com.cognizant.services.Impl.ReimbursementRequestsServiceImpl.FileUploadException.exception", null, Locale.getDefault()));
		}
		// Verify file size is less than 256KB
		if (file.getSize() > (256 * 1024L)) {
			throw new FileSizeLimitExceededException(messageSource.getMessage("com.cognizant.services.Impl.ReimbursementRequestsServiceImpl.FileSizeLimitExceededException.exception", null, Locale.getDefault()), file.getSize(), 256);
		}

		storeDocumentURL = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
				+ File.separator + "resources" + File.separator + "static" + File.separator + "documents"
				+ File.separator + file.getOriginalFilename();
		Files.copy(file.getInputStream(), Paths.get(storeDocumentURL), StandardCopyOption.REPLACE_EXISTING);
		return storeDocumentURL;
	}

}
