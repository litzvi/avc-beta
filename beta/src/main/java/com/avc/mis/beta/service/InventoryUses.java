/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.InventoryUseDTO;
import com.avc.mis.beta.dto.query.ItemAmountWithPoCode;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.process.InventoryUse;
import com.avc.mis.beta.entities.process.collection.StorageMovesGroup;
import com.avc.mis.beta.entities.process.inventory.StorageBase;
import com.avc.mis.beta.entities.process.inventory.StorageMove;
import com.avc.mis.beta.repositories.InventoryUseRepository;
import com.avc.mis.beta.repositories.ValueTablesRepository;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class InventoryUses {
	
	@Autowired private ProcessInfoDAO dao;

	@Autowired private InventoryUseRepository inventoryUseRepository;
	@Autowired private ValueTablesRepository valueTablesRepository;
	@Autowired private ProcessReader processReader;

	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void addGeneralInventoryUse(InventoryUse inventoryUse) {
		inventoryUse.setProcessType(dao.getProcessTypeByValue(ProcessName.GENERAL_USE));
		if(inventoryUse.getProductionLine() == null || 
				valueTablesRepository.findFunctionalityByProductionLine(inventoryUse.getProductionLine().getId()) != ProductionFunctionality.GENERAL_USE) {
			throw new IllegalStateException("Inventory Use has to have a Production Line with ProductionFunctionality.GENERAL_USE");
		}
		//Check that used items are from general
		if(!isUsedInItemGroup(inventoryUse.getStorageMovesGroups(), ItemGroup.GENERAL)) {
			throw new IllegalArgumentException("Inventory use can only be for GENERAL item groups");
		}
		dao.setStorageMovesProcessItem(inventoryUse.getStorageMovesGroups());
		dao.addPoProcessEntity(inventoryUse);
		dao.checkUsedInventoryAvailability(inventoryUse);
		dao.setRelocationPoWeights(inventoryUse);
		dao.setUsedProcesses(inventoryUse);
		//check if storage moves match the amounts of the used item
		dao.checkRelocationBalance(inventoryUse);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void addProductInventoryUse(InventoryUse inventoryUse) {
		inventoryUse.setProcessType(dao.getProcessTypeByValue(ProcessName.PRODUCT_USE));
		if(inventoryUse.getProductionLine() == null || 
				valueTablesRepository.findFunctionalityByProductionLine(inventoryUse.getProductionLine().getId()) != ProductionFunctionality.PRODUCT_USE) {
			throw new IllegalStateException("Inventory Use has to have a Production Line with ProductionFunctionality.PRODUCT_USE");
		}
		//Check that used items are from product (cashew)
		if(!isUsedInItemGroup(inventoryUse.getStorageMovesGroups(), ItemGroup.PRODUCT)) {
			throw new IllegalArgumentException("Inventory use can only be for PRODUCT item groups");
		}
		dao.setStorageMovesProcessItem(inventoryUse.getStorageMovesGroups());
		dao.addPoProcessEntity(inventoryUse);
		dao.checkUsedInventoryAvailability(inventoryUse);
		dao.setRelocationPoWeights(inventoryUse);
		dao.setUsedProcesses(inventoryUse);
		//check if storage moves match the amounts of the used item
		dao.checkRelocationBalance(inventoryUse);
		
	}
	
	public InventoryUseDTO getInventoryUse(int processId) {
		InventoryUseDTO inventoryUseDTO = new InventoryUseDTO();
		inventoryUseDTO.setGeneralProcessInfo(getInventoryUseRepository()
				.findGeneralProcessInfoByProcessId(processId, InventoryUse.class)
				.orElseThrow(
						()->new IllegalArgumentException("No inventory use with given process id")));
		inventoryUseDTO.setPoProcessInfo(getInventoryUseRepository()
				.findPoProcessInfoByProcessId(processId, InventoryUse.class).orElse(null));
		
		inventoryUseDTO.setStorageMovesGroups(
				CollectionItemWithGroup.getFilledGroups(
						getInventoryUseRepository()
						.findStorageMovesWithGroup(processId)));
		inventoryUseDTO.setItemCounts(
				CollectionItemWithGroup.getFilledGroups(
						getInventoryUseRepository()
						.findItemCountWithAmount(processId)));
		
		return inventoryUseDTO;
	}

	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void editGeneralInventoryUse(InventoryUse inventoryUse) {
		if(inventoryUse.getProductionLine() == null || 
				inventoryUseRepository.findFunctionalityByProductionLine(inventoryUse.getProductionLine().getId()) != ProductionFunctionality.GENERAL_USE) {
			throw new IllegalStateException("Inventory Use has to have a Production Line with ProductionFunctionality.GENERAL_USE");
		}
		//Check that used items are from general
		if(!isUsedInItemGroup(inventoryUse.getStorageMovesGroups(), ItemGroup.GENERAL)) {
			throw new IllegalArgumentException("Inventory use can only be for GENERAL item groups");
		}
		
		dao.setStorageMovesProcessItem(inventoryUse.getStorageMovesGroups());
		
		dao.checkRemovingUsedProduct(inventoryUse);
		
		dao.editPoProcessEntity(inventoryUse);
		
		dao.checkUsedInventoryAvailability(inventoryUse);
		dao.setUsedProcesses(inventoryUse);
		List<ItemAmountWithPoCode> usedPos = dao.setRelocationPoWeights(inventoryUse);
		dao.checkDAGmaintained(usedPos, inventoryUse.getId());

		dao.checkUsingProcesessConsistency(inventoryUse);
		dao.checkRelocationBalance(inventoryUse);

	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void editProductInventoryUse(InventoryUse inventoryUse) {
		if(inventoryUse.getProductionLine() == null || 
				inventoryUseRepository.findFunctionalityByProductionLine(inventoryUse.getProductionLine().getId()) != ProductionFunctionality.PRODUCT_USE) {
			throw new IllegalStateException("Inventory Use has to have a Production Line with ProductionFunctionality.PRODUCT_USE");
		}
		//Check that used items are from product (cashew)
		if(!isUsedInItemGroup(inventoryUse.getStorageMovesGroups(), ItemGroup.PRODUCT)) {
			throw new IllegalArgumentException("Inventory use can only be for PRODUCT item groups");
		}
		
		dao.setStorageMovesProcessItem(inventoryUse.getStorageMovesGroups());
		
		dao.checkRemovingUsedProduct(inventoryUse);
		
		dao.editPoProcessEntity(inventoryUse);
		
		dao.checkUsedInventoryAvailability(inventoryUse);
		dao.setUsedProcesses(inventoryUse);
		List<ItemAmountWithPoCode> usedPos = dao.setRelocationPoWeights(inventoryUse);
		dao.checkDAGmaintained(usedPos, inventoryUse.getId());

		dao.checkUsingProcesessConsistency(inventoryUse);
		dao.checkRelocationBalance(inventoryUse);
	}

	private boolean isUsedInItemGroup(StorageMovesGroup[] storageMovesGroups, ItemGroup itemGroup) {
		Set<Integer> usedStorageIds = null;
		for(StorageMovesGroup smg: storageMovesGroups) {
			usedStorageIds = Arrays.stream(smg.getStorageMoves()).map(StorageMove::getStorage).map(StorageBase::getId).collect(Collectors.toSet());
		}
		if(usedStorageIds != null) {
			List<ItemWithUnitDTO> items = getValueTablesRepository().findStoragesItems(usedStorageIds);
			if(items.stream().anyMatch(i -> i.getGroup() != itemGroup)) {
				return false;
			}
		}
		return true;
	}

}
