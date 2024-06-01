package com.example.services.Impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.dto.ReimbursementTypeDTO;
import com.example.entities.ReimbursementType;
import com.example.repositories.ReimbursementTypeRepo;
import com.example.services.Impl.ReimbursementTypeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TestReimbursementTypeServiceImpl {

	@Mock
	private ReimbursementTypeRepo reimbursementTypeRepo;

	@InjectMocks
	private ReimbursementTypeServiceImpl reimbursementTypeService;

	@BeforeEach
	void setUp() throws Exception {

	}

	@AfterEach
	void tearDown() throws Exception {

	}

	@Test
	public void WhenReimbursementTypesIsFound_ReturnReimbursementTypes() {

		try {
			List<ReimbursementType> reimbursementTypesList = new ArrayList<>();
			ReimbursementType reimbursementType = new ReimbursementType();

			reimbursementType.setId(105);
			reimbursementType.setType("FOODS");
			reimbursementTypesList.add(reimbursementType);

			reimbursementType.setId(106);
			reimbursementType.setType("WATERS");
			reimbursementTypesList.add(reimbursementType);

			when(reimbursementTypeRepo.findAll()).thenReturn(reimbursementTypesList);

			List<ReimbursementTypeDTO> reimbursementTypes = reimbursementTypeService.getReimbursementTypes();

			assertTrue(reimbursementTypes.size() == 2);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void WhenReimbursementTypesIsEmpty_ReturnException() {

		try {
			List<ReimbursementType> reimbursementTypesList = new ArrayList<>();

			when(reimbursementTypeRepo.findAll()).thenReturn(reimbursementTypesList);

			reimbursementTypeService.getReimbursementTypes();

			assertTrue(false);
		} catch (Exception e) {
			assertTrue(true);
		}

	}

}
