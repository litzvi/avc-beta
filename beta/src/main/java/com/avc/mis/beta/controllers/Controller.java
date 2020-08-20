/**
 * 
 */
package com.avc.mis.beta.controllers;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avc.mis.beta.dto.data.ApprovalTaskDTO;
import com.avc.mis.beta.dto.data.UserMessageDTO;
import com.avc.mis.beta.dto.process.GeneralProcessDTO;
import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.processinfo.RawItemQualityDTO;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.values.ValueObject;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ManagementType;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.values.Bank;
import com.avc.mis.beta.entities.values.CashewStandard;
import com.avc.mis.beta.entities.values.CompanyPosition;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.Country;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.ProductionLine;
import com.avc.mis.beta.entities.values.ShippingPort;
import com.avc.mis.beta.entities.values.SupplyCategory;
import com.avc.mis.beta.entities.values.Warehouse;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.ProcessInfoReader;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.QualityChecks;
import com.avc.mis.beta.service.Suppliers;
import com.avc.mis.beta.service.ValueTablesReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;




/**
 * @author Zvi
 *
 */
@RestController
@RequestMapping(path = "/api")
public class Controller {
	
	@Autowired
	private ValueTablesReader refeDao;
	
	@Autowired
	private ProcessInfoReader processDao;
	
	@Autowired
	private ObjectTablesReader objectTableReader;
	
	@Autowired
	private QualityChecks qualityChecks;
	
	@Autowired
	private ProcessInfoWriter processInfoWriter;
	
	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
	
	//should be done with one service
	@RequestMapping("/setup")
	public List<Object> getSetup() {
		List<Object> result = new ArrayList<Object>();
		
		List<CityDTO> cityholder = refeDao.getAllCitiesDTO();
		result.add(cityholder);
		
		List<Country> countryholder = refeDao.getAllCountries();
		result.add(countryholder);

		List<CompanyPosition> Positionholder = refeDao.getAllCompanyPositions();
		result.add(Positionholder);
		
		List<Bank> Bankholder = refeDao.getAllBanks();
		result.add(Bankholder);

		List<ContractType> Contractholder = refeDao.getAllContractTypes();
		result.add(Contractholder);
		
		List<BasicValueEntity<Warehouse>> Storageholder = refeDao.getAllWarehousesDTO();
		result.add(Storageholder);
		
		List<BasicValueEntity<Item>> CashewItemsholder = refeDao.getCashewItemsBasic();
		result.add(CashewItemsholder);
		
		List<BasicValueEntity<Item>> GeneralItemsholder = refeDao.getGeneralItemsBasic();
		result.add(GeneralItemsholder);
		
		List<SupplyCategory> Suplyholder = refeDao.getAllSupplyCategories();
		result.add(Suplyholder);

		List<BankBranchDTO> Branchholder = refeDao.getAllBankBranchesDTO();
		result.add(Branchholder);
		
		List<CashewStandard> standartholder = refeDao.getAllCashewStandards();
		result.add(standartholder);
		Map<ProcessName, List<ManagementType>> Managmentholder = processDao.getAllUserManagementTypes();
		result.add(Managmentholder);
		
		List<ShippingPort> ShippingPortsholder = refeDao.getAllShippingPorts();
		result.add(ShippingPortsholder);

		List<ProductionLine> ProductionLinesHolder = refeDao.getAllProductionLines();
		result.add(ProductionLinesHolder);
		return result; 
	}
	
