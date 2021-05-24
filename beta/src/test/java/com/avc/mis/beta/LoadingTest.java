/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dto.exportdoc.InventoryExportDoc;
import com.avc.mis.beta.dto.exportdoc.SecurityExportDoc;
import com.avc.mis.beta.dto.process.ContainerArrivalDTO;
import com.avc.mis.beta.dto.process.ContainerBookingDTO;
import com.avc.mis.beta.dto.process.ContainerLoadingDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.embeddable.ShipingDetails;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.process.ContainerArrival;
import com.avc.mis.beta.entities.process.ContainerBooking;
import com.avc.mis.beta.entities.process.ContainerLoading;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.ShipmentCode;
import com.avc.mis.beta.service.ContainerArrivals;
import com.avc.mis.beta.service.ContainerBookings;
import com.avc.mis.beta.service.Loading;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.WarehouseManagement;

/**
 * @author zvi
 *
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithUserDetails("eli")
public class LoadingTest {

	@Autowired TestService service;	
	@Autowired WarehouseManagement warehouseManagement;

	@Autowired ContainerBookings bookings;
	@Autowired ContainerArrivals arrivals;
	@Autowired Loading loadingService;
	@Autowired ProcessInfoWriter processInfoWriter;
	
	@Test
	void bookingTest() {
		Receipt receipt;
		try {
			receipt = service.addBasicCashewReceipt();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
		processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
		
		//test container booking
		ContainerBooking booking = service.addBasicContainerBooking();		
		bookings.addBooking(booking);		
		ContainerBookingDTO expectedBooking = new ContainerBookingDTO(booking);		
		ContainerBookingDTO actualBooking = bookings.getBooking(booking.getId());		
		bookings.editBooking(booking);		
		assertEquals(expectedBooking, actualBooking, "Failed test adding container loading");
		
		//test container arrival
		ContainerArrival arrival = new ContainerArrival();
		arrival.setRecordedTime(LocalDateTime.now());
		ContainerDetails containerDetails = new ContainerDetails();
		containerDetails.setContainerNumber("CONT01");
		containerDetails.setSealNumber("SEAL01");
		containerDetails.setContainerType("20'");		
		arrival.setContainerDetails(containerDetails);
		arrival.setShipingDetails(service.getShipingDetails());
		arrivals.addArrival(arrival);
		ContainerArrivalDTO expectedArrival = new ContainerArrivalDTO(arrival);
		ContainerArrivalDTO actualArrival = arrivals.getArrival(arrival.getId());		
		arrivals.editArrival(arrival);		
		assertEquals(expectedArrival, actualArrival, "Failed test adding container loading");

		
		//test loading
		ContainerLoading loading = new ContainerLoading();
//		ContainerBooking refBooking = new ContainerBooking();
//		refBooking.setId(booking.getId());
//		refBooking.setVersion(booking.getVersion());
		loading.setArrival(arrival);
		loading.setShipmentCode(service.addShipmentCode());
		loading.setRecordedTime(LocalDateTime.now());

		//get inventory storages for transfer
		List<ProcessItemInventory> poInventory = warehouseManagement.getAvailableInventory(null, null, null, null, new Integer[] {receipt.getPoCode().getId()}, null);
		loading.setUsedItemGroups(TestService.getUsedItemsGroups(poInventory));
		
		loadingService.addLoading(loading);

		ContainerLoadingDTO expectedLoading = new ContainerLoadingDTO(loading);
		ContainerLoadingDTO actualLoading = loadingService.getLoading(loading.getId());

		try {
			InventoryExportDoc inventoryExportDoc = loadingService.getInventoryExportDoc(loading.getId());
			System.out.println(inventoryExportDoc);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			throw e2;
		}
		
		SecurityExportDoc securityExportDoc;
		try {
			securityExportDoc = loadingService.getSecurityExportDoc(loading.getId());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		System.out.println(securityExportDoc);
		
		try {
			loadingService.editLoading(loading);		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}		
		assertEquals(expectedLoading, actualLoading, "Failed test adding container loading");
		
		processInfoWriter.removeProcess(loading.getId());
//		loadingService.removeLoading(loading.getId());
		processInfoWriter.removeProcess(arrival.getId());
//		arrivals.removeArrival(arrival.getId());
		bookings.removeBooking(booking.getId());


	}

	@Test
	void loadingTest() {
		/*
		Receipt receipt;
		try {
			receipt = service.addBasicCashewReceipt();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
		processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
				
		ContainerLoading loading = new ContainerLoading();
		ShipmentCode shipmentCode = service.addShipmentCode();
		loading.setShipmentCode(shipmentCode);
		loading.setRecordedTime(OffsetDateTime.now());
		
		ContainerDetails containerDetails = new ContainerDetails();
		containerDetails.setContainerNumber("CONT01");
		containerDetails.setSealNumber("SEAL01");
		containerDetails.setContainerType("20'");		
		loading.setContainerDetails(containerDetails);
		
		ShipingDetails shipingDetails = new ShipingDetails();
		shipingDetails.setEtd("2007-12-03");
		shipingDetails.setEta("2008-12-03");
		loading.setShipingDetails(shipingDetails);

		//get inventory storages for transfer
		List<ProcessItemInventory> poInventory = warehouseManagement.getAvailableInventory(null, null, null, null, new Integer[] {receipt.getPoCode().getId()});
		loading.setUsedItemGroups(TestService.getUsedItemsGroups(poInventory));
		
		loadingService.addLoading(loading);
		
		ContainerLoadingDTO expected;
		try {
			expected = new ContainerLoadingDTO(loading);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

		ContainerLoadingDTO actual = loadingService.getLoading(loading.getId());

		InventoryExportDoc inventoryExportDoc = loadingService.getInventoryExportDoc(loading.getId());
		System.out.println(inventoryExportDoc);
		
		SecurityExportDoc securityExportDoc =loadingService.getSecurityExportDoc(loading.getId());
		System.out.println(securityExportDoc);
		
		loadingService.editLoading(loading);
		
		assertEquals(expected, actual, "Failed test adding container loading");
	*/
	}	
}
