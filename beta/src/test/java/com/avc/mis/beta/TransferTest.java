package com.avc.mis.beta;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

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
import com.avc.mis.beta.dto.view.StorageInventoryRow;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.StorageTransfer;
import com.avc.mis.beta.entities.processinfo.CountAmount;
import com.avc.mis.beta.entities.processinfo.ItemCount;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.processinfo.Storage;
import com.avc.mis.beta.entities.processinfo.UsedItem;
import com.avc.mis.beta.entities.processinfo.UsedItemsGroup;
import com.avc.mis.beta.entities.values.Item;
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
		Receipt receipt = service.addBasicCashewReceipt();
		processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
		processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
		
		StorageTransfer transfer = new StorageTransfer();
		transfer.setPoCode(receipt.getPoCode());
		transfer.setRecordedTime(OffsetDateTime.now());


		//get inventory storages for transfer
		List<ProcessItemInventory> poInventory = warehouseManagement.getInventoryByPo(receipt.getPoCode().getId());
		
//		List<ProcessItemDTO> poInventory = warehouseManagement
//				.getProcessItemsWithPoByPo(receipt.getPoCode().getId())
//				.stream()
//				.map(PoProcessItemEntry::getProcessItem)
//				.collect(Collectors.toList());
		transfer.setUsedItemGroups(getUsedItemsGroups(poInventory));
		transfer.setProcessItems(getProcessItems(poInventory));
		
				
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
		transfer.setPoCode(receipt.getPoCode());
		transfer.setRecordedTime(OffsetDateTime.now());
		transfer.setUsedItemGroups(getUsedItemsGroups(poInventory));
		transfer.setProcessItems(getProcessItems(poInventory));
		
		try {
			warehouseManagement.addStorageTransfer(transfer);
			fail("should fail on using items that are already used");
		} catch (IllegalArgumentException|InvalidDataAccessApiUsageException e) {}
		
		//check fail on changing items
		receipt = service.addOneItemCashewReceipt();
		processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
		processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
				
		transfer = new StorageTransfer();
		transfer.setPoCode(receipt.getPoCode());
		transfer.setRecordedTime(OffsetDateTime.now());
		
		poInventory = warehouseManagement.getInventoryByPo(receipt.getPoCode().getId());
		transfer.setUsedItemGroups(getUsedItemsGroups(poInventory));
		ProcessItem[] processItems = getProcessItems(poInventory);
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
		} catch (IllegalArgumentException|InvalidDataAccessApiUsageException e) {}
		
		//check fail on changing items
		receipt = service.addOneItemCashewReceipt();
		processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
		processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
				
		transfer = new StorageTransfer();
		transfer.setPoCode(receipt.getPoCode());
		transfer.setRecordedTime(OffsetDateTime.now());
		
		poInventory = warehouseManagement.getInventoryByPo(receipt.getPoCode().getId());
		transfer.setUsedItemGroups(getUsedItemsGroups(poInventory));
		processItems = getProcessItems(poInventory);
		for(ProcessItem i: processItems) {
			Storage[] storages = i.getStorageForms();
			for(Storage s: storages) {
				s.setNumberUnits(s.getNumberUnits().divide(BigDecimal.TEN));
			}
		}
		transfer.setProcessItems(processItems);
		warehouseManagement.addStorageTransfer(transfer);
		
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
		
	}
	
//	@Disabled
	@Test
	void transferWithCountTest() {
		Receipt receipt = service.addBasicCashewReceipt();
		processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
		processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
		
		StorageTransfer transfer = new StorageTransfer();
		transfer.setPoCode(receipt.getPoCode());
		transfer.setRecordedTime(OffsetDateTime.now());


		//get inventory storages for transfer
		List<ProcessItemInventory> poInventory = warehouseManagement.getInventoryByPo(receipt.getPoCode().getId());
		transfer.setUsedItemGroups(getUsedItemsGroups(poInventory));
		transfer.setProcessItems(getProcessItems(poInventory));
		transfer.setItemCounts(getItemCounts(poInventory));

		warehouseManagement.addStorageTransfer(transfer);
		
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

	/**
	 * @param poInventory
	 * @return
	 */
	private ItemCount[] getItemCounts(List<ProcessItemInventory> poInventory) {
		ItemCount[] itemCounts = new ItemCount[poInventory.size()];
		CountAmount[] countAmounts;
		for(int i=0; i<itemCounts.length; i++) {
			//build item count
			ProcessItemInventory processItemRow = poInventory.get(i);
			itemCounts[i] = new ItemCount();
			Item item = new Item();
			item.setId(processItemRow.getItem().getId());
			itemCounts[i].setItem(item);
			List<StorageInventoryRow> storagesRows = processItemRow.getStorageForms();
			StorageInventoryRow randStorage = storagesRows.get(0);
			itemCounts[i].setMeasureUnit(randStorage.getTotalBalance().getMeasureUnit());
			itemCounts[i].setContainerWeight(randStorage.getContainerWeight());
			countAmounts = new CountAmount[storagesRows.size()];
			int j=0;
			for(StorageInventoryRow storageRow: storagesRows) {
				countAmounts[j] = new CountAmount();
				countAmounts[j].setAmount(storageRow.getTotalBalance().getAmount());
				countAmounts[j].setOrdinal((storageRow.getOrdinal()));
				
				j++;
			}
			
			itemCounts[i].setAmounts(countAmounts);
		}
		return itemCounts;
	}

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
				usedItems[j].setUsedUnits(storagesRow.getNumberUnits());
				j++;
			}
			usedItemsGroups[i] = new UsedItemsGroup();
			usedItemsGroups[i].setUsedItems(usedItems);
			i++;

		}
		return usedItemsGroups;
	}

	private ProcessItem[] getProcessItems(List<ProcessItemInventory> poInventory) {
		ProcessItem[] processItems = new ProcessItem[poInventory.size()];
		Storage[] storageForms;
		for(int i=0; i<processItems.length; i++) {
			//build process item
			ProcessItemInventory processItemRow = poInventory.get(i);
			processItems[i] = new ProcessItem();
			Item item = new Item();
			item.setId(processItemRow.getItem().getId());
			processItems[i].setItem(item);
			List<StorageInventoryRow> storagesRows = processItemRow.getStorageForms();
			storageForms = new Storage[storagesRows.size()];
			int j=0;
			for(StorageInventoryRow storageRow: storagesRows) {
				storageForms[j] = new Storage();
				storageForms[j].setUnitAmount(storageRow.getUnitAmount());
				storageForms[j].setNumberUnits(storageRow.getNumberUnits());
				storageForms[j].setWarehouseLocation(service.getWarehouse());
				
				j++;
			}
			
			processItems[i].setStorageForms(storageForms);
		}
		return processItems;
	}
}
