package com.example.services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.ReimbursementTypeDTO;
import com.example.entities.ReimbursementType;
import com.example.repositories.ReimbursementTypeRepo;
import com.example.services.ReimbursementTypeService;


/**
 * Service class for handling reimbursement types.
 * 
 * @auther Kalash Vishwakarma
 */
@Service
public class ReimbursementTypeServiceImpl implements ReimbursementTypeService {

	ReimbursementTypeRepo reimbursementTypeRepo;

	@Autowired
	public ReimbursementTypeServiceImpl(ReimbursementTypeRepo reimbursementTypeRepo) {
		super();
		this.reimbursementTypeRepo = reimbursementTypeRepo;
	}

	/**
	 * Retrieves all reimbursement types
	 *
	 * @return List of reimbursement types
	 */
	//	Get the Reimbursement types
	@Override
	public List<ReimbursementTypeDTO> getReimbursementTypes() {

		List<ReimbursementType> allReimbursementTypes = reimbursementTypeRepo.findAll();

		if (allReimbursementTypes.isEmpty()) {
			throw new RuntimeException("Reimbursement Types Not Found.");
		}
		return allReimbursementTypes.stream().map(this::mapToDto).collect(Collectors.toList());

	}

	// Converting Entity to DTO	
	private ReimbursementTypeDTO mapToDto(ReimbursementType reimbursementType) {
		ReimbursementTypeDTO reimbursementTypeDTO = new ReimbursementTypeDTO();
		reimbursementTypeDTO.setId(reimbursementType.getId());
		reimbursementTypeDTO.setType(reimbursementType.getType());
		return reimbursementTypeDTO;
	}


}
