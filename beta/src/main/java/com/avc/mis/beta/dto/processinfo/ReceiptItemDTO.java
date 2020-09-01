/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.values.DataObject;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.ItemCategory;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.ExtraAdded;
import com.avc.mis.beta.entities.processinfo.ReceiptItem;
import com.avc.mis.beta.entities.processinfo.StorageWithSample;

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
public class ReceiptItemDTO extends ProcessItemDTO {
	
	private DataObject orderItem;
	private AmountWithUnit extraRequested;
//	private MeasureUnit measureUnit;

//	private Set<StorageDTO> extraAdded; //can use a SortedSet like ContactDetails to maintain order	
	
	public ReceiptItemDTO(Integer id, Integer version, 
			Integer itemId, String itemValue, ItemCategory itemCategory,
			/* Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName, */
			String groupName, String description, String remarks, boolean tableView,
			Integer orderItemId, Integer orderItemVersion, BigDecimal extraRequested, MeasureUnit measureUnit) {
		super(id, version, itemId, itemValue, itemCategory,
				/* poCodeId, contractTypeCode, supplierName, */groupName, description, remarks, tableView);
		if(orderItemId != null)
			this.orderItem = new DataObject(orderItemId, orderItemVersion);
		if(extraRequested != null) {
			this.extraRequested = new AmountWithUnit(extraRequested.setScale(MeasureUnit.SCALE), measureUnit);
		}
	}

	
	public ReceiptItemDTO(ReceiptItem receiptItem) {
		super(receiptItem.getId(), receiptItem.getVersion(),
				new ItemDTO(receiptItem.getItem()), receiptItem.getGroupName(),
				receiptItem.getDescription(), receiptItem.getRemarks());
		
		setStorageForms(Arrays.stream(receiptItem.getStorageForms())
				.map(i->{
					if(i instanceof ExtraAdded) {
						return new ExtraAddedDTO((ExtraAdded)i);
					}
					else {						
						return new StorageWithSampleDTO((StorageWithSample) i);
					}})
				.collect(Collectors.toCollection(() -> new TreeSet<StorageDTO>(Ordinal.ordinalComparator()))));

		if(receiptItem.getOrderItem() != null)
			this.orderItem = new DataObject(receiptItem.getOrderItem());
		if(receiptItem.getExtraRequested() != null) {
			this.extraRequested = receiptItem.getExtraRequested().setScale(MeasureUnit.SCALE);
		}
//		this.measureUnit = receiptItem.getMeasureUnit();
	}


	public ReceiptItemDTO(Integer id, Integer version,
			ItemDTO item, /* PoCodeDTO itemPo, */ 
			String groupName, String description, String remarks, 
			DataObject orderItem, AmountWithUnit extraRequested) {
		super(id, version, item, /* itemPo, */ groupName, description, remarks);
		this.orderItem = orderItem;
		if(extraRequested != null) {
			this.extraRequested = extraRequested.setScale(MeasureUnit.SCALE);
		}
//		this.measureUnit = measureUnit;
	}
	
	public Optional<AmountWithUnit> getTotalDifferance() {
		return getStorageForms().stream()
				.map(s -> ((StorageWithSampleDTO)s).getWeighedDifferance())
				.filter(d -> d != null)
				.reduce(AmountWithUnit::add);
	}
}
