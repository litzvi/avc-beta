
package com.avc.mis.beta.controllers;


import com.avc.mis.beta.dto.BaseEntityDTO;
import com.avc.mis.beta.dto.GeneralProcessDTO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.basic.PoCodeBasicWithProductCompany;
import com.avc.mis.beta.dto.basic.ProductionLineBasic;
import com.avc.mis.beta.dto.data.DataObjectWithName;
import com.avc.mis.beta.dto.process.collection.ApprovalTaskDTO;
import com.avc.mis.beta.dto.process.collection.UserMessageDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.CashewItemDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ManagementType;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.values.CashewStandard;
import com.avc.mis.beta.entities.values.Warehouse;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.ProcessInfoReader;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.ProcessReader;
import com.avc.mis.beta.service.QualityChecks;
import com.avc.mis.beta.service.Users;
import com.avc.mis.beta.service.ValueTablesReader;
import com.avc.mis.beta.service.WarehouseManagement;
import com.avc.mis.beta.service.report.row.TaskRow;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




/**
 * @author Zvi
 *
 */
@RestController
@RequestMapping(path = "/api")
public class Controller {
	
	@Autowired
	private SignedUrlsAws signedUrlsAws;
	
	@Autowired
	private ValueTablesReader refeDao;
	
	@Autowired
	private ProcessInfoReader processDao;
	
	@Autowired
	private ProcessReader processReader;
	
	@Autowired
	private ObjectTablesReader objectTableReader;
	
	@Autowired
	private QualityChecks qualityChecks;
	
	@Autowired
	private WarehouseManagement warehouseManagement;
	
	@Autowired
	private ProcessInfoWriter processInfoWriter;
	
	@Autowired
	private Users users;
	
	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
	
	@PostMapping("/urli")
	public URL urli(@RequestBody JsonNode path) throws IOException {
		return signedUrlsAws.uploadRequest(path.get("fileName").toString());
	}
	
	@PostMapping("/getUrls")
	public URL getUrls(@RequestBody JsonNode path) throws IOException {
		return signedUrlsAws.getRequest(path.get("fileName").toString());
	}
	
	
	//should be done with one service
	@RequestMapping("/setup")
	public List<Object> getSetup() {
		
		List<Object> result = new ArrayList<Object>();
		
		List<BasicValueEntity<Warehouse>> Storageholder = refeDao.getAllWarehousesDTO();
		result.add(Storageholder);
		
		List<CashewStandard> standartholder = refeDao.getAllCashewStandards();
		result.add(standartholder);
		
//		List<ItemDTO> CashewItemsholder = refeDao.getItemsByGroup(ItemGroup.PRODUCT);
//		result.add(CashewItemsholder);
		
		List<CashewItemDTO> CashewItemsholder = refeDao.getCashewItems(null, null, null, null);
		result.add(CashewItemsholder);
//		
		List<ItemDTO> GeneralItemsholder = refeDao.getItemsByGroup(ItemGroup.GENERAL);
		result.add(GeneralItemsholder);
		
		Map<ProcessName, List<ManagementType>> Managmentholder = processDao.getAllUserManagementTypes();
		result.add(Managmentholder);

		List<ProductionLineBasic> ProductionLinesHolder = refeDao.getAllBasicProductionLines();
		result.add(ProductionLinesHolder);
		
		List<ItemDTO> WasteItemsholder = refeDao.getItemsByGroup(ItemGroup.WASTE);
		WasteItemsholder.sort(new Comparator<BaseEntityDTO>() {
			@Override
			public int compare(BaseEntityDTO o1, BaseEntityDTO o2) {
				return Integer.compare(o1.getId(), o2.getId());
			}
		});
		result.add(WasteItemsholder);
		
		List<DataObjectWithName<Supplier>> CashewSuppliers = refeDao.getCashewSuppliersBasic();
		result.add(CashewSuppliers);
		
		List<DataObjectWithName<Supplier>> GeneralSuppliers = refeDao.getGeneralSuppliersBasic();
		result.add(GeneralSuppliers);
		
//		List<BasicValueEntity<Item>> CashewItemsraw = refeDao.getItemsByCategry(ItemCategory.RAW);
//		result.add(CashewItemsraw);
//		List<BasicValueEntity<Item>> CashewItemsclean = refeDao.getItemsByCategry(ItemCategory.CLEAN);
//		result.add(CashewItemsclean);
//		List<BasicValueEntity<Item>> CashewItemsroast = refeDao.getItemsByCategry(ItemCategory.ROAST);
//		result.add(CashewItemsroast);
		return result; 
	}
	
