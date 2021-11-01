package com.avc.mis.beta.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.QualityCheck;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.values.Bank;
import com.avc.mis.beta.entities.values.BankBranch;
import com.avc.mis.beta.entities.values.CashewStandard;
import com.avc.mis.beta.entities.values.City;
import com.avc.mis.beta.entities.values.CompanyPosition;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.Country;
import com.avc.mis.beta.entities.values.ProductionLine;
import com.avc.mis.beta.entities.values.SupplyCategory;
import com.avc.mis.beta.entities.values.Warehouse;
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
		for(Supplier var: mapper.readValue(listChanges.toString(), new TypeReference<List<Supplier>>(){})) {
			suppliersDao.addSupplier(var);
		}
	}
	

	
//	just for adding from xlex
	@PostMapping(value="/addAllCashewReceiveCheck")
	public void addAllCashewReceiveCheck(@RequestBody List<QualityCheck> listChanges) {
		System.out.println(listChanges);
		listChanges.forEach(k -> {
			System.out.println(k);
			qualityChecks.addCashewReceiptCheck(k);
		});
	}

//	just for adding from xlex
	@PostMapping(value="/addAllCashewOrders")
	public void addAllCashewOrders(@RequestBody List<PO> listChanges) throws JsonMappingException, JsonProcessingException {
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
				for(SupplyCategory var: mapper.readValue(newOne.toString(), new TypeReference<List<SupplyCategory>>(){})) {
					refeDaoWrite.addSupplyCategory(var);
				}
				break;
			case "CompanyPositions":
				refeDaoWrite.addCompanyPosition(mapper.readValue(newOne.toString(), CompanyPosition.class));
				break;
			case "Items":
				for(Item var: mapper.readValue(newOne.toString(), new TypeReference<List<Item>>(){})) {
//					refeDaoWrite.addItem(var);
				}
				break;
			case "ContractTypes":
				for(ContractType var: mapper.readValue(newOne.toString(), new TypeReference<List<ContractType>>(){})) {
					refeDaoWrite.addContractType(var);
				}
//				refeDaoWrite.addContractType(mapper.readValue(newOne.toString(), ContractType.class));
				break;
//			case "ProcessStatuses":
//				refeDaoWrite.addProcessStatus(mapper.readValue(newOne.toString(), ProcessStatus.class));
//				break;
			case "ProductionLines":
				refeDaoWrite.addProductionLine(mapper.readValue(newOne.toString(), ProductionLine.class));
				break;
			case "CashewStandards":
				refeDaoWrite.addCashewStandard(mapper.readValue(newOne.toString(), CashewStandard.class));
			default:
				break;
		}
	}
	
}
