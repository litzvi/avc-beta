/**
 * 
 */
package com.avc.mis.beta;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
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
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.CashewStandardDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.values.DataObjectWithName;
import com.avc.mis.beta.dto.values.ProcessBasic;
import com.avc.mis.beta.dto.values.UserBasic;
import com.avc.mis.beta.dto.view.ItemInventoryRow;
import com.avc.mis.beta.dto.view.LoadingRow;
import com.avc.mis.beta.dto.view.PoInventoryRow;
import com.avc.mis.beta.dto.view.PoRow;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.dto.view.RawQcRow;
import com.avc.mis.beta.dto.view.ReceiptRow;
import com.avc.mis.beta.dto.view.SupplierRow;
import com.avc.mis.beta.dto.view.UserRow;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.ItemCategory;
import com.avc.mis.beta.entities.enums.ManagementType;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.SupplyCategory;
import com.avc.mis.beta.service.InventoryReports;
import com.avc.mis.beta.service.Loading;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.ProcessInfoReader;
import com.avc.mis.beta.service.ProductionProcesses;
import com.avc.mis.beta.service.QualityChecks;
import com.avc.mis.beta.service.Receipts;
import com.avc.mis.beta.service.Suppliers;
import com.avc.mis.beta.service.Users;
import com.avc.mis.beta.service.ValueTablesReader;
import com.avc.mis.beta.service.WarehouseManagement;

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
	@Autowired InventoryReports cashewReports;
	@Autowired QualityChecks qualityChecks;
	@Autowired WarehouseManagement warehouseManagement;
	@Autowired ProductionProcesses productionProcesses;
	@Autowired Loading loading;
	
//	@Disabled
	@Test
	void queryTest() {

		PO po = service.addBasicCashewOrder();

		
		//get list of cashew orders
		Set<PoCodeDTO> openCashewOrdersBasic;
		openCashewOrdersBasic = objectTablesReader.findOpenCashewOrdersPoCodes();
		openCashewOrdersBasic.forEach(row -> System.out.println(row));
		
		Set<PoCodeDTO> inventoryCashewBasic = objectTablesReader.findCashewInventoryPoCodes();
		inventoryCashewBasic.forEach(row -> System.out.println(row));
		
		objectTablesReader.findOpenAndPendingCashewOrdersPoCodes().forEach(row -> System.out.println(row));
		
		//get list of cashew orders and receipts
		Set<PoCodeDTO> activeCashewBasic =  objectTablesReader.findAllPoCodes();
		activeCashewBasic.forEach(row -> System.out.println(row));
		
		//get active po codes - so we can add QC for them
		activeCashewBasic =  objectTablesReader.findOpenAndPendingCashewOrdersPoCodes();
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
		valueTablesReader.getCashewItemsBasic().forEach(i -> System.out.println(i));
		
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
		
		//map of all management for the different processes for current user
		Map<ProcessName, List<ManagementType>> processManagementMap = processInfoReader.getAllUserManagementTypes();
		processManagementMap.forEach((k,v) -> System.out.println("(" + k + ", " + v + ")"));
		
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
		
		//cashew inventory table by item
		List<ItemInventoryRow> inventoryRows  = cashewReports.getInventoryTableByItem(SupplyGroup.CASHEW);
		inventoryRows.forEach(r -> System.out.println(r));
		
		
		//cashew inventory table by po
		List<PoInventoryRow> poInventoryRows = cashewReports. getInventoryTableByPo(SupplyGroup.CASHEW);
		poInventoryRows.forEach(r -> System.out.println(r));		
		Set<PoCodeDTO> rawInventoryPos = objectTablesReader.findInventoryPoCodes(SupplyGroup.CASHEW);
		rawInventoryPos.forEach(r -> System.out.println(r));
		assertTrue(rawInventoryPos.size() == rawInventoryPos.size(), "po codes and po inventory row for cashew aren't consistent");
		
		
		
		
		//get all processes by po code/id
		for(UserMessageDTO message: messages) {
			if(message.getPoCode() != null) {
				Integer poId = message.getPoCode().getId();
				List<ProcessBasic> processBasics = processInfoReader.getAllProcessesByPo(poId);
				processBasics.forEach(s -> System.out.println(s));
			}
			
		}
		
		//test getting inventory storages by item
		List<ProcessItemInventory> itemInventory = warehouseManagement.getInventoryByItem(service.getItem().getId());
		itemInventory.forEach(i -> System.out.println(i));
		
		//test QC raw tables
		List<RawQcRow> rawQcRows = qualityChecks.getRawQualityChecks();
		rawQcRows.forEach(i -> System.out.println(i));
		
		//get cashew standards in DTOs
		List<CashewStandardDTO> cashewStandards = valueTablesReader.getAllCashewStandardsDTO();
		cashewStandards.forEach(i -> System.out.println(i));
		
		//get report of cleaning and roasting processes
		List<ProcessRow> processReport = productionProcesses.getProductionProcessesByType(ProcessName.CASHEW_CLEANING);
		processReport.forEach(i -> System.out.println(i));
		processReport = productionProcesses.getProductionProcessesByType(ProcessName.CASHEW_ROASTING);
		processReport.forEach(i -> System.out.println(i));
					
		//loading table
		List<LoadingRow> loadings = loading.getLoadings();
		loadings.forEach(i -> System.out.println(i));
		
		Set<PoCodeDTO> inventoryPoCodes;
		try {
			inventoryPoCodes = objectTablesReader.findInventoryPoCodes(new ItemCategory[]{ItemCategory.RAW, ItemCategory.CLEAN});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		inventoryPoCodes.forEach(i -> System.out.println(i));

		List<PoRow> allCashewOrders = orders.findAllCashewOrders();
		allCashewOrders.forEach(i -> System.out.println(i));
						
		service.cleanup(po);

	}
	
	@Test
	void oneQueryTest() {
		List<BasicValueEntity<Item>> wasteItems = valueTablesReader.getItemsByCategry(ItemCategory.WASTE);
		wasteItems.forEach(i->System.out.println(i));
	}
}
