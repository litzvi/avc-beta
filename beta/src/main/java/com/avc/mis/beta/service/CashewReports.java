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

import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.query.InventoryProcessItemWithStorage;
import com.avc.mis.beta.dto.report.ItemInventoryRow;
import com.avc.mis.beta.dto.report.PoInventoryRow;
import com.avc.mis.beta.dto.report.ProcessItemInventoryRow;
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
	
	@Autowired private WarehouseManagement warehouseManagement;
	
	public List<ItemInventoryRow> getInventoryTable() {

		List<ProcessItemInventoryRow> processItemRows = warehouseManagement.getInventory(SupplyGroup.CASHEW, null, null);
		
//		List<ProcessItemInventoryRow> processItemRows = getProcessItemRows();		
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
	
	public List<PoInventoryRow> getInventoryTableByPo() {
		
		List<ProcessItemInventoryRow> processItemRows = warehouseManagement.getInventory(SupplyGroup.CASHEW, null, null);

//		List<ProcessItemInventoryRow> processItemRows = getProcessItemRows();
		Map<PoCodeDTO, List<ProcessItemInventoryRow>> piMap = processItemRows.stream()
				.collect(Collectors.groupingBy(ProcessItemInventoryRow::getPoCode, Collectors.toList()));
		
		List<PoInventoryRow> inventoryRows = new ArrayList<PoInventoryRow>();
		piMap.forEach((k, v) -> {
			AmountWithUnit totalStock = v.stream()
					.map(pi -> pi.getTotalAmount())
					.reduce(new AmountWithUnit(BigDecimal.ZERO, v.get(0).getTotalAmount().getMeasureUnit()), AmountWithUnit::add);
			PoInventoryRow inventoryRow = new PoInventoryRow(k, totalStock, v);
			inventoryRows.add(inventoryRow);
		});
		return inventoryRows;
	}
	
	private List<ProcessItemInventoryRow> getProcessItemRows() {
		List<InventoryProcessItemWithStorage> processItemWithStorages = 
				getInventoryRepository().findInventoryProcessItemWithStorage(SupplyGroup.CASHEW, null, null);		

//		List<StorageInventoryRow> storageRows = getInventoryRepository().findInventoryStorage(SupplyGroup.CASHEW, null);
//		Map<Integer, List<StorageInventoryRow>> storageMap = storageRows.stream()
//				.collect(Collectors.groupingBy(StorageInventoryRow::getProcessItemId, Collectors.toList()));
//		
//		processItemRows.forEach(pi -> pi.setStorageForms(storageMap.get(pi.getId())));
//		
		return ProcessItemInventoryRow.getProcessItemInventoryRows(processItemWithStorages);
	}

}
