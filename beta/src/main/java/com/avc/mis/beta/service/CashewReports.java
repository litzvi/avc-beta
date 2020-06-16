/**
 * 
 */
package com.avc.mis.beta.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.tableRows.ItemInventoryRow;
import com.avc.mis.beta.dto.tableRows.ProcessItemInventoryRow;
import com.avc.mis.beta.dto.tableRows.StorageInventoryRow;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.repositories.InventoryRepository;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class CashewReports {
	
	@Autowired private InventoryRepository inventoryRepository;
	
	public List<ItemInventoryRow> getCashewInventoryTable() {
		
		List<StorageInventoryRow> storageRows = getInventoryRepository().findInventoryStorage(SupplyGroup.CASHEW);
		Map<Integer, List<StorageInventoryRow>> storageMap = storageRows.stream()
				.collect(Collectors.groupingBy(StorageInventoryRow::getProcessItemId, Collectors.toList()));
		
		List<ProcessItemInventoryRow> processItemRows = 
				getInventoryRepository().findInventoryProcessItem(SupplyGroup.CASHEW);		
		processItemRows.forEach(pi -> pi.setStorageForms(storageMap.get(pi.getId())));
		Map<BasicValueEntity<Item>, List<ProcessItemInventoryRow>> piMap = processItemRows.stream()
				.collect(Collectors.groupingBy(ProcessItemInventoryRow::getItem, Collectors.toList()));
		
		List<ItemInventoryRow> inventoryRows = new ArrayList<ItemInventoryRow>(piMap.size());
		piMap.forEach((k, v) -> {
			BigDecimal amount = v.stream()
					.map(pi -> pi.getTotalAmount().getAmount())
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			AmountWithUnit totalStock = new AmountWithUnit(amount, v.get(0).getTotalAmount().getMeasureUnit());
			ItemInventoryRow inventoryRow = new ItemInventoryRow(k, totalStock, v);
			inventoryRows.add(inventoryRow);
		});
		return inventoryRows;
	}

}
