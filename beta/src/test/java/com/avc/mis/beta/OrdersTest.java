/**
 * 
 */
package com.avc.mis.beta;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.dao.Orders;
import com.avc.mis.beta.dao.Suppliers;
import com.avc.mis.beta.dto.SupplierBasic;
import com.avc.mis.beta.entities.data.Item;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.ProcessType;
import com.avc.mis.beta.entities.process.ContractType;
import com.avc.mis.beta.entities.process.OrderItem;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.ProcessTypeDepricated;
import com.avc.mis.beta.entities.process.ProductionProcess;

/**
 * @author Zvi
 *
 */
@SpringBootTest
public class OrdersTest {
	
	private final int NUM_ITEMS = 3;

	@Autowired
	Orders orders;
	
	@Autowired
	Suppliers suppliers;
	
	private PO basicOrder() {
		//build purchase order
		PO po = new PO();
//		po.setId(5000001);
		ContractType contractType = new ContractType();
		contractType.setId(1);
		po.setContractType(contractType);
		Supplier supplier = new Supplier();
		supplier.setId(10);
		po.setSupplier(supplier);
		//build process
		ProductionProcess process = po.getOrderProcess();
		ProcessType processType = ProcessType.CASHEW_ORDER;
		process.setProcessType(processType);
		process.setTime(new Date(System.currentTimeMillis()));
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
			items[i].setDeliveryDate(new GregorianCalendar(2020, Calendar.MARCH, 13));
		}
		for(OrderItem item1: items) {
			System.out.println(item1);
		}
		
		po.setOrderItems(items);
		
				
		return po;
	}
	
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
		
		//get suppliers by supply category
		List<SupplierBasic> suppliersByCategory = suppliers.getSuppliersBasic(3);
		suppliersByCategory.forEach(supplier -> System.out.println(supplier));
		
	}
	
}
