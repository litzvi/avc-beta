/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.ProductionProcesses;
import com.avc.mis.beta.service.WarehouseManagement;

/**
 * @author Zvi
 *
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithUserDetails("eli")
public class ProductionTest {
	
	@Autowired TestService service;
	@Autowired ProcessInfoWriter infoWriter;
	@Autowired ProductionProcesses productionService;
	@Autowired WarehouseManagement warehouseManagement;
	
	@Test
	void cleaningTest() {
		Receipt receipt = service.addBasicCashewReceipt();
		infoWriter.setUserProcessDecision(receipt.getId(), DecisionType.APPROVED, null, null);
		infoWriter.setProcessStatus(ProcessStatus.FINAL, receipt.getId());
		
		ProductionProcess process = new ProductionProcess();
		process.setPoCode((PoCode) receipt.getPoCode());
		process.setRecordedTime(OffsetDateTime.now());
		List<ProcessItemInventory> poInventory = warehouseManagement.getAvailableInventory(null, null, null, null, new Integer[] {receipt.getPoCode().getId()});
		process.setUsedItemGroups(TestService.getUsedItemsGroups(poInventory));
		process.setProcessItems(service.getProcessItems(poInventory));
//		process.setWeightedPos(service.getProductWeightedPos(2));
		
		try {
			productionService.addProductionProcess(process, ProcessName.CASHEW_CLEANING);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		ProductionProcessDTO expected = new ProductionProcessDTO(process);		
		ProductionProcessDTO actual = productionService.getProductionProcess(process.getId());		

		assertEquals(expected, actual, "Cleaning production process not added or fetched correctly");

		infoWriter.removeAllProcesses(receipt.getPoCode().getId());
		
	}
}
