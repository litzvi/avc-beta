/**
 * 
 */
package com.avc.mis.beta;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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

import com.avc.mis.beta.dto.PoProcessDTO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.basic.ProcessBasic;
import com.avc.mis.beta.dto.basic.ShipmentCodeBasic;
import com.avc.mis.beta.dto.basic.UserBasic;
import com.avc.mis.beta.dto.data.DataObjectWithName;
import com.avc.mis.beta.dto.data.UserDTO;
import com.avc.mis.beta.dto.process.collection.ApprovalTaskDTO;
import com.avc.mis.beta.dto.process.collection.UserMessageDTO;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.CashewStandardDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.view.CashewQcRow;
import com.avc.mis.beta.dto.view.ContainerArrivalRow;
import com.avc.mis.beta.dto.view.ItemInventoryAmountWithOrder;
import com.avc.mis.beta.dto.view.ItemInventoryRow;
import com.avc.mis.beta.dto.view.LoadingRow;
import com.avc.mis.beta.dto.view.PoInventoryRow;
import com.avc.mis.beta.dto.view.PoItemRow;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.dto.view.ReceiptRow;
import com.avc.mis.beta.dto.view.SupplierRow;
import com.avc.mis.beta.dto.view.UserRow;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.ManagementType;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.GeneralProcess;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.values.SupplyCategory;
import com.avc.mis.beta.service.ContainerArrivals;
import com.avc.mis.beta.service.Loading;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.ObjectWriter;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.ProcessInfoReader;
import com.avc.mis.beta.service.ProcessReader;
import com.avc.mis.beta.service.ProcessSummaryReader;
import com.avc.mis.beta.service.QualityChecks;
import com.avc.mis.beta.service.Receipts;
import com.avc.mis.beta.service.Suppliers;
import com.avc.mis.beta.service.Users;
import com.avc.mis.beta.service.ValueTablesReader;
import com.avc.mis.beta.service.WarehouseManagement;
import com.avc.mis.beta.service.interfaces.ProductionProcessService;
import com.avc.mis.beta.service.report.InventoryReports;
import com.avc.mis.beta.service.report.LoadingReports;
import com.avc.mis.beta.service.report.row.CashewBaggedInventoryRow;
import com.avc.mis.beta.service.report.row.CashewExportReportRow;
import com.avc.mis.beta.service.report.row.FinishedProductInventoryRow;
import com.avc.mis.beta.service.report.row.ReceiptInventoryRow;

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
	@Autowired ObjectWriter objectWriter;
	@Autowired ObjectTablesReader objectTablesReader;
	@Autowired ValueTablesReader valueTablesReader;
	@Autowired ProcessReader processReader;
	@Autowired ProcessInfoReader processInfoReader;
	@Autowired ProcessSummaryReader processSummaryReader;
	@Autowired Users users;
	@Autowired Suppliers suppliers;
	@Autowired Orders orders;
	@Autowired Receipts receipts;
	@Autowired InventoryReports inventoryReports;
	@Autowired QualityChecks qualityChecks;
	@Autowired WarehouseManagement warehouseManagement;
	@Autowired ProductionProcessService productionProcesses;
	@Autowired Loading loading;
	@Autowired LoadingReports loadingReports;
	@Autowired ContainerArrivals containerArrivals;
	
