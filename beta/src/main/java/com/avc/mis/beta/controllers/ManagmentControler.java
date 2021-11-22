
/**
 * 
 */
package com.avc.mis.beta.controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.dto.basic.DataObjectWithName;
import com.avc.mis.beta.dto.basic.UserBasic;
import com.avc.mis.beta.dto.data.UserDTO;
import com.avc.mis.beta.dto.link.BillOfMaterialsDTO;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.BankDTO;
import com.avc.mis.beta.dto.values.CashewGradeDTO;
import com.avc.mis.beta.dto.values.CashewItemDTO;
import com.avc.mis.beta.dto.values.CashewStandardDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.values.CompanyPositionDTO;
import com.avc.mis.beta.dto.values.ContractTypeDTO;
import com.avc.mis.beta.dto.values.CountryDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.values.ProductionLineDTO;
import com.avc.mis.beta.dto.values.ShippingPortDTO;
import com.avc.mis.beta.dto.values.SupplyCategoryDTO;
import com.avc.mis.beta.dto.values.WarehouseDTO;
import com.avc.mis.beta.dto.view.UserRow;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.enums.ItemGroup;
import com.avc.mis.beta.entities.enums.ManagementType;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.PackageType;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.Role;
import com.avc.mis.beta.entities.link.ProcessManagement;
import com.avc.mis.beta.entities.values.CashewGrade;
import com.avc.mis.beta.service.BillOfMaterialService;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.ProcessInfoReader;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.Users;
import com.avc.mis.beta.service.ValueTablesReader;
import com.avc.mis.beta.service.ValueWriter;
import com.avc.mis.beta.service.report.OrderReports;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;


/**
 * @author Zvi
 *
 */

@RestController
@PreAuthorize("hasRole('SYSTEM_MANAGER')")
@RequestMapping(path = "/api/managment")
public class ManagmentControler {
	
	
	@Autowired
	private Users usersDao;
	
	@Autowired
	private ProcessInfoReader processInfoReader;
	
	@Autowired
	private ProcessInfoWriter processInfoWriter;
	
	@Autowired
	private ValueTablesReader refeDao;
	
	@Autowired
	private ValueWriter refeDaoWrite;
	
	@Autowired
	private OrderReports orderReports;
	
	@Autowired
	private Orders orders;
	
	@Autowired
	private BillOfMaterialService billOfMaterialService;
	
	@RequestMapping("/getAllUsers")
	public List<UserRow> getUsersTable() {
		return usersDao.getUsersTable();
	}
	
//	@RequestMapping("/getAllBasicUsers")
//	public List<UserBasic> getAllBasicUsers() {
//		return usersDao.getUsersBasic();
//	}
	
	@GetMapping(value="/getUser/{id}")
	public UserDTO getUser(@PathVariable("id") int userId) {
		return usersDao.getUserById(userId);
	}
	
	@PostMapping(value="/addUser")
	public Integer addUser(@RequestBody UserDTO user) {
		return usersDao.addUser(user);
//		return user.getId();
	}
	
	@PostMapping(value="/addUserFromPerson")
	public Integer addUserFromPerson(@RequestBody UserDTO user) {
		return usersDao.openUserForPerson(user);
//		return user.getId();
	}
	
	@PutMapping(value="/editUser")
	public int editUser(@RequestBody UserDTO user) {
		usersDao.editUser(user);
		return user.getId();
	}
	
	@GetMapping(value="/getAllProcessTypeAlerts")
	public Map<ProcessName, List<Pair>> getAllProcessTypeAlerts() {
		Map<ProcessName, List<Pair>> finalPairs = new HashMap<ProcessName, List<Pair>>();
//		System.out.println(processInfoReader.getAllProcessTypeAlerts());
//		Map<ProcessName, Map<UserBasic, List<BasicValueEntity<ProcessManagement>>>> alerts = 
//				processInfoReader.getAllProcessTypeAlerts();
//
//		alerts.forEach((k, v) -> {
//			System.out.println(k + ":\n");
//			v.forEach((l, w) -> {
//						System.out.println("\t" + l + "$");
//						w.forEach(u -> {
//							System.out.println("\t\t" + u);
//						});
//			});
//		});
		processInfoReader.getAllProcessTypeAlerts().forEach((k, v) -> {
			List<Pair> pairs = new ArrayList<Pair>();
			v.forEach((m, n) -> {
				pairs.add(new Pair(m, n));
			});
			finalPairs.put(k, pairs);
		});
		return finalPairs;
	}
	
