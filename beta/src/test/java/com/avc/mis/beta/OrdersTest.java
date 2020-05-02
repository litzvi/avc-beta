/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.avc.mis.beta.dao.Orders;
import com.avc.mis.beta.dao.ProcessInfoWriter;
import com.avc.mis.beta.dao.ProcessInfoReader;
import com.avc.mis.beta.dao.Suppliers;
import com.avc.mis.beta.dao.Users;
import com.avc.mis.beta.dao.ValueTablesReader;
import com.avc.mis.beta.dto.data.ApprovalTaskDTO;
import com.avc.mis.beta.dto.data.UserDTO;
import com.avc.mis.beta.dto.data.UserMessageDTO;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.values.PersonBasic;
import com.avc.mis.beta.dto.values.PoBasic;
import com.avc.mis.beta.dto.values.PoRow;
import com.avc.mis.beta.dto.values.SupplierBasic;
import com.avc.mis.beta.dto.values.UserRow;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.enums.Role;
import com.avc.mis.beta.entities.process.OrderItem;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.Item;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Zvi
 *
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WithUserDetails("eli")
public class OrdersTest {
	
	private final int NUM_ITEMS = 3;
	
	private final int PROCESS_NO = 5000124;

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
	ProcessInfoWriter processDAO;

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
		PoDTO expected = null;
		expected = new PoDTO(po);
		PoDTO actual;
		actual = orders.getOrder(po.getPoCode().getCode());
		assertEquals(expected, actual, "failed test adding po");
		
		//edit order status
		po.setOrderStatus(OrderStatus.OPEN_APPROVED);
		expected = new PoDTO(po);
		orders.editOrder(po);
		actual = orders.getOrder(po.getPoCode().getCode());
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
		List<SupplierBasic> suppliersByCategory = valueTableReader.getSuppliersBasic(3);
		suppliersByCategory.forEach(s -> System.out.println(s));
		
		//list of bank branches
		List<BankBranchDTO> branchList = valueTableReader.getAllBankBranchesDTO();
		branchList.forEach((i)->System.out.println(i));
		
		//get list of cities
		List<CityDTO> cityList =  valueTableReader.getAllCitiesDTO();
		for(CityDTO city: cityList)
			System.out.println(city);
		
		//get list of cashew orders
		List<PoBasic> posBasic =  orders.findCashewOrdersBasic(new OrderStatus[] {OrderStatus.OPEN_PENDING});
		for(PoBasic row: posBasic)
			System.out.println(row);
		
		//get list of cashew orders
		List<PoRow> pos =  orders.findCashewOrders(new OrderStatus[] {OrderStatus.OPEN_PENDING});
		for(PoRow row: pos) {
			System.out.println(row);
		}
		
		//get list of new message for user
		List<UserMessageDTO> messages;
		try {
			messages = processDisplay.getAllNewMessages();
			messages.forEach(m -> System.out.println(m));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		//get list approval tasks for user
		List<ApprovalTaskDTO> tasks;
		ObjectMapper objMapper = new ObjectMapper();
		try {
			tasks = processDisplay.getAllRequiredApprovals();
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
				processDAO.setProcessDecision(t.getId(), 
						DecisionType.APPROVED.name(), processSnapshot);
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//insert a user
		UserEntity user = new UserEntity();
		user.setUsername("isral" + SuppliersTests.SERIAL_NO);
		user.setPassword("309");
		user.getRoles().add(Role.ROLE_SYSTEM_MANAGER);
		users.addUser(user);
		Person p = user.getPerson();
		p.setName("isssssssssral" + SuppliersTests.SERIAL_NO);
		users.editPersonalDetails(user);
//		suppliers.permenentlyRemoveEntity(user);
//		suppliers.permenentlyRemoveEntity(user.getPerson());
		
		//insert user with 2 roles
		user = new UserEntity();
		user.setUsername("zvi" + SuppliersTests.SERIAL_NO);
		user.setPassword("309");
		user.getRoles().add(Role.ROLE_SYSTEM_MANAGER);
		user.getRoles().add(Role.ROLE_MANAGER);
		users.addUser(user);
		user.setUsername("zzzzzvi" + SuppliersTests.SERIAL_NO);
		user.setPassword("password");
		user.getRoles().clear();
		users.editUser(user);
		suppliers.permenentlyRemoveEntity(user);
		suppliers.permenentlyRemoveEntity(user.getPerson());
		
		//open user for existing person
		user = new UserEntity();
		user.setUsername("eli" + SuppliersTests.SERIAL_NO);
		user.setPassword("309");
		user.getRoles().add(Role.ROLE_SYSTEM_MANAGER);
		user.getRoles().add(Role.ROLE_MANAGER);
		Person person = new Person();
		person.setId(2);
		user.setPerson(person);
		users.openUserForPerson(user);
		UserDTO userByusername = users.getUserByUsername("eli" + SuppliersTests.SERIAL_NO);
		UserDTO userById = users.getUserById(user.getId());
		assertEquals(userByusername, userById, "Failed test fetching user by id vs. by username");
		user.setUsername("pessi" + SuppliersTests.SERIAL_NO);
		user.setPassword("password");
		user.getRoles().clear();
		users.editUser(user);
		users.permenentlyRemoveUser(user.getId());
		
		//get list of persons basic
		List<PersonBasic> personsBasic = users.getPersonsBasic();
		personsBasic.forEach(m -> System.out.println(m));
		
		//get users table
		List<UserRow> usersTable = users.getUsersTable();
		usersTable.forEach(u -> System.out.println(u));
		
		//get messages for logged in user
		List<UserMessageDTO> userMessages = processDisplay.getAllMessages();
		userMessages.forEach(m -> System.out.println(m));
		
	}	
}