	@RequestMapping("/getUserMassages")
	public List<UserMessageDTO> getMassages(@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end) {
			return processDao.getAllMessages(begin, end);
	}
	
	@RequestMapping("/getUserTasks")
	public List<TaskRow> getUserTasks(@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end) {
		return processDao.getTaskRows(new ProcessStatus[]{ProcessStatus.PENDING}, begin, end);
	}
	
	@RequestMapping("/getUserMassagesNumber")
	public int getMassagesNumber() {
		return processDao.getUserMassagesNumber(Arrays.asList(MessageLabel.NEW));
	}
	
	@RequestMapping("/getUserTasksNumber")
	public int getUserTasksNumber() {
		return processDao.getUserTasksNumber(new ProcessStatus[] {ProcessStatus.PENDING}, new DecisionType[] {DecisionType.EDIT_NOT_ATTENDED, DecisionType.NOT_ATTENDED});
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
				return processReader.getProcess(processId, type);
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
			return processReader.getProcess(processId, type);
//	}
	}
	
//	@PostMapping("/approveTask/{id}/{approve}")
//	public ResponseEntity<?> approveTask(@RequestBody JsonNode remarkSnapshot, @PathVariable("id") int id, @PathVariable("approve") DecisionType approve) {
//		processInfoWriter.setProcessDecision(id, approve, (remarkSnapshot.get("snapshot")).toString(), (remarkSnapshot.get("remarks")).toString());
//		return ResponseEntity.ok().build();
//	}
	
	@PostMapping("/approveTaskAndManagment/{approve}")
	public ObjectNode approveTaskAndManagment(@RequestBody JsonNode remarkSnapshot, @PathVariable("approve") DecisionType approve) throws JsonMappingException, JsonProcessingException {
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
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ProcessName type = mapper.readValue((remarkSnapshot.get("processName")).toString(), ProcessName.class);
		ObjectNode childNode1 = mapper.createObjectNode();
//		childNode1.put("approvals", (processReader.getProcess(processId, type)).getApprovals());
		return childNode1; 
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
	
	
//	@RequestMapping("/getCashewSuppliers")
//	public List<DataObjectWithName<Supplier>> getCashewSuppliers() {
//		return refeDao.getCashewSuppliersBasic();
//	}
	
	@RequestMapping("/findAllPoCodes")
	public List<PoCodeBasicWithProductCompany> findAllPoCodes() {
		return objectTableReader.findAllPoCodes();
	}
	
	@RequestMapping("/getStorageGeneralItem/{id}")
	public List<ProcessItemInventory> getStorageGeneralItem(@PathVariable("id") int itemId) {
		return warehouseManagement.getAvailableInventory(ItemGroup.GENERAL, null, null, itemId, null, null);
	}
	
	@RequestMapping("/findAvailableItems")
	public Set<BasicValueEntity<Item>> findAvailableItems() {
		return warehouseManagement.findAvailableInventoryItems(ItemGroup.GENERAL);
	}
	
	@PostMapping("/passChange")
	public ResponseEntity<?> passChange(@RequestBody JsonNode passwords) {
		String oldPassword = (passwords.get("oldPassword")).asText();
		String newPassword = (passwords.get("newPassword")).asText();
		users.changePassword(oldPassword, newPassword);
		return ResponseEntity.ok().build();
	}
}