	@PostMapping(value="/addAlertUsers")
	public ResponseEntity<?> addAlertUsers(@RequestBody JsonNode alerts) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ProcessName processName = ProcessName.valueOf(alerts.get("processName").asText());
//		ManagementType approvalType = ManagementType.valueOf(alerts.get("approvalType").toString());
//		List<UserEntity> listChanges = mapper.readValue((alerts.get("users")).toString(), new TypeReference<List<UserEntity>>(){});
		for(JsonNode var: mapper.readValue((alerts.get("adding")).toString(), new TypeReference<List<JsonNode>>(){})) {
			ManagementType approvalType = ManagementType.valueOf(var.get("managementType").asText());
			processInfoWriter.addProcessTypeAlert(var.get("userId").asInt(), processName, approvalType);
		}
		for(Integer var: mapper.readValue((alerts.get("removing")).toString(), new TypeReference<List<Integer>>(){})) {
			processInfoWriter.removeProcessTypeAlert(var);
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping(value="/getAllPersons")
	public List<DataObjectWithName<Person>> getAllPersons() {
		return usersDao.getPersonsBasic();
	}
	
	@GetMapping(value="/getAllRoles")
	public EnumSet<Role> getAllRoles() {
		return EnumSet.allOf(Role.class);
	}

	
	@GetMapping(value="/getAllSetupTable/{setupTable}")
	public List<?> getAllSetupTable(@PathVariable("setupTable") String setupTable) {
		Method method = null;
		try {
			method = refeDao.getClass().getMethod("getAll"+setupTable);
			return (List<?>) method.invoke(refeDao);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	@GetMapping(value="/getItemsSetupTable/{setupTable}")
	public <T> List<T> getItemsSetupTable(@PathVariable("setupTable") String setupTable) {
		ItemGroup itemGroup;
		if(setupTable.startsWith("C")) {
			if(setupTable.endsWith("QC")) {
				itemGroup = ItemGroup.QC;
			} else {
				itemGroup = ItemGroup.PRODUCT;
			}
			if(setupTable.contains("packed")) {
				return (List<T>) refeDao.getCashewItems(itemGroup, null, null, PackageType.PACKED);
			} else {
				return (List<T>) refeDao.getCashewItems(itemGroup, null, null, PackageType.BULK);
			}
		} else if(setupTable.startsWith("G")) {
			itemGroup = ItemGroup.GENERAL;
		} else {
			itemGroup = ItemGroup.WASTE;
		}
		if(setupTable.endsWith("packed")) {
			return (List<T>) refeDao.getItems(itemGroup, null, PackageType.PACKED);
		} else {
			return (List<T>) refeDao.getItems(itemGroup, null, PackageType.BULK);
		}
	}
	
	@PostMapping(value="/addNewItem/{setupTable}")
	public void addNewItem(@PathVariable("setupTable") String setupTable, @RequestBody JsonNode newOne) throws JsonMappingException, JsonProcessingException {
		ItemGroup itemGroup;
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		if(setupTable.startsWith("C")) {
			CashewItemDTO packed = mapper.readValue(newOne.toString(), CashewItemDTO.class);
			if(setupTable.endsWith("QC")) {
				packed.setGroup(ItemGroup.QC);
			} else {
				packed.setGroup(ItemGroup.PRODUCT);
			}
//			if(packed.getMeasureUnit() == null)
//				packed.setMeasureUnit(MeasureUnit.UNIT);
			refeDaoWrite.addCashewItem(packed);
			return;
		} else if(setupTable.startsWith("G")) {
			itemGroup = ItemGroup.GENERAL;
		} else {
			itemGroup = ItemGroup.WASTE;
		}
		ItemDTO normalItem = mapper.readValue(newOne.toString(), ItemDTO.class);
		normalItem.setGroup(itemGroup);
		refeDaoWrite.addItem(normalItem);
	}
	
	
	@PostMapping(value="/addNewSetup/{setupTable}")
	public void addNewSetup(@PathVariable("setupTable") String setupTable, @RequestBody JsonNode newOne) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		switch (setupTable) {
			case "Countries":
				refeDaoWrite.addCountry(mapper.readValue(newOne.toString(), CountryDTO.class));
				break;
			case "Banks":
				refeDaoWrite.addBank(mapper.readValue(newOne.toString(), BankDTO.class));
				break;
			case "Cities":
				refeDaoWrite.addCity(mapper.readValue(newOne.toString(), CityDTO.class));
				break;
			case "BankBranches":
				refeDaoWrite.addBankBranch(mapper.readValue(newOne.toString(), BankBranchDTO.class));
				break;
			case "Warehouses":
				refeDaoWrite.addWarehouse(mapper.readValue(newOne.toString(), WarehouseDTO.class));
				break;
			case "SupplyCategories":
				refeDaoWrite.addSupplyCategory(mapper.readValue(newOne.toString(), SupplyCategoryDTO.class));
				break;
			case "CashewGrades":
				refeDaoWrite.addCashewGrade(mapper.readValue(newOne.toString(), CashewGradeDTO.class));
				break;
			case "CompanyPositions":
				refeDaoWrite.addCompanyPosition(mapper.readValue(newOne.toString(), CompanyPositionDTO.class));
				break;
			case "ContractTypes":
				refeDaoWrite.addContractType(mapper.readValue(newOne.toString(), ContractTypeDTO.class));
				break;
//			case "ProcessStatuses":
//				refeDaoWrite.addProcessStatus(mapper.readValue(newOne.toString(), ProcessStatus.class));
//				break;
			case "ProductionLines":
				refeDaoWrite.addProductionLine(mapper.readValue(newOne.toString(), ProductionLineDTO.class));
				break;
			case "CashewStandards":
				refeDaoWrite.addCashewStandard(mapper.readValue(newOne.toString(), CashewStandardDTO.class));
				break;
			case "ShippingPorts":
				refeDaoWrite.addShippingPort(mapper.readValue(newOne.toString(), ShippingPortDTO.class));
				break;
			default:
				break;
		}
	}
	
	@PutMapping(value="/editSetup/{setupTable}")
	public void editSetup(@PathVariable("setupTable") String setupTable, @RequestBody JsonNode editOne) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		return refeDaoWrite.edit(mapper.readValue(editOne.toString(), setTable(setupTable, editOne)));
		refeDaoWrite.edit(mapper.readValue(editOne.toString(), setTable(setupTable, editOne)));
	}
	
	@PutMapping(value="/editItem/{setupTable}")
	public void editItem(@PathVariable("setupTable") String setupTable, @RequestBody JsonNode editOne) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		if(setupTable.startsWith("C")) { 
			CashewItemDTO cashewItem = mapper.readValue(editOne.toString(), CashewItemDTO.class);
			if(setupTable.endsWith("QC")) {
				cashewItem.setGroup(ItemGroup.QC);
			} else {
				cashewItem.setGroup(ItemGroup.PRODUCT);
			} 
//			return refeDaoWrite.edit(cashewItem);
			refeDaoWrite.edit(cashewItem);
			return;
		} 
		ItemDTO item = mapper.readValue(editOne.toString(), ItemDTO.class);
		if(setupTable.startsWith("G")) { 
			item.setGroup(ItemGroup.GENERAL); 
		} else {
			item.setGroup(ItemGroup.WASTE); 
		}
//		return refeDaoWrite.edit(item);
		refeDaoWrite.edit(item);
	}
	
