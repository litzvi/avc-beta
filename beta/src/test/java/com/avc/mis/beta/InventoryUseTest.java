/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dto.process.InventoryUseDTO;
import com.avc.mis.beta.dto.process.StorageRelocationDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.dto.view.StorageInventoryRow;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.item.BulkItem;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.process.InventoryUse;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.process.inventory.StorageMove;
import com.avc.mis.beta.entities.processinfo.CountAmount;
import com.avc.mis.beta.entities.processinfo.ItemCount;
import com.avc.mis.beta.entities.processinfo.StorageMovesGroup;
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
public class InventoryUseTest {

	@Autowired TestService service;	

	@Autowired WarehouseManagement warehouseManagement;
	@Autowired ProcessInfoWriter processInfoWriter;
	


	@Test
	void inventoryUseTest() {
		PO po = service.addBasicGeneralOrder();
		Receipt receipt = service.getGeneralOrderReceipt(po.getPoCode().getId());
		processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
		processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
		
		InventoryUse inventoryUse = new InventoryUse();
		inventoryUse.setRecordedTime(OffsetDateTime.now());


		//get inventory storages for use
		List<ProcessItemInventory> poInventory = warehouseManagement.getAvailableInventory(null, null, null, null, new Integer[] {receipt.getPoCode().getId()});
		inventoryUse.setUsedItemGroups(TestService.getUsedItemsGroups(poInventory));

		try {
			warehouseManagement.addGeneralInventoryUse(inventoryUse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		InventoryUseDTO expected;
		try {
			expected = new InventoryUseDTO(inventoryUse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		InventoryUseDTO actual = warehouseManagement.getInventoryUse(inventoryUse.getId());
		
		assertEquals(expected, actual, "Failed test adding InventoryUse");
		
		OffsetDateTime time =OffsetDateTime.of(1983, 11, 23, 1, 1, 1, 0, ZoneOffset.ofHours(7));
		inventoryUse.setRecordedTime(time);
		expected.setRecordedTime(time);		
		warehouseManagement.editGeneralInventoryUse(inventoryUse);
		actual = warehouseManagement.getInventoryUse(inventoryUse.getId());
		
		assertEquals(expected, actual, "Failed test edditing InventoryUse");
		
		List<ProcessRow> inventoryUses = warehouseManagement.getInventoryUses();
		inventoryUses.forEach(i -> System.out.println(i));
				
	}

}
