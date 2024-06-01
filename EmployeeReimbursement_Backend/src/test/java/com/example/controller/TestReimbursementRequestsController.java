package com.example.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

import com.example.controller.ReimbursementRequestsController;
import com.example.dto.NewReimbursementRequestsDTO;
import com.example.dto.ProcessReimbursementDTO;
import com.example.dto.ReimbursementDTO;
import com.example.dto.ReimbursementTypeDTO;
import com.example.exception.EmptyRemarkFoStatusException;
import com.example.exception.InvalidStatusException;
import com.example.exception.ReimbursementRequestsNotFoundException;
import com.example.services.ReimbursementRequestsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ReimbursementRequestsController.class)
public class TestReimbursementRequestsController {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private ObjectMapper objectMapper;

	@MockBean
	private ReimbursementRequestsService requestsServiceImpl;

	@InjectMocks
	private ReimbursementRequestsController requestsController;

	private ReimbursementTypeDTO reimbursementTypeDTOMock;
	private ReimbursementDTO reimbursementDTOMock;
	private NewReimbursementRequestsDTO newReimbursementRequestsDTOMock;
	private ProcessReimbursementDTO processReimbursementDTOMock;
	private MultipartFile file;
	private String expectedURL, newReimbursementRequestsString;

	@BeforeEach
	void setUp() throws Exception {

		file = new MockMultipartFile("test.pdf", "test.pdf", "application/pdf", "test sample".getBytes());
		expectedURL = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
				+ "resources" + File.separator + "static" + File.separator + "documents" + File.separator
				+ file.getOriginalFilename();
		reimbursementTypeDTOMock = ReimbursementTypeDTO.builder().id(100).type("FOOD").build();

		newReimbursementRequestsDTOMock = NewReimbursementRequestsDTO.builder().id(101).travelRequestId(1001)
				.requestRaisedByEmployeeId(201).invoiceNo("001").requestDate(new Date("04/15/2024"))
				.fromTravelDate(new Date("04/10/2024")).toTravelDate(new Date("04/28/2024"))
				.invoiceDate(new Date("03/01/2024")).invoiceAmount(1200)
				.requestProcessedOn(new Date("04/23/2024")).requestProcessedByEmployeeId(10).status("new").build();
		newReimbursementRequestsString = "{\r\n"
				+ "    \"travelRequestId\": 1001,\r\n"
				+ "    \"requestRaisedByEmployeeId\": 201,\r\n"
				+ "    \"requestDate\": \"2024-04-19\",\r\n"
				+ "    \"reimbursementTypeDTO\": {\r\n"
				+ "        \"id\": 101,\r\n"
				+ "        \"type\": \"FOOD\"\r\n"
				+ "    },\r\n"
				+ "    \"invoiceNo\": \"001\",\r\n"
				+ "    \"invoiceDate\": \"2024-04-15\",\r\n"
				+ "    \"invoiceAmount\": 1200,\r\n"
				+ "    \"requestProcessedOn\": \"2024-04-23\",\r\n"
				+ "    \"requestProcessedByEmployeeId\": 10,\r\n"
				+ "    \"fromTravelDate\": \"2024-04-10\",\r\n"
				+ "    \"toTravelDate\": \"2024-04-28\"\r\n"
				+ "}";
		reimbursementDTOMock = ReimbursementDTO.builder().id(101).travelRequestId(1001).requestRaisedByEmployeeId(201)
				.invoiceNo("001").requestDate(new Date("03/02/2024")).invoiceDate(new Date("03/01/2024"))
				.invoiceAmount(1200).documentUrl(expectedURL).requestProcessedOn(new Date("04/10/2024"))
				.requestProcessedByEmployeeId(10).status("new").reimbursementType(reimbursementTypeDTOMock).build();
		processReimbursementDTOMock = ProcessReimbursementDTO.builder().id(101).travelRequestId(1001)
				.requestRaisedByEmployeeId(201).invoiceNo("001").requestDate(new Date("03/02/2024"))
				.invoiceDate(new Date("03/01/2024")).invoiceAmount(1200).documentUrl(expectedURL)
				.requestProcessedOn(new Date("04/10/2024")).requestProcessedByEmployeeId(10).status("approved")
				.reimbursementType(reimbursementTypeDTOMock).build();
	}

	@AfterEach
	void tearDown() throws Exception {

	}

	// ++++++++++++++++++ handleAddNewReimbursementRequest +++++++++++++++++++
	@Test
	public void handleAddNewReimbursementRequest_returnPositiveStatusCode() throws Exception
	{		
		when(requestsServiceImpl.addReimbursementRequest(newReimbursementRequestsDTOMock)).thenReturn(newReimbursementRequestsDTOMock);
		when(objectMapper.readValue(newReimbursementRequestsString, NewReimbursementRequestsDTO.class)).thenReturn(newReimbursementRequestsDTOMock);
		ResponseEntity<?> response=requestsController.addNewReimbursementRequest(newReimbursementRequestsString, newReimbursementRequestsDTOMock.getMultipartFile());
		assertEquals(HttpStatus.CREATED,response.getStatusCode());
	}

