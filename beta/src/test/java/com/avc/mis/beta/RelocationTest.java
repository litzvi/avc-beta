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

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.process.StorageRelocationDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
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
	
//	@Test
//	void rawStationRelocationTest() {
//		Receipt receipt = service.addBasicCashewReceipt();
//		processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
//		processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
//		
//		StorageRelocation relocation = new StorageRelocation();
//		relocation.setPoCode((PoCode) receipt.getPoCode());
//		relocation.setRecordedTime(OffsetDateTime.now());
//		relocation.setProductionLine(productionLine);
//	}

	@Test
	void transferWithCountTest() {
		ReceiptDTO receipt = service.addBasicCashewReceipt();
		processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
		processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
		
		StorageRelocationDTO relocation = new StorageRelocationDTO();
		PoCodeBasic poCode = new PoCodeBasic();
		poCode.setId(receipt.getPoCode().getId());
		relocation.setPoCode(poCode);
		relocation.setRecordedTime(LocalDateTime.now());
		relocation.setProductionLine(service.getProductionLine(ProductionFunctionality.RAW_STATION));


		//get inventory storages for relocation
		List<ProcessItemInventory> poInventory = warehouseManagement.getAvailableInventory(null, null, null, null, new Integer[] {receipt.getPoCode().getId()}, null);
		relocation.setStorageMovesGroups(service.getStorageMovesDTOs(poInventory));
		relocation.setItemCounts(service.getItemCounts(poInventory));

		Integer relocationId;
		try {
			relocationId = warehouseManagement.addStorageRelocation(relocation);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		StorageRelocationDTO expected;
		try {
			expected = relocation;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		StorageRelocationDTO actual = warehouseManagement.getStorageRelocation(relocationId);
		
		assertEquals(expected, actual, "Failed test adding storageRelocation with Counts");
				
	}
	
	

//	private List<ItemCountDTO> getItemCounts(List<ProcessItemInventory> poInventory) {
//		List<ItemCountDTO> itemCounts = new ArrayList<ItemCountDTO>();
//		for(ProcessItemInventory processItemRow: poInventory) {
//			//build item count
//			ItemCountDTO itemCount = new ItemCountDTO();
//			itemCounts.add(itemCount);
//			List<CountAmountDTO> countAmounts = new ArrayList<CountAmountDTO>();
//			Item item = new Item();
//			item.setId(processItemRow.getItem().getId());
//			itemCount.setItem(new ItemWithUse(item));
//			List<StorageInventoryRow> storagesRows = processItemRow.getStorageForms();
//			StorageInventoryRow randStorage = storagesRows.get(0);
//			itemCount.setMeasureUnit(randStorage.getTotalBalance().getMeasureUnit());
////			itemCount.setContainerWeight(randStorage.getAccessWeight());
//			for(StorageInventoryRow storageRow: storagesRows) {
//				CountAmountDTO countAmount = new CountAmountDTO();
//				countAmounts.add(countAmount);
//				countAmount.setAmount(storageRow.getTotalBalance().getAmount());
//				countAmount.setOrdinal((storageRow.getOrdinal()));
//			}			
//			itemCount.setAmounts(countAmounts);
//		}
//		return itemCounts;
//	}
}
