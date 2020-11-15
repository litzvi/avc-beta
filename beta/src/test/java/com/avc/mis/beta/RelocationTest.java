/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dto.process.StorageRelocationDTO;
import com.avc.mis.beta.dto.process.StorageTransferDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.StorageInventoryRow;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.item.BulkItem;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.entities.process.StorageTransfer;
import com.avc.mis.beta.entities.processinfo.CountAmount;
import com.avc.mis.beta.entities.processinfo.ItemCount;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.processinfo.Storage;
import com.avc.mis.beta.entities.processinfo.StorageMove;
import com.avc.mis.beta.entities.processinfo.StorageMovesGroup;
import com.avc.mis.beta.entities.processinfo.UsedItem;
import com.avc.mis.beta.entities.processinfo.UsedItemsGroup;
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
public class RelocationTest {

	@Autowired TestService service;	

	@Autowired WarehouseManagement warehouseManagement;
	@Autowired ProcessInfoWriter processInfoWriter;

	@Test
	void transferWithCountTest() {
		Receipt receipt = service.addBasicCashewReceipt();
		processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
		processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
		
		StorageRelocation relocation = new StorageRelocation();
		relocation.setPoCode(receipt.getPoCode());
		relocation.setRecordedTime(OffsetDateTime.now());


		//get inventory storages for relocation
		List<ProcessItemInventory> poInventory = warehouseManagement.getCashewInventoryByPo(receipt.getPoCode().getId());
		relocation.setStorageMovesGroups(getStorageMoves(poInventory));
		relocation.setItemCounts(getItemCounts(poInventory));

		try {
			warehouseManagement.addStorageRelocation(relocation);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		StorageRelocationDTO expected;
		try {
			expected = new StorageRelocationDTO(relocation);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		StorageRelocationDTO actual = warehouseManagement.getStorageRelocation(relocation.getId());
		
		assertEquals(expected, actual, "Failed test adding storageRelocation with Counts");
				
	}
	
	/**
	 * @param poInventory
	 * @return
	 */
	private StorageMovesGroup[] getStorageMoves(List<ProcessItemInventory> poInventory) {
		StorageMovesGroup[] storageMovesGroups = new StorageMovesGroup[poInventory.size()];
		int i = 0;
		for(ProcessItemInventory processItemRow: poInventory) {
			StorageMove[] storageMoves = new StorageMove[processItemRow.getStorageForms().size()];
			int j = 0;
			for(StorageInventoryRow storagesRow: processItemRow.getStorageForms()) {
				storageMoves[j] = new StorageMove();
				Storage storage = new Storage();
				storageMoves[j].setStorage(storage);
				storage.setId(storagesRow.getId());
				storage.setVersion(storagesRow.getVersion());
				storageMoves[j].setNumberUsedUnits(storagesRow.getNumberUnits());
				storageMoves[j].setUnitAmount(storagesRow.getUnitAmount());
				storageMoves[j].setNumberUnits(storagesRow.getNumberUnits());
				storageMoves[j].setContainerWeight(storagesRow.getContainerWeight());
				storageMoves[j].setWarehouseLocation(service.getWarehouse());
				j++;
			}
			storageMovesGroups[i] = new StorageMovesGroup();
			storageMovesGroups[i].setStorageMoves(storageMoves);
			i++;

		}
		return storageMovesGroups;
	}

	private ItemCount[] getItemCounts(List<ProcessItemInventory> poInventory) {
		ItemCount[] itemCounts = new ItemCount[poInventory.size()];
		CountAmount[] countAmounts;
		for(int i=0; i<itemCounts.length; i++) {
			//build item count
			ProcessItemInventory processItemRow = poInventory.get(i);
			itemCounts[i] = new ItemCount();
			Item item = new BulkItem();
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
}
