/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.values.PoCodeDTO;
import com.avc.mis.beta.dto.view.ItemInventoryRow;
import com.avc.mis.beta.dto.view.PoInventoryRow;
import com.avc.mis.beta.dto.view.ProcessItemInventoryRow;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.repositories.InventoryRepository;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Service for various cashew reports
 * 
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class InventoryReports {
	
//	private static ItemCategory[] itemCategoies = new ItemCategory[] {ItemCategory.CLEAN, ItemCategory.PACKED, ItemCategory.RAW, ItemCategory.ROAST};

	
	@Autowired private InventoryRepository inventoryRepository;
	
	@Autowired private WarehouseManagement warehouseManagement;
	
	/**
	 * Gets report of all items that are currently in the inventory with full information needed for report display.
	 * @return List of ItemInventoryRow that have a balance in inventory
	 */
	public List<ItemInventoryRow> getInventoryTableByItem(ItemGroup group) {

		List<ProcessItemInventoryRow> processItemRows = warehouseManagement.getInventoryRows(group, null, null, null);
		
		Map<ItemDTO, List<ProcessItemInventoryRow>> piMap = processItemRows.stream()
				.collect(Collectors.groupingBy(ProcessItemInventoryRow::getItem, LinkedHashMap::new, Collectors.toList()));
		
		List<ItemInventoryRow> inventoryRows = new ArrayList<ItemInventoryRow>(piMap.size());
		piMap.forEach((k, v) -> {
			AmountWithUnit totalStock = v.stream()
					.map(pi -> pi.getTotalBalance()[0])
					.reduce(AmountWithUnit::add).get();
//			v.sort(new Comparator<ProcessItemInventoryRow>() {	
//				
//				@Override
//				public int compare(ProcessItemInventoryRow o1, ProcessItemInventoryRow o2) {
//					return o1.getReceiptDate().compareTo(o2.getReceiptDate());
//				}
//			});
			ItemInventoryRow inventoryRow = new ItemInventoryRow(k, totalStock, v);
			inventoryRows.add(inventoryRow);
		});
		
		return inventoryRows;
	}
	
	/**
	 * Gets report of all po code that currently have balance in inventory with full information needed for report display.
	 * @return List of PoInventoryRow that have a balance in inventory
	 */
	public List<PoInventoryRow> getInventoryTableByPo(ItemGroup group) {
		
		List<ProcessItemInventoryRow> processItemRows = warehouseManagement.getInventoryRows(group, null, null, null);

		Map<PoCodeDTO, List<ProcessItemInventoryRow>> piMap = processItemRows.stream()
				.collect(Collectors.groupingBy(ProcessItemInventoryRow::getPoCode, LinkedHashMap::new, Collectors.toList()));
		
		List<PoInventoryRow> inventoryRows = new ArrayList<PoInventoryRow>();
		piMap.forEach((k, v) -> {
			AmountWithUnit totalStock = v.stream()
					.map(pi -> pi.getTotalBalance()[0])
					.reduce(AmountWithUnit::add).get();
			PoInventoryRow inventoryRow = new PoInventoryRow(k, totalStock, v);
			inventoryRows.add(inventoryRow);
		});
//		inventoryRows.sort(new Comparator<PoInventoryRow>() {	
//			
//			@Override
//			public int compare(PoInventoryRow o1, PoInventoryRow o2) {
//				OffsetDateTime t1 = o1.getPoInventoryRows().get(0).getReceiptDate();
//				OffsetDateTime t2 = o2.getPoInventoryRows().get(0).getReceiptDate();
//				return t1.compareTo(t2);
//			}
//		});
		return inventoryRows;
	}

}
