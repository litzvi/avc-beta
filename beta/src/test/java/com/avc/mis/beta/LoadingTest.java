/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

import com.avc.mis.beta.dto.doc.InventoryExportDoc;
import com.avc.mis.beta.dto.doc.SecurityExportDoc;
import com.avc.mis.beta.dto.process.ContainerLoadingDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.embeddable.ShipingDetails;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.ContainerLoading;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.ShipmentCode;
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

	@Autowired Loading loadingService;
	@Autowired ProcessInfoWriter processInfoWriter;

	@Test
	void loadingTest() {
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
		ShipmentCode shipmentCode = service.getShipmentCode();
		shipmentCode.setPortOfDischarge(service.getShippingPort());
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
//		loading.setLoadedItems(getLoadedItems(poInventory));
		
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

	}	
}
