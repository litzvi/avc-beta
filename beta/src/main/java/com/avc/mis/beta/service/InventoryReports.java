/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.report.InventoryReportLine;
import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.view.ItemInventoryAmountWithOrder;
import com.avc.mis.beta.dto.view.ItemInventoryRow;
import com.avc.mis.beta.dto.view.ItemInventoryWithOrderRow;
import com.avc.mis.beta.dto.view.PoInventoryRow;
import com.avc.mis.beta.dto.view.PoItemRow;
import com.avc.mis.beta.dto.view.ProcessItemInventoryRow;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.repositories.InventoryRepository;
import com.avc.mis.beta.repositories.PORepository;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

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
		
	@Autowired private InventoryRepository inventoryRepository;
	@Autowired private PORepository poRepository;
	@Autowired private ValueTablesReader valueTablesReader;
		
	/**
	 * Gets report of all items that are currently in the inventory with full information needed for report display.
	 * @return List of ItemInventoryRow that have a balance in inventory
	 */
	public List<ItemInventoryRow> getInventoryTableByItem(ItemGroup group) {
		
		List<ProcessItemInventoryRow> processItemRows = getInventoryRows(group, null, null, null);
		
//		Map<ItemInventoryRow, List<ProcessItemInventoryRow>> piMap = processItemRows.stream()
//				.collect(Collectors.groupingBy(ProcessItemInventoryRow::getItemInventoryRow, LinkedHashMap::new, Collectors.toList()));
//		
//		List<ItemInventoryRow> inventoryRows = new ArrayList<ItemInventoryRow>();
//		piMap.forEach((k, v) -> {
//			k.setPoInventoryRows(v);
//			inventoryRows.add(k);
//		});
//		
//		return inventoryRows;
		
		
		return CollectionItemWithGroup.getFilledGroups(processItemRows, 
				(i -> {return new ItemInventoryRow(i.getItem());}), 
				Function.identity(), 
				ItemInventoryRow::setPoInventoryRows);
	}
	
	/**
	 * Gets report of all po code that currently have balance in inventory with full information needed for report display.
	 * @return List of PoInventoryRow that have a balance in inventory
	 */
	public List<PoInventoryRow> getInventoryTableByPo(ItemGroup group) {
		
		List<ProcessItemInventoryRow> processItemRows = getInventoryRows(group, null, null, null);

		BiConsumer<PoInventoryRow, List<ProcessItemInventoryRow>> setter = PoInventoryRow::setProductPoInventoryRows;
		if(group == ItemGroup.GENERAL) {
			setter = PoInventoryRow::setGeneralPoInventoryRows;			
		}
		else {
			setter = PoInventoryRow::setProductPoInventoryRows;			
		}
		
		return CollectionItemWithGroup.getFilledGroups(processItemRows, 
				(i -> {return new PoInventoryRow(i.getPoCode());}), 
				Function.identity(), 
				setter);

	}
	
	/**
	 * LIST OF INVENTORY ITEMS FOR REPORT
	 * (SHOWN IF PRODUCING PROCESS IS FINAL AND NOT USED BY FINAL PROCESS)
	 * Gets all information of items in the inventory, for provided supply group, item or po code.
	 * If one of the parameters are null than will ignore that constraint.
	 * For each stored item in inventory, provides information on the process item and balances.
	 * Items are considered in inventory if process status is final and it's not completely used.
	 * @param supplyGroup constrain to only this supply group, if null than any.
	 * @param itemCategory constrain to only items from given category, if null than any.
	 * @param itemId constrain to only this item, if null than any.
	 * @param poCodeId constrain to only this po, if null than any.
	 * @return List of ProcessItemInventoryRow
	 */
	private List<ProcessItemInventoryRow> getInventoryRows(ItemGroup group, ProductionUse[] productionUses, Integer itemId, Integer poCodeId) {
		boolean checkProductionUses = (productionUses != null);
		return getInventoryRepository().findInventoryProcessItemRows(checkProductionUses, productionUses, group, itemId, poCodeId);			
	}

	public InventoryReportLine getInventorySummary(@NonNull Integer poCodeId) {
		List<ItemAmount> inventory = inventoryRepository.findInventoryItemAmounts(false, null, ItemGroup.PRODUCT, null, poCodeId);
		
		if(inventory == null || inventory.isEmpty()) {
			return null;
		}
		
		InventoryReportLine reportLine = new InventoryReportLine();
		reportLine.setInventory(inventory);
		return reportLine;
	}
	
	public List<ItemInventoryAmountWithOrder> getInventoryWithOrderByItem(ItemGroup group) {
		
		//TODO giving all items in product - but only relevant to raw
		List<ItemAmount> inventory = inventoryRepository.findInventoryItemAmounts(false, null, group, null, null);
		List<ItemAmount> openOrders = poRepository.findOpenOrdersItemAmounts(null, group);
		
		List<ItemInventoryAmountWithOrder> inventoryAmountWithOrders = getValueTablesReader().getBasicItemsByGroup(group).stream()
				.map(i -> new ItemInventoryAmountWithOrder(i))
				.collect(Collectors.toList());
		
		CollectionItemWithGroup.fillGroups(
				inventoryAmountWithOrders, 
				inventory, 
				(i -> i.getItem()), 
				(i -> i.getItem()), 
				Function.identity(), 
				ItemInventoryAmountWithOrder::setInventory);
		
		CollectionItemWithGroup.fillGroups(
				inventoryAmountWithOrders, 
				openOrders, 
				(i -> i.getItem()), 
				(i -> i.getItem()), 
				Function.identity(), 
				ItemInventoryAmountWithOrder::setOrder);
		
		return inventoryAmountWithOrders;

	}
	
	@Deprecated
	public List<ItemInventoryWithOrderRow> getInventoryWithOrderTableByItem(ItemGroup group) {
		
		List<ProcessItemInventoryRow> processItemRows = getInventoryRows(group, null, null, null);
		List<PoItemRow> poItemRows = getPoRepository().findOpenOrdersByType(null, group);
		
//		List<ItemWithUnitDTO> items = getValueTablesReader().getItemsByGroup(group);
		List<ItemInventoryWithOrderRow> itemInventoryWithOrderRows = getValueTablesReader().getItemsByGroup(group).stream()
				.map(i -> new ItemInventoryWithOrderRow(i))
				.collect(Collectors.toList());
		
		CollectionItemWithGroup.fillGroups(
				itemInventoryWithOrderRows, 
				processItemRows, 
				(i -> {return new ItemInventoryWithOrderRow(i.getItem());}), 
				Function.identity(), 
				ItemInventoryWithOrderRow::setPoInventoryRows);
		
		CollectionItemWithGroup.fillGroups(
				itemInventoryWithOrderRows, 
				poItemRows, 
				(i -> {return new ItemInventoryWithOrderRow(i.getItem());}), 
				Function.identity(), 
				ItemInventoryWithOrderRow::setOrderItemRows);

		
		return itemInventoryWithOrderRows;
	}

}
