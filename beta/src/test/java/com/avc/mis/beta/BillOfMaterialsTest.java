/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.dto.item.BillOfMaterialsDTO;
import com.avc.mis.beta.dto.item.BomLineDTO;
import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.service.BillOfMaterialService;
import com.avc.mis.beta.service.ValueTablesReader;

/**
 * @author Zvi
 *
 */
@SpringBootTest
public class BillOfMaterialsTest {
	
	static final int BOM_LIST_LENGTH = 5;

	@Autowired BillOfMaterialService billOfMaterialService;
	@Autowired ValueTablesReader valueTablesReader;
	@Autowired TestService service;
	
	@Test
	void billOfMaterialsTest() {
		
		BillOfMaterialsDTO expected = new BillOfMaterialsDTO();
		expected.setProduct(new BasicValueEntity<Item>(service.getItem()));

		List<BomLineDTO> bomList = new ArrayList<BomLineDTO>();
		for(int i=0; i < BOM_LIST_LENGTH; i++) {
			BomLineDTO bomLine = new BomLineDTO();
			bomLine.setMaterial(new BasicValueEntity<Item>(service.getItem()));
			bomList.add(bomLine);
		}
		
		expected.setBomList(bomList);
		
		billOfMaterialService.addBillOfMaterials(expected);
		BillOfMaterialsDTO actual;
		try {
			actual = billOfMaterialService.getBillOfMaterialsByProduct(expected.getProduct().getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		assertEquals(expected, actual, "failed test adding bill of materials");
		
		billOfMaterialService.editBillOfMaterials(actual);
		
		List<ItemDTO> bomItems = valueTablesReader.getProductBillOfMaterials(expected.getProduct().getId(), null, null);
		bomItems.forEach(i -> System.out.println(i));
		
		List<BillOfMaterialsDTO> billOfMaterialsRows = billOfMaterialService.getAllBillOfMaterials();
		billOfMaterialsRows.forEach(i -> System.out.println(i));
		
//		List<ProcessItemInventory> bomInventories = billOfMaterialService.getProductBomInventory(expected.getProduct().getId(), null, null, null, null, null, null);
//		bomInventories.forEach(i -> System.out.println(i));
		
		billOfMaterialService.removeBillOfMaterials(actual.getId());

	}
	

}