	@RequestMapping("/getUserMassages")
	public List<UserMessageDTO> getMassages() {
		try {
			return processDao.getAllMessages();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/getUserTasks")
	public List<ApprovalTaskDTO> getUserTasks() {
		return processDao.getAllApprovals();
	}
	
	
	@RequestMapping("/getMassage/{id1}/{id2}/{id3}")
	public GeneralProcessDTO getMassage(@PathVariable("id1") int processId, @PathVariable("id2") int massagesId, @PathVariable("id3") ProcessName type) {
		processInfoWriter.setMessageLabel(massagesId, MessageLabel.SEEN);
//		switch(type) {
//			case CASHEW_RECEIPT_QC:
//			case SUPPLIER_QC:
//			case VINA_CONTROL_QC:
//			case SAMPLE_QC:
//				return getQcRawCheck(processId);
//			default:
				return processDao.getProcess(processId, type);
//		}
	}
	
	
	@RequestMapping("/getTask/{id1}/{id2}")
	public GeneralProcessDTO getTask(@PathVariable("id1") int processId, @PathVariable("id2") ProcessName type) {
//		switch(type) {
//		case CASHEW_RECEIPT_QC:
//		case SUPPLIER_QC:
//		case VINA_CONTROL_QC:
//		case SAMPLE_QC:
//			return getQcRawCheck(processId);
//		default:
			return processDao.getProcess(processId, type);
//	}
	}
	
//	@PostMapping("/approveTask/{id}/{approve}")
//	public ResponseEntity<?> approveTask(@RequestBody JsonNode remarkSnapshot, @PathVariable("id") int id, @PathVariable("approve") DecisionType approve) {
//		processInfoWriter.setProcessDecision(id, approve, (remarkSnapshot.get("snapshot")).toString(), (remarkSnapshot.get("remarks")).toString());
//		return ResponseEntity.ok().build();
//	}
	
	@PostMapping("/approveTaskAndManagment/{approve}")
	public ResponseEntity<?> approveTaskAndManagment(@RequestBody JsonNode remarkSnapshot, @PathVariable("approve") DecisionType approve) {
		int processId = (remarkSnapshot.get("id")).asInt();
		if(remarkSnapshot.get("remarks") == null) {
			processInfoWriter.setUserProcessDecision(processId, approve, (remarkSnapshot.get("snapshot")).toString(), null);
		} else {
			processInfoWriter.setUserProcessDecision(processId, approve, (remarkSnapshot.get("snapshot")).toString(), (remarkSnapshot.get("remarks")).toString());
		}
		if((remarkSnapshot.get("toLock")).asBoolean()) {
			processInfoWriter.setEditStatus(EditStatus.LOCKED, processId);
		}
		if((remarkSnapshot.get("toFinal")).asBoolean()) {
			processInfoWriter.setProcessStatus(ProcessStatus.FINAL, processId);
		}
		if((remarkSnapshot.get("toCancal")).asBoolean()) {
			processInfoWriter.setProcessStatus(ProcessStatus.CANCELLED, processId);
		}
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/taskManagment")
	public ResponseEntity<?> taskManagment(@RequestBody JsonNode remarkSnapshot) {
		int processId = (remarkSnapshot.get("id")).asInt();
		if((remarkSnapshot.get("toLock")).asBoolean()) {
			processInfoWriter.setEditStatus(EditStatus.LOCKED, processId);
		}
		if((remarkSnapshot.get("toFinal")).asBoolean()) {
			processInfoWriter.setProcessStatus(ProcessStatus.FINAL, processId);
		}
		if((remarkSnapshot.get("toCancal")).asBoolean()) {
			processInfoWriter.setProcessStatus(ProcessStatus.CANCELLED, processId);
		}
		return ResponseEntity.ok().build();
	}
	
	
	@GetMapping("/setMassageTask/{id}/{approve}")
	public ResponseEntity<?> setMassageTask(@PathVariable("id") int id, @PathVariable("approve") MessageLabel approve) {
		processInfoWriter.setMessageLabel(id, approve);
		return ResponseEntity.ok().build();
	}
	
//	@GetMapping("/getStandardts")
//	public List<CashewStandard> getStandardts() {
//		return refeDao.getAllCashewStandards();
//	}
	
//	private JsonNode getQcRawCheck(int processId) {
//		List<Object> qcs = new ArrayList<Object>();
//		QualityCheckDTO qualityCheckDTO = qualityChecks.getQcByProcessId(processId);
//		Set<RawItemQualityDTO> listChecks = qualityCheckDTO.getTestedItems();
//		listChecks.forEach(m -> {
//			Object[] array = {qualityCheckDTO.getProcessName(), m, qualityChecks.getCashewStatndard(m.getItem().getId(), "avc")};
//			qcs.add(array);
//		});
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode node = mapper.valueToTree(qualityCheckDTO);
//		JsonNode node2 = mapper.valueToTree(qcs);
//		JsonNode node3 = ((ObjectNode)node).putPOJO("testedItems", node2);
//		return node3;
//	}
	
}

