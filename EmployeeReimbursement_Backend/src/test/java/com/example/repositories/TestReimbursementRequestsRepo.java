package com.example.repositories;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import com.example.EmployeeReimbursementApplication;
import com.example.entities.ReimbursementRequests;
import com.example.entities.ReimbursementType;
import com.example.repositories.ReimbursementRequestsRepo;

@DataJpaTest
public class TestReimbursementRequestsRepo {

	@Autowired
	private ReimbursementRequestsRepo reimbursementRequestsRepo;
	
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void FindAllByTravelRequestId_ReturnsMoreThanOneReimbursementType() {

		// ******* ARRANGE ********//
		// Reimbursement Type
		ReimbursementType reimbursementType = new ReimbursementType();
		reimbursementType.setId(105);
		reimbursementType.setType("FOODS");
		entityManager.persist(reimbursementType);
		

		// ReimbursementRequest 01
		ReimbursementRequests reimbursementRequests_01 = new ReimbursementRequests();
		reimbursementRequests_01.setTravelRequestId(1001);
		reimbursementRequests_01.setRequestRaisedByEmployeeId(201);
		reimbursementRequests_01.setInvoiceNo("001");
		reimbursementRequests_01.setInvoiceDate(new Date("03/01/2024")); // past date
		reimbursementRequests_01.setInvoiceAmount(100);
		reimbursementRequests_01.setDocumentUrl("URL");
		reimbursementRequests_01.setRequestProcessedOn(new Date("04/10/2024")); // future date
		reimbursementRequests_01.setRequestProcessedByEmployeeId(10);
		reimbursementRequests_01.setStatus("new");
		reimbursementRequests_01.setReimbursementType(reimbursementType);
		entityManager.persist(reimbursementRequests_01);

		// ReimbursementRequest 02
		ReimbursementRequests reimbursementRequests_02 = new ReimbursementRequests();
		reimbursementRequests_02.setTravelRequestId(1001);
		reimbursementRequests_02.setRequestRaisedByEmployeeId(202);
		reimbursementRequests_02.setInvoiceNo("002");
		reimbursementRequests_02.setInvoiceDate(new Date("03/01/2024")); // past date
		reimbursementRequests_02.setInvoiceAmount(1000);
		reimbursementRequests_02.setDocumentUrl("URL");
		reimbursementRequests_02.setRequestProcessedOn(new Date("04/10/2024")); // future date
		reimbursementRequests_02.setRequestProcessedByEmployeeId(10);
		reimbursementRequests_02.setStatus("new");
		reimbursementRequests_02.setReimbursementType(reimbursementType);
		entityManager.persist(reimbursementRequests_02);

		// ******* ACT ********//
		List<ReimbursementRequests> allRequestsByTravelRequestId = reimbursementRequestsRepo
				.findByTravelRequestId(1001);
		// ******* ASSERT ********//
		assertTrue(allRequestsByTravelRequestId.size() == 2);

	}

	@Test
	public void FindAllByTravelRequestId_ReturnsOneReimbursementType() {

		// ******* ARRANGE ********//
		// Reimbursement Type
		ReimbursementType reimbursementType = new ReimbursementType();
		reimbursementType.setId(105);
		reimbursementType.setType("FOODS");
		entityManager.persist(reimbursementType);

		// ReimbursementRequest 01
		ReimbursementRequests reimbursementRequests_01 = new ReimbursementRequests();
		reimbursementRequests_01.setTravelRequestId(1001);
		reimbursementRequests_01.setRequestRaisedByEmployeeId(201);
		reimbursementRequests_01.setInvoiceNo("001");
		reimbursementRequests_01.setInvoiceDate(new Date("03/01/2024")); // past date
		reimbursementRequests_01.setInvoiceAmount(100);
		reimbursementRequests_01.setDocumentUrl("URL");
		reimbursementRequests_01.setRequestProcessedOn(new Date("04/10/2024")); // future date
		reimbursementRequests_01.setRequestProcessedByEmployeeId(10);
		reimbursementRequests_01.setStatus("new");
		reimbursementRequests_01.setReimbursementType(reimbursementType);
		entityManager.persist(reimbursementRequests_01);

		// ******* ACT ********//
		Iterable<ReimbursementRequests> it = reimbursementRequestsRepo.findByTravelRequestId(1001);
		// ******* ASSERT ********//
		assertTrue(it.iterator().hasNext());

	}

	@Test
	public void testFindAllByTravelRequestId_Negative() {
		Iterable<ReimbursementRequests> it = reimbursementRequestsRepo.findByTravelRequestId(1001);
		assertTrue(!it.iterator().hasNext());
	}
}
