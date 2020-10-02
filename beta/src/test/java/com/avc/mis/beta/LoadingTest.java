/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dto.process.ContainerLoadingDTO;
import com.avc.mis.beta.dto.process.StorageTransferDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.StorageInventoryRow;
import com.avc.mis.beta.entities.data.ContactDetails;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.embeddable.ShipingDetails;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ShippingContainerType;
import com.avc.mis.beta.entities.process.ContainerLoading;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.ShipmentCode;
import com.avc.mis.beta.entities.process.StorageTransfer;
import com.avc.mis.beta.entities.processinfo.LoadedItem;
import com.avc.mis.beta.entities.processinfo.Storage;
import com.avc.mis.beta.entities.processinfo.UsedItem;
import com.avc.mis.beta.entities.processinfo.UsedItemsGroup;
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
		Receipt receipt = service.addBasicCashewReceipt();
		processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
		processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
		
		ContainerLoading loading = new ContainerLoading();
		ShipmentCode shipmentCode = new ShipmentCode();
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
		List<ProcessItemInventory> poInventory = warehouseManagement.getInventoryByPo(receipt.getPoCode().getId());
		loading.setUsedItemGroups(getUsedItemsGroups(poInventory));
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

		
		assertEquals(expected, actual, "Failed test adding container loading");

	}
	
	
//	private LoadedItem[] getLoadedItems(List<ProcessItemInventory> poInventory) {
//		UsedItemsGroup[] usedItemsGroups = new UsedItemsGroup[poInventory.size()];
//		return null;
//	}

	private UsedItemsGroup[] getUsedItemsGroups(List<ProcessItemInventory> poInventory) {
		UsedItemsGroup[] usedItemsGroups = new UsedItemsGroup[poInventory.size()];
		int i = 0;
		for(ProcessItemInventory processItemRow: poInventory) {
			UsedItem[] usedItems = new UsedItem[processItemRow.getStorageForms().size()];
			int j = 0;
			for(StorageInventoryRow storagesRow: processItemRow.getStorageForms()) {
				usedItems[j] = new UsedItem();
				Storage storage = new Storage();
				usedItems[j].setStorage(storage);
				storage.setId(storagesRow.getId());
				storage.setVersion(storagesRow.getVersion());
				usedItems[j].setNumberUsedUnits(storagesRow.getNumberUnits());
				j++;
			}
			usedItemsGroups[i] = new UsedItemsGroup();
			usedItemsGroups[i].setUsedItems(usedItems);
			i++;

		}
		return usedItemsGroups;
	}
}
