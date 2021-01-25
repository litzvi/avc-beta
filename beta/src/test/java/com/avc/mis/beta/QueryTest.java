/**
 * 
 */
package com.avc.mis.beta;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import com.avc.mis.beta.dto.basic.ProcessBasic;
import com.avc.mis.beta.dto.basic.UserBasic;
import com.avc.mis.beta.dto.data.DataObjectWithName;
import com.avc.mis.beta.dto.data.UserDTO;
import com.avc.mis.beta.dto.process.PoProcessDTO;
import com.avc.mis.beta.dto.processinfo.ApprovalTaskDTO;
import com.avc.mis.beta.dto.processinfo.UserMessageDTO;
import com.avc.mis.beta.dto.report.FinalReport;
import com.avc.mis.beta.dto.report.ProductionReportLine;
import com.avc.mis.beta.dto.report.QcReportLine;
import com.avc.mis.beta.dto.report.ReceiptReportLine;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.CashewStandardDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.dto.view.CashewQcRow;
import com.avc.mis.beta.dto.view.ItemInventoryRow;
import com.avc.mis.beta.dto.view.LoadingRow;
import com.avc.mis.beta.dto.view.PoInventoryRow;
import com.avc.mis.beta.dto.view.PoRow;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.dto.view.ReceiptRow;
import com.avc.mis.beta.dto.view.SupplierRow;
import com.avc.mis.beta.dto.view.UserRow;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.ManagementType;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.PO;
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
	
	@Disabled
	@Test
	void queryTest() {

		PO po;
		try {
			po = service.addBasicCashewOrder();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			throw e2;
		}

		
		//get list of cashew orders
		Set<PoCodeBasic> openCashewOrdersBasic;
		openCashewOrdersBasic = objectTablesReader.findOpenCashewOrdersPoCodes();
		openCashewOrdersBasic.forEach(row -> System.out.println(row));
		
		Set<PoCodeBasic> inventoryCashewBasic = objectTablesReader.findCashewAvailableInventoryPoCodes();
		inventoryCashewBasic.forEach(row -> System.out.println(row));
		
		objectTablesReader.findOpenAndPendingCashewOrdersPoCodes().forEach(row -> System.out.println(row));
		
		//get list of cashew orders and receipts
		List<PoCodeBasic> activeCashewBasic =  objectTablesReader.findAllPoCodes();
		activeCashewBasic.forEach(row -> System.out.println(row));
		
		//get active po codes - so we can add QC for them
		Set<PoCodeBasic> openAndPendingCashewOrdersPoCodes = objectTablesReader.findOpenAndPendingCashewOrdersPoCodes();
		if(openAndPendingCashewOrdersPoCodes.isEmpty()) {
			fail("Couldn't test fetching purchase order by po code");
		}
		openAndPendingCashewOrdersPoCodes.forEach(row -> System.out.println(row));
		
		//get order by process id
		List<PoRow> poRows;
		try {
			poRows = orders.findOpenCashewOrders();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			throw e2;
		}
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
		valueTablesReader.getItemsByGroup(ItemGroup.PRODUCT).forEach(i -> System.out.println(i));
		
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
		List<ItemInventoryRow> inventoryRows;
		try {
			inventoryRows = cashewReports.getInventoryTableByItem(ItemGroup.PRODUCT);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		inventoryRows.forEach(r -> System.out.println(r));
		
		
		//cashew inventory table by po
		List<PoInventoryRow> poInventoryRows = cashewReports. getInventoryTableByPo(ItemGroup.PRODUCT);
		poInventoryRows.forEach(r -> System.out.println(r));		
		Set<PoCodeBasic> rawInventoryPos = objectTablesReader.findAvailableInventoryPoCodes(ItemGroup.PRODUCT);
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
		List<ProcessItemInventory> itemInventory = warehouseManagement.getAvailableInventoryByItem(service.getItem().getId());
		if(itemInventory != null)
			itemInventory.forEach(i -> System.out.println(i));
		
		//test QC raw tables
		List<CashewQcRow> rawQcRows;
		try {
			rawQcRows = qualityChecks.getRawQualityChecks();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
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
		
		Set<PoCodeBasic> inventoryPoCodes;
		try {
			inventoryPoCodes = objectTablesReader.findAvailableInventoryPoCodes(new ProductionUse[]{ProductionUse.RAW_KERNEL, ProductionUse.CLEAN});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		inventoryPoCodes.forEach(i -> System.out.println(i));

		List<PoRow> allCashewOrders = orders.findAllCashewOrders();
		allCashewOrders.forEach(i -> System.out.println(i));
		
		List<BasicValueEntity<Item>> wasteItems = valueTablesReader.getBasicItemsByPrudoctionUse(ProductionUse.RAW_KERNEL);
		wasteItems.forEach(i->System.out.println(i));
		
		List<ProcessRow> transferRows;
		try {
			transferRows = warehouseManagement.getStorageTransfers();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		transferRows.forEach(i -> System.out.println(i));
						
		List<ProcessRow> relocationRows = warehouseManagement.getStorageRelocations();
		relocationRows.forEach(i -> System.out.println(i));
		
		List<PoCodeBasic> poCodes = objectTablesReader.findAllPoCodes();
		if(poCodes.isEmpty())
			fail("No po codes to test");
		poCodes.forEach(c -> {
			Map<ProcessName, List<PoProcessDTO>> qcProcessesMap = qualityChecks.getAllQualityChecksByPo(c.getId());
			qcProcessesMap.forEach((k, v) -> {
				if(!v.isEmpty()) {
					System.out.println("Process name: " + k);
					v.forEach(r -> System.out.println(r));
				}
			});
		});
		
		poCodes.forEach(c -> System.out.println(orders.getPoCode(c.getId())));
		
		poCodes = objectTablesReader.findFreePoCodes();
		poCodes.forEach(i -> System.out.println(i));
		
		Map<ProcessName, List<PoProcessDTO>> qcProcessesMap = qualityChecks.getAllQualityChecksByPo(44952);
		qcProcessesMap.forEach((k, v) -> {
			if(!v.isEmpty()) {
				System.out.println("Process name: " + k);
				v.forEach(r -> System.out.println(r));
			}
		});
		
		List<PoCodeBasic> freePoCodes = objectTablesReader.findFreeMixPoCodes();
		List<PoCodeBasic> freeMixPoCodes = objectTablesReader.findFreeMixPoCodes();

		//final report
		poCodes.forEach(c -> System.out.println(processInfoReader.getPoFinalReport(c.getId())));
		poCodes.forEach(c -> System.out.println(qualityChecks.getQcSummary(ProcessName.CASHEW_RECEIPT_QC, c.getId())));
		poCodes.forEach(c -> System.out.println(productionProcesses.getProductionSummary(ProcessName.CASHEW_CLEANING, c.getId())));
		poCodes.forEach(c -> System.out.println(receipts.getReceiptSummary(c.getId())));

		poCodes.forEach(c -> System.out.println(processInfoReader.getFinalReport(c.getId())));

		
		service.cleanup(po);

	}
	
//	@Disabled
	@Test
	void oneQueryTest() {
				
		FinalReport finalReport;
		try {
			finalReport = processInfoReader.getFinalReport(5);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		System.out.println(finalReport);
		System.out.println(finalReport.getReceiptQC());

		List<PoCodeBasic> poCodes = objectTablesReader.findAllPoCodes();
		poCodes.forEach(c -> System.out.println(orders.getPoCode(c.getId())));
		
//		List<PoCodeBasic> poCodes = objectTablesReader.findFreePoCodes();
//		poCodes.forEach(i -> System.out.println(i));

		
//		List<QcReportLine> qcReportLines = qualityChecks.getQcSummary(ProcessName.CASHEW_RECEIPT_QC, 1234);
//		qcReportLines.forEach(c -> System.out.println(c));
	}
}
