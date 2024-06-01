package com.example.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.controller.ReimbursementTypeController;
import com.example.dto.ReimbursementTypeDTO;
import com.example.services.Impl.ReimbursementTypeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TestReimbursementTypeController {

	@Mock
	private ReimbursementTypeServiceImpl typeServiceImpl;
	
	@InjectMocks
	private ReimbursementTypeController typeController;
	
	
	private ReimbursementTypeDTO reimbursementTypeDTOMock;

	@BeforeEach
	void setUp() throws Exception {
		reimbursementTypeDTOMock = ReimbursementTypeDTO.builder().id(100).type("FOOD").build();

	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	
	
	@Test
	public void handleGetReimbursementTypes_returnPositiveStatusCode() throws Exception
	{		
		when(typeServiceImpl.getReimbursementTypes()).thenReturn(Arrays.asList(reimbursementTypeDTOMock));		
		ResponseEntity<?> response=typeController.getReimbursementTypes();
		assertEquals(HttpStatus.OK,response.getStatusCode());
	}

	@Test
	public void handleGetReimbursementTypes_returnNegativeStatusCode() throws Exception
	{		
		when(typeServiceImpl.getReimbursementTypes()).thenReturn(Arrays.asList(new ReimbursementTypeDTO()));		
		ResponseEntity<?> response=typeController.getReimbursementTypes();
		assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
	}
	

	@Test
	public void handleGetReimbursementTypes_returnReimbursementTypeDTOList() throws Exception
	{		
		when(typeServiceImpl.getReimbursementTypes()).thenReturn(Arrays.asList(reimbursementTypeDTOMock));		
		ResponseEntity<List<ReimbursementTypeDTO>> response = typeController.getReimbursementTypes();		
		List<ReimbursementTypeDTO> actual = response.getBody();
		assertEquals(Arrays.asList(reimbursementTypeDTOMock), actual);		
	}

	@Test
	public void handleGetReimbursementTypes_returnNull() throws Exception {
		when(typeServiceImpl.getReimbursementTypes()).thenReturn(Arrays.asList(new ReimbursementTypeDTO()));		
		ResponseEntity<List<ReimbursementTypeDTO>> response = typeController.getReimbursementTypes();
		assertNull(response.getBody());
	}



}