	@Test
	public void handleAddNewReimbursementRequest_returnNegativeStatusCode() throws Exception
	{		
		when(requestsServiceImpl.addReimbursementRequest(newReimbursementRequestsDTOMock)).thenReturn(new NewReimbursementRequestsDTO());
		when(objectMapper.readValue(newReimbursementRequestsString, NewReimbursementRequestsDTO.class)).thenReturn(newReimbursementRequestsDTOMock);
		ResponseEntity<?> response=requestsController.addNewReimbursementRequest(newReimbursementRequestsString, newReimbursementRequestsDTOMock.getMultipartFile());
		assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
	}

	// ++++++++++++++++++++++ handleGetAllReimbursementsForTravelRequestId +++++++++
	@Test
	public void handleGetAllReimbursementsForTravelRequestId_returnPositiveStatusCode() throws Exception
	{		
		when(requestsServiceImpl.getAllReimbursementRequestsForTravelRequestId(reimbursementDTOMock.getTravelRequestId())).thenReturn(Arrays.asList(reimbursementDTOMock));		
		ResponseEntity<List<ReimbursementDTO>> response = requestsController.getAllReimbursementsForTravelRequestId(reimbursementDTOMock.getTravelRequestId());		
		assertEquals(HttpStatus.OK, response.getStatusCode());		
	}

