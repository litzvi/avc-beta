
/**
 * 
 */
package com.avc.mis.beta.controllers;

import com.avc.mis.beta.dto.basic.UserBasic;
import com.avc.mis.beta.dto.data.DataObjectWithName;
import com.avc.mis.beta.dto.data.UserDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.view.UserRow;
import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.ManagementType;
import com.avc.mis.beta.entities.enums.PackageType;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.Role;
import com.avc.mis.beta.entities.item.CashewItem;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.values.Bank;
import com.avc.mis.beta.entities.values.BankBranch;
import com.avc.mis.beta.entities.values.CashewStandard;
import com.avc.mis.beta.entities.values.City;
import com.avc.mis.beta.entities.values.CompanyPosition;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.Country;
import com.avc.mis.beta.entities.values.ProductionLine;
import com.avc.mis.beta.entities.values.ShippingPort;
import com.avc.mis.beta.entities.values.SupplyCategory;
import com.avc.mis.beta.entities.values.Warehouse;
import com.avc.mis.beta.service.ProcessInfoReader;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.Users;
import com.avc.mis.beta.service.ValueTablesReader;
import com.avc.mis.beta.service.ValueWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;


/**
 * @author Zvi
 *
 */

@RestController
@PostAuthorize("hasRole('SYSTEM_MANAGER')")
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
	public int addUser(@RequestBody UserEntity user) {
		usersDao.addUser(user);
		return user.getId();
	}
	
	@PostMapping(value="/addUserFromPerson")
	public Integer addUserFromPerson(@RequestBody UserEntity user) {
		usersDao.openUserForPerson(user);
		return user.getId();
	}
	
	@PutMapping(value="/editUser")
	public int editUser(@RequestBody UserEntity user) {
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
			System.out.println(var);
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
			itemGroup = ItemGroup.PRODUCT;
			if(setupTable.endsWith("packed")) {
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
			CashewItem packed = mapper.readValue(newOne.toString(), CashewItem.class);
			packed.setItemGroup(ItemGroup.PRODUCT);
			refeDaoWrite.addCashewItem(packed);
			return;
		} else if(setupTable.startsWith("G")) {
			itemGroup = ItemGroup.GENERAL;
		} else {
			itemGroup = ItemGroup.WASTE;
		}
		Item normalItem = mapper.readValue(newOne.toString(), Item.class);
		normalItem.setItemGroup(itemGroup);
		refeDaoWrite.addItem(normalItem);
	}
	
	
	@PostMapping(value="/addNewSetup/{setupTable}")
	public void addNewSetup(@PathVariable("setupTable") String setupTable, @RequestBody JsonNode newOne) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		switch (setupTable) {
			case "Countries":
				refeDaoWrite.addCountry(mapper.readValue(newOne.toString(), Country.class));
				break;
			case "Banks":
				refeDaoWrite.addBank(mapper.readValue(newOne.toString(), Bank.class));
				break;
			case "Cities":
				refeDaoWrite.addCity(mapper.readValue(newOne.toString(), City.class));
				break;
			case "BankBranches":
				refeDaoWrite.addBankBranch(mapper.readValue(newOne.toString(), BankBranch.class));
				break;
			case "Warehouses":
				refeDaoWrite.addWarehouse(mapper.readValue(newOne.toString(), Warehouse.class));
				break;
			case "SupplyCategories":
				refeDaoWrite.addSupplyCategory(mapper.readValue(newOne.toString(), SupplyCategory.class));
				break;
			case "CompanyPositions":
				refeDaoWrite.addCompanyPosition(mapper.readValue(newOne.toString(), CompanyPosition.class));
				break;
			case "ContractTypes":
				refeDaoWrite.addContractType(mapper.readValue(newOne.toString(), ContractType.class));
				break;
//			case "ProcessStatuses":
//				refeDaoWrite.addProcessStatus(mapper.readValue(newOne.toString(), ProcessStatus.class));
//				break;
			case "ProductionLines":
				refeDaoWrite.addProductionLine(mapper.readValue(newOne.toString(), ProductionLine.class));
				break;
			case "CashewStandards":
				refeDaoWrite.addCashewStandard(mapper.readValue(newOne.toString(), CashewStandard.class));
				break;
			case "ShippingPorts":
				refeDaoWrite.addShippingPort(mapper.readValue(newOne.toString(), ShippingPort.class));
				break;
			default:
				break;
		}
	}
	
	@PutMapping(value="/editSetup/{setupTable}")
	public ValueEntity editSetup(@PathVariable("setupTable") String setupTable, @RequestBody JsonNode editOne) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return refeDaoWrite.edit(mapper.readValue(editOne.toString(), setTable(setupTable, editOne)));
	}
	
	@PutMapping(value="/editItem/{setupTable}")
	public ValueEntity editItem(@PathVariable("setupTable") String setupTable, @RequestBody JsonNode editOne) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ItemGroup itemGroup;
		if(setupTable.startsWith("C")) {
			itemGroup = ItemGroup.PRODUCT;
		} else if(setupTable.startsWith("G")) {
			itemGroup = ItemGroup.GENERAL;
		} else {
			itemGroup = ItemGroup.WASTE;
		}
		if(setupTable.startsWith("C")) { 
			itemGroup = ItemGroup.PRODUCT; 
			CashewItem cashewItem = mapper.readValue(editOne.toString(), CashewItem.class);
			cashewItem.setItemGroup(itemGroup); 
			return refeDaoWrite.edit(cashewItem);
		} else if(setupTable.startsWith("G")) { 
			itemGroup = ItemGroup.GENERAL; 
		} else { 
			itemGroup = ItemGroup.WASTE; 
		} 
		Item item = mapper.readValue(editOne.toString(), Item.class); 
		item.setItemGroup(itemGroup); 
		return refeDaoWrite.edit(item);
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
			refeDaoWrite.remove(Item.class, deleteOne.get("id").asInt());
