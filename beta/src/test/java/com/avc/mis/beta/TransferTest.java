package com.avc.mis.beta;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dto.process.StorageTransferDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.StorageTransfer;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.WarehouseManagement;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithUserDetails("eli")
public class TransferTest {

	@Autowired TestService service;	

	@Autowired WarehouseManagement warehouseManagement;
	@Autowired ProcessInfoWriter processInfoWriter;
	
//	@Disabled
	@Test
	void transferTest() {
		try {
			Receipt receipt = service.addBasicCashewReceipt();
			processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
			processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
			
			StorageTransfer transfer = new StorageTransfer();
			transfer.setPoCode((PoCode) receipt.getPoCode());
			transfer.setRecordedTime(OffsetDateTime.now());


			//get inventory storages for transfer
			List<ProcessItemInventory> poInventory = warehouseManagement.getAvailableInventory(null, null, null, null, new Integer[] {receipt.getPoCode().getId()});
			
//		List<ProcessItemDTO> poInventory = warehouseManagement
//				.getProcessItemsWithPoByPo(receipt.getPoCode().getId())
//				.stream()
//				.map(PoProcessItemEntry::getProcessItem)
//				.collect(Collectors.toList());
			transfer.setUsedItemGroups(TestService.getUsedItemsGroups(poInventory));
			transfer.setProcessItems(service.getProcessItems(poInventory));
			
					
//		System.out.println(transfer.getUsedItems().length);
//		System.out.println(transfer.getProcessItems().length);
//		for(ProcessItem processItem: transfer.getProcessItems()) {
//			System.out.println(processItem.getItem() + ", " + processItem.getStorageForms().length);
//		}

			try {
				warehouseManagement.addStorageTransfer(transfer);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				throw e1;
			}
					
			//TODO check if usedItems exceeds inventory should fail
			transfer = new StorageTransfer();
			transfer.setPoCode((PoCode) receipt.getPoCode());
			transfer.setRecordedTime(OffsetDateTime.now());
			transfer.setUsedItemGroups(TestService.getUsedItemsGroups(poInventory));
			transfer.setProcessItems(service.getProcessItems(poInventory));
			
			try {
				warehouseManagement.addStorageTransfer(transfer);
				fail("should fail on using items that are already used");
			} catch (IllegalArgumentException|InvalidDataAccessApiUsageException e) {}
			
			//check fail on changing items
			receipt = service.addOneItemCashewReceipt();
			processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
			processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
					
			transfer = new StorageTransfer();
			transfer.setPoCode((PoCode) receipt.getPoCode());
			transfer.setRecordedTime(OffsetDateTime.now());
			
			poInventory = warehouseManagement.getAvailableInventory(null, null, null, null, new Integer[] {receipt.getPoCode().getId()});
			
			transfer.setUsedItemGroups(TestService.getUsedItemsGroups(poInventory));
			ProcessItem[] processItems = service.getProcessItems(poInventory);

			List<Item> items = service.getItems();
			if(items.size() < 2) {
				fail("not enough items for test");
			}
			Item item = items.get(0);
			if(item.equals(processItems[0].getItem())) {
				item = items.get(1);
			}	
			for(ProcessItem i: processItems) {
				i.setItem(item);
			}
			transfer.setProcessItems(processItems);
			try {
				warehouseManagement.addStorageTransfer(transfer);
				fail("should fail on out item different than in item");
			} 
			catch (IllegalArgumentException|InvalidDataAccessApiUsageException e) {}
			
			
			//check fail on changing items
			receipt = service.addOneItemCashewReceipt();
			processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
			processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
					
			transfer = new StorageTransfer();
			transfer.setPoCode((PoCode) receipt.getPoCode());
			transfer.setRecordedTime(OffsetDateTime.now());
			
			poInventory = warehouseManagement.getAvailableInventory(null, null, null, null, new Integer[] {receipt.getPoCode().getId()});
			transfer.setUsedItemGroups(TestService.getUsedItemsGroups(poInventory));
			processItems = service.getProcessItems(poInventory);
			for(ProcessItem i: processItems) {
				Storage[] storages = i.getStorageForms();
				for(Storage s: storages) {
					s.setNumberUnits(s.getNumberUnits().divide(BigDecimal.TEN));
				}
			}
			transfer.setProcessItems(processItems);
			try {
				warehouseManagement.addStorageTransfer(transfer);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				throw e1;
			}
			
			StorageTransferDTO expected;
			try {
				expected = new StorageTransferDTO(transfer);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
			StorageTransferDTO actual = warehouseManagement.getStorageTransfer(transfer.getId());
			
			assertEquals(expected, actual, "Failed test adding storageTransfer");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
	}
	
//	@Disabled
	@Test
	void transferWithCountTest() {
		Receipt receipt = service.addBasicCashewReceipt();
		processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
		processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
		
		StorageTransfer transfer = new StorageTransfer();
		transfer.setPoCode((PoCode) receipt.getPoCode());
		transfer.setRecordedTime(OffsetDateTime.now());


		//get inventory storages for transfer
		List<ProcessItemInventory> poInventory = warehouseManagement.getAvailableInventory(null, null, null, null, new Integer[] {receipt.getPoCode().getId()});
		transfer.setUsedItemGroups(TestService.getUsedItemsGroups(poInventory));
		transfer.setProcessItems(service.getProcessItems(poInventory));
		transfer.setItemCounts(service.getItemCounts(poInventory));

		try {
			warehouseManagement.addStorageTransfer(transfer);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		
		StorageTransferDTO expected;
		try {
			expected = new StorageTransferDTO(transfer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		StorageTransferDTO actual = warehouseManagement.getStorageTransfer(transfer.getId());
		
		assertEquals(expected, actual, "Failed test adding storageTransfer with counts");
				
	}

	
}
