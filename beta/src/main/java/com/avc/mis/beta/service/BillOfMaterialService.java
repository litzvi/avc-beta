/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dto.item.BillOfMaterialsDTO;
import com.avc.mis.beta.dto.item.BomLineDTO;
import com.avc.mis.beta.dto.query.BomProductWithMaterialLine;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.entities.enums.PackageType;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.item.BillOfMaterials;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.repositories.BillOfMaterialsRepository;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;
import com.sun.mail.handlers.image_gif;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * 
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class BillOfMaterialService {
	
	@Autowired private DeletableDAO dao;

	@Autowired private BillOfMaterialsRepository bomRepository;
	@Autowired private WarehouseManagement warehouseManagement;
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addBillOfMaterials(BillOfMaterialsDTO billOfMaterials) {
		dao.addEntity(billOfMaterials.fillEntity(new BillOfMaterials()));
	}
	
	public BillOfMaterialsDTO getBillOfMaterialsByProduct(int productId) {
		BillOfMaterials billOfMaterials = getBomRepository().findBillOfMaterialsByProduct(productId);
		if(billOfMaterials == null) {
			throw new IllegalArgumentException("No bill of materials for given product id");
		}
		return new BillOfMaterialsDTO(billOfMaterials);	
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editBillOfMaterials(BillOfMaterialsDTO billOfMaterials) {
		dao.editEntity(billOfMaterials.fillEntity(new BillOfMaterials()));
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void removeBillOfMaterials(Integer billOfMaterialsId) {
		dao.permenentlyRemoveEntity(BillOfMaterials.class, billOfMaterialsId);
	}
		
	public List<BillOfMaterialsDTO> getAllBillOfMaterials() {
		return CollectionItemWithGroup.getFilledGroups(getBomRepository().findAllBomProductWithMaterialLine(), 
				BomProductWithMaterialLine::getBillOfMaterials, 
				BomProductWithMaterialLine::getBomLine, 
				BillOfMaterialsDTO::setBomList);
		
	}
		
//	public List<ProcessItemInventory> getProductBomInventory(@NonNull Integer productId, 
//			ItemGroup group, ProductionUse[] productionUses, ProductionFunctionality[] functionalities, 
//			PackageType packageType, Integer[] poCodeIds, Integer excludeProcessId) {
//		BillOfMaterialsDTO billOfMaterials = getBillOfMaterialsByProduct(productId);
//		Integer[] itemIds = billOfMaterials.getBomList().stream().map(i -> i.getMaterial().getId()).toArray(Integer[]::new);
//		return warehouseManagement.getAvailableInventory(group, productionUses, functionalities, itemIds, packageType, poCodeIds, excludeProcessId);
//	}
	
//	public Object[] getProductBomInventoryAndMissingItems(@NonNull Integer productId, 
//			ItemGroup group, ProductionUse[] productionUses, ProductionFunctionality[] functionalities, 
//			PackageType packageType, Integer[] poCodeIds, Integer excludeProcessId) {
//		BillOfMaterialsDTO billOfMaterials = getBillOfMaterialsByProduct(productId);
//		Integer[] itemIds = billOfMaterials.getBomList().stream().map(i -> i.getMaterial().getId()).toArray(Integer[]::new);
//		List<ProcessItemInventory> inventory = warehouseManagement.getAvailableInventory(group, productionUses, functionalities, itemIds, packageType, poCodeIds, excludeProcessId);
//		return new Object[]{inventory, Arrays.asList(itemIds).removeAll(inventory.stream().map(i -> i.getItem().getId()).collect(Collectors.toList()))};
//	}
}
