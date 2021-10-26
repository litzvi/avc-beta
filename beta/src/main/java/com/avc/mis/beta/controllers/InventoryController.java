package com.avc.mis.beta.controllers;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.process.InventoryUseDTO;
import com.avc.mis.beta.dto.process.StorageRelocationDTO;
import com.avc.mis.beta.dto.process.StorageTransferDTO;
import com.avc.mis.beta.dto.view.InventoryTransactionRow;
import com.avc.mis.beta.dto.view.ItemInventoryAmountWithOrder;
import com.avc.mis.beta.dto.view.ItemInventoryRow;
import com.avc.mis.beta.dto.view.PoInventoryRow;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.InventoryUse;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.entities.process.StorageTransfer;
import com.avc.mis.beta.service.InventoryUses;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.ProductionProcesses;
import com.avc.mis.beta.service.ValueTablesReader;
import com.avc.mis.beta.service.WarehouseManagement;
import com.avc.mis.beta.service.report.InventoryReports;
import com.avc.mis.beta.service.report.InventoryUseReports;
import com.avc.mis.beta.service.report.StorageRelocationReports;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/inventory")
public class InventoryController {

	
	@Autowired
	private InventoryReports inventoryReports;
	
	@Autowired
	private InventoryUseReports inventoryUseReports;
	
	@Autowired
	private ValueTablesReader refeDao;
	
	@Autowired
	private ObjectTablesReader objectTableReader;
	
	@Autowired
	private WarehouseManagement warehouseManagement;
	
	@Autowired
	private InventoryUses inventoryUses;
	
	@Autowired
	private ProductionProcesses productionProcesses;
	
	@Autowired
	private StorageRelocationReports storageRelocationReports;
	
	
	@PostMapping(value="/addStorageTransfer")
	public StorageTransferDTO addStorageTransfer(@RequestBody StorageTransfer transfer) {
		warehouseManagement.addStorageTransfer(transfer);
		return warehouseManagement.getStorageTransfer(transfer.getId());
	}
	
	@PostMapping(value="/addRelocationTransfer")
	public StorageRelocationDTO addRelocationTransfer(@RequestBody StorageRelocation relocation) {
		warehouseManagement.addStorageRelocation(relocation);
		return warehouseManagement.getStorageRelocation(relocation.getId());
	}
	
	@PostMapping(value="/addMaterialUse")
	public InventoryUseDTO addMaterialUse(@RequestBody InventoryUse inventoryUse) {
		inventoryUses.addGeneralInventoryUse(inventoryUse);
		return inventoryUses.getInventoryUse(inventoryUse.getId());
	}
	
	@PostMapping(value="/addCashewUse")
	public InventoryUseDTO addCashewUse(@RequestBody InventoryUse inventoryUse) {
		inventoryUses.addProductInventoryUse(inventoryUse);
		return inventoryUses.getInventoryUse(inventoryUse.getId());
	}
	
	@PutMapping(value="/editStorageTransfer")
	public StorageTransferDTO editStorageTransfer(@RequestBody StorageTransfer transfer) {
		warehouseManagement.editStorageTransfer(transfer);
		return warehouseManagement.getStorageTransfer(transfer.getId());
	}
	
	@PutMapping(value="/editRelocationTransfer")
	public StorageRelocationDTO editRelocationTransfer(@RequestBody StorageRelocation relocation) {
		warehouseManagement.editStorageRelocation(relocation);
		return warehouseManagement.getStorageRelocation(relocation.getId());
	}
	
	@PutMapping(value="/editMaterialUse")
	public InventoryUseDTO editMaterialUse(@RequestBody InventoryUse inventoryUse) {
		inventoryUses.editGeneralInventoryUse(inventoryUse);
		return inventoryUses.getInventoryUse(inventoryUse.getId());
	}
	
	@PutMapping(value="/editCashewUse")
	public InventoryUseDTO editCashewUse(@RequestBody InventoryUse inventoryUse) {
		inventoryUses.editProductInventoryUse(inventoryUse);
		return inventoryUses.getInventoryUse(inventoryUse.getId());
	}
	
	@RequestMapping("/getStorageTransfer/{id}")
	public StorageTransferDTO getStorageTransfer(@PathVariable("id") int id) {
		return warehouseManagement.getStorageTransfer(id);
	}
	
	@RequestMapping("/getStorageRelocation/{id}")
	public StorageRelocationDTO getStorageRelocation(@PathVariable("id") int id) {
		return warehouseManagement.getStorageRelocation(id);
	}
	
	@RequestMapping("/getStroageUse/{id}")
	public InventoryUseDTO getStroageUse(@PathVariable("id") int id) {
		return warehouseManagement.getInventoryUse(id);
	}
	
	@RequestMapping("/getTransferCounts")
	public List<ProcessRow> getTransferCounts() {
		return warehouseManagement.getStorageTransfers();
	}
	
	@RequestMapping("/getStorageRelocations/{id}")
	public List<ProcessRow> getStorageRelocations(@PathVariable("id") ProductionFunctionality functionality,
			@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		return storageRelocationReports.getStorageRelocations(functionality, begin, end);
	}
	
	@RequestMapping("/getCashewInventoryItem")
	public List<ItemInventoryRow> getCashewInventoryItem() {
		return inventoryReports.getInventoryTableByItem(ItemGroup.PRODUCT);
	}
	
	@RequestMapping("/getCashewInventoryByPo")
	public List<PoInventoryRow> getCashewInventoryByPo() {
		return inventoryReports.getInventoryTableByPo(ItemGroup.PRODUCT);
	}
	
