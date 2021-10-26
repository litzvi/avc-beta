/**
 * 
 */
package com.avc.mis.beta.dto.process.collection;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.Optional;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.data.DataObject;
import com.avc.mis.beta.dto.process.inventory.ExtraAddedDTO;
import com.avc.mis.beta.dto.process.inventory.StorageDTO;
import com.avc.mis.beta.dto.process.inventory.StorageWithSampleDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.collection.OrderItem;
import com.avc.mis.beta.entities.process.collection.ReceiptItem;
import com.avc.mis.beta.entities.process.inventory.ExtraAdded;
import com.avc.mis.beta.entities.process.inventory.StorageWithSample;
import com.avc.mis.beta.utilities.ListGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class ReceiptItemDTO extends ProcessItemDTO  implements ListGroup<StorageDTO> {
	
	private AmountWithUnit receivedOrderUnits;
	private AmountWithCurrency unitPrice;
	private DataObject<OrderItem> orderItem;
	private AmountWithUnit extraRequested;

	@JsonIgnore
	@EqualsAndHashCode.Exclude
	private Integer referencedOrder;

	
	public ReceiptItemDTO(Integer id, Integer version, Integer ordinal,
			Integer itemId, String itemValue, ProductionUse productionUse, 
			AmountWithUnit unit, Class<? extends Item> clazz,
			MeasureUnit measureUnit,
			/* Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName, */
			String groupName, String description, String remarks, boolean tableView,
			BigDecimal orderUnits, MeasureUnit orderMU, BigDecimal unitPrice, Currency currency,
			Integer referencedOrder,
			Integer orderItemId, Integer orderItemVersion, BigDecimal extraRequested, MeasureUnit extraMU) {
		super(id, version, ordinal, itemId, itemValue, productionUse, unit, clazz, 
				measureUnit,
				/* poCodeId, contractTypeCode, supplierName, */groupName, description, remarks, tableView);
		if(orderUnits != null) {
			this.receivedOrderUnits = new AmountWithUnit(orderUnits.setScale(MeasureUnit.SCALE), orderMU);
		}
		if(unitPrice != null) {
			this.unitPrice = new AmountWithCurrency(unitPrice, currency);
		}
		this.referencedOrder = referencedOrder;
		if(orderItemId != null)
			this.orderItem = new DataObject<OrderItem>(orderItemId, orderItemVersion);
		if(extraRequested != null) {
			this.extraRequested = new AmountWithUnit(extraRequested.setScale(MeasureUnit.SCALE), extraMU);
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
		if(receiptItem.getOrderItem() != null) {
			this.orderItem = new DataObject<OrderItem>(receiptItem.getOrderItem());
		}
		if(receiptItem.getExtraRequested() != null) {
			this.extraRequested = receiptItem.getExtraRequested().setScale(MeasureUnit.SCALE);
		}
//		this.measureUnit = receiptItem.getMeasureUnit();
	}


//	public ReceiptItemDTO(Integer id, Integer version, Integer ordinal,
//			ItemDTO item, /* PoCodeBasic itemPo, */ MeasureUnit measureUnit,
//			String groupName, String description, String remarks, boolean tableView,
//			AmountWithUnit receivedOrderUnits, AmountWithCurrency unitPrice,
//			DataObject<OrderItem> orderItem, AmountWithUnit extraRequested) {
//		super(id, version, ordinal, item, /* itemPo, */ measureUnit, groupName, description, remarks, tableView);
//		this.receivedOrderUnits = receivedOrderUnits;
//		this.unitPrice = unitPrice;
//		this.orderItem = orderItem;
//		if(extraRequested != null) {
//			this.extraRequested = extraRequested.setScale(MeasureUnit.SCALE);
//		}
////		this.measureUnit = measureUnit;
//	}
	
	public Optional<BigDecimal> getTotalDifferance() {
		return getStorageForms().stream()
				.map(s -> ((StorageWithSampleDTO)s).getWeighedDifferance())
				.filter(d -> d != null)
				.reduce(BigDecimal::add);
	}
	
	@Override
	public ReceiptItem fillEntity(Object entity) {
		ReceiptItem receiptItem;
		if(entity instanceof ReceiptItem) {
			receiptItem = (ReceiptItem) entity;
		}
		else {
			throw new IllegalArgumentException("Param has to be ReceiptItem class");
		}
		super.fillEntity(receiptItem);
		receiptItem.setOrderItem((OrderItem) getOrderItem().fillEntity(new OrderItem()));
		receiptItem.setReceivedOrderUnits(getReceivedOrderUnits());
		receiptItem.setUnitPrice(getUnitPrice());
		receiptItem.setExtraRequested(getExtraRequested());
		
		return receiptItem;
	}

}
