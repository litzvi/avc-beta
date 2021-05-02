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
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.InventoryUseDTO;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.process.InventoryUse;
import com.avc.mis.beta.entities.process.collection.UsedItemsGroup;
import com.avc.mis.beta.entities.process.inventory.StorageBase;
import com.avc.mis.beta.entities.process.inventory.UsedItem;
import com.avc.mis.beta.repositories.InventoryUseRepository;
import com.avc.mis.beta.repositories.ValueTablesRepository;

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

	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addGeneralInventoryUse(InventoryUse inventoryUse) {
		//Check that used items are from general
		if(!isUsedInItemGroup(inventoryUse.getUsedItemGroups(), ItemGroup.GENERAL)) {
			throw new IllegalArgumentException("Inventory use can only be for GENERAL item groups");
		}				
		inventoryUse.setProcessType(dao.getProcessTypeByValue(ProcessName.GENERAL_USE));
		dao.addTransactionProcessEntity(inventoryUse);
		dao.checkUsedInventoryAvailability(inventoryUse);
		dao.setGeneralPoWeights(inventoryUse);
		dao.setUsedProcesses(inventoryUse);
	}
	
	public InventoryUseDTO getInventoryUse(int processId) {
		InventoryUseDTO inventoryUseDTO = new InventoryUseDTO();
		inventoryUseDTO.setGeneralProcessInfo(getInventoryUseRepository()
				.findGeneralProcessInfoByProcessId(processId, InventoryUse.class)
				.orElseThrow(
						()->new IllegalArgumentException("No inventory use with given process id")));
		inventoryUseDTO.setPoProcessInfo(getInventoryUseRepository()
				.findPoProcessInfoByProcessId(processId, InventoryUse.class).orElse(null));
		
		getProcessReader().setTransactionProcessCollections(inventoryUseDTO);
		
		return inventoryUseDTO;
	}

	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editGeneralInventoryUse(InventoryUse inventoryUse) {
		//Check that used items are from general
		if(!isUsedInItemGroup(inventoryUse.getUsedItemGroups(), ItemGroup.GENERAL)) {
			throw new IllegalArgumentException("Inventory use can only be for GENERAL item groups");
		}			
		dao.editTransactionProcessEntity(inventoryUse);
		
		dao.checkUsedInventoryAvailability(inventoryUse);
		dao.setGeneralPoWeights(inventoryUse);
		dao.setUsedProcesses(inventoryUse);
	}

	private boolean isUsedInItemGroup(UsedItemsGroup[] usedItemGroups, ItemGroup itemGroup) {
		Set<Integer> usedStorageIds = null;
		for(UsedItemsGroup uig: usedItemGroups) {
			usedStorageIds = Arrays.stream(uig.getUsedItems()).map(UsedItem::getStorage).map(StorageBase::getId).collect(Collectors.toSet());
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
