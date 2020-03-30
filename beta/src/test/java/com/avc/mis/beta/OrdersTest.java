/**
 * 
 */
package com.avc.mis.beta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.dao.Orders;
import com.avc.mis.beta.dao.ReferenceTables;
import com.avc.mis.beta.dao.Suppliers;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.values.PoBasic;
import com.avc.mis.beta.dto.values.PoRow;
import com.avc.mis.beta.dto.values.SupplierBasic;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.process.OrderItem;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.Item;

/**
 * @author Zvi
 *
 */
@SpringBootTest
public class OrdersTest {
	
	private final int NUM_ITEMS = 3;
	
	private final int PROCESS_NO = 5000102;

	@Autowired
	Orders orders;
	
	@Autowired
	Suppliers suppliers;
	
	@Autowired
	ReferenceTables referenceTables;
	
	private PO basicOrder() {
		//build purchase order
		PO po = new PO();
		po.setId(PROCESS_NO);
		ContractType contractType = new ContractType();
		contractType.setId(1);
		po.setContractType(contractType);
		Supplier supplier = SuppliersTests.basicSupplier();
		suppliers.addSupplier(supplier);
		po.setSupplier(supplier);
		//build process
		ProductionProcess process = po.getOrderProcess();
		process.setTime(LocalDateTime.now());
		//add order items
		OrderItem[] items = new OrderItem[NUM_ITEMS];
		Item item = new Item();
		item.setId(1);
		for(int i=0; i<items.length; i++) {
			items[i] = new OrderItem();
			items[i].setItem(item);
			items[i].setNumberUnits(new BigDecimal(i));
			items[i].setCurrency("USD");
			items[i].setUnitPrice(new BigDecimal("1.16"));
			items[i].setDeliveryDate(LocalDate.of(1983, 11, 23));
		}
				
		po.setOrderItems(items);
		
		
				
		return po;
	}
//	@Disabled
	@Test
	void ordersTest() {
		//insert an order 
		PO po = basicOrder();
		try {
			orders.addCashewOrder(po);
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		PoDTO poDTO = orders.getOrder(PROCESS_NO);	
		System.out.println(poDTO);
		
		Supplier supplier = po.getSupplier();
		orders.removeOrder(po.getId());
		suppliers.permenentlyRemoveSupplier(supplier.getId());
		
		
		
		//get suppliers by supply category
		List<SupplierBasic> suppliersByCategory = suppliers.getSuppliersBasic(3);
		suppliersByCategory.forEach(s -> System.out.println(s));
		
		//list of bank branches
		List<BankBranchDTO> branchList = referenceTables.getAllBankBranchesDTO();
		branchList.forEach((i)->System.out.println(i));
		
		//get list of cities
		List<CityDTO> cityList =  referenceTables.getAllCitiesDTO();
		for(CityDTO city: cityList)
			System.out.println(city);
		
		//get list of cashew orders
		List<PoBasic> posBasic =  orders.findCashewOrdersBasic(new OrderStatus[] {OrderStatus.OPEN_PENDING});
		for(PoBasic row: posBasic)
			System.out.println(row);
		
		//get list of cashew orders
		List<PoRow> pos =  orders.findCashewOrders(new OrderStatus[] {OrderStatus.OPEN_PENDING});
		for(PoRow row: pos) {
			try {
				System.out.println(row);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
			
		

		
	}
	
}
