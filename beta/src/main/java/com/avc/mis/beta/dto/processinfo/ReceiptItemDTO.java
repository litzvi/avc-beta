/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.Optional;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.values.DataObject;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.processinfo.ExtraAdded;
import com.avc.mis.beta.entities.processinfo.OrderItem;
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
	
	private AmountWithUnit receivedOrderUnits;
	private AmountWithCurrency unitPrice;
	private DataObject<OrderItem> orderItem;
	private AmountWithUnit extraRequested;
//	private MeasureUnit measureUnit;

//	private Set<StorageDTO> extraAdded; //can use a SortedSet like ContactDetails to maintain order	
	
	public ReceiptItemDTO(Integer id, Integer version, Integer ordinal,
			Integer itemId, String itemValue, ProductionUse productionUse, Class<? extends Item> clazz,
			/* Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName, */
			String groupName, String description, String remarks, boolean tableView,
			BigDecimal orderUnits, MeasureUnit orderMU, BigDecimal unitPrice, Currency currency,
			Integer orderItemId, Integer orderItemVersion, BigDecimal extraRequested, MeasureUnit measureUnit) {
		super(id, version, ordinal, itemId, itemValue, productionUse, clazz,
				/* poCodeId, contractTypeCode, supplierName, */groupName, description, remarks, tableView);
		if(orderUnits != null) {
			this.receivedOrderUnits = new AmountWithUnit(orderUnits.setScale(MeasureUnit.SCALE), orderMU);
		}
		if(unitPrice != null) {
			this.unitPrice = new AmountWithCurrency(unitPrice, currency);
		}
		if(orderItemId != null)
			this.orderItem = new DataObject<OrderItem>(orderItemId, orderItemVersion);
		if(extraRequested != null) {
			this.extraRequested = new AmountWithUnit(extraRequested.setScale(MeasureUnit.SCALE), measureUnit);
		}
	}

	
	public ReceiptItemDTO(ReceiptItem receiptItem) {
		super(receiptItem);
//		super(receiptItem.getId(), receiptItem.getVersion(), receiptItem.getOrdinal(),
//				new ItemDTO(receiptItem.getItem()), receiptItem.getGroupName(),
//				receiptItem.getDescription(), receiptItem.getRemarks());
		
		setStorageForms(Arrays.stream(receiptItem.getStorageForms())
				.map(i->{
					if(i instanceof ExtraAdded) {
						return new ExtraAddedDTO((ExtraAdded)i);
					}
					else {						
						return new StorageWithSampleDTO((StorageWithSample) i);
					}})
				.collect(Collectors.toList()));

		this.receivedOrderUnits = receiptItem.getReceivedOrderUnits().setScale(MeasureUnit.SCALE);
		if(receiptItem.getUnitPrice() != null) {
			this.unitPrice = receiptItem.getUnitPrice().clone();
		}
		else {
			this.unitPrice = null;
		}
		if(receiptItem.getOrderItem() != null)
			this.orderItem = new DataObject<OrderItem>(receiptItem.getOrderItem());
		if(receiptItem.getExtraRequested() != null) {
			this.extraRequested = receiptItem.getExtraRequested().setScale(MeasureUnit.SCALE);
		}
//		this.measureUnit = receiptItem.getMeasureUnit();
	}


	public ReceiptItemDTO(Integer id, Integer version, Integer ordinal,
			ItemDTO item, /* PoCodeDTO itemPo, */ 
			String groupName, String description, String remarks, 
			AmountWithUnit receivedOrderUnits, AmountWithCurrency unitPrice,
			DataObject<OrderItem> orderItem, AmountWithUnit extraRequested) {
		super(id, version, ordinal, item, /* itemPo, */ groupName, description, remarks);
		this.receivedOrderUnits = receivedOrderUnits;
		this.unitPrice = unitPrice;
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
