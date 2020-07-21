package com.avc.mis.beta;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dto.view.ProcessItemInventoryRow;
import com.avc.mis.beta.dto.view.StorageInventoryRow;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.StorageTransfer;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.processinfo.Storage;
import com.avc.mis.beta.entities.processinfo.UsedItem;
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
	
	@Test
	void transferTest() {
		Receipt receipt = service.addBasicCashewReceipt();
		processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
		
		StorageTransfer transfer = new StorageTransfer();
		transfer.setPoCode(receipt.getPoCode());
		transfer.setRecordedTime(OffsetDateTime.now());


		//get inventory storages for transfer
		List<ProcessItemInventoryRow> poInventory;
		try {
			poInventory = warehouseManagement.getInventoryByPo(receipt.getPoCode().getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
//		List<ProcessItemDTO> poInventory = warehouseManagement
//				.getProcessItemsWithPoByPo(receipt.getPoCode().getId())
//				.stream()
//				.map(PoProcessItemEntry::getProcessItem)
//				.collect(Collectors.toList());
		transfer.setUsedItems(getUsedItems(poInventory));
		transfer.setProcessItems(getProcessItems(poInventory));
		
				
//		System.out.println(transfer.getUsedItems().length);
//		System.out.println(transfer.getProcessItems().length);
//		for(ProcessItem processItem: transfer.getProcessItems()) {
//			System.out.println(processItem.getItem() + ", " + processItem.getStorageForms().length);
//		}

		warehouseManagement.addStorageTransfer(transfer);
		
		//TODO check if usedItems exceeds inventory should fail
	}

	private UsedItem[] getUsedItems(List<ProcessItemInventoryRow> poInventory) {
		List<UsedItem> usedItems = new ArrayList<UsedItem>();
		for(ProcessItemInventoryRow processItemRow: poInventory) {
			for(StorageInventoryRow storagesRow: processItemRow.getStorageForms()) {
				UsedItem usedItem = new UsedItem();
				Storage storage = new Storage();
				usedItem.setStorage(storage);
				storage.setId(storagesRow.getId());
				storage.setVersion(storagesRow.getVersion());
				usedItem.setNumberUnits(storagesRow.getNumberUnits());
				usedItems.add(usedItem);
			}
		}
		return usedItems.toArray(new UsedItem[usedItems.size()]);
	}

	private ProcessItem[] getProcessItems(List<ProcessItemInventoryRow> poInventory) {
		ProcessItem[] processItems = new ProcessItem[poInventory.size()];
		Storage[] storageForms;
		for(int i=0; i<processItems.length; i++) {
			//build process item
			ProcessItemInventoryRow processItemRow = poInventory.get(i);
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
