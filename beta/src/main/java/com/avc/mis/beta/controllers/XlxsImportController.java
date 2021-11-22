package com.avc.mis.beta.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avc.mis.beta.dto.data.SupplierDTO;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.BankDTO;
import com.avc.mis.beta.dto.values.CashewStandardDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.values.CompanyPositionDTO;
import com.avc.mis.beta.dto.values.ContractTypeDTO;
import com.avc.mis.beta.dto.values.CountryDTO;
import com.avc.mis.beta.dto.values.ProductionLineDTO;
import com.avc.mis.beta.dto.values.SupplyCategoryDTO;
import com.avc.mis.beta.dto.values.WarehouseDTO;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.QualityChecks;
import com.avc.mis.beta.service.Receipts;
import com.avc.mis.beta.service.Suppliers;
import com.avc.mis.beta.service.ValueWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(path = "/api/xlxsimport")
public class XlxsImportController {

	@Autowired
	private Orders ordersDao;
	
	@Autowired
	private Receipts orderRecipt;
	
	@Autowired
	private ValueWriter refeDaoWrite;
	
	@Autowired
	private Suppliers suppliersDao;
	
	@Autowired
	private QualityChecks qualityChecks;
	
	@Autowired
	private ProcessInfoWriter processInfoWriter;
	
	
//	just for adding from xlex
	@PostMapping(value="/addAllSupplier")
	public void addAllSupplier(@RequestBody JsonNode listChanges) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		for(SupplierDTO var: mapper.readValue(listChanges.toString(), new TypeReference<List<SupplierDTO>>(){})) {
			suppliersDao.addSupplier(var);
		}
	}
	

	
//	just for adding from xlex
	@PostMapping(value="/addAllCashewReceiveCheck")
	public void addAllCashewReceiveCheck(@RequestBody List<QualityCheckDTO> listChanges) {
		System.out.println(listChanges);
		listChanges.forEach(k -> {
			System.out.println(k);
			qualityChecks.addCashewReceiptCheck(k);
		});
	}

//	just for adding from xlex
	@PostMapping(value="/addAllCashewOrders")
	public void addAllCashewOrders(@RequestBody List<PoDTO> listChanges) throws JsonMappingException, JsonProcessingException {
		System.out.println(listChanges);
		listChanges.forEach(k -> {
			System.out.println(k);
			ordersDao.addCashewOrder(k);
		});
	}
	
//	just for adding from xlex
	@PostMapping(value="/addAllReceiveCashewOrder")
	public void addAllReceiveCashewOrder(@RequestBody List<ReceiptDTO> listChanges) throws JsonMappingException, JsonProcessingException {
		listChanges.forEach(k -> {
			System.out.println(k);
			orderRecipt.addCashewOrderReceipt(k);
		});
	}
	
	@PostMapping("/approveAll")
	public ResponseEntity<?> approveAll(@RequestBody List<JsonNode> listChanges) {
		listChanges.forEach(k -> {
			System.out.println(k);
			int processId = k.get("id").asInt();
			processInfoWriter.setUserProcessDecision(processId, DecisionType.APPROVED, k.toString(), null);
			processInfoWriter.setProcessStatus(ProcessStatus.FINAL, processId);
		});
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(value="/addAllNewSetup/{setupTable}")
	public void addAllNewSetup(@PathVariable("setupTable") String setupTable, @RequestBody JsonNode newOne) throws JsonMappingException, JsonProcessingException {
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
				for(SupplyCategoryDTO var: mapper.readValue(newOne.toString(), new TypeReference<List<SupplyCategoryDTO>>(){})) {
					refeDaoWrite.addSupplyCategory(var);
				}
				break;
			case "CompanyPositions":
				refeDaoWrite.addCompanyPosition(mapper.readValue(newOne.toString(), CompanyPositionDTO.class));
				break;
			case "Items":
				for(Item var: mapper.readValue(newOne.toString(), new TypeReference<List<Item>>(){})) {
//					refeDaoWrite.addItem(var);
				}
				break;
			case "ContractTypes":
				for(ContractTypeDTO var: mapper.readValue(newOne.toString(), new TypeReference<List<ContractTypeDTO>>(){})) {
					refeDaoWrite.addContractType(var);
				}
//				refeDaoWrite.addContractType(mapper.readValue(newOne.toString(), ContractType.class));
				break;
//			case "ProcessStatuses":
//				refeDaoWrite.addProcessStatus(mapper.readValue(newOne.toString(), ProcessStatus.class));
//				break;
			case "ProductionLines":
				refeDaoWrite.addProductionLine(mapper.readValue(newOne.toString(), ProductionLineDTO.class));
				break;
			case "CashewStandards":
				refeDaoWrite.addCashewStandard(mapper.readValue(newOne.toString(), CashewStandardDTO.class));
			default:
				break;
		}
	}
	
}
