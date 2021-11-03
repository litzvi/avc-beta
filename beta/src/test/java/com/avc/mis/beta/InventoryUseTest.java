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
import com.avc.mis.beta.dto.basic.ProductionLineBasic;
import com.avc.mis.beta.dto.process.InventoryUseDTO;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.entities.codes.GeneralPoCode;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.process.InventoryUse;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.service.InventoryUses;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.WarehouseManagement;
import com.avc.mis.beta.service.report.InventoryUseReports;

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
	@Autowired InventoryUses inventoryUses;
	@Autowired InventoryUseReports inventoryUseReports;
	@Autowired ProcessInfoWriter processInfoWriter;
	


	@Test
	void generalInventoryUseTest() {
		PoDTO po = service.addBasicGeneralOrder();
		ReceiptDTO receipt = service.getGeneralOrderReceipt(po.getPoCode().getId());
		processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
		processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
		
		InventoryUseDTO inventoryUse = new InventoryUseDTO();
		inventoryUse.setRecordedTime(LocalDateTime.now());
		PoCodeBasic poCode = new PoCodeBasic();
		poCode.setId(receipt.getPoCode().getId());
		inventoryUse.setPoCode(poCode);
		inventoryUse.setProductionLine(new ProductionLineBasic(service.getProductionLine(ProductionFunctionality.GENERAL_USE)));


		//get inventory storages for use
		List<ProcessItemInventory> poInventory = warehouseManagement.getAvailableInventory(null, null, null, null, new Integer[] {receipt.getPoCode().getId()}, null);
		inventoryUse.setStorageMovesGroups(service.getStorageMovesDTOs(poInventory));

		Integer inventoryUseId;
		try {
			inventoryUseId = inventoryUses.addGeneralInventoryUse(inventoryUse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		InventoryUseDTO expected = inventoryUse;
		InventoryUseDTO actual = inventoryUses.getInventoryUse(inventoryUseId);
		
		assertEquals(expected, actual, "Failed test adding InventoryUse");
		
		inventoryUse = actual;
		LocalDateTime time =LocalDateTime.now();
		inventoryUse.setRecordedTime(time);
		expected.setRecordedTime(time);		
		inventoryUses.editGeneralInventoryUse(inventoryUse);
		actual = inventoryUses.getInventoryUse(inventoryUseId);
		
		assertEquals(expected, actual, "Failed test edditing InventoryUse");
		
		List<ProcessRow> inventoryUses = inventoryUseReports.getInventoryUses(ProcessName.GENERAL_USE);
		inventoryUses.forEach(i -> System.out.println(i));
		
				
	}
	
	@Test
	void productInventoryUseTest() {
		PoDTO po = service.addBasicCashewOrder();
		ReceiptDTO receipt;
		try {
			receipt = service.getCashewOrderReceipt(po.getPoCode().getId());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		processInfoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
		processInfoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
		
		InventoryUseDTO inventoryUse = new InventoryUseDTO();
		inventoryUse.setRecordedTime(LocalDateTime.now());
		PoCodeBasic poCode = new PoCodeBasic();
		poCode.setId(receipt.getPoCode().getId());
		inventoryUse.setPoCode(poCode);
		inventoryUse.setProductionLine(new ProductionLineBasic(service.getProductionLine(ProductionFunctionality.PRODUCT_USE)));


		//get inventory storages for use
		List<ProcessItemInventory> poInventory = warehouseManagement.getAvailableInventory(null, null, null, null, new Integer[] {receipt.getPoCode().getId()}, null);
		inventoryUse.setStorageMovesGroups(service.getStorageMovesDTOs(poInventory));

		Integer inventoryUseId;
		try {
			inventoryUseId = inventoryUses.addProductInventoryUse(inventoryUse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		InventoryUseDTO expected = inventoryUse;
		InventoryUseDTO actual = inventoryUses.getInventoryUse(inventoryUseId);
		
		assertEquals(expected, actual, "Failed test adding InventoryUse");
		
		inventoryUse = actual;
		LocalDateTime time =LocalDateTime.now();
		inventoryUse.setRecordedTime(time);
		expected.setRecordedTime(time);		
		inventoryUses.editProductInventoryUse(inventoryUse);
		actual = inventoryUses.getInventoryUse(inventoryUseId);
		
		assertEquals(expected, actual, "Failed test edditing InventoryUse");
		
		List<ProcessRow> inventoryUses = inventoryUseReports.getInventoryUses(ProcessName.PRODUCT_USE);
		inventoryUses.forEach(i -> System.out.println(i));
		
				
	}

}
