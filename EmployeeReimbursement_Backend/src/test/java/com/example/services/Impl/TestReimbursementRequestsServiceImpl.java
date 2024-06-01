package com.example.services.Impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.NewReimbursementRequestsDTO;
import com.example.dto.ProcessReimbursementDTO;
import com.example.dto.ReimbursementDTO;
import com.example.dto.ReimbursementTypeDTO;
import com.example.entities.ReimbursementRequests;
import com.example.entities.ReimbursementType;
import com.example.exception.ExpenseExceedsException;
import com.example.exception.InvalidInvoiceDateException;
import com.example.repositories.ReimbursementRequestsRepo;
import com.example.repositories.ReimbursementTypeRepo;
import com.example.services.Impl.ReimbursementRequestsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TestReimbursementRequestsServiceImpl {

	@Mock
	private ReimbursementRequestsRepo reimbursementRequestsRepo;

	@Mock
	private MultipartFile mockFile;

	@Mock
	private ModelMapper modelMapper;

	@Mock
	MessageSource messageSource;

	@Spy
	@InjectMocks
	private ReimbursementRequestsServiceImpl reimbursementRequestsService;

	private ReimbursementRequests reimbursementRequestMock;
	private ProcessReimbursementDTO processReimbursementDTOMock;
	private ReimbursementDTO reimbursementDTOMock;
	private NewReimbursementRequestsDTO newReimbursementRequestsDTOMock;
	private ReimbursementTypeDTO reimbursementTypeDTOMock;
	private ReimbursementType reimbursementTypeMock;
	private MultipartFile file;
	private String expectedURL;

	@BeforeEach
	void setUp() throws Exception {

		file = new MockMultipartFile("test.pdf", "test.pdf", "application/pdf", "test sample".getBytes());
		expectedURL = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
				+ "resources" + File.separator + "static" + File.separator + "documents" + File.separator
				+ file.getOriginalFilename();

		reimbursementTypeMock = ReimbursementType.builder().id(100).type("FOOD").build();
		reimbursementTypeDTOMock = ReimbursementTypeDTO.builder().id(100).type("FOOD").build();

		reimbursementRequestMock = ReimbursementRequests.builder().id(101).travelRequestId(1001)
				.requestRaisedByEmployeeId(201).invoiceNo("001").requestDate(new Date("03/02/2024"))
				.invoiceDate(new Date("03/01/2024")).invoiceAmount(1200).documentUrl(expectedURL)
				.requestProcessedOn(new Date("04/10/2024")).requestProcessedByEmployeeId(10).status("new")
				.reimbursementType(reimbursementTypeMock).build();

		processReimbursementDTOMock = ProcessReimbursementDTO.builder().id(101).travelRequestId(1001)
				.requestRaisedByEmployeeId(201).invoiceNo("001").requestDate(new Date("03/02/2024"))
				.invoiceDate(new Date("03/01/2024")).invoiceAmount(1200).documentUrl(expectedURL)
				.requestProcessedOn(new Date("04/10/2024")).requestProcessedByEmployeeId(10).status("approved")
				.reimbursementType(reimbursementTypeDTOMock).build();
		reimbursementDTOMock = ReimbursementDTO.builder().id(101).travelRequestId(1001).requestRaisedByEmployeeId(201)
				.invoiceNo("001").requestDate(new Date("03/02/2024")).invoiceDate(new Date("03/01/2024"))
				.invoiceAmount(1200).documentUrl(expectedURL).requestProcessedOn(new Date("04/10/2024"))
				.requestProcessedByEmployeeId(10).status("new").reimbursementType(reimbursementTypeDTOMock).build();

		newReimbursementRequestsDTOMock = NewReimbursementRequestsDTO.builder().id(101).travelRequestId(1001)
				.requestRaisedByEmployeeId(201).invoiceNo("001").requestDate(new Date("03/02/2024"))
				.fromTravelDate(new Date("01/01/2024")).toTravelDate(new Date("03/02/2024"))
				.invoiceDate(new Date("03/01/2024")).invoiceAmount(1200).documentUrl(expectedURL)
				.requestProcessedOn(new Date("04/10/2024")).reimbursementTypeDTO(reimbursementTypeDTOMock)
				.requestProcessedByEmployeeId(10).status("new").multipartFile(file).build();
	}

	@AfterEach
	void tearDown() throws Exception {

	}

	// ********** getAllReimbursementRequestsForTravelRequestId ************
	// ---------------------------------------------------------------------
	@DisplayName("When ReimbursementRequests found for given TravelRequestId return ReimbursementRequests list")
	@Test
	public void getAllReimbursementRequestsForTravelRequestId_WhenTravelRequestIdFound_ReturnReimbursementRequests() {
		try {
			// Arrange
			List<ReimbursementRequests> reimbursementRequestsList = new ArrayList<>();
			reimbursementRequestsList.add(reimbursementRequestMock);
			when(reimbursementRequestsRepo.findByTravelRequestId(reimbursementRequestMock.getTravelRequestId()))
					.thenReturn(reimbursementRequestsList);
			lenient().when(modelMapper.map(reimbursementRequestMock, ReimbursementDTO.class))
					.thenReturn(reimbursementDTOMock);

			// Act
			List<ReimbursementDTO> reimbursementDTO = reimbursementRequestsService
					.getAllReimbursementRequestsForTravelRequestId(reimbursementRequestMock.getTravelRequestId());
			// Assert
			Iterator<ReimbursementDTO> itr = reimbursementDTO.listIterator();
			assertTrue(reimbursementDTO.size() == 1);
			assertTrue(itr.hasNext());
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@DisplayName("When ReimbursementRequests not found for given TravelRequestId return Exception")
	@Test
	public void getAllReimbursementRequestsForTravelRequestId_WhenTravelRequestIdNotFound_ThrowException() {
		try {
			// Arrange
			List<ReimbursementRequests> reimbursementRequestsList = new ArrayList<>();
			when(reimbursementRequestsRepo.findByTravelRequestId(reimbursementRequestMock.getTravelRequestId()))
					.thenReturn(reimbursementRequestsList);
			// Act
			List<ReimbursementDTO> reimbursementDTO = reimbursementRequestsService
					.getAllReimbursementRequestsForTravelRequestId(reimbursementRequestMock.getTravelRequestId());
			// Assert
			assertFalse(reimbursementDTO.size() >= 0);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	// ********** processReimbursementRequests ************
	// ----------------------------------------------------
	@DisplayName("UpdateReimbursementRequests for given ReimbursementRequestsId")
	@Test
	public void processReimbursementRequests_WhenReimbursementRequestsIdFound_UpdateReimbursementRequests() {
		try {
			// Arrange
			when(reimbursementRequestsRepo.findById(reimbursementRequestMock.getId()))
					.thenReturn(Optional.of(reimbursementRequestMock));

			reimbursementRequestMock.setStatus("approved");
			when(reimbursementRequestsRepo.save(reimbursementRequestMock)).thenReturn(reimbursementRequestMock);

			when(modelMapper.map(reimbursementRequestMock, ProcessReimbursementDTO.class))
					.thenReturn(processReimbursementDTOMock);
			when(modelMapper.map(processReimbursementDTOMock, ReimbursementRequests.class))
					.thenReturn(reimbursementRequestMock);

			// Act
			ProcessReimbursementDTO processedRequests = reimbursementRequestsService
					.processReimbursementRequests(reimbursementRequestMock.getId(), processReimbursementDTOMock);

			// Assert
			assertEquals(processReimbursementDTOMock, processedRequests);
			assertEquals(processedRequests.getStatus(), "approved");

		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@DisplayName("When ReimbursementRequests not found by Id return Exception")
	@Test
	public void processReimbursementRequests_WhenReimbursementRequestsIdNotFound_ThrowException() {
		try {
			// Arrange
			ReimbursementRequests emptyReimbursementRequests = new ReimbursementRequests();
			when(reimbursementRequestsRepo.findById(reimbursementRequestMock.getId()))
					.thenReturn(Optional.of(emptyReimbursementRequests));
			// Act
			ProcessReimbursementDTO processedRequests = reimbursementRequestsService
					.processReimbursementRequests(reimbursementRequestMock.getId(), processReimbursementDTOMock);
			// Assert
			assertNull(processedRequests);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@DisplayName("When ReimbursementRequests Status is Invalid return Exception")
	@Test
	public void processReimbursementRequests_WhenReimbursementRequestsStatusIsInvalid_ThrowException() {
		try {
			// Arrange
			processReimbursementDTOMock.setStatus("random_status");
			ReimbursementRequests emptyReimbursementRequests = new ReimbursementRequests();
			when(reimbursementRequestsRepo.findById(reimbursementRequestMock.getId()))
					.thenReturn(Optional.of(emptyReimbursementRequests));
			// Act
			ProcessReimbursementDTO processedRequests = reimbursementRequestsService
					.processReimbursementRequests(reimbursementRequestMock.getId(), processReimbursementDTOMock);
			// Assert
			assertNull(processedRequests);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@DisplayName("When updating Reimbursement check Status is Rejected AND Remark is not Empty update Reimbursement")
	@Test
	public void processReimbursementRequests_WhenStatusIsRejectedAndReamrkIsNotEmpty_ReturnProcessReimbursementDTO() {
		try {
			// Arrange
			processReimbursementDTOMock.setStatus("rejected");
			processReimbursementDTOMock.setRemark("This is Remark");

			when(reimbursementRequestsRepo.findById(reimbursementRequestMock.getId()))
					.thenReturn(Optional.of(reimbursementRequestMock));

			reimbursementRequestMock.setStatus("rejected");
			reimbursementRequestMock.setRemark("This is Remark");

			when(reimbursementRequestsRepo.save(reimbursementRequestMock)).thenReturn(reimbursementRequestMock);
			when(modelMapper.map(reimbursementRequestMock, ProcessReimbursementDTO.class))
					.thenReturn(processReimbursementDTOMock);
			when(modelMapper.map(processReimbursementDTOMock, ReimbursementRequests.class))
					.thenReturn(reimbursementRequestMock);

			// Act
			ProcessReimbursementDTO processedRequests = reimbursementRequestsService
					.processReimbursementRequests(reimbursementRequestMock.getId(), processReimbursementDTOMock);
			// Assert
			assertEquals(processedRequests, processReimbursementDTOMock);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			assertTrue(false);
		}
	}

	@DisplayName("When updating Reimbursement check if Status is Rejected AND Remark is Empty then throw Exception")
	@Test
	public void processReimbursementRequests_WhenStatusIsRejectedAndReamrkIsEmpty_ThrowException() {
		try {
			// Arrange
			processReimbursementDTOMock.setStatus("Rejected");
			processReimbursementDTOMock.setRemark("");

			when(reimbursementRequestsRepo.findById(reimbursementRequestMock.getId()))
					.thenReturn(Optional.of(reimbursementRequestMock));

			// Act
			reimbursementRequestsService.processReimbursementRequests(reimbursementRequestMock.getId(),
					processReimbursementDTOMock);
			// Assert
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	// ********** getReimbursementRequestById ************
	// ----------------------------------------------------
	@DisplayName("When Reimbursement Id is present return the Reimbursement Request")
	@Test
	public void getReimbursementRequestById_WhenReimbursementFoundById_ReturnReimbursementDTO() {
		try {
			// Arrange
			when(reimbursementRequestsRepo.findById(reimbursementRequestMock.getId()))
					.thenReturn(Optional.of(reimbursementRequestMock));
			when(modelMapper.map(reimbursementRequestMock, ReimbursementDTO.class)).thenReturn(reimbursementDTOMock);
			// Act
			ReimbursementDTO reimbursementDTO = reimbursementRequestsService
					.getReimbursementRequestById(reimbursementRequestMock.getId());

			// Assert
			assertEquals(reimbursementDTOMock, reimbursementDTO);
			assertEquals(reimbursementRequestMock.getId(), reimbursementDTO.getId());
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@DisplayName("When Reimbursement not found present for given Id throw Exception")
	@Test
	public void getReimbursementRequestById_WhenReimbursementNotFoundById_ThrowException() {
		try {
			// Arrange
			when(reimbursementRequestsRepo.findById(reimbursementRequestMock.getId()))
					.thenReturn(Optional.of(new ReimbursementRequests()));
			// Act
			ReimbursementDTO reimbursementDTO = reimbursementRequestsService
					.getReimbursementRequestById(reimbursementRequestMock.getId());
			// Assert
			assertTrue(reimbursementDTO.getId() != 0);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	// ********** addReimbursementRequest ************
	// ----------------------------------------------------
	@DisplayName("Given NewReimbursementRequestsDTO saves request and returns NewReimbursementRequestsDTO")
	@Test
	public void addReimbursementRequest_WhenNewReimbursementRequestsDTOIsGiven_ReturnNewReimbursementRequestsDTO()
			throws IOException {

		try {
			// Arrange
			doReturn(expectedURL).when(reimbursementRequestsService)
					.verifyDocument(newReimbursementRequestsDTOMock.getMultipartFile());
			doReturn(false).when(reimbursementRequestsService).verifyTotalAllowedExpense(
					reimbursementTypeDTOMock.getType().toUpperCase(),
					newReimbursementRequestsDTOMock.getInvoiceAmount());

			when(reimbursementRequestsRepo.save(reimbursementRequestMock)).thenReturn(reimbursementRequestMock);

			lenient().when(modelMapper.map(reimbursementTypeDTOMock, ReimbursementType.class))
					.thenReturn(reimbursementTypeMock);
			lenient().when(modelMapper.map(reimbursementTypeMock, ReimbursementTypeDTO.class))
					.thenReturn(reimbursementTypeDTOMock);

			lenient().when(modelMapper.map(newReimbursementRequestsDTOMock, ReimbursementRequests.class))
					.thenReturn(reimbursementRequestMock);
			lenient().when(modelMapper.map(reimbursementRequestMock, NewReimbursementRequestsDTO.class))
					.thenReturn(newReimbursementRequestsDTOMock);
			// Act
			NewReimbursementRequestsDTO addedReimbursementRequest = reimbursementRequestsService
					.addReimbursementRequest(newReimbursementRequestsDTOMock);
			// Assert
			assertEquals(expectedURL, addedReimbursementRequest.getDocumentUrl());
			assertEquals("new", addedReimbursementRequest.getStatus());
			verify(reimbursementRequestsService, times(1))
					.verifyDocument(newReimbursementRequestsDTOMock.getMultipartFile());
			verify(reimbursementRequestsService, times(1)).verifyTotalAllowedExpense(
					reimbursementTypeDTOMock.getType().toUpperCase(),
					newReimbursementRequestsDTOMock.getInvoiceAmount());
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@DisplayName("While adding request if document path length is zero throws exception")
	@Test
	public void addReimbursementRequest_WhenDocumentURLLengthIsZero_ReturnException()
			throws IOException, ParseException {

		try {
			// Arrange
			doReturn("").when(reimbursementRequestsService)
					.verifyDocument(newReimbursementRequestsDTOMock.getMultipartFile());

			NewReimbursementRequestsDTO addReimbursementRequest = reimbursementRequestsService.addReimbursementRequest(newReimbursementRequestsDTOMock);
			assertEquals(newReimbursementRequestsDTOMock.getMultipartFile(), addReimbursementRequest.getMultipartFile());
		} catch (Exception e) {
			assertTrue(true);
		}

	}

	@Test
	public void addReimbursementRequest_WhenFromTravelDateIsAfterInvoiceDate_ReturnException()
			throws IOException, ParseException {
		// Arrange
		newReimbursementRequestsDTOMock.setFromTravelDate(new Date("05/01/2024"));
		doReturn(expectedURL).when(reimbursementRequestsService)
				.verifyDocument(newReimbursementRequestsDTOMock.getMultipartFile());

		// Act & Assert
		assertThrows(InvalidInvoiceDateException.class,
				() -> reimbursementRequestsService.addReimbursementRequest(newReimbursementRequestsDTOMock));

	}

	@Test
	public void addReimbursementRequest_WhenToTravelDateIsBeforeInvoiceDate_ReturnException()
			throws IOException, ParseException {
		// Arrange
		newReimbursementRequestsDTOMock.setToTravelDate(new Date("01/01/2024"));
		doReturn(expectedURL).when(reimbursementRequestsService)
				.verifyDocument(newReimbursementRequestsDTOMock.getMultipartFile());

		// Act & Assert
		assertThrows(InvalidInvoiceDateException.class,
				() -> reimbursementRequestsService.addReimbursementRequest(newReimbursementRequestsDTOMock));

	}

	@Test
	public void addReimbursementRequest_WhenInvoiceDateIsNotInRangeOfFromTravelDateAndToTravelDate_ReturnException()
			throws Exception {
		// Arrange
		newReimbursementRequestsDTOMock.setFromTravelDate(new Date("05/01/2024"));
		newReimbursementRequestsDTOMock.setToTravelDate(new Date("01/01/2024"));
		doReturn(expectedURL).when(reimbursementRequestsService)
				.verifyDocument(newReimbursementRequestsDTOMock.getMultipartFile());

		// Act & Assert
		assertThrows(InvalidInvoiceDateException.class,
				() -> reimbursementRequestsService.addReimbursementRequest(newReimbursementRequestsDTOMock));
	}

	@Test
	public void addReimbursementRequest_WhenInvoiceAmountExceeds_GivesTrue_ReturnException() throws Exception {
		// Arrange
		newReimbursementRequestsDTOMock.setInvoiceAmount(800);
		doReturn(expectedURL).when(reimbursementRequestsService)
				.verifyDocument(newReimbursementRequestsDTOMock.getMultipartFile());
		doReturn(true).when(reimbursementRequestsService).verifyTotalAllowedExpense(
				reimbursementTypeDTOMock.getType().toUpperCase(), newReimbursementRequestsDTOMock.getInvoiceAmount());

		// Act & Assert
		assertThrows(ExpenseExceedsException.class,
				() -> reimbursementRequestsService.addReimbursementRequest(newReimbursementRequestsDTOMock));

	}

	// ********** verifyTotalAllowedExpense ************
	// ----------------------------------------------------
	@Test
	public void verifyTotalAllowedExpense_FoodType_ValidTotalAmount_ReturnFalse() {

		reimbursementTypeDTOMock = ReimbursementTypeDTO.builder().id(100).type("FOOD").build();

		newReimbursementRequestsDTOMock.setInvoiceAmount(1200);
		boolean allowedExpenseExceeds = reimbursementRequestsService.verifyTotalAllowedExpense(
				reimbursementTypeDTOMock.getType().toUpperCase(), newReimbursementRequestsDTOMock.getInvoiceAmount());

		assertFalse(allowedExpenseExceeds);
	}

	@Test
	public void verifyTotalAllowedExpense_FoodType_InValidTotalAmount_ReturnTrue() {

		reimbursementTypeDTOMock = ReimbursementTypeDTO.builder().id(100).type("FOOD").build();

		newReimbursementRequestsDTOMock.setInvoiceAmount(1600);
		boolean allowedExpenseExceeds = reimbursementRequestsService.verifyTotalAllowedExpense(
				reimbursementTypeDTOMock.getType().toUpperCase(), newReimbursementRequestsDTOMock.getInvoiceAmount());

		assertTrue(allowedExpenseExceeds);
	}

	@Test
	public void verifyTotalAllowedExpense_WaterType_ValidTotalAmount_ReturnFalse() {

		reimbursementTypeDTOMock = ReimbursementTypeDTO.builder().id(100).type("WATER").build();

		newReimbursementRequestsDTOMock.setInvoiceAmount(1300);
		boolean allowedExpenseExceeds = reimbursementRequestsService.verifyTotalAllowedExpense(
				reimbursementTypeDTOMock.getType().toUpperCase(), newReimbursementRequestsDTOMock.getInvoiceAmount());

		assertFalse(allowedExpenseExceeds);
	}

	@Test
	public void verifyTotalAllowedExpense_WaterType_InValidTotalAmount_ReturnTrue() {

		reimbursementTypeDTOMock = ReimbursementTypeDTO.builder().id(100).type("WATER").build();

		newReimbursementRequestsDTOMock.setInvoiceAmount(800);
		boolean allowedExpenseExceeds = reimbursementRequestsService.verifyTotalAllowedExpense(
				reimbursementTypeDTOMock.getType().toUpperCase(), newReimbursementRequestsDTOMock.getInvoiceAmount());

		assertTrue(allowedExpenseExceeds);
	}

	@Test
	public void verifyTotalAllowedExpense_LaundryType_ValidTotalAmount_ReturnFalse() {

		reimbursementTypeDTOMock = ReimbursementTypeDTO.builder().id(100).type("LAUNDRY").build();

		newReimbursementRequestsDTOMock.setInvoiceAmount(400);
		boolean allowedExpenseExceeds = reimbursementRequestsService.verifyTotalAllowedExpense(
				reimbursementTypeDTOMock.getType().toUpperCase(), newReimbursementRequestsDTOMock.getInvoiceAmount());

		assertFalse(allowedExpenseExceeds);
	}

	@Test
	public void verifyTotalAllowedExpense_LaundryType_InValidTotalAmount_ReturnTrue() {

		reimbursementTypeDTOMock = ReimbursementTypeDTO.builder().id(100).type("LAUNDRY").build();

		newReimbursementRequestsDTOMock.setInvoiceAmount(700);
		boolean allowedExpenseExceeds = reimbursementRequestsService.verifyTotalAllowedExpense(
				reimbursementTypeDTOMock.getType().toUpperCase(), newReimbursementRequestsDTOMock.getInvoiceAmount());

		assertTrue(allowedExpenseExceeds);
	}

	@Test
	public void verifyTotalAllowedExpense_LocalTravelType_ValidTotalAmount_ReturnFalse() {

		reimbursementTypeDTOMock = ReimbursementTypeDTO.builder().id(100).type("LOCAL TRAVEL").build();

		newReimbursementRequestsDTOMock.setInvoiceAmount(800);
		boolean allowedExpenseExceeds = reimbursementRequestsService.verifyTotalAllowedExpense(
				reimbursementTypeDTOMock.getType().toUpperCase(), newReimbursementRequestsDTOMock.getInvoiceAmount());

		assertFalse(allowedExpenseExceeds);
	}

	@Test
	public void verifyTotalAllowedExpense_LocalTravelType_InValidTotalAmount_ReturnTrue() {

		reimbursementTypeDTOMock = ReimbursementTypeDTO.builder().id(100).type("LOCAL TRAVEL").build();

		newReimbursementRequestsDTOMock.setInvoiceAmount(1700);
		boolean allowedExpenseExceeds = reimbursementRequestsService.verifyTotalAllowedExpense(
				reimbursementTypeDTOMock.getType().toUpperCase(), newReimbursementRequestsDTOMock.getInvoiceAmount());

		assertTrue(allowedExpenseExceeds);
	}

	// ********** verifyDocument ************
	// ----------------------------------------------------
	@Test
	public void validateDocument_EmptyFile_ThrowsFileNotFoundException() {
		when(mockFile.isEmpty()).thenReturn(true);
		assertThrows(FileNotFoundException.class, ()-> reimbursementRequestsService.verifyDocument(mockFile));
	}

	@Test
	public void validateDocument_NonPDFFile_ThrowsFileUploadException() {
		when(mockFile.isEmpty()).thenReturn(false);
		when(mockFile.getContentType()).thenReturn("image/jpeg");
		assertThrows(FileUploadException.class, ()-> reimbursementRequestsService.verifyDocument(mockFile));
	}

	@Test
	public void validateDocument_FileSizeExceedsLimit_ThrowsFileSizeLimitExceededException() {
		when(mockFile.isEmpty()).thenReturn(false);
		when(mockFile.getContentType()).thenReturn("application/pdf");
		when(mockFile.getSize()).thenReturn(300*1024L);
		assertThrows(FileSizeLimitExceededException.class, ()-> reimbursementRequestsService.verifyDocument(mockFile));
	}

	@Test
	public void validateDocument_SuccessfullFileUpload_ReturnsDocumentUrl() throws IOException {
		
			when(mockFile.isEmpty()).thenReturn(false);
			when(mockFile.getContentType()).thenReturn("application/pdf");
			when(mockFile.getSize()).thenReturn(200 * 1024L);
			when(mockFile.getOriginalFilename()).thenReturn("test.pdf");
			when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[10]));
			
			String expectedURL = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
					+ File.separator + "resources" + File.separator + "static" + File.separator + "documents"
					+ File.separator + mockFile.getOriginalFilename();

			
			try(MockedStatic<Files> mockedStatic = Mockito.mockStatic(Files.class)){
				mockedStatic.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class))).thenAnswer(invocation-> {
					Object[] args = invocation.getArguments();					
					return null;
				});
			}
			String actualUrl = reimbursementRequestsService.verifyDocument(mockFile);		
			assertEquals(expectedURL, actualUrl);
		
	}

}