//	@Disabled
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
		List<PoItemRow> poRows;
		try {
			poRows = orders.findOpenCashewOrderItems();
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
		List<DataObjectWithName<Person>> personsBasic = users.getPersonsBasic();
		personsBasic.forEach(m -> System.out.println(m));
		
		
		//find table of open cashew orders
		orders.findOpenCashewOrderItems().forEach(i -> System.out.println(i));
		
		
		//get list of cashew items
		try {
			if(valueTablesReader != null)
				System.out.println("hello, i'm not null");
			else
				System.out.println("hello, i'm null");
			System.out.println(valueTablesReader.getItemsByGroup(ItemGroup.PRODUCT) == null);
			valueTablesReader.getItemsByGroup(ItemGroup.PRODUCT).forEach(i -> System.out.println(i));
		} catch (Exception e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			throw e3;
		}
		
		//print received orders
		List<ReceiptRow> receiptRows;
		try {
			receiptRows = receipts.findFinalCashewReceipts();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			throw e2;
		}
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
			List<DataObjectWithName<Supplier>> suppliersByCategory = 
					valueTablesReader.getSuppliersBasic(supplyCategory.getId());
			suppliersByCategory.forEach(s -> System.out.println(s));
		}
		
		//cashew inventory table by item
		List<ItemInventoryRow> inventoryRows;
		try {
			inventoryRows = inventoryReports.getInventoryTableByItem(ItemGroup.PRODUCT);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		inventoryRows.forEach(r -> System.out.println(r));
		
		
		//cashew inventory table by po
		List<PoInventoryRow> poInventoryRows = inventoryReports. getInventoryTableByPo(ItemGroup.PRODUCT);
		poInventoryRows.forEach(r -> System.out.println(r));		
		Set<PoCodeBasic> rawInventoryPos = objectTablesReader.findAvailableInventoryPoCodes(ItemGroup.PRODUCT);
		rawInventoryPos.forEach(r -> System.out.println(r));
		assertTrue(rawInventoryPos.size() == rawInventoryPos.size(), "po codes and po inventory row for cashew aren't consistent");
		
		
		
		
		//get all processes by po code/id
		for(PoCode poCode: objectTablesReader.getAllPoCodes()) {
			List<ProcessBasic<GeneralProcess>> processBasics = processReader.getAllProcessesByPo(poCode.getId());
			processBasics.forEach(s -> System.out.println(s));
			
		}
		
		//test getting inventory storages by item
		List<ProcessItemInventory> itemInventory;
		try {
			itemInventory = warehouseManagement.getAvailableInventory(null, null, null, service.getItem().getId(), null, null);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			throw e2;
		}
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
		List<ProcessRow> processReport;
		try {
			processReport = productionProcesses.getProductionProcessesByType(ProcessName.CASHEW_CLEANING);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			throw e2;
		}
		processReport.forEach(i -> System.out.println(i));
		processReport = productionProcesses.getProductionProcessesByType(ProcessName.CASHEW_ROASTING);
		processReport.forEach(i -> System.out.println(i));
					
		//loading table
		List<LoadingRow> loadings;
		try {
			loadings = loading.getLoadings();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
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

//		List<PoRow> allCashewOrders = orders.findAllCashewOrders();
//		allCashewOrders.forEach(i -> System.out.println(i));
		
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
						
		List<ProcessRow> relocationRows = warehouseManagement.getStorageRelocations(null);
		relocationRows.forEach(i -> System.out.println(i));
		
		List<PoCodeBasic> poCodes = objectTablesReader.findAllPoCodes();
		if(poCodes.isEmpty())
			fail("No po codes to test");
		
		poCodes.forEach(c -> System.out.println(objectWriter.getPoCode(c.getId())));
		
		poCodes = objectTablesReader.findFreePoCodes();
		poCodes.forEach(i -> System.out.println(i));
		
		List<PoCodeBasic> freePoCodes = objectTablesReader.findFreeMixPoCodes();
		List<PoCodeBasic> freeMixPoCodes = objectTablesReader.findFreeMixPoCodes();

		//final report
		poCodes.forEach(c -> System.out.println(processSummaryReader.getFinalReport(c.getId())));
		poCodes.forEach(c -> System.out.println(processSummaryReader.getQcSummary(ProcessName.CASHEW_RECEIPT_QC, c.getId())));
		poCodes.forEach(c -> System.out.println(processSummaryReader.getProductionSummary(ProcessName.CASHEW_CLEANING, c.getId())));
		poCodes.forEach(c -> System.out.println(processSummaryReader.getReceiptSummary(c.getId())));

		poCodes.forEach(c -> System.out.println(processSummaryReader.getFinalReport(c.getId())));
				
		List<ItemInventoryAmountWithOrder> inventoryWithOrder = inventoryReports.getInventoryWithOrderByItem(ItemGroup.GENERAL);	
		inventoryWithOrder.forEach(i -> System.out.println(i));

		inventoryWithOrder = inventoryReports.getInventoryWithOrderByItem(ItemGroup.PRODUCT);	
		inventoryWithOrder.forEach(i -> System.out.println(i));
		
		List<ShipmentCodeBasic> shipmentCodes = objectTablesReader.findFreeShipmentCodes();
		shipmentCodes.forEach(i -> System.out.println(i));

		List<ContainerArrivalRow> containerArrivalRows = containerArrivals.getContainerArrivals();
		containerArrivalRows.forEach(i -> System.out.println(i));

		List<ProcessRow> inventoryUsesTable = warehouseManagement.getInventoryUses();
//		if(inventoryUsesTable.isEmpty())
//			fail("No inventory uses to test");
//		else
			System.out.println(inventoryUsesTable.size());

		Set<BasicValueEntity<Item>> availableItems =  warehouseManagement.findCashewAvailableInventoryItems();
		availableItems.forEach(i -> System.out.println(i));
		
		availableItems =  warehouseManagement.findGeneralAvailableInventoryItems();
		availableItems.forEach(i -> System.out.println(i));
		
		availableItems =  warehouseManagement.findAvailableInventoryItems(ProductionUse.values(), ProductionFunctionality.values());
		availableItems.forEach(i -> System.out.println(i));

		availableItems =  warehouseManagement.findAvailableInventoryItems(ProductionUse.values(), ItemGroup.WASTE);
		availableItems.forEach(i -> System.out.println(i));

		availableItems =  warehouseManagement.findAvailableInventoryItems(ProductionUse.values());
		availableItems.forEach(i -> System.out.println(i));

		availableItems =  warehouseManagement.findAvailableInventoryItems(ItemGroup.QC);
		availableItems.forEach(i -> System.out.println(i));
		
		service.cleanup(po);

	}
	
//	@Disabled
	@Test
	void oneQueryTest() {
		
		
		//get list of cashew orders and receipts
		List<PoCodeBasic> activeCashewBasic =  objectTablesReader.findAllPoCodes();
		System.out.println("cashew:");
		System.out.println("size:" + activeCashewBasic.size());
		activeCashewBasic.forEach(row -> System.out.println(row));

		List<PoCodeBasic> activeGeneralBasic =  objectTablesReader.findAllGeneralPoCodes();
		System.out.println("general:");
		System.out.println("size:" + activeGeneralBasic.size());
		activeGeneralBasic.forEach(row -> System.out.println(row));
		
		List<ReceiptInventoryRow> receiptInventoryRows = inventoryReports.getReceiptInventoryRows(ItemGroup.PRODUCT, new ProductionUse[] {ProductionUse.RAW_KERNEL}, 
				LocalDateTime.now());
//				LocalDateTime.of(2021, 5, 1, 0, 0));
		receiptInventoryRows.forEach(row -> {System.out.println(row); });
		
		List<FinishedProductInventoryRow> finishedProductInventoryRows;
		try {
			finishedProductInventoryRows = inventoryReports.getFinishedProductInventoryRows(ItemGroup.PRODUCT, 
					new ProductionUse[] {ProductionUse.PACKED, ProductionUse.ROAST, ProductionUse.ROAST, ProductionUse.TOFFEE}, 
					LocalDateTime.now());
//					LocalDateTime.of(2021, 5, 1, 0, 0));
			finishedProductInventoryRows.forEach(row -> {System.out.println(row); 
				System.out.println("boxes="+row.getBoxes());
				System.out.println("lbs="+row.getWeightInLbs());
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		
		List<CashewBaggedInventoryRow> cashewBaggedInventoryRows = inventoryReports.getCashewBaggedInventoryRows(ItemGroup.PRODUCT, 
					new ProductionUse[] {ProductionUse.PACKED}, 
					LocalDateTime.now());
		
		try {
			cashewBaggedInventoryRows.forEach(row -> {
			System.out.print("item="+row.getItem().getValue()+", ");
			System.out.print("brand="+row.getBrand()+", ");
			System.out.print("code="+row.getCode()+", ");
			System.out.print("whole?="+row.isWhole()+", ");
			System.out.print("size="+row.getBagSize().getValue()+", ");
			System.out.print("salt="+row.getSaltLevel()+", ");
			System.out.print("box quanyity="+row.getBagsInBox()+", ");
			System.out.print("boxes="+row.getBoxQuantity()+", ");
			System.out.print("bags="+row.getBagQuantity()+", ");
			System.out.print("lbs="+row.getWeightInLbs()+", ");
			System.out.println();
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		List<CashewExportReportRow> cashewExportReportRows = loadingReports.getCashewExportReportRows(null, null);
		cashewExportReportRows.forEach(row -> {
			System.out.print("item="+row.getItem().getValue()+", ");
			System.out.print("whole?="+row.isWhole()+", ");
			System.out.print("salt="+row.getSaltLevel()+", ");
			System.out.print("boxes="+row.getBoxQuantity()+", ");
			System.out.print("bags="+row.getBagQuantity()+", ");
			System.out.print("lbs="+row.getWeightInLbs()+", ");
			System.out.println(row);

			System.out.println();
		});
	}
}