//		}
	}
	
	private Class<? extends ValueEntity> setTable(String setupTable, JsonNode newOne) {
		
		switch (setupTable) {
			case "Countries":
				return Country.class;
//				return mapper.readValue(newOne.toString(), Country.class);
			case "Banks":
				return Bank.class;
//				return mapper.readValue(newOne.toString(), Bank.class);
			case "Cities":
				return City.class;
//				return mapper.readValue(newOne.toString(), City.class);
			case "BankBranches":
				return BankBranch.class;
//				return mapper.readValue(newOne.toString(), BankBranch.class);
			case "Warehouses":
				return Warehouse.class;
//				return mapper.readValue(newOne.toString(), Warehouse.class);
			case "SupplyCategories":
				return SupplyCategory.class;
//				return mapper.readValue(newOne.toString(), SupplyCategory.class);
			case "CompanyPositions":
				return CompanyPosition.class;
//				return mapper.readValue(newOne.toString(), CompanyPosition.class);
			case "ContractTypes":
				return ContractType.class;
//				return mapper.readValue(newOne.toString(), ContractType.class);
//			case "ProcessStatuses":
//				return ProcessStatus.class;
//				return mapper.readValue(newOne.toString(), ProcessStatus.class);
			case "ProductionLines":
				return ProductionLine.class;
//				return mapper.readValue(newOne.toString(), ProductionLine.class);
			case "CashewStandards":
				return CashewStandard.class;
			case "ShippingPorts":
				return ShippingPort.class;
		}
		return null;
	}
	
	@RequestMapping("/getCountries")
	public List<Country> getCountries() {
		return refeDao.getAllCountries();
	}
	
	@RequestMapping("/getBanks")
	public List<Bank> getBanks() {
		return refeDao.getAllBanks();
	}
	
	@RequestMapping("/getCashewItems")
	public List<ItemDTO> getCashewItems() {
		return refeDao.getItemsByGroup(ItemGroup.PRODUCT);
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


