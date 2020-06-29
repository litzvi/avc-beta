/**
 * 
 */
package com.avc.mis.beta;

import static org.assertj.core.api.Assertions.fail;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dto.data.ApprovalTaskDTO;
import com.avc.mis.beta.dto.data.UserDTO;
import com.avc.mis.beta.dto.data.UserMessageDTO;
import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.queryRows.ItemInventoryRow;
import com.avc.mis.beta.dto.queryRows.PoRow;
import com.avc.mis.beta.dto.queryRows.ReceiptRow;
import com.avc.mis.beta.dto.queryRows.SupplierRow;
import com.avc.mis.beta.dto.queryRows.UserRow;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.values.DataObjectWithName;
import com.avc.mis.beta.dto.values.UserBasic;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.values.SupplyCategory;
import com.avc.mis.beta.service.CashewReports;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.Receipts;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.ProcessInfoReader;
import com.avc.mis.beta.service.QualityChecks;
import com.avc.mis.beta.service.Suppliers;
import com.avc.mis.beta.service.Users;
import com.avc.mis.beta.service.ValueTablesReader;

/**
 * @author Zvi
 *
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithUserDetails("eli")
public class QueryTest {
	
	@Autowired TestService service;
	@Autowired ObjectTablesReader objectTablesReader;
	@Autowired ValueTablesReader valueTablesReader;
	@Autowired ProcessInfoReader processInfoReader;
	@Autowired Users users;
	@Autowired Suppliers suppliers;
	@Autowired Orders orders;
	@Autowired Receipts receipts;
	@Autowired CashewReports cashewReports;
	@Autowired QualityChecks qualityChecks;
	
//	@Disabled
	@Test
	void queryTest() {

		PO po = service.addBasicCashewOrder();

		
		//get list of cashew orders
		List<PoCodeDTO> openCashewOrdersBasic =  objectTablesReader.findOpenCashewOrdersPoCode();
		openCashewOrdersBasic.forEach(row -> System.out.println(row));
		
		//get list of cashew orders and receipts
		List<PoCodeDTO> activeCashewBasic =  objectTablesReader.findActiveCashewPoCode();
		activeCashewBasic.forEach(row -> System.out.println(row));
		
		//get active po codes - so we can add QC for them
		activeCashewBasic =  objectTablesReader.findActiveCashewPoCode();
		if(activeCashewBasic.isEmpty()) {
			fail("Couldn't test fetching purchase order by po code");
		}
		activeCashewBasic.forEach(row -> System.out.println(row));
		
		//get order by process id
		List<PoRow> poRows =  orders.findOpenCashewOrders();
		if(poRows.isEmpty()) {
			fail("Couldn't test fetching purchase order by process id, no open orders");
		}
		System.out.println(orders.getOrderByProcessId(poRows.get(0).getId()));
				
				
		//list of bank branches
		List<BankBranchDTO> branchList = valueTablesReader.getAllBankBranchesDTO();
		branchList.forEach((i)->System.out.println(i));
		
		//get list of cities
		List<CityDTO> cityList =  valueTablesReader.getAllCitiesDTO();
		cityList.forEach(c -> System.out.println(c));
		
		//get list of persons basic
		List<DataObjectWithName> personsBasic = users.getPersonsBasic();
		personsBasic.forEach(m -> System.out.println(m));
		
		
		//find table of open cashew orders
		orders.findOpenCashewOrders().forEach(i -> System.out.println(i));
		
		
		//get list of cashew items
		valueTablesReader.getCashewitemsBasic().forEach(i -> System.out.println(i));
		
		//print received orders
		List<ReceiptRow> receiptRows = receipts.findFinalCashewReceipts();
		receiptRows.forEach(r -> System.out.println(r));
		
		
		//get messages for logged in user
		List<UserMessageDTO> userMessages = processInfoReader.getAllMessages();
		userMessages.forEach(m -> System.out.println(m));
				
		//get required approvals for logged in user		
		List<ApprovalTaskDTO> requierdTasks = processInfoReader.getAllRequiredApprovals();
		requierdTasks.forEach(i -> System.out.println(i));
		
		//get all approvals for logged in user		
		List<ApprovalTaskDTO> tasks = processInfoReader.getAllApprovals();
		tasks.forEach(i -> System.out.println(i));

		//get list of new message for user
		List<UserMessageDTO> messages = processInfoReader.getAllNewMessages();
		messages.forEach(m -> System.out.println(m));
		
		//get processTypeAlerts
		Map<ProcessName, Map<UserBasic, List<BasicValueEntity<ProcessManagement>>>> alerts = 
				processInfoReader.getAllProcessTypeAlerts();
		alerts.forEach((k, v) -> {
			System.out.println(k + ":\n");
			v.forEach((l, w) -> {
						System.out.println("\t" + l + "$");
						w.forEach(u -> {
							System.out.println("\t\t" + u);
						});
			});
		});
		
		//get users table
		List<UserRow> usersTable = users.getUsersTable();
		usersTable.forEach(u -> System.out.println(u));
		
		//get user by id
		List<UserEntity> userList = objectTablesReader.getAllUsers();
		userList.forEach(u -> {UserDTO user = users.getUserById(u.getId());
			System.out.println(user);
			});
		
		
		//print list of suppliers table
		List<SupplierRow> list = suppliers.getSuppliersTable();
		list.forEach(s -> System.out.println(s));
			
		//get all warhouse with id and value only
		valueTablesReader.getAllWarehousesDTO().forEach(w -> System.out.println(w));
		

		//get suppliers by supply category
		List<SupplyCategory> supplyCategories = valueTablesReader.getAllSupplyCategories();
		for(SupplyCategory supplyCategory: supplyCategories) {
			List<DataObjectWithName> suppliersByCategory = 
					valueTablesReader.getSuppliersBasic(supplyCategory.getId());
			suppliersByCategory.forEach(s -> System.out.println(s));
		}
		
		//cashew inventory table
		List<ItemInventoryRow> inventoryRows = cashewReports.getInventoryTable();
		inventoryRows.forEach(r -> System.out.println(r));
			
		service.cleanup(po);

	}
	
	@Test
	void oneQueryTest() {
		

	}
}