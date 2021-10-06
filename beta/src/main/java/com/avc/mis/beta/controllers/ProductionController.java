
package com.avc.mis.beta.controllers;

import java.time.LocalDateTime;
import java.util.List;
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

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.entities.enums.PackageType;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.ProductionProcesses;
import com.avc.mis.beta.service.ValueTablesReader;
import com.avc.mis.beta.service.WarehouseManagement;
import com.avc.mis.beta.service.report.ProductionProcessReports;

@RestController
@RequestMapping(path = "/api/production")
public class ProductionController {

	
//	@Autowired
//	private CashewReports cashewReports;
//	
	@Autowired
	private ValueTablesReader refeDao;
	
	@Autowired
	private ObjectTablesReader objectTableReader;
	
	@Autowired
	private WarehouseManagement warehouseManagement;
	
	@Autowired
	private ProductionProcesses productionProcesses;
	
	@Autowired
	private ProductionProcessReports productionProcessReports;
	
	@RequestMapping("/allProduction/{id}")
	public List<ProcessRow> allProduction(@PathVariable("id") ProcessName process,
			@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		return productionProcessReports.getProductionProcessesByType(process, begin, end);
	}
	
	
	@RequestMapping("/getAllPos/{id}")
	public Set<PoCodeBasic> getAllPos(@PathVariable("id") ProductionUse usage) {
		switch (usage) {
			case RAW_KERNEL:
				return warehouseManagement.findAvailableInventoryPoCodes(new ProductionUse[]{usage}, new ProductionFunctionality[]{ProductionFunctionality.RAW_STATION}, null);
			case CLEAN:
				return warehouseManagement.findAvailableInventoryPoCodes(new ProductionUse[]{usage}, new ProductionFunctionality[]{ProductionFunctionality.ROASTER_IN}, null);
			default:
				return warehouseManagement.findAvailableInventoryPoCodes(new ProductionUse[]{usage}, null);
		}
	}
	
	@RequestMapping("/getAllPosToPack/{id}")
	public Set<PoCodeBasic> getAllPosToPack(@PathVariable("id") Boolean withPacked) {
			return warehouseManagement.findAvailableInventoryPoCodes(new ProductionUse[]{ProductionUse.TOFFEE, ProductionUse.ROAST}, withPacked? null : PackageType.BULK);
	}
	
	@RequestMapping("/getAllPosQc/{id}")
	public Set<PoCodeBasic> getAllPosQc(@PathVariable("id") Boolean withAll) {
		if(withAll) {
			return warehouseManagement.findAvailableInventoryPoCodes(null, ItemGroup.WASTE, 4, null, null);
		}
		return warehouseManagement.findAvailableInventoryPoCodes(new ProductionFunctionality[]{ProductionFunctionality.ROASTER, ProductionFunctionality.PACKING}, ItemGroup.WASTE, 4, null, null);
	}
	
