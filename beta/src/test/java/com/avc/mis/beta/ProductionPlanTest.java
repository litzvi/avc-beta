/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.dao.ProcessDAO;
import com.avc.mis.beta.dto.basic.BasicDataEntity;
import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.dto.process.ProductionPlanDTO;
import com.avc.mis.beta.dto.process.collectionItems.ProcessItemPlanDTO;
import com.avc.mis.beta.dto.process.collectionItems.UsedItemPlanDTO;
import com.avc.mis.beta.dto.process.group.ProductionPlanRowDTO;
import com.avc.mis.beta.dto.values.ProductionLineDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.ItemGroup;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.process.group.ProcessItem;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;
import com.avc.mis.beta.service.ProductionPlans;
import com.avc.mis.beta.service.WarehouseManagement;

/**
 * @author Zvi
 *
 */
@SpringBootTest
public class ProductionPlanTest {
	
	static final int PLAN_LIST_LENGTH = 5;

	@Autowired ProcessDAO dao;

	
	@Autowired ProductionPlans productionPlans;
//	@Autowired ValueTablesReader valueTablesReader;
	@Autowired TestService service;
	@Autowired WarehouseManagement warehouseManagement;
	
	@Test
	void productionPlanTest() {
		
		ProductionPlanDTO expected = new ProductionPlanDTO();
		expected.setRecordedTime(LocalDateTime.now());

		List<ProductionPlanRowDTO> productionPlanRows = new ArrayList<ProductionPlanRowDTO>();
		for(int i=0; i < PLAN_LIST_LENGTH; i++) {
			ProductionPlanRowDTO row = new ProductionPlanRowDTO();
			row.setProcessType(new BasicValueEntity<ProcessType>(dao.getProcessTypeByValue(ProcessName.CASHEW_ROASTING)));
			ProductionLineDTO lineDTO = service.getProductionLine(ProductionFunctionality.ROASTER);
			row.setProductionLine(new BasicValueEntity<ProductionLine>(lineDTO.getId(), lineDTO.getValue()));
			row.setPlannedDate("1983-11-23");
//			process item plans
//			used item plans
			List<ProcessItemInventory> poInventory = warehouseManagement.getAvailableInventory(ItemGroup.PRODUCT, null, null, null, null, null);
			row.setUsedItemPlans(getUsedItemPlans(poInventory));
			row.setProcessItemPlans(getProcessItemPlans(poInventory));
			
			productionPlanRows.add(row);
		}
		
		expected.setProductionPlanRows(productionPlanRows);
		System.out.println("num rows" + expected.getProductionPlanRows().size());
		
		Integer processId;
		try {
			processId = productionPlans.addProductionPlan(expected);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		ProductionPlanDTO actual;
		try {
			actual = productionPlans.getProductionPlan(processId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		assertEquals(expected, actual, "failed test adding production plan");
		
		

	}
	
	private List<ProcessItemPlanDTO> getProcessItemPlans(List<ProcessItemInventory> poInventory) {
		List<ProcessItemPlanDTO> processItemPlans = new ArrayList<ProcessItemPlanDTO>();
		for(int i=0; i<PLAN_LIST_LENGTH; i++) {
			//build process item plan
			ProcessItemInventory processItemRow = poInventory.get(i);
			ProcessItemPlanDTO processItemPlan = new ProcessItemPlanDTO();
//			processItemPlan.setOrdinal(i);
			Item item = service.getItem(processItemRow.getItem());
//			item.setValue(processItemRow.getItem().getValue());
			processItemPlan.setItem(new BasicValueEntity<Item>(item));
			processItemPlan.setNumberUnits(new AmountWithUnit(BigDecimal.TEN.setScale(MeasureUnit.SCALE), MeasureUnit.LBS));
			
			processItemPlans.add(processItemPlan);
		}
		return processItemPlans;
	}
	
	private List<UsedItemPlanDTO> getUsedItemPlans(List<ProcessItemInventory> poInventory) {
		List<UsedItemPlanDTO> usedItemPlans = new ArrayList<UsedItemPlanDTO>();
		for(int i=0; i<PLAN_LIST_LENGTH; i++) {
			//build process item plan
			ProcessItemInventory processItemRow = poInventory.get(i);
			UsedItemPlanDTO usedItemPlan = new UsedItemPlanDTO();
//			usedItemPlan.setOrdinal(i);
			
			usedItemPlan.setProcessItem(new BasicDataEntity<ProcessItem>(processItemRow.getId(), processItemRow.getVersion()));
			usedItemPlan.setNumberUnits(new AmountWithUnit(BigDecimal.TEN.setScale(MeasureUnit.SCALE), MeasureUnit.LBS));
			
			usedItemPlans.add(usedItemPlan);
		}
		return usedItemPlans;
	}
	

}
