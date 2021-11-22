package com.avc.mis.beta;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dto.basic.ItemWithUnitDTO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.process.StorageTransferDTO;
import com.avc.mis.beta.dto.process.group.ProcessItemDTO;
import com.avc.mis.beta.dto.process.storages.StorageDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessStatus;
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
		Integer transferId;
		try {
			ReceiptDTO receipt;
			try {
				receipt = service.addBasicCashewReceipt();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				throw e2;
			}
			processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
			processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
			
			StorageTransferDTO transfer = new StorageTransferDTO();
			PoCodeBasic poCode = new PoCodeBasic();
			poCode.setId(receipt.getPoCode().getId());
			transfer.setPoCode(poCode);
			transfer.setRecordedTime(LocalDateTime.now());


			//get inventory storages for transfer
			List<ProcessItemInventory> poInventory = warehouseManagement.getAvailableInventory(null, null, null, null, new Integer[] {receipt.getPoCode().getId()}, null);
			
//		List<ProcessItemDTO> poInventory = warehouseManagement
//				.getProcessItemsWithPoByPo(receipt.getPoCode().getId())
//				.stream()
//				.map(PoProcessItemEntry::getProcessItem)
//				.collect(Collectors.toList());
			transfer.setUsedItemGroups(TestService.getUsedItemsGroupsDTOs(poInventory));
			transfer.setProcessItems(service.getProcessItemsDTOs(poInventory));
			
					
//		System.out.println(transfer.getUsedItems().length);
//		System.out.println(transfer.getProcessItems().length);
//		for(ProcessItem processItem: transfer.getProcessItems()) {
//			System.out.println(processItem.getItem() + ", " + processItem.getStorageForms().length);
//		}

			try {
				try {
					transferId = warehouseManagement.addStorageTransfer(transfer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw e;
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				throw e1;
			}
					
			//TODO check if usedItems exceeds inventory should fail
			transfer = new StorageTransferDTO();
			poCode.setId(receipt.getPoCode().getId());
			transfer.setPoCode(poCode);
			transfer.setRecordedTime(LocalDateTime.now());
			transfer.setUsedItemGroups(TestService.getUsedItemsGroupsDTOs(poInventory));
			transfer.setProcessItems(service.getProcessItemsDTOs(poInventory));
			
			try {
				warehouseManagement.addStorageTransfer(transfer);
				fail("should fail on using items that are already used");
			} catch (IllegalArgumentException|InvalidDataAccessApiUsageException e) {}
			
			//check fail on changing items
			receipt = service.addOneItemCashewReceipt();
			processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
			processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
					
			transfer = new StorageTransferDTO();
			poCode.setId(receipt.getPoCode().getId());
			transfer.setPoCode(poCode);
			transfer.setRecordedTime(LocalDateTime.now());
			
			poInventory = warehouseManagement.getAvailableInventory(null, null, null, null, new Integer[] {receipt.getPoCode().getId()}, null);
			
			transfer.setUsedItemGroups(TestService.getUsedItemsGroupsDTOs(poInventory));
			List<ProcessItemDTO> processItems = service.getProcessItemsDTOs(poInventory);

			List<ItemDTO> items = service.getItemsByGroup(null);
			if(items.size() < 2) {
				fail("not enough items for test");
			}
			ItemDTO item = items.get(0);
			if(item.equals(processItems.get(0).getItem())) {
				item = items.get(1);
			}	
			for(ProcessItemDTO i: processItems) {
				i.setItem(new ItemWithUnitDTO(service.getItem(item)));
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
					
			transfer = new StorageTransferDTO();
			poCode.setId(receipt.getPoCode().getId());
			transfer.setPoCode(poCode);
			transfer.setRecordedTime(LocalDateTime.now());
			
			poInventory = warehouseManagement.getAvailableInventory(null, null, null, null, new Integer[] {receipt.getPoCode().getId()}, null);
			transfer.setUsedItemGroups(TestService.getUsedItemsGroupsDTOs(poInventory));
			processItems = service.getProcessItemsDTOs(poInventory);
			for(ProcessItemDTO i: processItems) {
				List<StorageDTO> storages = i.getStorageForms();
				for(StorageDTO s: storages) {
					s.setNumberUnits(s.getNumberUnits().divide(BigDecimal.TEN));
				}
			}
			transfer.setProcessItems(processItems);
			try {
				transferId = warehouseManagement.addStorageTransfer(transfer);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				throw e1;
			}
			
			StorageTransferDTO expected = transfer;
			
			StorageTransferDTO actual = warehouseManagement.getStorageTransfer(transferId);
			
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
		ReceiptDTO receipt = service.addBasicCashewReceipt();
		processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
		processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
		
		StorageTransferDTO transfer = new StorageTransferDTO();
		PoCodeBasic poCode = new PoCodeBasic();
		poCode.setId(receipt.getPoCode().getId());
		transfer.setPoCode(poCode);
		transfer.setRecordedTime(LocalDateTime.now());


		//get inventory storages for transfer
		List<ProcessItemInventory> poInventory = warehouseManagement.getAvailableInventory(null, null, null, null, new Integer[] {receipt.getPoCode().getId()}, null);
		transfer.setUsedItemGroups(TestService.getUsedItemsGroupsDTOs(poInventory));
		transfer.setProcessItems(service.getProcessItemsDTOs(poInventory));
		transfer.setItemCounts(service.getItemCounts(poInventory));

		Integer transferId;
		try {
			transferId = warehouseManagement.addStorageTransfer(transfer);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		
		StorageTransferDTO expected = transfer;
		StorageTransferDTO actual = warehouseManagement.getStorageTransfer(transferId);
		
		assertEquals(expected, actual, "Failed test adding storageTransfer with counts");
				
	}

	
}
