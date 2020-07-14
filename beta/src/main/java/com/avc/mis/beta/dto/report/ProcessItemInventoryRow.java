/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.query.InventoryProcessItemWithStorage;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProcessItemInventoryRow extends ValueDTO {

//	ItemDTO item; //measure unit is already in totalAmount
	private BasicValueEntity<Item> item;
	private PoCodeDTO poCode;
	private OffsetDateTime receiptDate;
	private AmountWithUnit totalAmount;
	private AmountWithUnit totalLots; //total amount in lots
	
	private List<StorageInventoryRow> storageForms;
	
	public ProcessItemInventoryRow(Integer id, Integer itemId, String itemValue,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			OffsetDateTime receiptDate/* , BigDecimal totalAmount, MeasureUnit measureUnit */) {
		super(id);
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		this.poCode = new PoCodeDTO(poCodeId, contractTypeCode, contractTypeSuffix, supplierName);
		this.receiptDate = receiptDate;
//		this.totalAmount = new AmountWithUnit(
//				totalAmount.setScale(AmountWithUnit.SCALE, RoundingMode.HALF_DOWN), measureUnit);
				
	}
	
	public ProcessItemInventoryRow(Integer id, BasicValueEntity<Item> item, 
			PoCodeDTO poCode, OffsetDateTime receiptDate) {
		super(id);
		this.item = item;
		this.poCode = poCode;
		this.receiptDate = receiptDate;
	}
	
	public void setStorageForms(List<StorageInventoryRow> storageForms) {
		if(storageForms.size() > 0) {
			MeasureUnit measureUnit = storageForms.get(0).getUnitAmount().getMeasureUnit();
			this.totalAmount = storageForms.stream()
					.map(StorageInventoryRow::getTotalBalance)
					.reduce(new AmountWithUnit(BigDecimal.ZERO, measureUnit), AmountWithUnit::add);
			this.totalLots = new AmountWithUnit(
					MeasureUnit.convert(totalAmount, MeasureUnit.LOT)
					.setScale(AmountWithUnit.SCALE, RoundingMode.HALF_DOWN), MeasureUnit.LOT);

		}
		this.storageForms = storageForms;
		
	}
	
	public static List<ProcessItemInventoryRow> getProcessItemInventoryRows(
			List<InventoryProcessItemWithStorage> processItemWithStorages) {
		Map<ProcessItemInventoryRow, List<StorageInventoryRow>> processItemStorageMap = processItemWithStorages
			.stream()
			.collect(Collectors.groupingBy(
					InventoryProcessItemWithStorage::getProcessItemInventoryRow, 
					Collectors.mapping(InventoryProcessItemWithStorage::getStorageInventoryRow,
							Collectors.toList())));
		
		List<ProcessItemInventoryRow> processItemInventoryRow = new ArrayList<ProcessItemInventoryRow>();
		processItemStorageMap.forEach((k, v) -> {
			k.setStorageForms(v);
			processItemInventoryRow.add(k);
		});
		
		return processItemInventoryRow;
	}
	
}