	@RequestMapping("/getCashewInventoryOrder")
	public List<ItemInventoryAmountWithOrder> getCashewInventoryOrder() {
		return inventoryReports.getInventoryWithOrderByItem(ItemGroup.PRODUCT, null);
	}
	
	@RequestMapping("/getGeneralInventoryItem")
	public List<ItemInventoryRow> getGeneralInventoryItem() {
		return inventoryReports.getInventoryTableByItem(ItemGroup.GENERAL);
	}
	
	@RequestMapping("/getMaterialUses")
	public List<ProcessRow> getMaterialUses(@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		return inventoryUseReports.getInventoryUses(ProcessName.GENERAL_USE, begin, end);
	}
	
	@RequestMapping("/getCashewUses")
	public List<ProcessRow> getCashewUses(@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		return inventoryUseReports.getInventoryUses(ProcessName.PRODUCT_USE, begin, end);
	}
	
	@RequestMapping("/getInventoryTransactions")
	public List<InventoryTransactionRow> getInventoryTransactions(@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		return warehouseManagement.getInventoryTransactions(ItemGroup.GENERAL, null, null, begin, end);
	}

	
	@RequestMapping("/getGeneralInventoryByPo")
	public List<PoInventoryRow> getGeneralInventoryByPo() {
		return inventoryReports.getInventoryTableByPo(ItemGroup.GENERAL);
	}
	
	@RequestMapping("/getGeneralInventoryOrder")
	public List<ItemInventoryAmountWithOrder> getGeneralInventoryOrder() {
		return inventoryReports.getInventoryWithOrderByItem(ItemGroup.GENERAL, null);
	}
	
	@RequestMapping("/getPoCashewCodesInventory")
	public Set<PoCodeBasic> getPoCashewCodesInventory() {
		return objectTableReader.findCashewAvailableInventoryPoCodes();
	}
	
//	@RequestMapping("/getPoCashewCodesRaw")
//	public Set<PoCodeBasic> getPoCashewCodesInventory() {
//		return objectTableReader.findCashewAvailableInventoryPoCodes();
//	}
//	
//	@RequestMapping("/getPoCashewCodesClean")
//	public Set<PoCodeBasic> getPoCashewCodesInventory() {
//		return objectTableReader.findCashewAvailableInventoryPoCodes();
//	}
	
	
	@RequestMapping(value={"/getStoragePo/{id}", "/getStoragePo/{id}/{process}"})
	public List<ProcessItemInventory> getStoragePo(@PathVariable("id") Integer[] poCodes, @PathVariable("process") Optional<Integer> process) {
		if (process.isPresent()) {
			return warehouseManagement.getAvailableInventory(null, null, null, null, poCodes, process.get());
	    } else {
	    	return warehouseManagement.getAvailableInventory(null, null, null, null, poCodes, null);
	    }
	}
	
	@RequestMapping(value={"/getStorageRawPo/{id}", "/getStorageRawPo/{id}/{process}"})
	public List<ProcessItemInventory> getStorageRawPo(@PathVariable("id") Integer[] poCodes, @PathVariable("process") Optional<Integer> process) {
		if (process.isPresent()) {
			return warehouseManagement.getAvailableInventory(ItemGroup.PRODUCT, new ProductionUse[]{ProductionUse.RAW_KERNEL}, null, null, poCodes, process.get());
	    } else {
	    	return warehouseManagement.getAvailableInventory(ItemGroup.PRODUCT, new ProductionUse[]{ProductionUse.RAW_KERNEL}, null, null, poCodes, null);
	    }
	}
	
	@RequestMapping(value={"/getStorageCleanPo/{id}", "/getStorageCleanPo/{id}/{process}"})
	public List<ProcessItemInventory> getStorageCleanPo(@PathVariable("id") Integer[] poCodes, @PathVariable("process") Optional<Integer> process) {
		if (process.isPresent()) {
			return warehouseManagement.getAvailableInventory(ItemGroup.PRODUCT, new ProductionUse[]{ProductionUse.CLEAN}, null, null, poCodes, process.get());
	    } else {
	    	return warehouseManagement.getAvailableInventory(ItemGroup.PRODUCT, new ProductionUse[]{ProductionUse.CLEAN}, null, null, poCodes, null);
	    }
	}
	
	@RequestMapping("/getStorageTransferItem/{id}")
	public List<ProcessItemInventory> getStorageTransferItem(@PathVariable("id") int itemId) {
		return warehouseManagement.getAvailableInventory(ItemGroup.PRODUCT, null, null, itemId, null, null);
	}
	
	@RequestMapping("/getAllPos/{id}")
	public Set<PoCodeBasic> getAllPos(@PathVariable("id") ProductionUse item) {
		return objectTableReader.findAvailableInventoryPoCodes(new ProductionUse[]{item});
	}
	
	@RequestMapping("/findAllPoCodes")
	public List<PoCodeBasic> getAllPoCodes() {
		List<PoCodeBasic> allPoCodes = new ArrayList<PoCodeBasic>();
		allPoCodes.addAll(objectTableReader.findCashewAvailableInventoryPoCodes());
		allPoCodes.addAll(objectTableReader.findGeneralAvailableInventoryPoCodes());
		return allPoCodes;
	}
	
	@RequestMapping("/getAllCashewPos")
	public Set<PoCodeBasic> getAllCashewPos() {
		return objectTableReader.findCashewAvailableInventoryPoCodes();
	}
	
	@RequestMapping("/getAllStorageCashew/{id}")
	public List<ProcessItemInventory> getAllStorageCashew(@PathVariable("id") int poCode) {
		return warehouseManagement.getAvailableInventory(ItemGroup.PRODUCT, null, null, null, new Integer[] {poCode}, null);
	}

}
