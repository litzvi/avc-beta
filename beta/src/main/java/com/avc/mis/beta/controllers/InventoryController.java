package com.avc.mis.beta.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.process.StorageTransferDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.view.ItemInventoryRow;
import com.avc.mis.beta.dto.view.PoInventoryRow;
import com.avc.mis.beta.dto.view.ProcessItemInventoryRow;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.entities.process.StorageTransfer;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.service.CashewReports;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.ProductionProcesses;
import com.avc.mis.beta.service.ValueTablesReader;
import com.avc.mis.beta.service.WarehouseManagement;

@RestController
@RequestMapping(path = "/api/inventory")
public class InventoryController {

	
	@Autowired
	private CashewReports cashewReports;
	
	@Autowired
	private ValueTablesReader refeDao;
	
	@Autowired
	private ObjectTablesReader objectTableReader;
	
	@Autowired
	private WarehouseManagement warehouseManagement;
	
	@Autowired
	private ProductionProcesses productionProcesses;
	
	
	@PostMapping(value="/addStorageTransfer")
	public ResponseEntity<StorageTransferDTO> addStorageTransfer(@RequestBody StorageTransfer transfer) {
		warehouseManagement.addStorageTransfer(transfer);
		return ResponseEntity.ok(warehouseManagement.getStorageTransfer(transfer.getId()));
	}
	
	@PutMapping(value="/editStorageTransfer")
	public ResponseEntity<StorageTransferDTO> editStorageTransfer(@RequestBody StorageTransfer transfer) {
		warehouseManagement.editStorageTransfer(transfer);
		return ResponseEntity.ok(warehouseManagement.getStorageTransfer(transfer.getId()));
	}
	
	@PostMapping(value="/addStorageListTransfer")
	public ResponseEntity<StorageTransferDTO> addStorageListTransfer(@RequestBody List<StorageTransfer> transfer) {
		transfer.forEach((m) -> {
			warehouseManagement.addStorageTransfer(m);
		});
		
		return ResponseEntity.ok(warehouseManagement.getStorageTransfer(transfer.get(0).getId()));
	}
	
	@RequestMapping("/getStorageTransfer/{id}")
	public StorageTransferDTO getStorageTransfer(@PathVariable("id") int id) {
		return warehouseManagement.getStorageTransfer(id);
	}
	
	@RequestMapping("/allProduction/{id}")
	public List<ProcessRow> allProduction(@PathVariable("id") ProcessName process) {
		return productionProcesses.getProductionProcessesByType(process);
	}
	
	@RequestMapping("/getCashewInventoryItem")
	public List<ItemInventoryRow> getCashewInventoryItem() {
		return cashewReports.getInventoryTableByItem();
	}
	
	@RequestMapping("/getInventoryTableByPo")
	public List<PoInventoryRow> getInventoryTableByPo() {
		return cashewReports.getInventoryTableByPo();
	}
	
	@RequestMapping("/getAllItems")
	public List<Item> getAllItems() {
		return refeDao.getAllItems();
	}
	
	@RequestMapping("/getCashewItems")
	public List<BasicValueEntity<Item>> getCashewItems() {
		return refeDao.getCashewItemsBasic();
	}
	
	@RequestMapping("/getGeneralItems")
	public List<BasicValueEntity<Item>> getGeneralItems() {
		return refeDao.getGeneralItemsBasic();
	}
	
	@RequestMapping("/getPoCashewCodesInventory")
	public Set<PoCodeDTO> getPoCashewCodesInventory() {
		return objectTableReader.findCashewInventoryPoCodes();
	}
	
	@RequestMapping("/getStorageTransferPo/{id}")
	public List<ProcessItemInventoryRow> getStorageTransferPo(@PathVariable("id") int poCode) {
		return warehouseManagement.getInventoryByPo(poCode);
	}
	
	@RequestMapping("/getStorageTransferItem/{id}")
	public List<ProcessItemInventoryRow> getStorageTransferItem(@PathVariable("id") int itemId) {
		return warehouseManagement.getInventoryByItem(itemId);
	}
	
//	@PostMapping(value="/addCleaningTransfer")
//	public ResponseEntity<ProductionProcessDTO> addCleaningTransfer(@RequestBody ProductionProcess process) {
//		productionProcesses.addProductionProcess(process, ProcessName.CASHEW_CLEANING);
//		return ResponseEntity.ok(productionProcesses.getProductionProcess(process.getId()));
//	}
//	

}
