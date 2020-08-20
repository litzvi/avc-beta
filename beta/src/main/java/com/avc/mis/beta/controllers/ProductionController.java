
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
import com.avc.mis.beta.dto.view.SupplierRow;
import com.avc.mis.beta.entities.enums.ItemCategory;
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
@RequestMapping(path = "/api/production")
public class ProductionController {

	
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
	
	@RequestMapping("/allProduction/{id}")
	public List<ProcessRow> allProduction(@PathVariable("id") ProcessName process) {
		return productionProcesses.getProductionProcessesByType(process);
	}

	
	@RequestMapping("/getInventoryTableByPo")
	public List<PoInventoryRow> getInventoryTableByPo() {
		return cashewReports.getInventoryTableByPo();
	}
	
	@RequestMapping("/getAllItemsRaw")
	public List<BasicValueEntity<Item>> getAllItemsRaw() {
		return refeDao.getItemsByCategry(ItemCategory.RAW);
	}
	@RequestMapping("/getAllItemsClean")
	public List<BasicValueEntity<Item>> getAllItemsClean() {
		return refeDao.getItemsByCategry(ItemCategory.CLEAN);
	}
	@RequestMapping("/getAllItemsRoast")
	public List<BasicValueEntity<Item>> getAllItemsRoast() {
		return refeDao.getItemsByCategry(ItemCategory.ROAST);
	}
	
		
	@RequestMapping("/getPoCashewCodesInventory")
	public Set<PoCodeDTO> getPoCashewCodesInventory() {
		return objectTableReader.findCashewInventoryPoCodes();
	}
	
	@RequestMapping("/getStorageTransferPo/{id}")
	public List<ProcessItemInventoryRow> getStorageTransferPo(@PathVariable("id") int poCode) {
		return warehouseManagement.getInventoryByPo(poCode);
	}
	
	@RequestMapping("/getTransferProduction/{id}")
	public ProductionProcessDTO getTransferProduction(@PathVariable("id") int id) {
		return productionProcesses.getProductionProcess(id);
	}
	
	@PostMapping(value="/addCleaningTransfer")
	public ResponseEntity<ProductionProcessDTO> addCleaningTransfer(@RequestBody ProductionProcess process) {
		productionProcesses.addProductionProcess(process, ProcessName.CASHEW_CLEANING);
		return ResponseEntity.ok(productionProcesses.getProductionProcess(process.getId()));
	}
	
	@PostMapping(value="/addRoastingTransfer")
	public ResponseEntity<ProductionProcessDTO> addRoastingTransfer(@RequestBody ProductionProcess process) {
		productionProcesses.addProductionProcess(process, ProcessName.CASHEW_ROASTING);
		return ResponseEntity.ok(productionProcesses.getProductionProcess(process.getId()));
	}
	
	@PostMapping(value="/addPackingTransfer")
	public ResponseEntity<ProductionProcessDTO> addPackingTransfer(@RequestBody ProductionProcess process) {
		productionProcesses.addProductionProcess(process, ProcessName.PACKING);
		return ResponseEntity.ok(productionProcesses.getProductionProcess(process.getId()));
	}
	
	@PutMapping(value="/editProductionTransfer")
	public ResponseEntity<ProductionProcessDTO> editProductionTransfer(@RequestBody ProductionProcess process) {
		productionProcesses.editProductionProcess(process);
		return ResponseEntity.ok(productionProcesses.getProductionProcess(process.getId()));
	}
	

}