	@RequestMapping("/findFreeMixPoCodes")
	public List<PoCodeBasic> findFreeMixPoCodes() {
		return objectTableReader.findFreeMixPoCodes();
	}
	
	
	@RequestMapping("/getStorageRawPo/{id}")
	public List<ProcessItemInventory> getStorageRawPo(@PathVariable("id") int poCode) {
		return warehouseManagement.getAvailableInventory(ItemGroup.PRODUCT, new ProductionUse[]{ProductionUse.RAW_KERNEL}, new ProductionFunctionality[]{ProductionFunctionality.RAW_STATION}, null, new Integer[] {poCode}, null);
	}
	@RequestMapping("/getStorageCleanPo/{id}")
	public List<ProcessItemInventory> getStorageCleanPo(@PathVariable("id") int poCode) {
		return warehouseManagement.getAvailableInventory(ItemGroup.PRODUCT, new ProductionUse[]{ProductionUse.CLEAN}, new ProductionFunctionality[]{ProductionFunctionality.ROASTER_IN}, null, new Integer[] {poCode}, null);
	}
	@RequestMapping("/getStorageRoastPo/{id}")
	public List<ProcessItemInventory> getStorageRoastPo(@PathVariable("id") int poCode) {
		return warehouseManagement.getAvailableInventory(ItemGroup.PRODUCT, new ProductionUse[]{ProductionUse.ROAST}, null, null, new Integer[] {poCode}, null);
	}
	@RequestMapping("/getStorageToPackPo/{id}/{id1}")
	public List<ProcessItemInventory> getStorageToPackPo(@PathVariable("id") int poCode, @PathVariable("id1") Boolean withPacked) {
		return warehouseManagement.getAvailableInventory(ItemGroup.PRODUCT, new ProductionUse[]{ProductionUse.ROAST, ProductionUse.TOFFEE}, null, null, withPacked? null : PackageType.BULK, new Integer[] {poCode}, null);
	}
	@RequestMapping("/getStorageQcPo/{id}/{id1}")
	public List<ProcessItemInventory> getStorageQcPo(@PathVariable("id") int poCode, @PathVariable("id1") Boolean withAll) {
		if(withAll) {
			return warehouseManagement.getAvailableInventory(ItemGroup.WASTE, null, null, 4, new Integer[] {poCode}, null);
		}
		return warehouseManagement.getAvailableInventory(ItemGroup.WASTE, null, new ProductionFunctionality[]{ProductionFunctionality.ROASTER, ProductionFunctionality.PACKING}, 4, new Integer[] {poCode}, null);
	}
	@RequestMapping("/getStorageQcPos/{poCodes}/{id1}")
	public List<ProcessItemInventory> getStorageQcPos(@PathVariable("poCodes") Integer[] poCodes, @PathVariable("id1") Boolean withAll) {
		if(withAll) {
			return warehouseManagement.getAvailableInventory(ItemGroup.WASTE, null, null, 4, poCodes, null);
		}
		return warehouseManagement.getAvailableInventory(ItemGroup.WASTE, null, new ProductionFunctionality[]{ProductionFunctionality.ROASTER, ProductionFunctionality.PACKING}, 4, poCodes, null);
	}
	@RequestMapping("/getStorageToPackPos/{poCodes}/{id}")
	public List<ProcessItemInventory> getStorageRoastPos(@PathVariable("poCodes") Integer[] poCodes, @PathVariable("id") Boolean withPacked) {
		return warehouseManagement.getAvailableInventory(ItemGroup.PRODUCT, new ProductionUse[]{ProductionUse.ROAST, ProductionUse.TOFFEE}, null, null, withPacked? null : PackageType.BULK, poCodes, null);
	}
	
	@RequestMapping("/getProduction/{id}")
	public ProductionProcessDTO getProduction(@PathVariable("id") int id) {
		return productionProcesses.getProductionProcess(id);
	}
	
	@PostMapping(value="/addCleaningTransfer")
	public ProductionProcessDTO addCleaningTransfer(@RequestBody ProductionProcess process) {
		productionProcesses.addProductionProcess(process, ProcessName.CASHEW_CLEANING);
		return productionProcesses.getProductionProcess(process.getId());
	}
	
	@PostMapping(value="/addRoastingTransfer")
	public ProductionProcessDTO addRoastingTransfer(@RequestBody ProductionProcess process) {
		productionProcesses.addProductionProcess(process, ProcessName.CASHEW_ROASTING);
		return productionProcesses.getProductionProcess(process.getId());
	}
	
	@PostMapping(value="/addToffeeTransfer")
	public ProductionProcessDTO addToffeeTransfer(@RequestBody ProductionProcess process) {
		productionProcesses.addProductionProcess(process, ProcessName.CASHEW_TOFFEE);
		return productionProcesses.getProductionProcess(process.getId());
	}
	
	@PostMapping(value="/addPackingTransfer")
	public ProductionProcessDTO addPackingTransfer(@RequestBody ProductionProcess process) {
		productionProcesses.addProductionProcess(process, ProcessName.PACKING);
		return productionProcesses.getProductionProcess(process.getId());
	}
	
	@PostMapping(value="/addQcPackingTransfer")
	public ProductionProcessDTO addQcPackingTransfer(@RequestBody ProductionProcess process) {
		productionProcesses.addProductionProcess(process, ProcessName.BAD_QUALITY_PACKING);
		return productionProcesses.getProductionProcess(process.getId());
	}
	
	@PutMapping(value="/editProductionTransfer")
	public ProductionProcessDTO editProductionTransfer(@RequestBody ProductionProcess process) {
		productionProcesses.editProductionProcess(process);
		return productionProcesses.getProductionProcess(process.getId());
	}
	

}
