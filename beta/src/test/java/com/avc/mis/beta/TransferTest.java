package com.avc.mis.beta;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.dto.processinfo.StorageDTO;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.StorageTransfer;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.processinfo.Storage;
import com.avc.mis.beta.entities.processinfo.UsedItem;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.service.WarehouseManagement;

@SpringBootTest
public class TransferTest {

	@Autowired TestService service;	

	@Autowired WarehouseManagement warehouseManagement;
	
	@Test
	void transferTest() {
		Receipt receipt = service.addBasicCashewReceipt();
		
		StorageTransfer transfer = new StorageTransfer();
		transfer.setPoCode(receipt.getPoCode());
		transfer.setRecordedTime(OffsetDateTime.now());


		//get inventory storages for transfer
		List<ProcessItemDTO> poInventory = warehouseManagement.getInventoryByPo(receipt.getPoCode().getId());
		transfer.setUsedItems(getUsedItems(poInventory));
		transfer.setProcessItems(getProcessItems(poInventory));
		
				
//		System.out.println(transfer.getUsedItems().length);
//		System.out.println(transfer.getProcessItems().length);
//		for(ProcessItem processItem: transfer.getProcessItems()) {
//			System.out.println(processItem.getItem() + ", " + processItem.getStorageForms().length);
//		}

		warehouseManagement.addStorageTransfer(transfer);
	}

	private UsedItem[] getUsedItems(List<ProcessItemDTO> poInventory) {
		List<UsedItem> usedItems = new ArrayList<UsedItem>();
		for(ProcessItemDTO processItemDTO: poInventory) {
			for(StorageDTO storagesDTO: processItemDTO.getStorageForms()) {
				UsedItem usedItem = new UsedItem();
				Storage storage = new Storage();
				usedItem.setStorage(storage);
				storage.setId(storagesDTO.getId());
				storage.setVersion(storagesDTO.getVersion());
				usedItem.setNumberUnits(storagesDTO.getNumberUnits());
				usedItems.add(usedItem);
			}
		}
		return usedItems.toArray(new UsedItem[usedItems.size()]);
	}

	private ProcessItem[] getProcessItems(List<ProcessItemDTO> poInventory) {
		ProcessItem[] processItems = new ProcessItem[poInventory.size()];
		Storage[] storageForms;
		for(int i=0; i<processItems.length; i++) {
			//build process item
			ProcessItemDTO processItemDTO = poInventory.get(i);
			processItems[i] = new ProcessItem();
			Item item = new Item();
			item.setId(processItemDTO.getItem().getId());
			processItems[i].setItem(item);
			Set<StorageDTO> storagesDTO = processItemDTO.getStorageForms();
			storageForms = new Storage[storagesDTO.size()];
			int j=0;
			for(StorageDTO storageDTO: storagesDTO) {
				storageForms[j] = new Storage();
				storageForms[j].setUnitAmount(storageDTO.getUnitAmount());
				storageForms[j].setNumberUnits(storageDTO.getNumberUnits());
				storageForms[j].setWarehouseLocation(service.getWarehouse());
				
				j++;
			}
			
			processItems[i].setStorageForms(storageForms);
		}
		return processItems;
	}
}
