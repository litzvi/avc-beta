/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dto.basic.ContainerArrivalBasic;
import com.avc.mis.beta.dto.exportdoc.InventoryExportDoc;
import com.avc.mis.beta.dto.exportdoc.SecurityExportDoc;
import com.avc.mis.beta.dto.process.ContainerArrivalDTO;
import com.avc.mis.beta.dto.process.ContainerLoadingDTO;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.service.ContainerArrivals;
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

	@Autowired ContainerArrivals arrivals;
	@Autowired Loading loadingService;
	@Autowired ProcessInfoWriter processInfoWriter;
	
	@Test
	void bookingTest() {
		ReceiptDTO receipt;
		try {
			receipt = service.addBasicCashewReceipt();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
		processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
		
		//test container arrival
		ContainerArrivalDTO arrival = new ContainerArrivalDTO();
		arrival.setRecordedTime(LocalDateTime.now());
		ContainerDetails containerDetails = new ContainerDetails();
		containerDetails.setContainerNumber("CONT01");
		containerDetails.setSealNumber("SEAL01");
		containerDetails.setContainerType("20'");		
		arrival.setContainerDetails(containerDetails);
		arrival.setShipingDetails(service.getShipingDetails());
		Integer arrivalId = arrivals.addArrival(arrival);
		ContainerArrivalDTO expectedArrival = arrival;
		ContainerArrivalDTO actualArrival = arrivals.getArrival(arrivalId);		
		arrivals.editArrival(actualArrival);		
		assertEquals(expectedArrival, actualArrival, "Failed test adding container loading");

		
		//test loading
		ContainerLoadingDTO loading = new ContainerLoadingDTO();
		loading.setProductionLine(service.getProductionLine(ProductionFunctionality.LOADING));
//		ContainerBooking refBooking = new ContainerBooking();
//		refBooking.setId(booking.getId());
//		refBooking.setVersion(booking.getVersion());
		loading.setArrival(new ContainerArrivalBasic(actualArrival));
		loading.setShipmentCode(service.addShipmentCode());
		loading.setRecordedTime(LocalDateTime.now());

		//get inventory storages for transfer
		List<ProcessItemInventory> poInventory = warehouseManagement.getAvailableInventory(null, null, null, null, new Integer[] {receipt.getPoCode().getId()}, null);
		loading.setStorageMovesGroups(service.getStorageMovesDTOs(poInventory));
		
		Integer loadingId;
		try {
			loadingId = loadingService.addLoading(loading);
		} catch (Exception e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			throw e3;
		}

		ContainerLoadingDTO expectedLoading = loading;
		ContainerLoadingDTO actualLoading = loadingService.getLoading(loadingId);

		try {
			InventoryExportDoc inventoryExportDoc = loadingService.getInventoryExportDoc(loadingId);
			System.out.println(inventoryExportDoc);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			throw e2;
		}
		
		SecurityExportDoc securityExportDoc;
		try {
			securityExportDoc = loadingService.getSecurityExportDoc(loadingId);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		System.out.println(securityExportDoc);
		
		try {
			loadingService.editLoading(actualLoading);
			expectedLoading = loadingService.getLoading(actualLoading.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}		
		assertEquals(expectedLoading, actualLoading, "Failed test editing container loading");
		
		processInfoWriter.removeProcess(loadingId);
//		loadingService.removeLoading(loading.getId());
		processInfoWriter.removeProcess(arrivalId);
//		arrivals.removeArrival(arrival.getId());


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
