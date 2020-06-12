/**
 * 
 */
package com.avc.mis.beta;

import static org.assertj.core.api.Assertions.fail;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.process.OrderItem;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.Suppliers;
import com.avc.mis.beta.service.ValueTablesReader;

/**
 * @author Zvi
 *
 */
@Service
public class TestService {
	
	@Autowired private Suppliers suppliers;	
	@Autowired ValueTablesReader valueTableReader;
	@Autowired Orders orders;
	
	private int RAND_NUM = LocalDateTime.now().hashCode();
	
	public Supplier addBasicSupplier() {
		Supplier supplier = new Supplier();
		supplier.setName("service supplier " + RAND_NUM++);
		suppliers.addSupplier(supplier);
		return supplier;
	}
	
	public PO addBasicCashewOrder() {
		
		//build purchase order
		PO po = new PO();
		PoCode poCode = new PoCode();
		po.setPoCode(poCode);
		poCode.setId(RAND_NUM++);
		Supplier supplier = addBasicSupplier();
		poCode.setSupplier(supplier);
		List<ContractType> contractTypes = valueTableReader.getAllContractTypes();
		if(contractTypes.isEmpty())
			fail("No Contract Types in database for running this test");
		poCode.setContractType(contractTypes.get(0));
		
		//build process
		po.setRecordedTime(OffsetDateTime.now());
		
		//add order items
		OrderItem[] items = orderItems(OrdersTest.NUM_ITEMS);				
		po.setOrderItems(items);
		orders.addCashewOrder(po);
		return po;
	}
	
	private OrderItem[] orderItems(int numOfItems) {
		OrderItem[] orderItems = new OrderItem[numOfItems];
		List<Item> items = valueTableReader.getAllItems();
		if(items.size() < orderItems.length)
			fail("Database has less than " + orderItems.length + " items, not enough for test");
		for(int i=0; i<orderItems.length; i++) {
			orderItems[i] = new OrderItem();
			orderItems[i].setItem(items.get(i));
			orderItems[i].setNumberUnits(new AmountWithUnit(new BigDecimal(i+1), "KG"));
			orderItems[i].setUnitPrice(new AmountWithCurrency("1.16", "USD"));
			orderItems[i].setDeliveryDate("1983-11-23");
		}
		return orderItems;
	}

	public void cleanup(Supplier supplier) {
		suppliers.permenentlyRemoveSupplier(supplier.getId());		
	}
	
	public void cleanup(PO po) {
		PoCode poCode = po.getPoCode();
		Supplier supplier = poCode.getSupplier();
		orders.removeOrder(po.getId());
		suppliers.permenentlyRemoveEntity(poCode);
		cleanup(supplier);
		
	}
}