	@Test
	public void handleGetAllReimbursementsForTravelRequestId_returnNegativeStatusCode() throws Exception {
		ReimbursementDTO dto = new ReimbursementDTO();
		when(requestsServiceImpl
				.getAllReimbursementRequestsForTravelRequestId(reimbursementDTOMock.getTravelRequestId()))
				.thenReturn(Arrays.asList(dto));
		ResponseEntity<List<ReimbursementDTO>> response = requestsController
				.getAllReimbursementsForTravelRequestId(reimbursementDTOMock.getTravelRequestId());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void handleGetAllReimbursementsForTravelRequestId_returnReimbursementDTOList() throws Exception
	{		
		when(requestsServiceImpl.getAllReimbursementRequestsForTravelRequestId(reimbursementDTOMock.getTravelRequestId())).thenReturn(Arrays.asList(reimbursementDTOMock));		
		ResponseEntity<List<ReimbursementDTO>> response = requestsController.getAllReimbursementsForTravelRequestId(reimbursementDTOMock.getTravelRequestId());
		List<ReimbursementDTO> actual = response.getBody();
		assertTrue(actual.size() > 0);		
	}

	@Test
	public void handleGetAllReimbursementsForTravelRequestId_returnNull() throws Exception {
		ReimbursementDTO dto = new ReimbursementDTO();
		when(requestsServiceImpl
				.getAllReimbursementRequestsForTravelRequestId(reimbursementDTOMock.getTravelRequestId()))
				.thenReturn(Arrays.asList(dto));
		ResponseEntity<List<ReimbursementDTO>> response = requestsController
				.getAllReimbursementsForTravelRequestId(reimbursementDTOMock.getTravelRequestId());
		assertNull(response.getBody());
	}

	@Test
	public void uriHandleGetAllReimbursementsForTravelRequestId_Positive() throws Exception
	{
		when(requestsServiceImpl.getAllReimbursementRequestsForTravelRequestId(reimbursementDTOMock.getTravelRequestId())).thenReturn(Arrays.asList(reimbursementDTOMock));
		MvcResult mvcResult=mockMvc
				.perform(get("http://localhost:8080/api/reimbursements/"+reimbursementDTOMock.getTravelRequestId()+"/requests"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].travelRequestId", is(reimbursementDTOMock.getTravelRequestId())))
				.andReturn();
	}

	@Test
	public void uriHandleGetAllReimbursementsForTravelRequestId_Negative() throws Exception
	{
		when(requestsServiceImpl.getAllReimbursementRequestsForTravelRequestId(reimbursementDTOMock.getTravelRequestId())).thenReturn(Arrays.asList(reimbursementDTOMock));
		MvcResult mvcResult=mockMvc
				.perform(get("http://localhost:8080/api/reimbursements/"+reimbursementDTOMock.getTravelRequestId()+"/requestsss"))
				.andExpect(status().isNotFound())
				.andReturn();
	}

	// +++++++++++++++++++ handleGetReimbursementById ++++++++++++++++++
	@Test
	public void handleGetReimbursementById_returnPositiveStatusCode() throws ReimbursementRequestsNotFoundException
	{
		when(requestsServiceImpl.getReimbursementRequestById(reimbursementDTOMock.getId())).thenReturn(reimbursementDTOMock);
		
		ResponseEntity<ReimbursementDTO> response = requestsController.getReimbursementById(reimbursementDTOMock.getId());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void handleGetReimbursementById_returnNegativeStatusCode() throws ReimbursementRequestsNotFoundException
	{
		when(requestsServiceImpl.getReimbursementRequestById(reimbursementDTOMock.getId())).thenReturn(new ReimbursementDTO());
		
		ResponseEntity<ReimbursementDTO> response = requestsController.getReimbursementById(reimbursementDTOMock.getId());
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void handleGetReimbursementById_returnReimbursementDTO() throws ReimbursementRequestsNotFoundException
	{
		when(requestsServiceImpl.getReimbursementRequestById(reimbursementDTOMock.getId())).thenReturn(reimbursementDTOMock);
		
		ResponseEntity<ReimbursementDTO> response = requestsController.getReimbursementById(reimbursementDTOMock.getId());
		
		assertEquals(reimbursementDTOMock, response.getBody());
	}

	@Test
	public void handleGetReimbursementById_returnNullValue() throws ReimbursementRequestsNotFoundException
	{
		when(requestsServiceImpl.getReimbursementRequestById(reimbursementDTOMock.getId())).thenReturn(new ReimbursementDTO());
		
		ResponseEntity<ReimbursementDTO> response = requestsController.getReimbursementById(reimbursementDTOMock.getId());
		
		
		assertNull(response.getBody());
	}

	@Test
	public void uriHandleGetReimbursementById_Positive() throws Exception
	{
		when(requestsServiceImpl.getReimbursementRequestById(reimbursementDTOMock.getId())).thenReturn(reimbursementDTOMock);
		MvcResult mvcResult=mockMvc
				.perform(get("http://localhost:8080/api/reimbursements/"+reimbursementDTOMock.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(reimbursementDTOMock.getId())))
				.andReturn();
	}

	@Test
	public void uriHandleGetReimbursementById_Negative() throws Exception
	{
		when(requestsServiceImpl.getReimbursementRequestById(reimbursementDTOMock.getId())).thenReturn(reimbursementDTOMock);
		MvcResult mvcResult=mockMvc
				.perform(get("http://localhost:8080/api/reimbursementss/"+reimbursementDTOMock.getId()))
				.andExpect(status().isNotFound())
				.andReturn();
	}

	// +++++++++++++++++++++ handleProcessReimbursementById +++++++++++++++++++
	@Test
	public void handleProcessReimbursementById_returnPositiveStatusCode() throws ReimbursementRequestsNotFoundException, InvalidStatusException, EmptyRemarkFoStatusException	{
		when(requestsServiceImpl.processReimbursementRequests(processReimbursementDTOMock.getId(), processReimbursementDTOMock)).thenReturn(processReimbursementDTOMock);			
		ResponseEntity<ProcessReimbursementDTO> response = requestsController.processReimbursementById(processReimbursementDTOMock.getId(), processReimbursementDTOMock);		
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void handleProcessReimbursementById_returnNegativeStatusCode() throws ReimbursementRequestsNotFoundException, InvalidStatusException, EmptyRemarkFoStatusException	{
		when(requestsServiceImpl.processReimbursementRequests(processReimbursementDTOMock.getId(), processReimbursementDTOMock)).thenReturn(new ProcessReimbursementDTO());			
		ResponseEntity<ProcessReimbursementDTO> response = requestsController.processReimbursementById(processReimbursementDTOMock.getId(), processReimbursementDTOMock);		
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void handleProcessReimbursementById_returnProcessReimbursementDTO() throws ReimbursementRequestsNotFoundException, InvalidStatusException, EmptyRemarkFoStatusException	{
		when(requestsServiceImpl.processReimbursementRequests(processReimbursementDTOMock.getId(), processReimbursementDTOMock)).thenReturn(processReimbursementDTOMock);			
		ResponseEntity<ProcessReimbursementDTO> response = requestsController.processReimbursementById(processReimbursementDTOMock.getId(), processReimbursementDTOMock);		
		
		assertEquals(processReimbursementDTOMock, response.getBody());
	}

	@Test
	public void handleProcessReimbursementById_returnNullValue() throws ReimbursementRequestsNotFoundException, InvalidStatusException, EmptyRemarkFoStatusException	{
		when(requestsServiceImpl.processReimbursementRequests(processReimbursementDTOMock.getId(), processReimbursementDTOMock)).thenReturn(new ProcessReimbursementDTO());			
		ResponseEntity<ProcessReimbursementDTO> response = requestsController.processReimbursementById(processReimbursementDTOMock.getId(), processReimbursementDTOMock);		
		
		assertNull(response.getBody());
	}

}
