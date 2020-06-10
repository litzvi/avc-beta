/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

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
	
	public static int PROCESS_NO = 9000099;

	@Autowired
	Orders orders;
	
	@Autowired
	Users users;
	
	@Autowired
	Suppliers suppliers;
	
	@Autowired
	ValueTablesReader valueTableReader;
	
	@Autowired
	ProcessInfoReader processDisplay;
	
	@Autowired
	ProcessInfoWriter processInfoWriter;
	
//	@Autowired private PORepository poRepository;
	@Autowired private ProcessInfoDAO dao;
	
	@Autowired ObjectTablesReader objectTableReader;
	

	private PO basicOrder() {
		//build purchase order
		PO po = new PO();
		PoCode poCode = new PoCode();
		po.setPoCode(poCode);
		poCode.setId(PROCESS_NO);
		Supplier supplier = SuppliersTest.basicSupplier();
		suppliers.addSupplier(supplier);
		poCode.setSupplier(supplier);
		ContractType contractType = new ContractType();
		contractType.setId(1);
		poCode.setContractType(contractType);
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
			items[i].setNumberUnits(new BigDecimal(i+1).setScale(2));
			items[i].setCurrency("USD");
			items[i].setMeasureUnit("KG");
			items[i].setUnitPrice(new BigDecimal("1.16"));
			items[i].setDeliveryDate("1983-11-23");
		}
		return items;
	}
	
//	@Disabled
	@Test
	void ordersTest() {
		//insert an order 
		PO po = basicOrder();
		orders.addCashewOrder(po);
		PoDTO expected = null;
		expected = new PoDTO(po);
		PoDTO actual;
		actual = orders.getOrder(po.getPoCode().getCode());
		System.out.println(expected);
		System.out.println(actual);
		try {
			assertEquals(expected, actual, "failed test adding po");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		
		//edit order status
		po.setOrderStatus(OrderStatus.OPEN_APPROVED);
		expected = new PoDTO(po);
		System.out.println("before edit");
		orders.editOrder(po);
		System.out.println("after edit");
		actual = orders.getOrder(po.getPoCode().getCode());
		assertEquals(expected, actual, "failed test editing po order status");		
		
		PoCode poCode = po.getPoCode();
		Supplier supplier = poCode.getSupplier();
		orders.removeOrder(po.getId());
		suppliers.permenentlyRemoveEntity(poCode);
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
		List<DataObjectWithName> suppliersByCategory = valueTableReader.getSuppliersBasic(3);
		suppliersByCategory.forEach(s -> System.out.println(s));
						
		//get list approval tasks for user
		List<ApprovalTaskDTO> tasks;
		ObjectMapper objMapper = new ObjectMapper();
		try {
			tasks = processDisplay.getAllRequiredApprovals();

//			ApprovalTask task = new ApprovalTask();
			tasks.forEach(t -> {
//				task.setId(t.getId());
				ProductionProcessDTO p = processDisplay.getProcess(t.getProcessId(), t.getProcessName().name());
				String processSnapshot = null;
				try {
					processSnapshot = objMapper.writeValueAsString(p);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				t.setDecisionType(DecisionType.APPROVED.name());
//				task.setProcessVersion(p.getVersion());
				processInfoWriter.setProcessDecision(t.getId(), 
						DecisionType.APPROVED.name(), processSnapshot, null);
				
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//insert a user
		UserEntity user = new UserEntity();
		user.setUsername("isral" + SuppliersTest.SERIAL_NO);
		user.setPassword("309");
		user.getRoles().add(Role.ROLE_SYSTEM_MANAGER);
		users.addUser(user);
		Person p = user.getPerson();
		p.setName("isssssssssral" + SuppliersTest.SERIAL_NO);
		users.editPersonalDetails(user);
		suppliers.permenentlyRemoveEntity(user);
		suppliers.permenentlyRemoveEntity(user.getPerson());
		
		//insert user with 2 roles
		user = new UserEntity();
		user.setUsername("zvi" + SuppliersTest.SERIAL_NO);
		user.setPassword("309");
		user.getRoles().add(Role.ROLE_SYSTEM_MANAGER);
		user.getRoles().add(Role.ROLE_MANAGER);
		users.addUser(user);
		user.setUsername("zzzzzvi" + SuppliersTest.SERIAL_NO);
		user.setPassword("password");
		user.getRoles().clear();
		users.editUser(user);
		users.permenentlyRemoveUser(user.getId());
		users.permenentlyRemovePerson(user.getPerson().getId());
		
		//open user for existing person
		user = new UserEntity();
		user.setUsername("eli" + SuppliersTest.SERIAL_NO);
		user.setPassword("309");
		user.getRoles().add(Role.ROLE_SYSTEM_MANAGER);
		user.getRoles().add(Role.ROLE_MANAGER);
		Person person = new Person();
		person.setId(10);
		user.setPerson(person);
		users.openUserForPerson(user);
		UserDTO userByusername = users.getUserByUsername("eli" + SuppliersTest.SERIAL_NO);
		UserDTO userById = users.getUserById(user.getId());
		assertEquals(userByusername, userById, "Failed test fetching user by id vs. by username");
		user.setUsername("pessi" + SuppliersTest.SERIAL_NO);
		user.setPassword("password");
		user.getRoles().clear();
		users.editUser(user);
		
		//add, edit, remove processTypeAlert
		Integer processAlertId = processInfoWriter.addProcessTypeAlert(user.getId(), 
		dao.getProcessTypeByValue(ProcessName.CASHEW_ORDER), ApprovalType.REQUIRED_APPROVAL);
		System.out.println("OrdersTest 279");
		ProcessAlert processAlert = processDisplay.getProcessTypeAlert(processAlertId);
//	fail("finished");
		processInfoWriter.editProcessTypeAlert(processAlert, ApprovalType.REVIEW);
//		users.permenentlyRemoveUser(user.getId());

		
	}	
}