	@DeleteMapping(value="/deleteSetup/{setupTable}")
	public void deleteSetup(@PathVariable("setupTable") String setupTable, @RequestBody JsonNode deleteOne) {
		refeDaoWrite.remove(setTable(setupTable, deleteOne), deleteOne.get("id").asInt());
	}
	
	@DeleteMapping(value="/deleteItem/{setupTable}")
	public void deleteItem(@PathVariable("setupTable") String setupTable, @RequestBody JsonNode deleteOne) {
//		if(setupTable.endsWith("packed")) {
//			refeDaoWrite.remove(PackedItem.class, deleteOne.get("id").asInt());
//		} else {
			refeDaoWrite.remove(ItemDTO.class, deleteOne.get("id").asInt());
//		}
	}
	
	private Class<? extends ValueDTO> setTable(String setupTable, JsonNode newOne) {
		
		switch (setupTable) {
			case "Countries":
				return CountryDTO.class;
//				return mapper.readValue(newOne.toString(), Country.class);
			case "Banks":
				return BankDTO.class;
//				return mapper.readValue(newOne.toString(), Bank.class);
			case "Cities":
				return CityDTO.class;
//				return mapper.readValue(newOne.toString(), City.class);
			case "BankBranches":
				return BankBranchDTO.class;
//				return mapper.readValue(newOne.toString(), BankBranch.class);
			case "Warehouses":
				return WarehouseDTO.class;
//				return mapper.readValue(newOne.toString(), Warehouse.class);
			case "SupplyCategories":
				return SupplyCategoryDTO.class;
//				return mapper.readValue(newOne.toString(), SupplyCategory.class);
			case "CashewGrades":
				return CashewGradeDTO.class;
			case "CompanyPositions":
				return CompanyPositionDTO.class;
//				return mapper.readValue(newOne.toString(), CompanyPosition.class);
			case "ContractTypes":
				return ContractTypeDTO.class;
//				return mapper.readValue(newOne.toString(), ContractType.class);
//			case "ProcessStatuses":
//				return ProcessStatus.class;
//				return mapper.readValue(newOne.toString(), ProcessStatus.class);
			case "ProductionLines":
				return ProductionLineDTO.class;
//				return mapper.readValue(newOne.toString(), ProductionLine.class);
			case "CashewStandards":
				return CashewStandardDTO.class;
			case "ShippingPorts":
				return ShippingPortDTO.class;
		}
		return null;
	}
	
