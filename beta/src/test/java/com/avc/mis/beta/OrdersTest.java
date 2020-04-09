/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.dao.Orders;
import com.avc.mis.beta.dao.ProcessDisplay;
import com.avc.mis.beta.dao.ReferenceTables;
import com.avc.mis.beta.dao.Suppliers;
import com.avc.mis.beta.dto.data.ApprovalTaskDTO;
import com.avc.mis.beta.dto.data.UserMessageDTO;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.values.PoBasic;
import com.avc.mis.beta.dto.values.PoRow;
import com.avc.mis.beta.dto.values.SupplierBasic;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.process.ApprovalTask;
import com.avc.mis.beta.entities.process.OrderItem;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.Item;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Zvi
 *
 */
@SpringBootTest
public class OrdersTest {
	
	private final int NUM_ITEMS = 3;
	
	private final int PROCESS_NO = 5000124;

	@Autowired
	Orders orders;
	
	@Autowired
	Suppliers suppliers;
	
	@Autowired
	ReferenceTables referenceTables;
	
	@Autowired
	ProcessDisplay processDisplay;
	
	private PO basicOrder() {
		//build purchase order
		PO po = new PO();
		PoCode poCode = new PoCode();
		po.setPoCode(poCode);
//		poCode.setId(PROCESS_NO);
		ContractType contractType = new ContractType();
		contractType.setId(1);
		poCode.setContractType(contractType);
		Supplier supplier = SuppliersTests.basicSupplier();
		suppliers.addSupplier(supplier);
		po.setSupplier(supplier);
		//build process
		po.setRecordedTime(OffsetDateTime.now());
		//add order items
		OrderItem[] items = orderItems(NUM_ITEMS);				
		po.setOrderItems(items);
		return po;
	}
	
	private OrderItem[] orderItems(int numOfItems) {
		OrderItem[] items = new OrderItem[numOfItems];
		Item item = new Item();
		item.setId(1);
		for(int i=0; i<items.length; i++) {
			items[i] = new OrderItem();
			items[i].setItem(item);
			items[i].setNumberUnits(new BigDecimal(i).setScale(2));
			items[i].setCurrency("USD");
			items[i].setMeasureUnit("KG");
			items[i].setUnitPrice(new BigDecimal("1.16"));
			items[i].setDeliveryDate(LocalDate.of(1983, 11, 23));
		}
		return items;
	}
	
//	@Disabled
	@Test
	void ordersTest() {
		//insert an order 
		PO po = basicOrder();
		orders.addCashewOrder(po);
		PoDTO expected = new PoDTO(po);
		PoDTO actual = orders.getOrder(po.getPoCode().getId());	
		assertEquals(expected, actual, "failed test adding po");
		
		//edit order status
		po.setOrderStatus(OrderStatus.OPEN_APPROVED);
		expected = new PoDTO(po);
		orders.editOrder(po);
		actual = orders.getOrder(po.getPoCode().getId());	
		assertEquals(expected, actual, "failed test editing po order status");		
		
		Supplier supplier = po.getSupplier();
		orders.removeOrder(po.getId());
		suppliers.permenentlyRemoveSupplier(supplier.getId());
		
		//remove a line/item from order
		po = basicOrder();
		orders.addCashewOrder(po);
		OrderItem[] items = new OrderItem[NUM_ITEMS-1];
		items[0] = po.getOrderItems()[0];
		items[1] = po.getOrderItems()[1];
		po.setOrderItems(items);;
		expected = new PoDTO(po);
		orders.editOrder(po);
		actual = orders.getOrderByProcessId(po.getId());	
		assertEquals(expected, actual, "failed test editing po order status");
//		
//		supplier = po.getSupplier();
//		orders.removeOrder(po.getId());
//		suppliers.permenentlyRemoveSupplier(supplier.getId());
		
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
		
		//get list of message for user
		List<UserMessageDTO> messages;
		try {
			messages = processDisplay.getAllMessages(1);
			messages.forEach(m -> System.out.println(m));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//get list approval tasks for user
		List<ApprovalTaskDTO> tasks;
		ObjectMapper objMapper = new ObjectMapper();
		try {
			tasks = processDisplay.getAllRequiredApprovals(1);
//			ApprovalTask task = new ApprovalTask();
			tasks.forEach(t -> {
//				task.setId(t.getId());
				ProductionProcessDTO p = processDisplay.getProcess(t.getProcessId(), t.getProcessType());
				String processSnapshot = null;
				try {
					processSnapshot = objMapper.writeValueAsString(p);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				t.setDecisionType(DecisionType.APPROVED.name());
//				task.setProcessVersion(p.getVersion());
				processDisplay.setProcessDecision(t.getId(), 
						DecisionType.APPROVED.name(), processSnapshot);
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}	
}
