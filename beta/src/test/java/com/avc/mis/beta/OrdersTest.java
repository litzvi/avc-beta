/**
 * 
 */
package com.avc.mis.beta;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.data.ApprovalTaskDTO;
import com.avc.mis.beta.dto.data.UserDTO;
import com.avc.mis.beta.dto.data.UserMessageDTO;
import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.values.DataObjectWithName;
import com.avc.mis.beta.dto.values.PoRow;
import com.avc.mis.beta.dto.values.UserRow;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.ProcessAlert;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.ApprovalType;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.Role;
import com.avc.mis.beta.entities.process.OrderItem;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.repositories.PORepository;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.ProcessInfoReader;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.Suppliers;
import com.avc.mis.beta.service.Users;
import com.avc.mis.beta.service.ValueTablesReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Zvi
 *
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WithUserDetails("eli")
public class OrdersTest {
	
	public static final int NUM_ITEMS = 3;
	
//	public static int PROCESS_NO = 9000107;
	public int PROCESS_NO = LocalDateTime.now().hashCode();

	@Autowired TestService service;

	@Autowired
	Orders orders;
	
	@Autowired
	private Suppliers suppliers;
	
	@Autowired
	private ValueTablesReader valueTableReader;
	
	@Autowired
	private ProcessInfoReader processDisplay;
	
	@Autowired
	private ProcessInfoWriter processInfoWriter;
		

	public PO basicOrder(ValueTablesReader valueTableReader) {
		
		//build purchase order
		PO po = new PO();
		PoCode poCode = new PoCode();
		po.setPoCode(poCode);
		poCode.setId(PROCESS_NO);
		Supplier supplier = service.addBasicSupplier();
		poCode.setSupplier(supplier);
		List<ContractType> contractTypes = valueTableReader.getAllContractTypes();
		if(contractTypes.isEmpty())
			fail("No Contract Types in database for running this test");
		poCode.setContractType(contractTypes.get(0));
		
		//build process
		po.setRecordedTime(OffsetDateTime.now());
		
		//add order items
		OrderItem[] items = orderItems(NUM_ITEMS, valueTableReader);				
		po.setOrderItems(items);
		return po;
	}
	
	private OrderItem[] orderItems(int numOfItems, ValueTablesReader valueTableReader) {
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
	
		
	private PO insertOrder() {
		//insert an order 
		PO po = basicOrder(valueTableReader);
		orders.addCashewOrder(po);
		return po;
	}
	
//	@Disabled
	@Test
	void ordersTest() {
		
		//insert an order 
		PO po = insertOrder();
		PoDTO expected = null;
		expected = new PoDTO(po);
		PoDTO actual = orders.getOrder(po.getPoCode().getCode());
		assertEquals(expected, actual, "failed test adding po");

		//edit order status
		po.setOrderStatus(OrderStatus.OPEN_APPROVED);
		expected = new PoDTO(po);
		orders.editOrder(po);
		actual = orders.getOrder(po.getPoCode().getCode());
		assertEquals(expected, actual, "failed test editing po order status");		
		
		//cleanup
		service.cleanup(po);

		//remove a line/item from order
		po = insertOrder();
		OrderItem[] oldItems = po.getOrderItems();
		OrderItem[] items = new OrderItem[oldItems.length - 1];
		IntStream.range(0, items.length).forEach(i -> items[i] = oldItems[i]);
		po.setOrderItems(items);;
		expected = new PoDTO(po);
		orders.editOrder(po);
		actual = orders.getOrderByProcessId(po.getId());	
		assertEquals(expected, actual, "failed test editing po order status");
				
		//get list approval tasks for user
		List<ApprovalTaskDTO> tasks;
		ObjectMapper objMapper = new ObjectMapper();
		tasks = processDisplay.getAllRequiredApprovals();

		tasks.forEach(t -> {
			ProductionProcessDTO p = processDisplay.getProcess(t.getProcessId(), t.getProcessName().name());
			String processSnapshot = null;
			try {
				processSnapshot = objMapper.writeValueAsString(p);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			processInfoWriter.setProcessDecision(t.getId(), 
					DecisionType.APPROVED.name(), processSnapshot, null);
			
		});
		
		//cleanup
		service.cleanup(po);

	}	
}