	@RequestMapping("/getCountries")
	public List<CountryDTO> getCountries() {
		return refeDao.getAllCountries();
	}
	
	@RequestMapping("/getBanks")
	public List<BankDTO> getBanks() {
		return refeDao.getAllBanks();
	}
	
	@RequestMapping("/getCashewItems")
	public List<ItemDTO> getCashewItems() {
		return refeDao.getItemsByGroup(ItemGroup.PRODUCT);
	}
	
	@RequestMapping("/getCashewGrades")
	public List<BasicValueEntity<CashewGrade>> getCashewGrades() {
		return refeDao.getAllCashewGradesBasic();
	}
	
	
	@DeleteMapping(value="/removeAllProcesses/{id}")
	public ResponseEntity<?> removeAllProcesses(@PathVariable("id") int poCode) {
		processInfoWriter.removeAllProcesses(poCode);
		return ResponseEntity.ok().build();
	}
	@DeleteMapping(value="/removeProcess/{id}")
	public ResponseEntity<?> removeProcess(@PathVariable("id") int processId) {
		processInfoWriter.removeProcess(processId);
		return ResponseEntity.ok().build();
	}
	
//	@RequestMapping("/getCashewOrdersOpen")
//	public List<PoItemRow> getCashewOrdersOpen() {
//		return orderReports.findOpenCashewOrderItems();
//	}
//	
	@RequestMapping("/getItemBom/{id}")
	public BillOfMaterialsDTO getItemBom(@PathVariable("id") int productId) {
		return billOfMaterialService.getBillOfMaterialsByProduct(productId);
	}
	
	@PostMapping(value="/addItemBom")
	public BillOfMaterialsDTO addItemBom(@RequestBody BillOfMaterialsDTO bom) {
		billOfMaterialService.addBillOfMaterials(bom);
		return bom;
	}
	
	@PutMapping(value="/editItemBom")
	public BillOfMaterialsDTO editItemBom(@RequestBody BillOfMaterialsDTO bom) {
		billOfMaterialService.editBillOfMaterials(bom);
		return bom;
	}
	
	@RequestMapping("/getAllBoms")
	public List<BillOfMaterialsDTO> getItemBom() {
		return billOfMaterialService.getAllBillOfMaterials();
	}
	
	@Data
	private class Pair
	{
		UserBasic username;
		List<BasicValueEntity<ProcessManagement>> procecces;

	    public Pair(UserBasic m, List<BasicValueEntity<ProcessManagement>> procecces)
	    {
	        this.username = m;
	        this.procecces = procecces;
	    }
	}
}


